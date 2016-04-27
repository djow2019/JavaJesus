package level;

import game.Game;
import game.SoundHandler;
import game.entities.Entity;
import game.entities.FireEntity;
import game.entities.Mob;
import game.entities.Player;
import game.entities.Spawner;
import game.entities.particles.HealthBar;
import game.entities.projectiles.Projectile;
import game.entities.vehicles.Vehicle;
import game.graphics.JJFont;
import game.graphics.Screen;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;

import save.SaveFile;
import level.tile.Tile;

public abstract class Level implements Serializable {

	private static final long serialVersionUID = -5963535226522522466L;

	protected int[] tiles;
	public int width;
	public int height;
	protected List<Entity> entities = new CopyOnWriteArrayList<Entity>();
	protected List<Mob> mobs = new CopyOnWriteArrayList<Mob>();
	protected List<Player> players = new CopyOnWriteArrayList<Player>();
	public List<Mob> killList = new CopyOnWriteArrayList<Mob>();
	private String imagePath;
	private transient BufferedImage image;
	public Point spawnPoint;
	public Point startingSpawnPoint;
	
	public SaveFile saveData;
	
	public boolean initEntities = false;

	protected int[] tileColours;

	public boolean isLoaded = false;
	private int loadType = 0;
	private String name;
	
	private Screen screen;

	public static Rectangle screenRectangle = new Rectangle(500, 500);

	public Level(String imagePath, boolean loadNow, String name, Screen screen) {
		this.name = name;
		this.saveData = new SaveFile(name);
		spawnPoint = new Point(0, 0);
		startingSpawnPoint = new Point(0, 0);
		if (imagePath != null) {
			this.imagePath = imagePath;
			loadType = 0;
			if (loadNow) {
				isLoaded = true;
				this.loadLevelFromFile();
			}
		} else {
			this.width = 64;
			this.height = 64;
			tiles = new int[width * height];
			loadType = 1;
			if (loadNow) {
				isLoaded = true;
				this.generateLevel();
			}
		}
		this.screen = screen;
	}

	public void load() {
		if (!isLoaded) {
			if (loadType == 0) {
				this.loadLevelFromFile();
			} else if (loadType == 1) {
				this.generateLevel();
			} else {
				generateLevel();
			}
			init();
			isLoaded = true;
		}
	}
	
	public Clip getBackgroundMusic() {

		return SoundHandler.sound.background1;
	}

	public Level(int width, int height, boolean loadNow, Screen screen) {
		spawnPoint = new Point(0, 0);
		startingSpawnPoint = new Point(0, 0);
		this.width = width;
		this.height = height;
		tiles = new int[width * height];
		loadType = 2;
		if (loadNow) {
			isLoaded = true;
			generateLevel();
		}
		this.screen = screen;
	}

	protected abstract void initNPCPlacement();

	protected abstract void initSpawnerPlacement();

	protected abstract void initChestPlacement();

	protected abstract void otherEntityPlacement();

	protected abstract void initMapTransporters();

