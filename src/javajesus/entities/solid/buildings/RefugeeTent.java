package javajesus.entities.solid.buildings;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.Entity;
import javajesus.entities.transporters.Door;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.RefugeeTentInterior;

/*
 * A small tent
 */
public class RefugeeTent extends Building {

	/**
	 * Creates a refugee tent
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 * @throws IOException 
	 */
	public RefugeeTent(Level level, int x, int y) throws IOException {
		super(level, x, y, new int[] { 0xFF111111, 0xFFF8FA8F, 0xFF000000 }, Sprite.refugee_tent);

		if (level != null)
		level.add(new Door(level, x + 34, y + 8, new RefugeeTentInterior(new Point(x + 40, y + 18), level),0,0));
	}

	@Override
    public byte getId(){
        return Entity.REFUGEE_TENT;
    }
}
