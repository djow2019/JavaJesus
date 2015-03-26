package ca.javajesus.game.entities.structures;

import java.awt.Point;
import java.awt.Rectangle;

import ca.javajesus.game.entities.SolidEntity;
import ca.javajesus.game.entities.structures.transporters.Transporter;
import ca.javajesus.game.graphics.Screen;
import ca.javajesus.game.graphics.Sprite;
import ca.javajesus.level.Level;
import ca.javajesus.level.interior.CastleInterior;

public class RussianOrthodoxChurch extends SolidEntity {

	public RussianOrthodoxChurch(Level level, int x, int y) {
		super(level, x, y, 96, 80);
		this.shadow = new Rectangle(width, (5 * height / 6));
		this.shadow.setLocation(x + 12, y);
		this.bounds = new Rectangle(width, (height / 6) - 8);
		this.bounds.setLocation(x + 12, y + shadow.height);
		level.addEntity(new Transporter(level, x + 43, y + 64,
				new CastleInterior(new Point(x + 43, y + 167), this.level)));

	}

	public void render(Screen screen) {

		screen.render((int) x, (int) y, new int[] {0xFF111111, 0xFF0069AC, 0xFFFFBC02 }, Sprite.russian_orthodox_church);

	}

}