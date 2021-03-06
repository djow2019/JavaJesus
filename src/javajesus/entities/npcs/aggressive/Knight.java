package javajesus.entities.npcs.aggressive;

import java.awt.geom.Ellipse2D;

import javajesus.entities.Entity;
import javajesus.entities.Mob;
import javajesus.entities.monsters.Monster;
import javajesus.entities.npcs.NPC;
import javajesus.graphics.Screen;
import javajesus.level.Level;
import javajesus.utility.Direction;

public class Knight extends NPC {

	// dimensions of the knight
	private static final int WIDTH = 16, HEIGHT = 16;

	// Range that the monster can target another
	private Ellipse2D.Double aggroRadius;

	// the attack range radius, 32 (number of units) * 8 (units) = 256
	private static final int RADIUS = 8;
	
	// range for damage
	private static final int DAMAGE_RANGE = 5;

	// cooldown from attacks
	private boolean cooldown = true;

	// internal timer for attack cooldown
	private int attackTickCount;

	// the amount of ticks between attacks
	private static final int attackDelay = 40;

	// how long the attack position is rendered in ticks
	private static final int attackAnimationLength = 30;

	// how fast the player toggles steps
	private static final int WALKING_ANIMATION_SPEED = 4;

	/**
	 * Knight ctor()
	 * 
	 * @param level - level it is on
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param health - health of the knight
	 * @param walkPath - the walking pattern
	 * @param walkDistance - the length of the walking pattern
	 */
	public Knight(Level level, int x, int y, int health, String walkPath, int walkDistance) {
		super(level, "Knight", x, y, 1, WIDTH, HEIGHT, health, new int[] { 0xFF111111, 0xFF888888, 0xFFe8e8e8, 0xFF111111,0xFF7E7E7E},
				0, 0, walkPath, walkDistance);
		
		// expand the outer range a little bit
		setOuterBoundsRange(4);

		// initialize the radius
		if (level != null) {
			aggroRadius = new Ellipse2D.Double(x - RADIUS / 2, y - RADIUS / 2, RADIUS, RADIUS);
			findTarget();
		}
	}
	
	/**
	 * Knight ctor()
	 * 
	 * @param level - level it is on
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public Knight(Level level, int x, int y) {
		this(level, x, y, 100, "stand", 0);
	}

	/**
	 * Updates the targeted mob
	 */
	protected void findTarget() {

		// if the target is dead or out of range, reset the target
		if (target != null && (target.isDead() || !(aggroRadius.intersects(target.getBounds())))) {
			target.setTargeted(false);
			target = null;
		}

		// assign a new target
		if (target == null) {
			
			// closest targetted mob
			Mob closest = null;
			
			// grow from small radius to large radius
			for (int i = 1; i < 20; i++) {
				
				// update the radius
				aggroRadius = new Ellipse2D.Double(getX() - (i * 8) / 2, getY() - (i * 8) / 2, i * 8, i * 8);
				
				for (Mob mob : getLevel().getMobs()) {
					if ((mob instanceof Monster) && aggroRadius.intersects(mob.getBounds()) && !mob.isDead()) {
						
						// target the mob if it is not being targeted already
						// or if it is already within range
						if (!mob.isTargeted() || (getOuterBounds().intersects(mob.getOuterBounds()))) {
							target = mob;
							mob.setTargeted(true);
							return;

							// mob already being targetted
						} else if (closest == null){
							closest = mob;
						}
					}
				}
			}
			
			// at this point, no target has been selected so default to closest
			if (closest != null) {
				target = closest;
				return;
			}
			
		}

	}

	/**
	 * Updates the Knight
	 */
	public void tick() {
		
		// make sure the knight has a valid target
		findTarget();
		
		// do basic npc logic
		if (target == null) {
			super.tick();
			return;
		} 
		
		// force the mob to move around the mob collision
		if (collisionStrategy.isMobCollision(0, 0)) {
			moveAroundMobCollision();
		}
		
		// attacking cooldown loop
		if (cooldown) {
			attackTickCount++;
			isShooting = attackTickCount < attackAnimationLength;
			if (attackTickCount > attackDelay) {
				attackTickCount = 0;
				cooldown = false;
			}
		}

		// attack the target if given a chance
		if (!cooldown && target != null && getOuterBounds().intersects(target.getOuterBounds())) {
			cooldown = true;
			this.attack(DAMAGE_RANGE, target);
		}

		// change in x and y
		int dx = 0, dy = 0;

		// whether or not the monster should move
		// check the bounds if the monster prefers long range or not
		if (!(getBounds().intersects(target.getOuterBounds()))) {

			// move towards the target horizontally
			if (target.getX() > getX()) {
				dx++;
			} else if (target.getX() < getX()) {
				dx--;
			}

			// move towards the target vertically
			if (target.getY() > getY()) {
				dy++;
			} else if (target.getY() < getY()) {
				dy--;
			}

		}

		// move the monster towards the target
		if ((dx != 0 || dy != 0) && !collisionStrategy.isMobCollision(dx, dy)) {
			move(dx, dy);
		}
	}

