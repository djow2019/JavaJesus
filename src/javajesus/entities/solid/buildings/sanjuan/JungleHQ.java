package javajesus.entities.solid.buildings.sanjuan;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.Entity;
import javajesus.entities.solid.buildings.Building;
import javajesus.entities.transporters.Door;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.PoorHouseInterior;

/*
 * The head quarters for the jungle place
 */
public class JungleHQ extends Building {

	/**
	 * Creates a jungle hq
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 * @throws IOException 
	 */
	public JungleHQ(Level level, int x, int y) throws IOException {
		super(level, x, y, new int[] { 0xFF111111, 0xFF248F24, 0xFF4D4DFF }, Sprite.jugle_hq);

		if (level != null) {
		level.add(new Door(level, x + 61, y + 80, new PoorHouseInterior(new Point(x + 40, y + 67), getLevel()),0,0));
		level.add(new Door(level, x + 75, y + 80, new PoorHouseInterior(new Point(x + 40, y + 67), getLevel()),0,0));
		}
	}

	@Override
    public byte getId(){
        return Entity.JUNGLE_HQ;
    }
}
