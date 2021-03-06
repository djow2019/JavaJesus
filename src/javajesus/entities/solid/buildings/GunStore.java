package javajesus.entities.solid.buildings;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.Entity;
import javajesus.entities.transporters.Door;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.GunStoreInterior;

/*
 * A gunstore front
 */
public class GunStore extends Building {

	/**
	 * Creates a gunstore
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 * @throws IOException 
	 */
	public GunStore(Level level, int x, int y) throws IOException {
		super(level, x, y, new int[] { 0xFF111111, 0xFFFFFAB0, 0xFFABD3FF }, Sprite.gunstore);

		if (level != null)
		level.add(new Door(level, x + 30, y + 24, new GunStoreInterior(new Point(x + 35, y + 37), getLevel()),0,0));
	}

	@Override
    public byte getId(){
        return Entity.GUNSTORE ;
    }
}