	/**
	 * Displays the Knight to the screen
	 */
	public void render(Screen screen) {

		// modifier used for rendering in different scales/directions
		int modifier = UNIT_SIZE;

		// no x or y offset, use the upper left corner as absolute
		int xOffset = getX(), yOffset = getY();

		// the horizontal position on the spritesheet
		int xTile = 0;

		// whether or not to render backwards
		boolean flip = ((numSteps >> WALKING_ANIMATION_SPEED) & 1) == 1;

		// adjust spritesheet offsets
		if (getDirection() == Direction.NORTH) {
			xTile = 12;
			if (isMoving) {
				xTile += 2 + (flip ? 2 : 0);
				flip = false;
			}
			if (isShooting) {
				xTile = 23;
			}
			if (!isMoving && !isShooting) {
				xTile += ((tickCount % 120 <= 60) ? 19 : 0);
			}
		} else if (getDirection() == Direction.SOUTH) {
			
			if (isMoving) {
				xTile += 2 + (flip ? 2 : 0);
				flip = false;
			}
			if (isShooting) {
				xTile = 18;
			} 
			if (!isMoving && !isShooting) {
				xTile += ((tickCount % 120 <= 60) ? 27 : 0);
			}
		} else {
			xTile = 6 + (flip ? 3 : 0);
			
			if (isShooting) {
				xTile = 20;
			}
			if (!isMoving && !isShooting) {
				xTile += ((tickCount % 120 <= 60) ? 23 : 0);
			}
			
			flip = getDirection() == Direction.WEST;
		}
		
		// death image
		if (isDead())
			xTile = 25;
		
		// base 2x2 quadrant y-axis symmetry 
		if (isLongitudinal() || isDead()) {

			// Upper body
			screen.render(xOffset + (modifier * (flip ? 1 : 0)), yOffset, xTile, yTile, getSpriteSheet(), flip);

			// Upper body
			screen.render(xOffset + modifier - (modifier * (flip ? 1 : 0)), yOffset, xTile + 1, yTile, getSpriteSheet(),
			        flip);

			// Lower Body
			screen.render(xOffset + (modifier * (flip ? 1 : 0)), yOffset + modifier, xTile, yTile + 1, getSpriteSheet(),
			        flip);

			// Lower Body
			screen.render(xOffset + modifier - (modifier * (flip ? 1 : 0)), yOffset + modifier, xTile + 1, yTile + 1,
			        getSpriteSheet(), flip);
			
			// Lower sword
			screen.render(xOffset + (modifier * (flip ? 1 : 0)), yOffset + 2 * modifier, xTile, yTile + 2, getSpriteSheet(),
			        flip);

			// Lower sword
			screen.render(xOffset + modifier - (modifier * (flip ? 1 : 0)), yOffset + 2 * modifier, xTile + 1, yTile + 2,
			        getSpriteSheet(), flip);
		} else if (isLatitudinal() && !isDead()) {
			
			// move x offset over if west
			if (getDirection() == Direction.WEST) {
				xOffset -= 8;
			}
			
			// iterate top to bottom
			for (int i = 0; i < 2; i++) {
				
				// Left body
				screen.render(xOffset + (modifier * (flip ? 2 : 0)), yOffset + i * modifier, xTile, yTile + i, getSpriteSheet(), flip);
				
				// Middle body
				screen.render(xOffset + modifier, yOffset + i * modifier, xTile + 1, yTile + i, getSpriteSheet(), flip);
				
				// Right body
				screen.render(xOffset + (modifier * 2) - (modifier * (flip ? 2 : 0)), yOffset + i * modifier, xTile + 2, yTile + i, getSpriteSheet(), flip);
						
			}
			
		}
	}

	/**
	 * Moves the knight on the level
	 * 
	 * @param dx - the total change in x
	 * @param dy - the total change in y
	 */
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);

		aggroRadius.setFrame(getX() - RADIUS / 2, getY() - RADIUS / 2, RADIUS, RADIUS);
	}

	@Override
	public int getStrength() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public int getDefense() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte getId() {
		// TODO Auto-generated method stub
		return Entity.KNIGHT;
	}

}
