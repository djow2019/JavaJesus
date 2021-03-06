package javajesus.entities;

import java.awt.Rectangle;
import java.util.Random;

import javajesus.dataIO.EntityData;
import javajesus.entities.effects.Shadow;
import javajesus.entities.particles.HealthBar;
import javajesus.entities.strategies.CollisionStrategy;
import javajesus.entities.strategies.LooseCollisionStrategy;
import javajesus.graphics.JJFont;
import javajesus.graphics.Screen;
import javajesus.graphics.SpriteSheet;
import javajesus.level.Level;
import javajesus.level.tile.Tile;
import javajesus.utility.Direction;
import javajesus.utility.movement.Path;
import javajesus.utility.movement.PathFactory;

/*
 * A mob is an entity that has complex interactions in a level with other entities
 */
public abstract class Mob extends Entity implements Damageable, Skills {

	// the name of the mob
	private String name;

	// the base speed of the mob
	private double speed;

	// flip value
	protected int numSteps;

	// the direction the mob is moving/facing
	private Direction movingDir = Direction.SOUTH;

	// the current amount of health
	private short health;

	// the max health (upper bound)
	private short maxHealth;

	// the health bar that is displayed beneath each mob
	private HealthBar bar;

	// the spritesheet each mob uses for its images
	private SpriteSheet sheet;

	// determines if the mob is currently on fire
	private boolean onFire;

	// determines if another mob is attacking this mob
	private boolean isTargeted;

	// randomly generates colors and damage
	protected static final Random random = new Random();

	// the padding around each mob where other mobs can interact
	private Rectangle outerBounds;
	
	// determines if the mob is standing on grass
	protected boolean onGrass;

	// determines if the mob should have the swimming animation
	protected boolean isSwimming;

	// determines if the mob should have the swinging animation
	protected boolean isSwinging;

	// determines if the mob should have the shooting animation
	protected boolean isShooting;

	// determines if the mob should have the talking animation
	protected boolean isTalking;

	// the hit cooldown in ticks
	private static final int HIT_COOLDOWN = 15;

	// the talking cooldown in ticks
	private static final int TALKING_COOLDOWN = secondsToTicks(5);

	// hit color of mob
	public static final int[] mobHitColor = { 0xFFDBA800, 0xFFDBA800, 0xFFDBA800, 0, 0 };

	// the base unit of each box on the spritesheet
	protected static final int UNIT_SIZE = 8;

	// talking cooldown
	protected int talkCount;

	// the damage value to be displayed
	protected String damageTaken = "";

	// determines if the mob was recently damaged (for displaying damage
	// indicators)
	protected boolean isHit;

	// the target of the mob, used for attacking other mobs
	protected Mob target;

	// damage cooldown
	private int isHitTicks;

	// internal timers
	protected int tickCount = 0;
	private int fireTickCount = 0;

	// the path for the mob to follow
	protected Path path;

	// the extra space, or padding, around each mob
	public static final int OUTER_BOUNDS_RANGE = 2;

	// whether or not the mob can clip through bounds
	private boolean clip;

	// whether or not mobs can push the player around
	protected boolean collisionImmune;

	// color of water
	private static final int[] waterColor = { 0xFF5A52FF, 0xFF000000, 0xFF000000 };
	
	// color of fire
	private static final int[] firecolor = { 0xFFF7790A, 0xFFF72808, 0xFF000000 };
	
	// color of grass, will be updated depending on grass type
	private int[] grassColor = { 0xFF3fa235, 0xFF277c1d, 0xFF000000 };

	// for knockback color flicker
	protected boolean knockbackCooldown;
	private int knockbackTicks;
	private int knockbackLength = 10;

	// whether or not this mob is immune to fire
	private boolean fireImmune;
	
	protected CollisionStrategy collisionStrategy;

	/**
	 * Creates a mob that can interact with other entities on a level
	 * 
	 * @param level         the level to place it on
	 * @param name          the name of the mob
	 * @param x             the x coordinate on the map
	 * @param y             the y coordinate on the map
	 * @param speed         the base speed of the mob (usually 1)
	 * @param width         the horizontal space of the mob
	 * @param height        the vertical space of the mob
	 * @param sheet         the spritesheet that contains the image of the mob
	 * @param defaultHealth the starting highest health value
	 */
	public Mob(Level level, String name, int x, int y, double speed, int width, int height, SpriteSheet sheet,
			int defaultHealth) {
		super(level, x, y);

		// instance data
		this.name = new String(name);
		this.speed = speed;
		this.health = (short) defaultHealth;
		this.maxHealth = (short) defaultHealth;
		this.setBounds(x, y, width, height);
		this.setOuterBounds(width, height);
		this.sheet = sheet;
		collisionStrategy = new LooseCollisionStrategy(this);

	}

