package ca.javajesus.level;

import java.awt.Point;

import ca.javajesus.game.entities.vehicles.Vehicle;

public class RoadLevel extends Level {

	public RoadLevel() {
		super("/Levels/Test_Levels/Road_Test_Level.png", false, "Road Level");
		this.spawnPoint = new Point(50, 50);
	}

	@Override
	protected void initNPCPlacement() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initSpawnerPlacement() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initChestPlacement() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void otherEntityPlacement() {
		
	}

	@Override
	protected void initMapTransporters() {
		// TODO Auto-generated method stub
		
	}

}
