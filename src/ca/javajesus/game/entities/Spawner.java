package ca.javajesus.game.entities;

import java.util.Random;

import ca.javajesus.game.Game;
import ca.javajesus.game.JavaRectangle;
import ca.javajesus.game.entities.monsters.Demon;
import ca.javajesus.game.entities.monsters.GangMember;
import ca.javajesus.game.entities.particles.HealthPack;
import ca.javajesus.game.graphics.Screen;
import ca.javajesus.level.Level;

public class Spawner extends Entity {

	private static final long serialVersionUID = -1243740183193796893L;
	
	private String type;
	private Random random = new Random();
	private int amount = 0;
	private Entity currentEntity;

	public Spawner(Level level, int x, int y, String type) {
		super(level);
		this.x = x;
		this.y = y;
		this.type = type;
		amount = -1;
		this.bounds = new JavaRectangle(1, 1, this);
		this.bounds.setLocation(x, y);
	}

	public Spawner(Level level, int x, int y, String type, int amount) {
		super(level);
		this.x = x;
		this.y = y;
		this.type = type;
		this.amount = amount;
		this.bounds = new JavaRectangle(1, 1, this);
		this.bounds.setLocation(x, y);
	}

	public void tick() {

		if (currentEntity == null) {
			spawnMob();
		}
		if (currentEntity instanceof Mob) {
			if (((Mob) currentEntity).isDead && random.nextInt(1000) == 0
					&& level.getMobs().size() < Game.ENTITY_LIMIT)
				spawnMob();
		} else if (random.nextInt(1000) == 0
				&& level.getEntities().size() < Game.ENTITY_LIMIT) {
			spawnMob();
		}

		if (amount == 0) {
			level.remEntity(this);
		}

	}

	public void spawnMob() {

		if (amount > 0) {
			amount--;
			this.level.addEntity(getEntity());
		} else if (amount == -1) {
			this.level.addEntity(getEntity());
		}

	}

	private Entity getEntity() {
		switch (type) {
		case "Demon":
			return currentEntity = new Demon(this.level, "Demon", x, y, 1);
		case "Gang":
			return currentEntity = new GangMember(this.level, "Gang", x, y, 1,
					200, random.nextInt(2));
		case "Health":
			return currentEntity = new HealthPack(this.level, x, y);
		default:
			return null;
		}
	}

	public void render(Screen screen) {
	}

}