	/**
	 * Moves a mob on the level
	 * 
	 * @param dx - the total change in x
	 * @param dy - the total change in y
	 */
	public void move(int dx, int dy) {

		// move animation proportional to speed
		numSteps++;

		// sign of movement along x axis
		int sign = 0;
		if (dx < 0) {
			setDirection(Direction.WEST);
			sign = -1;
		} else if (dx > 0) {
			setDirection(Direction.EAST);
			sign = 1;
		}

		// move pixel by pixel for maximum accuracy
		for (int i = 0; i < Math.abs(dx); i++) {
			if (!collisionStrategy.willCollide(1 * sign, 0) || clip) {
				super.move(1 * sign, 0);
				this.moveOuterBounds(1 * sign, 0);
			} else {
				clearPath();
				break;
			}
		}

		// sign of movement along y axis
		if (dy < 0) {
			setDirection(Direction.NORTH);
			sign = -1;
		} else if (dy > 0) {
			setDirection(Direction.SOUTH);
			sign = 1;
		}

		// move pixel by pixel for maximum accuracy
		for (int i = 0; i < Math.abs(dy); i++) {
			if (!collisionStrategy.willCollide(0, 1 * sign) || clip) {
				super.move(0, 1 * sign);
				this.moveOuterBounds(0, 1 * sign);
			} else {
				clearPath();
				break;
			}
		}

		// move the health bar
		if (bar != null) {
			bar.moveTo(getX(), getY() - 8);
		}

	}

	/**
	 * Determines the shortest path to reach a certain point while avoiding
	 * obstacles
	 * 
	 * @param dx The number of steps on x-coordinate (-dx is west, +dx is east)
	 * @param dy The number of steps on y-coordinate (-dy is south, +dy is north)
	 * @return An array that determines the steps that must be taken in order to
	 *         reach the goal<br>
	 * 
	 *         <b>The return types are as specified.</b>
	 *         <ul>
	 *         <li>0 - North</li>
	 *         <li>1 - North-East</li>
	 *         <li>2 - East</li>
	 *         <li>3 - South-East</li>
	 *         <li>4 - South</li>
	 *         <li>5 - South-West</li>
	 *         <li>6 - West</li>
	 *         <li>7 - North-West</li>
	 *         <li>8 - Terminator</li>
	 *         </ul>
	 */
	public int[] findPath(int dx, int dy) {
		int[] stepInst = new int[100];

		// If no change is requested then the terminated array is returned.
		if (dx == 0 && dy == 0) {
			stepInst[0] = 8;
		}

		// If the path is straight, will check for collisions
		// East Only
		boolean collision = false;
		if (dx > 0 && dy == 0) {
			// Check for solid tiles
			for (int i = 0; i < dx; i++) {
				if (this.getLevel().getTile(getX() >> 3, getY() >> 3).isSolid()) {
					collision = true;
				}
			}

			// If no solid, returns simple movement
			if (collision) {
				for (int i = 0; i < dx; i++)
					stepInst[i] = 2;
				stepInst[dx] = 8;
				return stepInst;
			}
		}

		// West Only
		if (dx < 0 && dy == 0) {
			// Check for solid tiles
			for (int i = 0; i > dx; i--) {
				if (this.getLevel().getTile(getX() >> 3, getY() >> 3).isSolid()) {
					collision = true;
				}
			}

			// If no solid, returns simple movement
			if (collision) {
				for (int i = 0; i > dx; i--)
					stepInst[i] = 6;
				stepInst[dx] = 8;
				return stepInst;
			}
		}

		return stepInst;
	}

	/**
	 * Forces a mob to move out of a collision
	 */
	protected void moveAroundMobCollision() {

		Mob other = collisionStrategy.getMobCollision();

		// if there is no collision, do nothing
		if (other == null)
			return;

		// the direction the mob should go to escape
		int dx = 0;
		int dy = 0;

		// where the two mobs are colliding
		Rectangle intersection = getBounds().intersection(other.getBounds());

		// the center intersection points
		double xx = intersection.getCenterX();
		double yy = intersection.getCenterY();

		// get the optimal direction to escape
		if (xx >= this.getBounds().getCenterX()) {
			dx--;
		}
		if (xx < this.getBounds().getCenterX()) {
			dx++;
		}
		if (yy >= this.getBounds().getCenterY()) {
			dy--;
		}
		if (yy < this.getBounds().getCenterY()) {
			dy++;
		}

		this.move(dx, dy);
	}

