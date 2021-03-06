package javajesus.entities.transporters;

import javajesus.graphics.Screen;
import javajesus.graphics.SpriteSheet;
import javajesus.level.Level;
import javajesus.utility.Direction;

/*
 * Stairs
 */
public class Stairs extends Transporter {

	// the different textures for stairs
	public static final int WOOD = 11, STONE = 7, CARPET = 9;

	// different colors for different textures
	private static final int[] COLOR_WOOD = { 0xFF111111, 0xFF704200, 0xFF000000 },
			COLOR_STONE = { 0xFF4c4c4c, 0xFF909090, 0xFF636363 }, COLOR_CARPET = { 0xFF111111, 0xFF704200, 0xFF1F7901 };

	// the direction the stairs are facing
	private Direction direction;

	// the offset on the spritesheet
	private int xTile;

	// the colorset
	private int[] color;

	// size of each tile
	private static final int SIZE = 8;

	/**
	 * Creates stairs
	 * 
	 * @param currentLevel - the level it is on
	 * @param x - the x coord
	 * @param y - the y coord
	 * @param outside - the level it goes to
	 * @param direction - the direction the stairs are facing
	 * @param type - the material of the stairs Ex: Stairs.WOOD
	 */
	public Stairs(Level currentLevel, int x, int y, Level nextLevel, Direction direction, int type) {
		super(currentLevel, x, y, 0 , 0, nextLevel);

		// instance data
		this.direction = direction;
		xTile = type;
		assignColor();
		
		// set the bounds
		switch (direction) {
		case NORTH:
		case SOUTH:
			getBounds().setSize(SIZE * 2, SIZE);
			break;
		case EAST:
		case WEST:
			getBounds().setSize(SIZE, SIZE * 2);
			break;
		default:
			break;
		}
	}

	/**
	 * Assigns the appropriate color for the type
	 */
	private void assignColor() {
		switch (xTile) {
		case WOOD:
			color = COLOR_WOOD;
			break;
		case STONE:
			color = COLOR_STONE;
			break;
		default:
			color = COLOR_CARPET;
			break;
		}
	}

	/**
	 * Displays the stairs on the screen
	 */
	public void render(Screen screen) {

		switch (direction) {
		case NORTH:
			screen.render(getX(), getY(), xTile, 6, SpriteSheet.stairs, false, color);
			screen.render(getX() + SIZE, getY(), xTile + 1, 6, SpriteSheet.stairs, false, color);
			break;
		case EAST:
			screen.render(getX(), getY(), xTile, 7, SpriteSheet.stairs, false, color);
			screen.render(getX(), getY() + SIZE, xTile, 8, SpriteSheet.stairs, false, color);
			break;
		case WEST:
			screen.render(getX(), getY(), xTile, 5, SpriteSheet.stairs, false, color);
			screen.render(getX() + SIZE, getY(), xTile + 1, 5, SpriteSheet.stairs, false, color);
			break;
		default:
			screen.render(getX(), getY(), xTile, +9, SpriteSheet.stairs, false, color);
			screen.render(getX(), getY() + SIZE, xTile, 10, SpriteSheet.stairs, false, color);
			break;
		}
	}
}
