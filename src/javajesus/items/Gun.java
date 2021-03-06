package javajesus.items;

import java.net.URL;
import java.util.Random;

import javajesus.SoundHandler;
import javajesus.entities.Player;
import javajesus.entities.projectiles.Arrow;
import javajesus.entities.projectiles.BlackHoleDetonator;
import javajesus.entities.projectiles.Bullet;
import javajesus.entities.projectiles.FireBall;
import javajesus.entities.projectiles.Laser;
import javajesus.entities.projectiles.Missile;
import javajesus.level.Level;
import javajesus.utility.Direction;

/*
 * A gun is an item that also allows a player to shoot
 */
public class Gun extends Item {

	// max amount of ammo
	private int clipSize;

	// current amount of ammo in clip
	private int ammo;

	// spare bullets in inventory
	private int availableAmmo;

	// whether or not the player is reloading
	private boolean isReloading;

	// the reload time between bullets TODO implement it
	private boolean doesReload;

	// ticks inbetween each reload
	private int reloadTicks;

	// fire rate of the gun
	private int FIRE_RATE;

	// damage range
	private static final int DAMAGE_RANGE = 5;
	
	// for calculating random range of damage
	private static final Random random = new Random();

	// ticks between each shot fired
	private int fireTicks = 1;

	// whether or not the gun can be fired
	private boolean canFire;

	// damage per shot fired
	private int damage;

	// offset on the spritesheet for player animation
	private int playerOffset;
	
	// offset of the reload animation on the player spritesheet
	private int[] reloadOffsets = { 42, 48, 54};

	// type of ammo
	private Ammo type;

	// sound the gun will make
	private URL clip;

	/*
	 * The different types of ammo available
	 */
	public enum Ammo {
		REVOLVER, RIFLE, ARROW, FIREBALL, LASER, MISSILE, BLACKHOLE, FLAMETHROWER, SHELL;
	}

	/**
	 * Creates a gun
	 * 
	 * @param name - the name of the gun
	 * @param id - the unique ID of the gun
	 * @param xTile - the xtile on the ITEM spritesheet
	 * @param yTile - the y tile on the ITEM spritesheet
	 * @param color - the color of the gun
	 * @param description - the description of the gun
	 * @param yPlayerSheet - the y tile on the PLAYER spritesheet
	 * @param clipSize - the maximum ammo clip size
	 * @param rate - the rate of fire
	 * @param reload - the rate of reload
	 * @param damage - the damage per shot
	 * @param type - the type of ammo used
	 * @param clip - the clip sound
	 */
	public Gun(String name, int id, int xTile, int yTile, int[] color, String description, int yPlayerSheet,
			int clipSize, int rate, boolean doesReload, int damage, Ammo type, URL clip) {
		super(name, id, xTile, yTile, color, description, true);
		this.playerOffset = yPlayerSheet;
		this.clipSize = clipSize;
		this.ammo = clipSize;
		this.doesReload = doesReload;
		this.FIRE_RATE = rate;
		this.damage = damage;
		this.type = type;
		this.clip = clip;
	}

	/**
	 * Updates the gun
	 */
	public void tick() {

		fireTicks++;

		// increase the count displayed while reloading
		if (isReloading && reloadTicks % 15 == 0) {
			if (ammo < clipSize && availableAmmo > 0) {
				ammo++;
				availableAmmo--;
			} else {
				isReloading = false;
				reloadTicks = 0;
				return;
			}
		}

		if (isReloading) {
			reloadTicks++;
		}

		if (fireTicks % FIRE_RATE == 0) {
			canFire = true;
		}

	}
	


	/**
	 * Reloads the gun
	 * 
	 * @param bullets - number of bullets in player inventory
	 * @return number of bullets used
	 */
	public int reload(int bullets) {
		isReloading = true;
		this.availableAmmo = bullets;
		return this.clipSize - this.ammo;
	}
	
	
	/**
	 * Initializes the sounds of the gun
	 */
	public void initSound() {
		switch (getName()) {
		case "Shotgun":
			this.clip = SoundHandler.shotgun;
			break;
		case "Assault Rifle":
			this.clip = SoundHandler.assaultRifle;
			break;
		case "Laser Revolver":
			this.clip = SoundHandler.laser;
			break;
		default:
			this.clip = SoundHandler.revolver;
			break;
		}
	}