	/**
	 * Updates a mob 60 times per second
	 */
	public void tick() {

		tickCount++;
		
		// check for grass
		if( collisionStrategy.isGrassCollision(0, 0))
			setOnGrass(true);
		else
			setOnGrass(false);

		if (knockbackCooldown) {
			if (knockbackTicks++ > knockbackLength) {
				knockbackCooldown = false;
				knockbackTicks = 0;
			}
		}

		// talking cooldown loop
		if (isTalking) {
			talkCount++;
			if (talkCount > TALKING_COOLDOWN) {
				talkCount = 0;
				isTalking = false;
			}
		}

		// damage cooldown loop
		if (isHit) {
			isHitTicks++;
			if (isHitTicks > HIT_COOLDOWN) {
				isHitTicks = 0;
				isHit = false;
			}
		}

		// checks if the mob is on water
		isSwimming = getLevel().getTile((getX() + UNIT_SIZE) >> 3, (getY() + getBounds().height) >> 3).equals(Tile.SEA1)
				|| getLevel().getTile((getX() + UNIT_SIZE) >> 3, (getY() + getBounds().height) >> 3).equals(Tile.SEA2)
				|| getLevel().getTile((getX() + UNIT_SIZE) >> 3, (getY() + getBounds().height) >> 3).equals(Tile.SEA3)
				|| getLevel().getTile((getX() + UNIT_SIZE) >> 3, (getY() + getBounds().height) >> 3).equals(Tile.SEA4);

		// animate bobbing water color
		if (isSwimming) {
			if (tickCount % 60 < 15) {
				waterColor[0] = 0xFF5266FF;
				waterColor[1] = 0xFF000000;
				waterColor[2] = 0xFF000000;
			} else if (tickCount % 60 < 30) {
				waterColor[0] = 0xFF3D54FF;
				waterColor[1] = 0xFF5266FF;
				waterColor[2] = 0xFF000000;
			} else if (tickCount % 60 < 45) {
				waterColor[0] = 0xFF3D54FF;
				waterColor[1] = 0xFF000000;
				waterColor[2] = 0xFF000000;
			} else {
				waterColor[0] = 0xFF5266FF;
				waterColor[1] = 0xFF5266FF;
				waterColor[2] = 0xFF000000;
			}
		}

		// take damage for being on fire
		if (onFire) {

			if (++fireTickCount > secondsToTicks(3)) {
				fireTickCount = 0;
				onFire = false;
			}

			// 10 dps
			if (fireTickCount % 12 == 0) {
				this.damage(1);
			}
			
			// Animates the fire color
			if (tickCount % 60 < 15) {
				firecolor[0] = 0xFFffc403;
				firecolor[1] = 0xFFF7790A;
				firecolor[2] = 0xFFF72808;
			} else if (tickCount % 60 < 30) {
				firecolor[1] = 0xFFffc403;
				firecolor[2] = 0xFFF7790A;
				firecolor[0] = 0xFFF72808;
			} else if (tickCount % 60 < 45) {
				firecolor[2] = 0xFFffc403;
				firecolor[0] = 0xFFF7790A;
				firecolor[1] = 0xFFF72808;
			} else {
				firecolor[0] = 0xFFffc403;
				firecolor[1] = 0xFFF7790A;
				firecolor[2] = 0xFF000000;
			}

		}

		if (onFire && isSwimming) {
			onFire = false;
		}

		// automate movement with a script
		if (path != null && path.isNotEmpty()) {

			// first update the path
			path.update();

			// now make sure it exists after updating
			if (path.isNotEmpty()) {

				// change in x and y
				int dx = 0, dy = 0;

				if (path.next().getDestination().getX() > getX()) {
					dx++;
				} else if (path.next().getDestination().getX() < getX()) {
					dx--;
				}

				if (path.next().getDestination().getY() > getY()) {
					dy++;
				} else if (path.next().getDestination().getY() < getY()) {
					dy--;
				}
				if (dx != 0 || dy != 0) {
					move(dx, dy);
					return;
				}
			}

		}
		

		// force the mob to move around the mob collision
		if (!collisionImmune && collisionStrategy.isMobCollision(0, 0)) {
			moveAroundMobCollision();
			return;
		}

	}

