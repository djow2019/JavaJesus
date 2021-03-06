package javajesus.entities.solid.buildings;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.Entity;
import javajesus.entities.transporters.Door;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.ApartmentLobby;

/*
 * An apartment building 
 */
public class ApartmentHighRise extends Building {

	/**
	 * Creates an apartment
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 * @throws IOException 
	 */
	public ApartmentHighRise(Level level, int x, int y) throws IOException {
		super(level, x, y, new int[] { 0xFF111111, 0xFF673101, 0xFFABD3FF }, Sprite.apartment);

		if (level != null)
		getLevel().add(new Door(level, x + 31, y + 208, new ApartmentLobby(new Point(x + 36, y + 216), level),0,0));
	}

    @Override
    public byte getId(){
        return Entity.APARTMENT_HIGH_RISE;
    }

}
