package javajesus.entities.structures;

import java.awt.Point;

import javajesus.entities.SolidEntity;
import javajesus.entities.structures.transporters.Transporter;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.GunStoreInterior;

/*
 * A gunstore front
 */
public class GunStore extends Building {

	private static final long serialVersionUID = 7439780840552868539L;

	public GunStore(Level level, int x, int y) {
		super(level, x, y, new int[] { 0xFF111111, 0xFFFFFAB0, 0xFFABD3FF }, Sprite.gunstore, SolidEntity.QUARTER);
		
		this.setBounds(getBounds().x + 4, getBounds().y, getBounds().width - 8, getBounds().height);

		level.add(new Transporter(level, x + 29, y + 26,
				new GunStoreInterior(new Point(x + 35, y + 37), getLevel())));
	}

}