	/**
	 * Displays the pixels of the mob to the screen
	 */
	public void render(Screen screen) {

		// modifier used for rendering in different scales/directions
		int modifier = UNIT_SIZE;

		// no x or y offset, use the upper left corner as absolute
		int xOffset = getX(), yOffset = getY();

		// render only the water animation down the bottom half
		if (isSwimming) {

			// depth effect to water
			yOffset += 4 + ((tickCount % 120 <= 60) ? 1 : 0);

			// water rings
			screen.render(xOffset, yOffset + modifier, 0, 0, SpriteSheet.dynamic, false, waterColor);
			screen.render(xOffset + modifier, yOffset + modifier, 0, 0, SpriteSheet.dynamic, true, waterColor);
		}

		// Handles fire animation
		if (onFire) {

			screen.render(xOffset, yOffset, 0, 1, SpriteSheet.dynamic, false, firecolor);
			screen.render(xOffset + modifier, yOffset, 0, 1, SpriteSheet.dynamic, true, firecolor);

		}
		
		if (onGrass) {
			int feetHeight = this.getBounds().height - modifier;
			if( this.getDirection() == Direction.NORTH || this.getDirection() == Direction.SOUTH ) {
				screen.render(xOffset, yOffset + feetHeight, 0, 2, SpriteSheet.dynamic, false, grassColor);
				screen.render(xOffset + modifier, yOffset + feetHeight, 1, 2, SpriteSheet.dynamic, false, grassColor);
			} else {
				screen.render(xOffset, yOffset + feetHeight, 0, 3, SpriteSheet.dynamic, false, grassColor);
				screen.render(xOffset + modifier, yOffset + feetHeight, 1, 3, SpriteSheet.dynamic, false, grassColor);
			}
			if( this.isDead() ) {
				screen.render(xOffset, yOffset + feetHeight, 2, 3, SpriteSheet.dynamic, false, grassColor);
				screen.render(xOffset + modifier, yOffset + feetHeight, 3, 3, SpriteSheet.dynamic, false, grassColor);
			}
			
		}

		// displays text overhead
		if (isTalking) {
			JJFont.render(name, screen, xOffset - (name.length() * 4 - 8), yOffset - modifier,
					new int[] { 0xFF000000, 0xFF000000, 0xFFFFCC00 }, 1);
		}
		/**
		// displays damage indicators overhead
		if (isHit) {
			int scale = isHitTicks / 8 + 1;
			JJFont.render(damageTaken, screen, xOffset + isHitX - (int) (0.5 * modifier * scale),
					yOffset - modifier + isHitY - (int) (0.5 * modifier * scale), damageIndicatorColor, scale);
		}
		*/
	}

	/**
	 * Returns basic information about the mob
	 */
	public String toString() {
		return super.toString() + "\nName: " + name;
	}

	/**
	 * Triggers the death animation and closure
	 */
	public void remove() {

		// remove the healthbar
		if (bar != null) {
			getLevel().remove(bar);
			bar.setBounds(0, 0, 0, 0);
			bar = null;
		}

		// reset data
		isHit = false;
		isTalking = false;
		isShooting = false;
		setTargeted(false);
		onFire = false;
		knockbackCooldown = false;

	}

	/**
	 * Sets a mob on fire
	 */
	public void ignite() {
		if (!fireImmune) {
			onFire = true;
		}
	}
	

	/**
	 * @param val - whether or not the mob can be ignited
	 */
	public void setFireImmune(boolean val) {
		fireImmune = val;
	}
	
	/**
	 * Sets onGrass to be true so that the grass animation can be rendered
	 * Over the mob's feet
	 */
	public void setOnGrass(boolean val) {
		onGrass = val;
	}
	
	/**
	 * Changes the Grass' Color depending on Grass type
	 * @param val the Grass Color
	 */
	public void setGrassColor(int [] val) {
		grassColor = val;
	}
	
	
	/**
	 * Replenishes the mob health
	 * 
	 * @param num - the amount to heal -1 to fully heal
	 */
	public void heal(int num) {

		// heal to full
		if (num == -1) {
			health = maxHealth;

			// heal by a certain amount
		} else {
			health += num;

			// cant go over max and check for overflow
			if (health > maxHealth || health < -1000) {
				health = maxHealth;
			}
		}

	}

