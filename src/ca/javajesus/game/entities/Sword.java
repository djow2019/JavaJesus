package ca.javajesus.game.entities;

import ca.javajesus.game.gfx.RotateMatrix;
import ca.javajesus.game.gfx.Screen;
import ca.javajesus.game.gfx.Sprite;
import ca.javajesus.level.Level;

public class Sword extends Entity {

	private Sprite sprite;
	private Player player;
	protected int color;
	private final int swingSpeed = 200;
	private int tickCount = 0;
	private int swordCount;
	RotateMatrix matrix;

	public Sword(Level level, Sprite sprite, Player player, int color) {
		super(level);
		this.sprite = sprite;
		this.player = player;
		this.color = color;
		matrix = new RotateMatrix(sprite);
	}

	public void tick() {
		
		tickCount++;
		if (tickCount > swingSpeed) {
			tickCount = 0;
			level.remEntity(this);
		}
<<<<<<< Updated upstream
		if (tickCount == 25) {
=======
		if ((tickCount == 200) || (tickCount == 150) || (tickCount == 100)  || (tickCount == 50)){
		    swordCount++;
>>>>>>> Stashed changes
			sprite.pixels = matrix.rotate(90);
		}
	}

	public void render(Screen screen) {
		this.x = player.x - 10;
		this.y = player.y - 15;
		screen.render((int) x, (int) y, color, sprite);
	}

}
