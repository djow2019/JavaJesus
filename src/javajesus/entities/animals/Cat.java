package javajesus.entities.animals;

import javajesus.entities.Entity;
import javajesus.graphics.SpriteSheet;
import javajesus.level.Level;

/*
 *	A Cat Mob 
 */
public class Cat extends Animal {
	
	// color of the cat
	private static final int[] color = { 0xFF111111, 0xFFf3cdf6, 0xFFeaeaea, 0xFFa6a6a6, 0xFF111111 };

	/**
	 * @param level - level it is on
	 * @param x - x coord
	 * @param y - y coord
	 */
	public Cat(Level level, int x, int y) {
		super(level, "Cat", x, y, 16, 16, SpriteSheet.quadrapeds, 10, color);
	}

	@Override
	public byte getId() {
		return Entity.CAT;
	}

}
