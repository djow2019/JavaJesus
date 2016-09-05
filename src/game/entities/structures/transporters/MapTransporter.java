package game.entities.structures.transporters;

import game.entities.Player;
import game.graphics.Screen;
import level.Level;
import level.story.BautistasDomain;
import level.story.EdgeOfTheWoods;
import level.story.EdgeOfTheWoodsTop;
import level.story.LordHillsboroughsDomain;
import level.story.OrchardValley;
import level.story.SanCisco;
import level.story.SanJuan;
import level.story.TechTopia;
import utility.Direction;

/*
 * A map transporter is placed on the edges of a level to transition  between two outisde levels
 */
public class MapTransporter extends Transporter {

	private static final long serialVersionUID = -788215519977991994L;

	// which side the map transporter is placed on a map
	private Direction dir;

	// name of the next level
	private String savedLevel;

	/**
	 * Creates a Map transporter that automatically transports a player that is
	 * on the edge of a map
	 * 
	 * @param currentLevel
	 *            the level it is on
	 * @param x
	 *            the x coord
	 * @param y
	 *            the y coord
	 * @param nextLevel
	 *            the next level
	 * @param dir
	 *            the side on the map
	 * @param width
	 *            the width of the transporter
	 * @param height
	 *            the height of the transporter
	 * 
	 *            Map Transporters dont use the next level variable to avoid
	 *            saving recursion, so next Level will always return null.
	 * 
	 *            Use savedLevel instead, the name of the nextLevel
	 */
	public MapTransporter(Level currentLevel, int x, int y, String savedLevel,
			Direction dir, int width, int height) {
		super(currentLevel, x, y, null);
		this.dir = dir;
		setBounds(getX(), getY(), width, height);

		this.savedLevel = savedLevel;
	}
	
	/**
	 * Overwrites the returning nextLevel
	 * Instead it returns the static instance of the city it leads to based on the name
	 */
	public Level getNextLevel() {
		
		switch (savedLevel) {
		
		case Level.BAUTISTA:
			return BautistasDomain.level;
		case Level.EDGE_MAIN: 
			return EdgeOfTheWoods.level;
		case Level.EDGE_TOP: 
			return EdgeOfTheWoodsTop.level;
		case Level.HILLSBOROUGH:
			return LordHillsboroughsDomain.level;
		case Level.ORCHARD:
			return OrchardValley.level;
		case Level.CISCO:
			return SanCisco.level;
		case Level.JUAN:
			return SanJuan.level;
		case Level.TECH:
			return TechTopia.level;
		default:
			System.err.println("Could not map transporter level");
			return null;
		}
		
	}

	/**
	 * Calculates the new spawnpoint for the next level
	 */
	public void calcNewSpawn(Player player) {

		// the x and y spawn coords
		int x, y;

		switch (dir) {

		// NORTH EDGE OF MAP
		case NORTH: {

			// X is proportional to width
			x = getNextLevel().getWidth() * player.getX()
					/ getLevel().getWidth();

			y = (getNextLevel().getHeight() * 8) - 16;
			break;
		}

		// SOUTH EDGE OF MAP
		case SOUTH: {

			// X is proportional to width
			x = getNextLevel().getWidth() * player.getX()
					/ getLevel().getWidth();

			y = 16;
			break;
		}

		// EAST EDGE OF MAP
		case EAST: {

			x = 16;

			// Y is proportional to height
			y = getNextLevel().getHeight() * player.getY()
					/ getLevel().getHeight();
			break;
		}

		// WEST EDGE OF MAP
		default: {

			x = (getNextLevel().getWidth() * 8) - 16;

			// Y is proportional to height
			y = getNextLevel().getHeight() * player.getY()
					/ getLevel().getHeight();
			break;
		}
		}

		// update the next level spawnpoint
		getNextLevel().setSpawnPoint(x, y);
	}

	/**
	 * Blank because it is invisible
	 */
	public void render(Screen screen) {
	}

}
