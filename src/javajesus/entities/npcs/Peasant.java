package javajesus.entities.npcs;

import javajesus.entities.Entity;
import javajesus.entities.Type;
import javajesus.level.Level;

/*
 * Generic Peasant npc
 */
public class Peasant extends NPC implements Type {

	// types of peasants
	public static final int MALE = 0, FEMALE = 1, BOY = 2, GIRL = 3;
	
	// the type of peasant
	private byte type;

	/**
	 * Peasant ctor()
	 * 
	 * @param level - level it is on
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param type - Peasant.MALE/FEMALE/BOY/GIRL
	 */
	public Peasant(Level level, int x, int y, int type) {
		this(level, x, y, type, "", 0);
	}
	
	/**
	 * Peasant ctor()
	 * 
	 * @param level - level it is on
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param type - type of peasant to render
	 * @param walkPath - walk path
	 * @param walkDistance - walk distance
	 */
	public Peasant(Level level, int x, int y, int type, String walkPath, int walkDistance) {
		super(level, "Peasant", x, y, 1, 16, 16, 100, new int[] { 0xFF111111,
				0xFF715B17, 0xFFEDC5AB , 0xFF49250a, 0}, 0, 19, walkPath, walkDistance);
		
		// instance data
		this.type = (byte) type;
		
		// calculate the tile offsets
		update();
	}
	
	/**
	 * Sets the correct x and y tiles on the spritesheet
	 */
	private void update() {
		switch (type) {
		case FEMALE: {
			xTile = 0;
			yTile = 21;
			break;
		}
		case BOY: {
			xTile = 14;
			yTile = 19;
			break;
		}
		case GIRL: {
			xTile = 14;
			yTile = 21;
			break;
		}
		default: // MALE
			xTile = 0;
			yTile = 19;
			break;
		}
	}

	@Override
	public int getStrength() {
		return 0;
	}

	@Override
	public int getDefense() {
		return 0;
	}

	@Override
	public byte getId() {
		return Entity.PEASANT;
	}

	@Override
	public byte getType() {
		return type;
	}

	@Override
	public void setType(byte type) {
		this.type = type;
		update();
	}

}
