package javajesus.entities.solid.buildings.techtopia;

import java.awt.Point;

import javajesus.entities.solid.buildings.Building;
import javajesus.entities.transporters.Transporter;
import javajesus.graphics.Sprite;
import javajesus.level.Level;
import javajesus.level.interior.PoorHouseInterior;

/*
 * Parody of Stanford
 */
public class CardinalUniversity extends Building {

	// serialization
	private static final long serialVersionUID = 8968241141418375189L;

	/**
	 * Creates cardinal unviersity
	 * 
	 * @param level - the level it is on
	 * @param x - the x coord on the level
	 * @param y - the y coord on the level
	 */
	public CardinalUniversity(Level level, int x, int y) {
		super(level, x, y, new int[] { 0xFFFF1111, 0xFFFFFFB2, 0xFFFFFFFF }, Sprite.cardinalUniversity);

		level.add(new Transporter(level, x + 82, y + 40, new PoorHouseInterior(new Point(x + 40, y + 67), level)));
		level.add(new Transporter(level, x + 106, y + 40, new PoorHouseInterior(new Point(x + 40, y + 67), level)));
	}
}