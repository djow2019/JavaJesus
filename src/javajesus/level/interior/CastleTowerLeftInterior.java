package javajesus.level.interior;

import java.awt.Point;

import javajesus.entities.Entity;
import javajesus.entities.Spawner;
import javajesus.entities.npcs.NPC;
import javajesus.entities.structures.furniture.Chest;
import javajesus.entities.structures.transporters.TransporterInterior;
import javajesus.level.Level;

public class CastleTowerLeftInterior extends Interior {

	private static final long serialVersionUID = 660767375624744770L;

	private Point exitPoint;

	public CastleTowerLeftInterior(Point point, Level level) {
		super("/Buildings/Generic Interiors/Castle_Interiors/Castle_Tower_Interior_Left.png", new Point(1935, 2088),
				level);
		this.exitPoint = point;
	}

	protected NPC[] getNPCPlacement() {
		return null;
	}

	protected Spawner[] getSpawnerPlacement() {
		return null;
	}

	protected Chest[] getChestPlacement() {
		return null;
	}

	protected Entity[] getOtherPlacement() {
		return new Entity[] {new TransporterInterior(this, 2104, 2056, new CastleBattlements(exitPoint, nextLevel), exitPoint)};
	}

}