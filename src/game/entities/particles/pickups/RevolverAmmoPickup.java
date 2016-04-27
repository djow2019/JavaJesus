package game.entities.particles.pickups;

import level.Level;
import items.Item;

public class RevolverAmmoPickup extends Pickup {
	
	private static final long serialVersionUID = -5425164885383677634L;

	public RevolverAmmoPickup(Level level, int x, int y) {
		super(level, x, y, Item.revolverAmmo, new int[] { 0xFFFFFFFF,
				0xFF990000, 0xFFFF0000 }, 2, 6, 20);
	}

}