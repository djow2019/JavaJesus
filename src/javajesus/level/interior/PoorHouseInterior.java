package javajesus.level.interior;

import java.awt.Point;

import javajesus.entities.Entity;
import javajesus.entities.Spawner;
import javajesus.entities.npcs.NPC;
import javajesus.entities.structures.furniture.Chest;
import javajesus.entities.structures.transporters.TransporterInterior;
import javajesus.level.Level;

public class PoorHouseInterior extends Interior {

	private static final long serialVersionUID = -4406965337751303583L;

	private Point exitPoint;

	public PoorHouseInterior(Point point, Level level) {
		super("/Buildings/Generic Interiors/Generic_Poor_House_Interior.png", new Point(256, 272), level);
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
		return new Entity[] {new TransporterInterior(this, 252, 278, nextLevel, exitPoint)};
		
		
	}

}