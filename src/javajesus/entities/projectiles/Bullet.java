package javajesus.entities.projectiles;

import javax.sound.sampled.Clip;

import javajesus.entities.Mob;
import javajesus.level.Level;
import javajesus.utility.Direction;

/*
 * A bullet projectile
 */
public class Bullet extends Projectile {

	// serialization
	private static final long serialVersionUID = 6386517640048489710L;

	// the color set for all bullets
	private static final int[] color = { 0xFF000000, 0xFF000000, 0xFFFFFF00 };

	/**
	 * Creates a bullet with a single direction
	 * 
	 * @param level -  What level it renders on
	 * @param x - The X position it will spawn at
	 * @param y - The Y position it will spawn at
	 * @param xTile - the x tile on the spritesheet
	 * @param yTile - the FIRST y tile on the spritesheet
	 * @param direction -  The direction it will move; NORTH, SOUTH, EAST, or WEST
	 * @param mob - the mob that fired the projectile
	 * @param damage - the damage this projectile should do on impact
	 * @param clip - the sound this projectile makes on fire
	 */
	public Bullet(Level level, int x, int y, Direction direction, Mob mob, int damage, final Clip clip) {
		super(level, x, y, 2, 1, 1, 2, 1, 0, 3, direction, mob, damage, color, clip);
	}

	/**
	 * Creates a bullet with complex direction
	 * 
	 * @param level -  What level it renders on
	 * @param x - The X position it will spawn at
	 * @param y - The Y position it will spawn at
	 * @param xTile - the x tile on the spritesheet
	 * @param yTile - the FIRST y tile on the spritesheet
	 * @param xPos - the x coordinate it will travel to
	 * @param yPos -  the y coordinate it will travel to
	 * @param mob - the mob that fired the projectile
	 * @param damage - the damage this projectile should do on impact
	 * @param clip - the sound this projectile makes on fire
	 */
	public Bullet(Level level, int x, int y, int xPos, int yPos, Mob mob, int damage, final Clip clip) {
		super(level, x, y, 2, 1, 1, 2, 1, 0, 3, xPos, yPos, mob, damage, color, clip);

	}

}
