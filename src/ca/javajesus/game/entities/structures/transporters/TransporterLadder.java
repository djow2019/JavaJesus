package ca.javajesus.game.entities.structures.transporters;

import java.awt.Point;

import ca.javajesus.game.graphics.Screen;
import ca.javajesus.game.graphics.SpriteSheet;
import ca.javajesus.level.Level;

public class TransporterLadder extends Transporter {

	public TransporterLadder(Level currentLevel, int x, int y,
			Level nextLevel) {
		super(currentLevel, x, y, nextLevel);
	}

	public TransporterLadder(Level currentLevel, int x, int y,
			Level nextLevel, Point point) {
		super(currentLevel, x, y, nextLevel, point);
	}

	public void render(Screen screen) {
		int[] color = { 0xFF663300, 0xFF663300, 0xFFFFDE00 };
		
		screen.render(x + 0, y - 8, 6 + 5 * 32, color, 0, 1, SpriteSheet.tiles);
		screen.render(x + 0, y + 0, 6 + 6 * 32, color, 0, 1, SpriteSheet.tiles);
	}
}