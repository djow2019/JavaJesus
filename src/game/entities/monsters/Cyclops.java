package game.entities.monsters;

import level.Level;
import utility.Direction;
import game.graphics.Screen;

/*
 * A Cyclops is a powerful monster that strikes fear into any foe
 */
public class Cyclops extends Monster {

	private static final long serialVersionUID = -6014297804180801819L;

	// dimensions of the cyclops
	private static final int WIDTH = 32, HEIGHT = 48;

	// how fast the player toggles steps
	private static final int WALKING_ANIMATION_SPEED = 4;

	// color set of a cyclops
	private static final int[] color = { 0xFF111111, 0xFFFFD99C, 0xFFFFFFFF };

	/**
	 * Creates a cyclops
	 * 
	 * @param level
	 *            the level it is on
	 * @param x
	 *            the x coord
	 * @param y
	 *            the y coord
	 * @param speed
	 *            how fast the cyclops moves
	 * @param health
	 *            the base health
	 */
	public Cyclops(Level level, int x, int y, int speed, int health) {
		super(level, "Cyclops", x, y, speed, WIDTH, HEIGHT, 14, health, 40);

	}

	/**
	 * Displays the cyclops to the screen
	 */
	public void render(Screen screen) {
		super.render(screen);

		// modifier used for rendering in different scales/directions
		int modifier = UNIT_SIZE * getScale();

		// no x or y offset, use the upper left corner as absolute
		int xOffset = getX(), yOffset = getY();

		// the horizontal position on the spritesheet
		int xTile = 0;

		// whether or not to render backwards
		// Cyclops has an asymmetric drumstick so it fake flips!
		boolean flip = ((numSteps >> WALKING_ANIMATION_SPEED) & 1) == 1;

		// adjust spritesheet offsets
		if (getDirection() == Direction.NORTH) {
			xTile = 24;
			if (flip) {
				xTile += 4;
				flip = false;
			}
		} else if (getDirection() == Direction.SOUTH) {
			xTile = 4;
			if (flip) {
				xTile += 4;
				flip = false;
			}
		} else {
			xTile = 12 + (flip ? 4 : 0);
			flip = getDirection() == Direction.WEST;
		}

		// position of walking or attacking
		int yTile = this.yTile;

		// dead has an absolute position
		if (isDead()) {
			flip = false;
			xTile = 32;
			yTile = 18;
		}

		// attacking animation
		if (isShooting) {
			yTile += 6;
		}

		// draw all 6 rows
		for (int i = 0; i < 6; i++) {

			// dead display only has 2 rows
			if (isDead() && i > 1) {
				break;
			}

			// left
			screen.render(xOffset + (modifier * (flip ? 3 : 0)), yOffset + i * modifier,
					xTile + (yTile + i) * getSpriteSheet().boxes, color, flip, getScale(), getSpriteSheet());

			// left center
			screen.render(xOffset + modifier + (modifier * (flip ? 1 : 0)), yOffset + i * modifier,
					(xTile + 1) + (yTile + i) * getSpriteSheet().boxes, color, flip, getScale(), getSpriteSheet());

			// right center
			screen.render(xOffset + 2 * modifier - (modifier * (flip ? 1 : 0)), yOffset + i * modifier,
					(xTile + 2) + (yTile + i) * getSpriteSheet().boxes, color, flip, getScale(), getSpriteSheet());

			// right
			screen.render(xOffset + 3 * modifier - (modifier * (flip ? 3 : 0)), yOffset + i * modifier,
					(xTile + 3) + (yTile + i) * getSpriteSheet().boxes, color, flip, getScale(), getSpriteSheet());

			if (isDead()) {
				screen.render(xOffset + 4 * modifier - (modifier * (flip ? 3 : 0)), yOffset + i * modifier,
						(xTile + 4) + (yTile + i) * getSpriteSheet().boxes, color, flip, getScale(), getSpriteSheet());

				screen.render(xOffset + 5 * modifier - (modifier * (flip ? 1 : 0)), yOffset + i * modifier,
						(xTile + 5) + (yTile + i) * getSpriteSheet().boxes, color, flip, getScale(), getSpriteSheet());

			}
		}

	}

	/**
	 * @return the Cyclop's strength
	 */
	public int getStrength() {
		return 20;
	}

}