	/**
	 * Knocks back a mob
	 * 
	 * @param strength - strength of the knockback
	 * @param dir      - direction from incoming damage (Mob will move OPPOSITE to
	 *                 this value)
	 */
	public void knockback(int strength, Direction dir) {

		if (knockbackCooldown || isDead()) {
			return;
		}

		// opposite direction
		Direction next = dir;

		// change in movement
		int dx = 0, dy = 0;

		// calculate movement
		switch (dir) {
		case NORTH:
			dy -= strength;
			next = Direction.SOUTH;
			break;
		case SOUTH:
			dy += strength;
			next = Direction.NORTH;
			break;
		case EAST:
			dx += strength;
			next = Direction.WEST;
			break;
		case WEST:
			dx -= strength;
			next = Direction.EAST;
			break;
		default:
			break;
		}

		// uses move method with different name to avoid a bug
		knockbackMove(dx, dy);

		// set the direction towards the player
		setDirection(next);

		knockbackCooldown = true;

	}

	/**
	 * Special duplicate of move to avoid namespace issue with regular move method
	 * that leads to missed method calls with knockback
	 * 
	 * @param dx
	 * @param dy
	 */
	public void knockbackMove(int dx, int dy) {

		// move animation proportional to speed
		numSteps++;

		// sign of movement along x axis
		int sign = 0;
		if (dx < 0) {
			setDirection(Direction.WEST);
			sign = -1;
		} else if (dx > 0) {
			setDirection(Direction.EAST);
			sign = 1;
		}

		// move pixel by pixel for maximum accuracy
		for (int i = 0; i < Math.abs(dx); i++) {
			if (!collisionStrategy.willCollide(1 * sign, 0) || clip) {
				super.move(1 * sign, 0);
				this.moveOuterBounds(1 * sign, 0);
			} else {
				clearPath();
				break;
			}
		}

		// sign of movement along y axis
		if (dy < 0) {
			setDirection(Direction.NORTH);
			sign = -1;
		} else if (dy > 0) {
			setDirection(Direction.SOUTH);
			sign = 1;
		}

		// move pixel by pixel for maximum accuracy
		for (int i = 0; i < Math.abs(dy); i++) {
			if (!collisionStrategy.willCollide(0, 1 * sign) || clip) {
				super.move(0, 1 * sign);
				this.moveOuterBounds(0, 1 * sign);
			} else {
				clearPath();
				break;
			}
		}

		// move the health bar
		if (bar != null) {
			bar.moveTo(getX(), getY() - 8);
		}

	}

	/**
	 * Deals damage to another mob Calculated by getStrength() + a random number in
	 * the range
	 * 
	 * @param range - random offset to add to strength
	 * @param other - the other thing to attack
	 */
	public void attack(int range, Damageable other) {
		other.damage(random.nextInt(range) + getStrength());
	}

	/**
	 * Inflicts damage to this mob Displays an indicator
	 * 
	 * @param damage - the damage inflicted to THIS mob
	 */
	public void damage(int damage) {

		// if the mob is dead, dont do more damage
		if (isDead()) {
			return;
		}
		
		if (!isHit) {

			// subtract from defense
			damage -= getDefense();
			if (damage <= 0) {
				damage = 0;
			}

			// hurt this mob
			hurt(damage);

			// for displaying indicators
			damageTaken = String.valueOf(damage);
			isHit = true;

			// remove a dead mob
			if (health <= 0) {
				remove();
			}
		}
	}

	/**
	 * Decreases a mob's health
	 * 
	 * @param damage - the value of damage
	 */
	protected void hurt(int damage) {
		this.health -= damage;
	}

	/**
	 * Speaks to this mob
	 * 
	 * @param player - the player that started speaking
	 */
	public void speak(Player player) {
	}

	/**
	 * @return the direction the mob is facing/moving
	 */
	public Direction getDirection() {
		return movingDir;
	}

	/**
	 * @return true if the mob is aligned horizontally
	 */
	public boolean isLatitudinal() {
		return getDirection() == Direction.EAST || getDirection() == Direction.WEST;
	}

	/**
	 * @return true if the mob is aligned vertically
	 */
	public boolean isLongitudinal() {
		return getDirection() == Direction.NORTH || getDirection() == Direction.SOUTH;
	}

	/**
	 * Changes the direction of the mob
	 * 
	 * @param dir - the direction the mob should face
	 */
	public void setDirection(Direction dir) {
		this.movingDir = dir;
	}

	/**
	 * @return the outer range of interaction
	 */
	public Rectangle getOuterBounds() {
		return outerBounds;
	}
	
