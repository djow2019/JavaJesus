package javajesus.entities.structures;

import java.awt.Point;

import javajesus.entities.SolidEntity;
import javajesus.entities.structures.transporters.Transporter;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.CatholicChapelInterior;

/*
 * A place to pray!
 */
public class CatholicChapel extends Building {

	private static final long serialVersionUID = -1699357795777579584L;

	public CatholicChapel(Level level, int x, int y) {
		super(level, x, y, new int[] { 0xFF111111, 0xFFFFFAB0, 0xFFABD3FF }, Sprite.catholic_chapel, SolidEntity.HALF);
		
		this.setBounds(getBounds().x + 4, getBounds().y, getBounds().width - 8, getBounds().height);

		level.add(new Transporter(level, x + 21, y + 47,
				new CatholicChapelInterior(new Point(x + 27, y + 57), getLevel())));
	}

}