	/**
	 * Fires the gun
	 * 
	 * @param x - the x coordinate
	 * @param y - the y coord
	 * @param dir - the direction
	 * @param player - the player shooting the gun
	 */
	public void fire(int x, int y, Direction dir, Player player) {

		// get the level
		Level level = player.getLevel();
		
		// get random damage based on player strength
		int damage = player.getStrength() + this.damage + random.nextInt(DAMAGE_RANGE);

		if (ammo > 0 && !isReloading && canFire) {
			switch (type) {
			case REVOLVER:
			case RIFLE:
				level.add(new Bullet(level, x, y, dir, player, damage, clip));
				break;
			case ARROW:
				level.add(new Arrow(level, x, y, dir, player, damage));
				break;
			case FIREBALL:
				level.add(new FireBall(level, x, y, dir, player, damage));
				break;
			case LASER:
				level.add(new Laser(level, x, y, dir, player, damage));
				break;
			case MISSILE:
				level.add(new Missile(level, x, y, dir, player, damage));
				break;
			case BLACKHOLE:
				level.add(new BlackHoleDetonator(level, x, y, dir, player, damage));
				break;
			case FLAMETHROWER:
				level.add(new FireBall(level, x, y, dir, player, damage));
				break;
			case SHELL:
				for (int i = 0; i < 3; i++) {
					level.add(new Bullet(level, x + random.nextInt(4) - 2, y + random.nextInt(4) - 2, dir, player, damage, clip));
				}
				break;
			}
			ammo--;
			canFire = false;
			fireTicks = 0;
		}
	}

	/**
	 * @return the audio clip used
	 */
	public final URL getClip() {
		return clip;
	}

	/**
	 * @return the current ammo
	 */
	public final int getCurrentAmmo() {
		return ammo;
	}

	/**
	 * @return the max ammo
	 */
	public final int getClipSize() {
		return clipSize;
	}

	/**
	 * @return true if the gun is reloading
	 */
	public boolean isReloading() {
		return isReloading;
	}
	/**
	 * @return true if this gun reloads
	 */
	public boolean reloads() {
		return doesReload;
	}

	/**
	 * @return the player offset in the spritesheet
	 */
	public int getYOffset() {
		return playerOffset;
	}
	/**
	 * 
	 * @param shootingDir Direction Player is shooting in
	 * @param movingDir Direction Player is moving in
	 * @param isShooting if the Player is shooting
	 * @param isMoving if the Player is Moving
	 * @param flip Whether we need to flip the Player sprite
	 * @param tickCount Current Tick Count
	 * @return
	 */
	public int getXOffset(Direction shootingDir, Direction movingDir, boolean isShooting, boolean isMoving,
			boolean flip, int tickCount) {
		int xTile = 0;
		//NORTH
		if ( (shootingDir == Direction.NORTH && isShooting) || 
				(movingDir == Direction.NORTH && !isShooting)) {
			xTile = 24 + (flip ? 0 : 3);
			// Handles standing still and breathing animations
			if (!isMoving)
				xTile = ((tickCount % 120 <= 60) ? 21 : 39);
		} 
		//SOUTH
		else if ( (shootingDir == Direction.SOUTH && isShooting) || 
				(movingDir == Direction.SOUTH && !isShooting) ) {
			xTile = 3 + (flip ? 0 : 3);
			// Handles standing still and breathing animations
			if (!isMoving)
				xTile = ((tickCount % 120 <= 60) ? 0 : 30);
		}
		// EAST
		else if( (shootingDir == Direction.EAST && isShooting) || 
				(movingDir == Direction.EAST && !isShooting) ){
			xTile = 9 + (flip ? 0 : 3);
			// Handles standing still and breathing animations
			if (!isMoving)
				xTile = ((tickCount % 120 <= 60) ? 9 : 33);
		}
		// WEST
		else if( (shootingDir == Direction.WEST && isShooting) || 
				(movingDir == Direction.WEST && !isShooting) ){
			xTile = 15 + (flip ? 0 : 3);
			// Handles standing still and breathing animations
			if (!isMoving)
				xTile = ((tickCount % 120 <= 60) ? 15 : 36);
		}
		return xTile;
	}
	
	/**
	 * @return The Item form of the Ammo type with this gun
	 */
	public Item getAmmo() {
		switch (type) {
		case REVOLVER:
			return Item.revolverAmmo;
		case ARROW:
			return Item.arrowAmmo;
		case LASER:
			return Item.laserAmmo;
		case MISSILE:
			return Item.rocketAmmo;
		case BLACKHOLE:
			return Item.revolverAmmo;
		case FLAMETHROWER:
			return Item.revolverAmmo;
		case SHELL:
			return Item.shotgunAmmo;
		case RIFLE:
			return Item.assaultRifleAmmo;
		default:
			return null;
		}
	}

}
