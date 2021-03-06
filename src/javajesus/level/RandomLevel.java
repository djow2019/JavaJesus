package javajesus.level;

import java.awt.Point;
import java.io.IOException;
import java.util.Random;

import javajesus.entities.Spawner;
import javajesus.entities.npcs.NPC;
import javajesus.entities.solid.buildings.Building;
import javajesus.entities.solid.buildings.CaveEntrance;
import javajesus.entities.solid.buildings.Hut;
import javajesus.entities.solid.buildings.NiceHouse;
import javajesus.entities.solid.buildings.PoorHouse;
import javajesus.level.generation.HeightMap;
import javajesus.level.generation.HeightMapTile;
import javajesus.quest.QuestLoader;
import javajesus.quest.factories.CharacterFactory;

public class RandomLevel extends Level {

	private HeightMapTile[][] heightmap;

	// number of random levels
	private static int numLevels;

	// random generator
	private static final Random random = new Random();

	/**
	 * Generates a random level with smooth terrain based on a simple array noise
	 * map
	 * 
	 * @param spawn - the entrance point
	 */

	public RandomLevel(Point spawn) {
		super("Random Level " + numLevels++, spawn);
	}

	private RandomCave lastCave;

	public RandomLevel(Point spawn, RandomCave lastCave) {
		super("Random Level " + numLevels++, spawn);
		this.lastCave = lastCave;
	}

	@Override
	public void generateLevel() throws IOException {

		// initialize tile array
		levelTiles = new int[LEVEL_WIDTH * LEVEL_HEIGHT];

		// generates last cave if there is one
		if (lastCave != null)
			add(new CaveEntrance(this, getSpawnPoint().x - 18, getSpawnPoint().y - 28, lastCave));

		Random rand = new Random();
		heightmap = new HeightMap(Level.LEVEL_WIDTH, Level.LEVEL_HEIGHT, true, false).generateHeightMap(20);
		for (int row = 0; row < heightmap.length; row++) {
			for (int col = 0; col < heightmap[row].length; col++) {
				int tile = col + row * Level.LEVEL_WIDTH;
				if (heightmap[row][col].getTile() < Level.LEVEL_WIDTH) {
					levelTiles[tile] = ((byte) heightmap[row][col].getTile()) & 0x000000FF;
				}
				if (heightmap[row][col].getHouse()) {
					levelTiles[tile] = ((byte) heightmap[row][col].getTile()) & 0x000000FF;
					add(getBuilding(col * 8, row * 8));
					if (rand.nextInt(4) == 0) {
						add(NPC.getRandomNPC(this, (col - 1) * 8, (row + 6) * 8));
					}
				}
				if (heightmap[row][col].getCave()) {
					levelTiles[tile] = ((byte) heightmap[row][col].getTile()) & 0x000000FF;
					add(new CaveEntrance(this, col * 8 - 18, row * 8 - 28,
							new RandomCave(this, new Point(col * 8, row * 8))));
				}
				if (heightmap[row][col].getSpawner()) {
					levelTiles[tile] = ((byte) heightmap[row][col].getTile()) & 0x000000FF;
					add(new Spawner(this, col * 8, row * 8, Spawner.DEMON));
				}
				if (heightmap[row][col].getTile() == 0) {
					if (row > heightmap.length / 2.0 && col > heightmap[0].length / 2.0) {
						add(new CaveEntrance(this, col * 8 - 18, row * 8 - 28, getSpawnPoint()));

					}
				}
			}
		}

		// now add any non unique quest givers
		CharacterFactory cf = CharacterFactoryFactory.make(this.getLevelId());
		if (cf != null) {
			cf.setNonUniqueCharacters(this);
			QuestLoader.initializeQuests(this);
		}
	}

	private Building getBuilding(int x, int y) throws IOException {
		switch (random.nextInt(10)) {
		case 1:
			return new NiceHouse(this, x, y);
		case 2:
			return new Hut(this, x, y);
		default:
			return new PoorHouse(this, x, y);
		}
	}

}
