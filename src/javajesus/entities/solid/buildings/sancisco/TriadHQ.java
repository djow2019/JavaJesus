package javajesus.entities.solid.buildings.sancisco;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.Entity;
import javajesus.entities.solid.buildings.Building;
import javajesus.entities.transporters.Door;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.PoorHouseInterior;

/*
 * Triad HQ
 */
public class TriadHQ extends Building {

	/**
	 * Creates a triad hq
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 * @throws IOException 
	 */
	public TriadHQ(Level level, int x, int y) throws IOException {
		super(level, x, y, new int[] { 0xFF335C33, 0xFF8D1919, 0xFF4D4DFF }, Sprite.triad_HQ);

		// change the bounds
		setBounds(getBounds().x + 15, getBounds().y, getBounds().width - 28, getBounds().height);

		if (level != null)
		level.add(new Door(level, x + 90, y + 155, new PoorHouseInterior(new Point(x + 40, y + 67), level),0,0));
	}

	@Override
    public byte getId(){
        return Entity.TRIAD_HQ;
    }
}
