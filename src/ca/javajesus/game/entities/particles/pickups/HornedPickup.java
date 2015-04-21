package ca.javajesus.game.entities.particles.pickups;

import ca.javajesus.items.Item;
import ca.javajesus.level.Level;

public class HornedPickup extends Pickup {
	
	private static final long serialVersionUID = 2925048197267329226L;

	public HornedPickup(Level level, int x, int y) {
		super(level, x, y, Item.horned, new int[] { 0xFFFFFFFF,
				0xFF990000, 0xFFFF0000 }, 2, 9, 1);
	}

}
