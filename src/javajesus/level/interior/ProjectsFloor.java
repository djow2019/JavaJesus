package javajesus.level.interior;

import java.awt.Point;
import java.io.IOException;

import javajesus.entities.transporters.Stairs;
import javajesus.entities.transporters.Transporter;
import javajesus.level.Level;
import javajesus.utility.Direction;

public class ProjectsFloor extends Interior {

	// current floor
	private static int floor;

	public ProjectsFloor(Point point, Level level) throws IOException {
		super("/VISUAL_DATA/STATICS/ARCHITECTURE/GENERIC/INTERIORS/Projects_Interiors/Projects_Floors_1_and_2",
		        new Point(580, 680), level);
	}

	@Override
	public Transporter[] getTransporters() throws IOException {

		Transporter[] entities = new Transporter[2];

		entities[0] = new Stairs(this, 344, 528, outside, Direction.EAST, Stairs.CARPET);

		if (floor++ < 2) {
			entities[1] = new Stairs(this, 344, 472, new ProjectsFloor(new Point(353, 536), this),
			        Direction.WEST, Stairs.CARPET);
		} else {
			entities[1] = new Stairs(this, 344, 472, new ProjectsTop(new Point(353, 536), this),
			        Direction.WEST, Stairs.CARPET);
			floor = 0;
		}

		return entities;
	}
}