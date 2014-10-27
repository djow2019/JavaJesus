package ca.javajesus.game.entities;

import java.awt.Rectangle;
import java.util.Random;

import ca.javajesus.game.gfx.Colors;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.level.Level;

public class HealthPack extends Particle {
	
	private Random random = new Random();
	private static int healthPackColour = Colors.get(-1, 0x00FFfffffa, 555, 500);
	private final Rectangle BOX = new Rectangle(10, 10);

	public HealthPack(Level level, double x, double y) {
		super(level, 9, healthPackColour, x, y);

		this.x += random.nextInt(400) - 200;
		this.y += random.nextInt(400) - 200;
	}

	public void render(Screen screen) {

		screen.render(this.x, this.y, tileNumber, color, 1, 1, sheet);
		BOX.setLocation((int) this.x, (int) this.y);
		for (Entity entity : level.getEntities()) {

			if (entity instanceof Mob) {
				if (BOX.intersects(((Mob) entity).hitBox)) {
					((Mob) entity).health = 100;
					level.remEntity(this);
				}
			}

		}
	}

}