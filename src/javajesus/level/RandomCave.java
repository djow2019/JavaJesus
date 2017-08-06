package javajesus.level;

import java.awt.Point;

import javax.sound.sampled.Clip;

import javajesus.SoundHandler;
import javajesus.entities.Chest;
import javajesus.entities.Spawner;
import javajesus.entities.transporters.Ladder;
import javajesus.level.generation.CaveGeneration;
import javajesus.level.tile.Tile;

/*
 * A randomly generated cave
 */
public class RandomCave extends Level {

	// unfiltered tile list of the cave
	private int[][] caveMap;
	
	// the level before entering this cave
	private Level prevLevel;

	// the number of caves there currently are
	private static int numCaves;

	/**
	 * Generates a random cave level
	 * 
	 * @param cycles - the cycles
	 * @param prevLevel - the previous level
	 * @param spawn - the spawn point in this random cave
	 */
	public RandomCave(int cycles, Level prevLevel, Point spawn) {
		super("Random Cave " + numCaves++, spawn);
		
		// instance data
		this.prevLevel = prevLevel;
		
		// fill the tile map
		generateLevel(3);
	}
	
	/**
	 * Generates a random cave level
	 * 
	 * @param prevLevel - the previous level
	 * @param spawn - the spawn point in this random cave
	 */
	public RandomCave(Level prevLevel, Point spawn) {
		this(3, prevLevel, spawn);
	}
	
	/**
	 * Fills the cave map tiles with real tiles
	 * 
	 * @param cycles - the smoothness of the generation
	 */
	protected void generateLevel(int cycles) {

		// fill the cave map tiles
		caveMap = CaveGeneration.generateCave(cycles);

		// whether or not a proper spawn point is found
		boolean spawnFound = false;
		
		// iterate through the tiles
		for (int row = 0; row < Level.LEVEL_HEIGHT; row++) {
			for (int col = 0; col < Level.LEVEL_WIDTH; col++) {
				
				// get the current tile
				int tile = col + row * Level.LEVEL_WIDTH;
				
				// Set the spawn point to a location where the player can move
				if (caveMap[row][col] == CaveGeneration.FLOOR) {
					levelTiles[tile] = Tile.CAVEFLOOR.getId();
					if (row > 5 * 8 && col > 5 * 8) {
						if (!spawnFound) {
							setSpawnPoint(col * 8, row * 8);
							add(new Ladder(this, col * 8, row * 8, prevLevel));
							spawnFound = true;
						}
					}
				}
					
				// Add in the wall tiles and entities
				switch (caveMap[row][col]) {
				case CaveGeneration.CAVE_WALL:
					levelTiles[tile] = Tile.CAVEWALL_ROCK.getId();
					break;
				case CaveGeneration.CAVE_BORDER_WALL:
					levelTiles[tile] = Tile.CAVEWALL.getId();
					break;
				case CaveGeneration.FLOOR_CHEST: 
					levelTiles[tile] = Tile.CAVEFLOOR.getId();
					add(new Chest(this, col * 8, row * 8, (byte) 1));
					break;
				case CaveGeneration.FLOOR_SPAWNER:
					levelTiles[tile] = Tile.CAVEFLOOR1.getId();
					add(new Spawner(this, col * 8, row * 8, Spawner.DEMON, 5));
					break;
				default:
					break;
				}
					
				
//				else if (caveMap[row][col] == 6) {
//					levelTiles[tile] = Tile.CAVEFLOOR_ROCK.getId();
//					add(new Ladder(this, col * 8, row * 8,
//							new RandomLevel(new Point(col * 8, row * 8), this)/*, new Point(col * 8, row * 8)*/));
//
//				}
			}
		}
	}
	
	public Clip getBackgroundMusic() {
		return SoundHandler.background2;
	}

}
