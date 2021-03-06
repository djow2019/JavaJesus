package javajesus.entities.solid.buildings;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.Entity;
import javajesus.entities.transporters.Door;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.TippeeInterior;

/*
 * It looks like tippee is spelled wrong
 */
public class Tippee extends Building {

	/**
	 * Creates a prison
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 * @throws IOException 
	 */
	public Tippee(Level level, int x, int y) throws IOException {
		super(level, x, y, new int[] { 0xFF522900, 0xFF977F66, 0xFF335C33 }, Sprite.tippee);

		if (level != null)
		level.add(new Door(level, x + 10, y + 32, new TippeeInterior(new Point(x + 16, y + 45), level),0,0));
	}

	@Override
    public byte getId(){
        return Entity.TIPPEE;
    }
}
