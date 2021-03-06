package editors;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JPanel;

import javajesus.entities.Damageable;
import javajesus.entities.Entity;
import javajesus.entities.Type;
import javajesus.graphics.Screen;

public class EntityGUI extends JPanel {
	
	// serialization
	private static final long serialVersionUID = 1L;

	// size of the item frame
	private static final int SIZE = 8;
	
	// the buffered image to render on the screen
	private final BufferedImage image;
	
	// pixel data of the buffered image
	int[] pixels;
	
	// the entity to display
	private final Entity entity;
	
	// holds the pixel data, large number to ensure building is contained
	private final Screen screen;
	
	// the number of tiles in each direction
	private int xTiles, yTiles;
	
	/**
	 * EntityGUI ctor()
	 * 
	 * @param panelWidth - width of the JPanel
	 * @param panelHeight - height of the Jpanel
	 * @param xTiles - tile width of the entity
	 * @param yTiles - tile height of the entity
	 */
	public EntityGUI(Entity entity, int panelWidth, int panelHeight, int xTiles, int yTiles) {
		
		// instance data
		this.entity = entity;
		this.xTiles = xTiles;
		this.yTiles = yTiles;
		screen = new Screen(xTiles * 8, yTiles * 8);
		
		// create the image
		image = new BufferedImage(SIZE * xTiles, SIZE * yTiles, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		// render the entity
		entity.render(screen);
		
		// set the size
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		
	}
	
	/**
	 * @param health - the max health of this entity
	 */
	public void setExtra1(short health) {
		if (entity instanceof Damageable) {
			((Damageable) entity).setMaxHealth(health);
		}
	}

	/**
	 * @param type - the type information of this entity
	 */
	public void setExtra2(byte type) {
		if (entity instanceof Type) {
			((Type) entity).setType(type);
			
			// clear the screen first
			screen.clear();
			
			// render the entity
			entity.render(screen);
		}
	}
	
	/**
	 * render()
	 * Renders these pixels onto screen pixels
	 * 
	 * @param screen - screen to render on
	 * @param xTile - the x coordinate of the entity
	 * @param yTile - the y coordinate of the entity
	 */
	public void render(Screen screen, int xTile, int yTile) {
		screen.renderChunk(this.screen, xTile, yTile);
	}
	
	/**
	 * Display the image of the item
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		// add screen pixels to image pixels
		for (int y = 0; y < yTiles * SIZE; y++) {
			for (int x = 0; x < xTiles * SIZE; x++) {
				pixels[x + y * (xTiles * SIZE)] = screen.getPixels()[x + y * screen.getWidth()];
			}

		}
		
		// draw the image
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
	
	/**
	 * @return the width in tiles
	 */
	public int getXTiles() {
		return xTiles;
	}
	
	/**
	 * @return the height in tiles
	 */
	public int getYTiles() {
		return yTiles;
	}
	
	/**
	 * @return the entity it contains
	 */
	public Entity getEntity() {
		return entity;
	}

}