	/**
	 * 
	 * @return the shadow for the mob
	 */
	public Shadow getSpriteShadow() {
		// TODO: Add isChild() method
		return new Shadow(getLevel(), getBounds().width, getBounds().height, isDead(), false);
	}
	
	/**
	 * 
	 * @return whether the mob is swimming or not
	 */
	public boolean getIsSwimming() {
		return isSwimming;
	}

	/**
	 * Changes the outer bounds range
	 * 
	 * @param width  - width of the mob
	 * @param height - height of the mob
	 */
	protected void setOuterBounds(int width, int height) {
		outerBounds = new Rectangle(getX() - OUTER_BOUNDS_RANGE, getY() - OUTER_BOUNDS_RANGE,
				width + OUTER_BOUNDS_RANGE * 2, height + OUTER_BOUNDS_RANGE * 2);
	}

	/**
	 * Changes the outer bounds size Default is 2
	 * 
	 * @param width  - width of the mob
	 * @param height - height of the mob
	 */
	protected void setOuterBoundsRange(int range) {
		outerBounds = new Rectangle(getX() - range, getY() - range, getBounds().width + range * 2,
				getBounds().height + range * 2);
	}

	/**
	 * Updates the location of the outer range
	 * 
	 * @param dx - the change in x
	 * @param dy - the change in y
	 */
	protected void moveOuterBounds(int dx, int dy) {
		outerBounds.translate(dx, dy);
	}

	/**
	 * Moves the mob to the specified x and y coord Also updates the bounds and
	 * outer bounds
	 * 
	 * @param x - the x coord
	 * @param y - the y coord
	 */
	public void moveTo(int x, int y) {
		super.moveTo(x, y);
		outerBounds.setLocation(x - OUTER_BOUNDS_RANGE, y - OUTER_BOUNDS_RANGE);

		if (bar != null) {
			bar.moveTo(getX(), getY() - 8);
		}
	}

	/**
	 * @return true if another mob is targeting this mob
	 */
	public boolean isTargeted() {
		return isTargeted;
	}

	/**
	 * Sets the mob as being targeted by another
	 * 
	 * @param isTargeted
	 */
	public void setTargeted(boolean isTargeted) {
		this.isTargeted = isTargeted;
	}

	/**
	 * @return The mob's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Changes the mob's name
	 * 
	 * @param s the new name
	 */
	public void setName(String s) {
		this.name = new String(s);
	}

	/**
	 * @return the mob's speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Changes the mob's speed
	 * 
	 * @param speed the new speed
	 */
	protected void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Determines if the mob is alive or not
	 * 
	 * @return true if dead
	 */
	public boolean isDead() {
		return health <= 0;
	}

	/**
	 * @return the mob's health bar
	 */
	public HealthBar getHealthBar() {
		return bar;
	}

	/**
	 * @return create the mob's health bar
	 */
	public void createHealthBar() {
		bar = new HealthBar(getLevel(), getX(), getY() - 8, this);
	}

	/**
	 * @return the tile the mob is on
	 */
	public Tile getTile() {
		return getLevel().getTileFromEntityCoords(getX(), getY());
	}

	/**
	 * @return the mob's max health
	 */
	public short getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Changes the mob's max health
	 */
	public void setMaxHealth(short health) {
		this.health = maxHealth = health;
	}

	/**
	 * @return the mob's current health
	 */
	public short getCurrentHealth() {
		return health;
	}

	/**
	 * @return the mob's spritesheet
	 */
	protected SpriteSheet getSpriteSheet() {
		return sheet;
	}

	/**
	 * Sets the mob's spritesheet to this sheet
	 */
	protected void setSpriteSheet(SpriteSheet sheet) {
		this.sheet = sheet;
	}

	/**
	 * @param dest - destination mob
	 */
	public void setPath(Mob dest) {
		if (dest instanceof Player) {
			this.path = PathFactory.make(this, dest, PathFactory.MDP);
		} else {
			this.path = PathFactory.make(this, dest, PathFactory.DIJKSTRA);
		}
	}

	/**
	 * Sets the path to null
	 */
	public void clearPath() {
		path = null;
	}

	/**
	 * @return the path it is following
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * @return the target of this mob
	 */
	public Mob getTarget() {
		return target;
	}

	/**
	 * Toggles whether or not the mob should clip solids
	 */
	protected void toggleClip() {
		clip = !clip;
	}

	@Override
	public long getData() {
		return EntityData.type2(getX(), getY(), getMaxHealth());
	}

}