	private void loadLevelFromFile() {
		try {
			this.image = ImageIO.read(Level.class.getResource(this.imagePath));
			this.width = this.image.getWidth();
			this.height = this.image.getHeight();
			tiles = new int[width * height];
			tileColours = this.image
					.getRGB(0, 0, width, height, null, 0, width);
			this.loadTiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void loadTiles() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tileCheck: for (Tile t : Tile.tiles) {
					if (t != null
							&& t.getLevelColour() == tileColours[x + y * width]) {
						if (t == Tile.GRASS) {
							t = Tile.GRASS();
						} else if (t == Tile.WASTELAND_GROUND1) {
							t = Tile.CONCRETE();
						}
						this.tiles[x + y * width] = t.getId();
						break tileCheck;
					}
				}
			}
		}
	}

	protected void generateLevel() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x * y % 10 < 10) {
					tiles[x + y * width] = Tile.GRASS.getId();
				} else {
					tiles[x + y * width] = Tile.STONE.getId();

				}

			}
		}

	}

	protected void saveLevelToFile() {
		try {
			ImageIO.write(image, "png",
					new File(Level.class.getResource(this.imagePath).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void alterTile(int x, int y, Tile newTile) {
		this.tiles[x + y * width] = newTile.getId();
		if (image != null) {
			image.setRGB(x, y, newTile.getLevelColour());
			tileColours[x + y * width] = newTile.getId();
		}

	}

	public void clear() {
		for (Mob m : killList) {
			this.remEntity(m);
			killList.remove(m);
		}
	}

	public void tick() {
		
		for (Entity e : this.getEntities()) {
			if (!e.getBounds().intersects(screenRectangle)) {
				continue;
			}
			if (e instanceof Mob) {
				if (!((Mob) e).isDead())
					e.tick();
			} else
				e.tick();
		}

		for (Tile t : Tile.tiles) {
			if (t == null) {
				break;
			}
			t.tick();
		}
	}

	public void renderTile(Screen screen, int xOffset, int yOffset) {
		if (xOffset < 0)
			xOffset = 0;
		if (xOffset > ((width << 3) - screen.width))
			xOffset = ((width << 3) - screen.width);
		if (yOffset < 0)
			yOffset = 0;
		if (yOffset > ((height << 3) - screen.height))
			yOffset = ((height << 3) - screen.height);

		screen.setOffset(xOffset, yOffset);

		for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++) {
			for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
				getTile(x, y).render(screen, this, x << 3, y << 3);
			}

		}
	}

	public void renderEntities(Screen screen) {

		screenRectangle.setLocation(Game.player.getX() - 250,
				Game.player.getY() - 250);

		for (Entity entity : this.getEntities()) {
			if (!entity.getBounds().intersects(screenRectangle)) {
				continue;
			}
			if (entity instanceof Projectile) {
				Projectile p = (Projectile) entity;
				if (!p.renderOnTop) {
					p.render(screen);
				}
			}
			if (entity instanceof Mob) {
				Mob m = (Mob) entity;
				if (!m.renderOnTop()) {
					m.render(screen);
					if (m.getHealthBar() != null && !m.isDead())
						m.getHealthBar().render(screen);
				}
			}
		}
		for (Entity e : this.getEntities()) {
			if (!e.getBounds().intersects(screenRectangle)) {
				continue;
			}
			if (!(e instanceof Mob || e instanceof HealthBar || e instanceof Projectile)) {
				e.render(screen);
			}
		}
		for (Entity entity : this.getEntities()) {
			if (!entity.getBounds().intersects(screenRectangle)) {
				continue;
			}
			if (entity instanceof Projectile) {
				Projectile p = (Projectile) entity;
				if (p.renderOnTop) {
					p.render(screen);
				}
			}
			if (entity instanceof Mob) {
				Mob m = (Mob) entity;
				if (m.renderOnTop()) {
					m.render(screen);
					if (m.getHealthBar() != null && !m.isDead())
						m.getHealthBar().render(screen);
				}
			}
		}
	}

	public void renderFont(String msg, Screen screen, int x, int y,
			int[] color, int scale) {
		JJFont.render(msg, screen, x, y, color, scale);
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Tile.VOID;
		return Tile.tiles[tiles[x + y * width]];

	}

	public void addEntity(Entity entity) {
		if (!(entity instanceof Player)) {
			this.entities.add(0, entity);
		} else {
			this.entities.add(entity);
		}
		if (entity instanceof Mob) {
			if (entity instanceof Vehicle) {
				this.mobs.add((Vehicle) entity);
			} else
				this.mobs.add((Mob) entity);
			if (entity instanceof Player) {
				this.players.add((Player) entity);
			}
		} 
	}

	public void addEntity(Entity entity, int index) {
		this.entities.add(index, entity);
		if (entity instanceof Mob) {
			if (entity instanceof Vehicle) {
				this.mobs.add(index, (Vehicle) entity);
			} else
				this.mobs.add(index, (Mob) entity);
			if (entity instanceof Player) {
				this.players.add(index, (Player) entity);
			}
		} 

	}

	public void remEntity(Entity entity) {
		this.entities.remove(entity);
		if (entity instanceof Mob) {
			this.mobs.remove((Mob) entity);
			if (entity instanceof Player) {
				this.players.remove((Player) entity);
			}
		} 
	}

	public void reset() {
		entities.clear();
		mobs.clear();
		players.clear();
		killList.clear();
		this.init();
		spawnPoint = new Point(startingSpawnPoint.x, startingSpawnPoint.y);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public List<Mob> getMobs() {
		return mobs;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Player getPlayer() {
		if (players.size() > 0)
			return players.get(0);
		else 
			return null;
	}

	public void addSpawner(int x, int y, String type) {
		this.addEntity(new Spawner(this, x, y, type));
	}

	public void addSpawner(int x, int y, String type, int amt) {
		this.addEntity(new Spawner(this, x, y, type, amt));
	}

	public void init() {
		if (!initEntities && this.mobs.size() <= 1) {
			initEntities = true;
			initMapTransporters();
			initNPCPlacement();
			initSpawnerPlacement();
			initChestPlacement();
			otherEntityPlacement();
			initMobLocations();
		}
	}
	
	public void initMobLocations() {
		for (Mob m: this.getMobs()) {
			m.updateBounds();
		}
	}

	public String toString() {
		return name + "\n Mobs: " + this.getMobs();
	}
	
	public Screen getScreen() {
		return screen;
	}
}