package javajesus.entities.solid.buildings.techtopia;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.Entity;
import javajesus.entities.solid.buildings.Building;
import javajesus.entities.transporters.Door;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.PoorHouseInterior;

/*
 * Tech topia city hall!
 */
public class TechTopiaCityHall extends Building {

	/**
	 * Creates tech topia city hall
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 * @throws IOException 
	 */
	public TechTopiaCityHall(Level level, int x, int y) throws IOException {
		super(level, x, y, new int[] { 0xFF283A28, 0xFF1F5C1F, 0xFFABD3FF }, Sprite.techTopia_city_hall);

		// change the bounds
		setBounds(getBounds().x + 15, getBounds().y, getBounds().width - 28, getBounds().height);

		if (level != null)
		level.add(new Door(level, x + 42, y + 80, new PoorHouseInterior(new Point(x + 40, y + 67), level),0,0));
	}
	
	@Override
    public byte getId(){
        return Entity.TECHTOPIA_CITY_HALL;
    }
}
