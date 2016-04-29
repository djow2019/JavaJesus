package game.entities.structures;

import java.awt.Point;
import game.entities.SolidEntity;
import game.entities.structures.transporters.Transporter;
import game.graphics.Sprite;
import level.Level;
import level.interior.PoorHouseInterior;

/*
 * A snazzy cinema
 */
public class SequoiaCinema extends Building {

	private static final long serialVersionUID = -7034832823882430032L;

	public SequoiaCinema(Level level, int x, int y) {
		super(level, x, y, new int[] { 0xFF111111, 0xFF8D1919, 0xFF4D4DFF }, Sprite.sequoiaCinema,
				SolidEntity.TWO_THIRDS);
		level.addEntity(
				new Transporter(level, x + 59, y + 99, new PoorHouseInterior(new Point(x + 40, y + 67), level)));
	}
}
