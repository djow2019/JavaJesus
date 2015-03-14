package ca.javajesus.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;

import ca.javajesus.level.Level;
import ca.javajesus.level.RandomLevel2;

public class ZombieEndurance extends Game {
	
	private static final long serialVersionUID = 1L;

	public ZombieEndurance() {
		super();
	}
	
	protected Level getLevel() {
		if (player == null) {
			return new RandomLevel2(Level.level1.width,Level.level1.height, new Point(1000, 1000), true);
		}
		return player.getLevel();

	}

	protected void render() {
		super.render();
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setFont(new Font("Verdana", 0, 20));
		g.setColor(Color.YELLOW);
		g.drawString("Score: " + player.score, 700, 20);
		ChatHandler.drawMessages(g);
		g.dispose();
		bs.show();
	}

}
