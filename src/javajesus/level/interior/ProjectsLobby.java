package javajesus.level.interior;

import java.awt.Point;

import javajesus.entities.Entity;
import javajesus.entities.Spawner;
import javajesus.entities.npcs.NPC;
import javajesus.entities.solid.furniture.Chest;
import javajesus.entities.transporters.TransporterInterior;
import javajesus.entities.transporters.TransporterStair;
import javajesus.level.Level;
import javajesus.utility.Direction;

public class ProjectsLobby extends Interior {

	private static final long serialVersionUID = 8102425096759263851L;

	private Point exitPoint;

	public ProjectsLobby(Point point, Level level) {
		super("/VISUAL_DATA/STATICS/ARCHITECTURE/GENERIC/INTERIORS/Projects_Interiors/Projects_Lobby.png", new Point(584, 680), level);
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
		return new Entity[] { new TransporterInterior(this, 585, 700, nextLevel, exitPoint),
				new TransporterStair(this, 344, 472, new ProjectsFloor(new Point(353, 536), this, 1),
						new Point(353, 536), Direction.WEST, TransporterStair.CARPET) };
	}
}
