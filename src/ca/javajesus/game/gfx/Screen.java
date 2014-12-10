package ca.javajesus.game.gfx;

import ca.javajesus.game.Game;

public class Screen {

	public static final int MAP_WIDTH = 64;
	public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

	/** 0xA indicates it will be used in binary operations, where A = an integer */
	public static final byte BIT_MIRROR_X = 0x01;
	public static final byte BIT_MIRROR_Y = 0x02;

	public int[] pixels;

	public int xOffset = 0;
	public int yOffset = 0;

	public int width;
	public int height;

	private int spriteSize;

	private Game game;

	public Screen(int width, int height, Game game) {
		this.width = width;
		this.height = height;
		this.game = game;
		pixels = new int[width * height];
		this.spriteSize = 8;

	}

	/**
	 * Renders things onto the screen
	 * 
	 * @param d
	 *            : The x position on the screen in which the tiles are rendered
	 * @param yOffset2
	 *            : The y position on the screen in which the tiles are rendered
	 * @param tile
	 *            : the tile number on the SpriteSheet, used with (xTile) +
	 *            (yTile) * 32
	 * @param colour
	 *            : The color of the sprite. Use with Colours.get()
	 * @param mirrorDir
	 *            : The direction the sprite is facing
	 * @param scale
	 *            : How large the object is, usually 1
	 * @param sheet
	 *            : The SpriteSheet that java will check for using the Tile
	 *            parameter
	 */
	public void render(double d, double yOffset2, int tile, int colour,
			int mirrorDir, double scale, SpriteSheet sheet) {

		d -= xOffset;
		yOffset2 -= yOffset;

		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

		double scaleMap = scale - 1;
		int xTile = tile % 32;
		int yTile = tile / 32;
		int tileOffset = (xTile << 3) + (yTile << 3) * sheet.width;
		for (int y = 0; y < spriteSize; y++) {
			int ySheet = y;
			if (mirrorY)
				ySheet = 7 - y;
			int yPixel = (int) (y + yOffset2 + (y * scaleMap) - ((scaleMap * spriteSize) / 2));
			for (int x = 0; x < spriteSize; x++) {
				int xPixel = (int) (x + d + (x * scaleMap) - ((scaleMap * spriteSize) / 2));
				int xSheet = x;
				if (mirrorX)
					xSheet = 7 - x;

				int col = (colour >> (sheet.pixels[xSheet + ySheet
						* sheet.width + tileOffset] * 8)) & 255;
				if (col < 255) {
					for (int yScale = 0; yScale < scale; yScale++) {
						if (yPixel + yScale < 0 || yPixel + yScale >= height)
							continue;
						for (int xScale = 0; xScale < scale; xScale++) {
							if (x + d < 0 || x + d >= width)
								continue;
							pixels[(xPixel + xScale) + (yPixel + yScale)
									* width] = col;

						}
					}
				}
			}
		}
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public Game getGame() {
		return game;
	}

	/** Used for rendering large entities */
	public void render(int xOffset, int yOffset, int colour, Sprite sprite) {

		xOffset -= this.xOffset;
		yOffset -= this.yOffset;

		for (int y = 0; y < sprite.ySize; y++) {
			int yPixel = (int) (y + yOffset);
			for (int x = 0; x < sprite.xSize; x++) {
				int xPixel = (int) (x + xOffset);
				int col = (colour >> (sprite.pixels[x + y * sprite.xSize] * 8)) & 255;
				if (col < 255) {
					if (xPixel >= 0 && yPixel >= 0 && xPixel < width
							&& yPixel < height)
						pixels[(xPixel) + (yPixel) * width] = col;
				}
			}
		}
	}
	
	/** Used for rendering entities reversed */
    public void render(int xOffset, int yOffset, int dir, int colour, Sprite sprite) {

        xOffset -= this.xOffset;
        yOffset -= this.yOffset;
        
        switch(dir)
        {
        case 3:
            for (int y = 0; y < sprite.ySize; y++) {
                int yPixel = (int) (y + yOffset);
                for (int x = 0; x < sprite.xSize; x++) {
                    int xPixel = (int) (x + xOffset);
                    int col = (colour >> (sprite.pixels[x + y * sprite.xSize] * 8)) & 255;
                    if (col < 255) {
                        if (xPixel >= 0 && yPixel >= 0 && xPixel < width
                                && yPixel < height)
                            pixels[(xPixel) + (yPixel) * width] = col;
                    }
                }
            }
            break;
        case 2:
            for (int y = 0; y < sprite.ySize; y++) {
                int yPixel = (int) (y + yOffset);
                for (int x = 0; x < sprite.xSize; x++) {
                    int xPixel = (int) (x + xOffset);
                    int col = (colour >> (sprite.pixels[(x) + y * sprite.xSize] * 8)) & 255;
                    if (col < 255) {
                        if (xPixel >= 150 && yPixel >= 0 && xPixel < width
                                && yPixel < height)
                            pixels[(7 - xPixel) + (yPixel) * width] = col;
                        else
                        {
                            pixels[(150-xPixel) + (yPixel) * width] = col;
                        }
                    }
                }
            }            
            break;
        case 1:
            for (int y = 0; y < sprite.ySize; y++) {
                int yPixel = (int) (y + yOffset);
                for (int x = 0; x < sprite.xSize; x++) {
                    int xPixel = (int) (x + xOffset);
                    int col = (colour >> (sprite.pixels[x + y * sprite.xSize] * 8)) & 255;
                    if (col < 255) {
                        if (xPixel >= 0 && yPixel >= 0 && xPixel < width
                                && yPixel < height)
                            pixels[(yPixel) + (xPixel) * width] = col;
                    }
                }
            }
            break;
        case 0:
            for (int y = 0; y < sprite.ySize; y++) {
                int yPixel = (int) (y + yOffset);
                for (int x = 0; x < sprite.xSize; x++) {
                    int xPixel = (int) (x + xOffset);
                    int col = (colour >> (sprite.pixels[x + y * sprite.xSize] * 8)) & 255;
                    if (col < 255) {
                        if (xPixel >= 0 && yPixel >= 0 && xPixel < width
                                && yPixel < height)
                            pixels[(yPixel) + (xPixel) * width] = col;
                    }
                }
            }
            break;
        }
        
    }
    
    /*public void render(double d, double yOffset2, int tile, int colour,
            int mirrorDir, double scale, SpriteSheet sheet) {

        d -= xOffset;
        yOffset2 -= yOffset;

        boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
        boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

        double scaleMap = scale - 1;
        int xTile = tile % 32;
        int yTile = tile / 32;
        int tileOffset = (xTile << 3) + (yTile << 3) * sheet.width;
        for (int y = 0; y < spriteSize; y++) {
            int ySheet = y;
            if (mirrorY)
                ySheet = 7 - y;
            int yPixel = (int) (y + yOffset2 + (y * scaleMap) - ((scaleMap * spriteSize) / 2));
            for (int x = 0; x < spriteSize; x++) {
                int xPixel = (int) (x + d + (x * scaleMap) - ((scaleMap * spriteSize) / 2));
                int xSheet = x;
                if (mirrorX)
                    xSheet = 7 - x;

                int col = (colour >> (sheet.pixels[xSheet + ySheet
                        * sheet.width + tileOffset] * 8)) & 255;
                if (col < 255) {
                            pixels[(xPixel + xScale) + (yPixel + yScale)
                                    * width] = col;

                }
            }
        }
    }*/
}
