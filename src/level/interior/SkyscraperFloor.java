package level.interior;

import java.awt.Point;

import game.entities.structures.transporters.TransporterStair;
import level.Level;
import utility.Direction;

public class SkyscraperFloor extends Interior {

	private static final long serialVersionUID = -1944615270414711599L;

	private int floorNum;
	private Level level;

	public SkyscraperFloor(Point point, Level level, int floorNum) {
		super("/Buildings/Generic Interiors/Skyscraper_Interiors/Skyscraper_Floors_1-10.png", new Point(500, 500),
				level);
		this.floorNum = floorNum;
		this.level = level;
	}

	protected void initNPCPlacement() {

	}

	protected void initSpawnerPlacement() {

	}

	protected void initChestPlacement() {

	}

	protected void otherEntityPlacement() {
		if (floorNum < 10) {
			add(new TransporterStair(this, 2112, 1968, new SkyscraperFloor(new Point(2116, 1971), this, floorNum + 1),
					new Point(2105, 2016), Direction.EAST, TransporterStair.WOOD));
		} else {
			add(new TransporterStair(this, 2112, 1968, new SkyscraperPent(new Point(2116, 1971), this),
					new Point(2105, 2016), Direction.EAST, TransporterStair.WOOD));
		}
		add(new TransporterStair(this, 2112, 2008, level, new Point(2105, 1975), Direction.SOUTH,
				TransporterStair.WOOD));
	}

}
