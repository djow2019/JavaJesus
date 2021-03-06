package javajesus.entities.solid.buildings;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.Entity;
import javajesus.entities.transporters.Door;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.CastleInterior;

/*
 *  Hillsborough's castle
 */
public class Castle extends Building {

	/**
	 * Creates a castle
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 * @throws IOException 
	 */
	public Castle(Level level, int x, int y) throws IOException {
		super(level, x, y, new int[] { 0xFF111111, 0xFF8D8D8D, 0xFFEEFEFF }, Sprite.castle);
		
		// change bounds
		setBounds(getBounds().x + 12, getBounds().y, getBounds().width - 24, getBounds().height);

		if (level != null)
		level.add(new Door(level, x + 154, y + 160, new CastleInterior(new Point(x + 43, y + 167), getLevel()),0,2));

	}

    @Override
    public byte getId(){
        return Entity.CASTLE;
    }

}
