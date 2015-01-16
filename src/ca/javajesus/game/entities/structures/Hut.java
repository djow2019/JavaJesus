package ca.javajesus.game.entities.structures;

import ca.javajesus.game.entities.SolidEntity;
import ca.javajesus.game.gfx.Colors;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.game.gfx.Sprite;
import ca.javajesus.level.Level;
import ca.javajesus.level.interior.PoorHouseInterior;

public class Hut extends SolidEntity {
	
	protected int color = Colors.get(-1, 111, Colors.fromHex("#6b420c"), Colors.fromHex("#fcff00"));
	
	public Hut(Level level, double x, double y) {
		super(level, x, y, 32, 32);
		level.addEntity(new Transporter(level, x + 10, y + 16, new PoorHouseInterior()));
	}
	
	public void render(Screen screen) {

		screen.render((int) x, (int) y, color, Sprite.hut_exterior);

	}

}
