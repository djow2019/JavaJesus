package ca.javajesus.game.entities.monsters;

import java.awt.Color;

import ca.javajesus.game.ChatHandler;
import ca.javajesus.game.Game;
import ca.javajesus.game.SoundHandler;
import ca.javajesus.game.entities.Player;
import ca.javajesus.game.entities.particles.HealthBar;
import ca.javajesus.game.gfx.Colors;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.level.Level;

public class Monkey extends Monster {

	public Monkey(Level level, String name, int x, int y, int speed, int health) {
		super(level, name, x, y, speed, 16, 16, 8, health, Colors.get(-1,
				Colors.fromHex("#2a1609"), Colors.fromHex("#391e0c"),
				Colors.fromHex("#b08162")));
		this.bar = new HealthBar(level, 0 + 2 * 32, this.x, this.y, this, 0);
		if (level != null)
			level.addEntity(bar);
	}

	public void tick() {
		super.tick();

		if (this.aggroRadius.intersects(Game.player.getBounds())
				&& random.nextInt(400) == 0) {
			sound.play(SoundHandler.sound.chimpanzee);
		}
		int xa = 0;
		int ya = 0;

		if (mob != null && this.aggroRadius.intersects(mob.getBounds())
				&& !this.getOuterBounds().intersects(mob.getBounds())) {

			if (mob.getX() > this.x) {
				xa++;
			}
			if (mob.getX() < this.x) {
				xa--;
			}
			if (mob.getY() > this.y) {
				ya++;
			}
			if (mob.getY() < this.y) {
				ya--;
			}
		}

		if ((xa != 0 || ya != 0) && !isSolidEntityCollision(xa, ya)
				&& !isMobCollision(xa, ya)) {
			setMoving(true);
			move(xa, ya);
		} else {
			setMoving(false);
		}
	}

	public void render(Screen screen) {
		super.render(screen);
		int xTile = 0;
		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		if (getDirection() == 0) {
			xTile += 10;
		}
		if (getDirection() == 1) {
			xTile += 2;
		} else if (getDirection() > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (getDirection() - 1) % 2;
			flipBottom = (getDirection() - 1) % 2;
		}

		int modifier = 8 * scale;
		double xOffset = x - modifier / 2;
		double yOffset = (y - modifier / 2 - 4) - modifier;

		if (isDead)
			xTile = 12;

		// Upper body
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile
				* 32, color, flipTop, scale, sheet);

		// Upper body
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset,
				(xTile + 1) + yTile * 32, color, flipTop, scale, sheet);

		// Lower Body
		screen.render(xOffset + (modifier * flipBottom), yOffset + modifier,
				xTile + (yTile + 1) * 32, color, flipBottom, scale, sheet);

		// Lower Body
		screen.render(xOffset + modifier - (modifier * flipBottom), yOffset
				+ modifier, (xTile + 1) + (yTile + 1) * 32, color, flipBottom,
				scale, sheet);
	}

	public void speak(Player player) {
		isTalking = true;
		ChatHandler.sendMessage("Chimp no speak with human.", Color.white);
		return;
	}
}
