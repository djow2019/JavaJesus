package javajesus.level.sandbox;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.Chest;
import javajesus.entities.Entity;
import javajesus.entities.Spawner;
import javajesus.entities.npcs.NPC;
import javajesus.entities.solid.trees.Forest;
import javajesus.entities.transporters.MapEdge;
import javajesus.level.Level;

/*
 * Fixed level for sandbox mode
 */
public class SandboxIslandLevel extends Level {

	/**
	 * SandboxOriginalLevel ctor()
	 * 
	 * Creates a fixed island map
	 * @throws IOException 
	 */
	public SandboxIslandLevel(int slot) throws IOException {
		super("/WORLD_DATA/SANDBOX_DATA/TEST_LEVELS/island", "Island Map", new Point(788, 792), slot);
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
		
		Forest.generateForest(this, 808, 888, 500, 400);
		
		return null;
		
	}

	protected MapEdge[] getMapTransporterPlacement() {
		return null;
	}

}
