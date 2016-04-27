package game.gui.slots;

import game.Game;
import game.graphics.Screen;
import game.graphics.SpriteSheet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class PlayerSlotGUI extends Slot {

	private static final long serialVersionUID = 1L;

	private Screen screen;
	private BufferedImage playerImage;
	private int[] pixels;
	
	private static final int PLAYER_WIDTH = 16, PLAYER_HEIGHT = 16;
	
	private double heightRatio;

	public PlayerSlotGUI(int width, int height, String file, double heightRatio) {
		super(file);

		this.heightRatio = heightRatio;
		this.setPreferredSize(new Dimension(width, height));
		playerImage = new BufferedImage(PLAYER_WIDTH, PLAYER_HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) playerImage.getRaster().getDataBuffer())
				.getData();
		screen = new Screen(PLAYER_WIDTH, PLAYER_HEIGHT);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (int y = 0; y < screen.getHeight(); y++) {
			for (int x = 0; x < screen.getWidth(); x++) {
				pixels[x + y * (PLAYER_WIDTH)] = screen.getPixels()[x + y * screen.getWidth()];
			}

		}

		renderPlayer(screen, 1);

		int xOffset = this.getWidth()  / 8;
		int yOffset = (int) (this.getHeight() * ((1 - heightRatio) / 2));

		g.drawImage(playerImage, xOffset, yOffset, this.getWidth() * 3 / 4,
				(int) (this.getHeight() * heightRatio), null);
		g.setColor(Color.YELLOW);
		g.setFont(new Font(Game.FONT_NAME, 0, 50));
		g.drawString(Game.player.toString(), 50, 65);
	}

	public void renderPlayer(Screen screen, int scale) {

		int modifier = 8 * scale;
		int xOffset = 0;
		int yOffset = 0;

		int xTile = 0, yTile = 0;
		int[] color = Game.player.getColor();
		SpriteSheet sheet = SpriteSheet.player;

		// Upper body 1
		screen.render(xOffset, yOffset, xTile + yTile
				* sheet.boxes, color, false, scale, sheet);
		// Upper Body 2
		screen.render(xOffset + modifier, yOffset,
				(xTile + 1) + yTile * sheet.boxes, color, false, scale, sheet);

		// Lower Body 1
		screen.render(xOffset, yOffset + modifier, xTile
				+ (yTile + 1) * sheet.boxes, color, false, scale, sheet);
		// Lower Body 2
		screen.render(xOffset + modifier, yOffset
				+ modifier, (xTile + 1) + (yTile + 1) * sheet.boxes, color,
				false, scale, sheet);

	}
	
}