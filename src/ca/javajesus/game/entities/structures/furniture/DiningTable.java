package ca.javajesus.game.entities.structures.furniture;

import ca.javajesus.game.gfx.Colors;
import ca.javajesus.level.Level;

public class DiningTable extends Furniture {
	
	public DiningTable(Level level, int x, int y) {
		super(level, x, y, Furniture.diningTableSprite, Colors.get(-1, 444, 123, 323));
		this.bounds.setSize(getSprite().xSize - 8, getSprite().ySize);
		this.shadow.setSize(16, 3);
		this.bounds.setLocation(x, y);
	}

}