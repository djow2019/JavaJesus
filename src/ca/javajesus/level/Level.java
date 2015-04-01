package ca.javajesus.level;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;

import ca.javajesus.game.Game;
import ca.javajesus.game.SoundHandler;
import ca.javajesus.game.entities.Entity;
import ca.javajesus.game.entities.FireEntity;
import ca.javajesus.game.entities.Mob;
import ca.javajesus.game.entities.Player;
import ca.javajesus.game.entities.Spawner;
import ca.javajesus.game.entities.particles.HealthBar;
import ca.javajesus.game.entities.projectiles.Projectile;
import ca.javajesus.game.entities.vehicles.Vehicle;
import ca.javajesus.game.graphics.JJFont;
import ca.javajesus.game.graphics.Screen;
import ca.javajesus.level.tile.Tile;

public abstract class Level implements java.io.Serializable {
	protected int[] tiles;
	public int width;
	public int height;
	protected List<Entity> entities = new CopyOnWriteArrayList<Entity>();
	protected List<Mob> mobs = new CopyOnWriteArrayList<Mob>();
	protected List<Player> players = new CopyOnWriteArrayList<Player>();
	public List<FireEntity> fireList = new CopyOnWriteArrayList<FireEntity>();
	public List<Mob> killList = new CopyOnWriteArrayList<Mob>();
	private String imagePath;
	private BufferedImage image;
	public Point spawnPoint;
	public Point startingSpawnPoint;

	protected int[] tileColours;

	public static Level level1 = new Level1();
	public static Level roadlevel = new RoadLevel();
	public static Level random = new RandomLevel(level1.width, level1.height);
	public static Level random2 = new RandomLevel2(level1.width, level1.height,
			new Point(500, 500));
	public static Level randomCave = new RandomCave(level1.width,
			level1.height, 5, level1, new Point(220, 79));

	public boolean isLoaded = false;
	private int loadType = 0;
	
	public static Rectangle screenRectangle= new Rectangle(350, 350);

	public Level(String imagePath, boolean loadNow) {
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
	}

	public void load() {
		if (!isLoaded) {
			if (loadType == 0) {
				this.loadLevelFromFile();
			} else if (loadType == 1) {
				this.generateLevel();
			} else {
				generateLevel();
				fireList.add(new FireEntity(this, -10, -10));
			}
			isLoaded = true;
		}
	}

	public Clip getBackgroundMusic() {
		return SoundHandler.sound.background1;
	}

	public Level(int width, int height, boolean loadNow) {
		spawnPoint = new Point(0, 0);
		startingSpawnPoint = new Point(0, 0);
		this.width = width;
		this.height = height;
		tiles = new int[width * height];
		loadType = 2;
		if (loadNow) {
			isLoaded = true;
			generateLevel();
			fireList.add(new FireEntity(this, -10, -10));
		}
	}

	protected abstract void initNPCPlacement();

	protected abstract void initSpawnerPlacement();

	protected abstract void initChestPlacement();

	protected abstract void otherEntityPlacement();

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
							t = getGrass();
						}
						this.tiles[x + y * width] = t.getId();
						break tileCheck;
					}
				}
			}
		}
	}

	private Tile getGrass() {
		Random random = new Random();

		if (random.nextInt(100) == 0) {
			return Tile.GRASS_FLOWER;
		}
		if (random.nextInt(6) == 0) {
			return Tile.GRASS2;
		}
		if (random.nextInt(6) == 0) {
			return Tile.GRASS3;
		} else {
			return Tile.GRASS;
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
		for (Mob m: killList) {
			this.remEntity(m);
			killList.remove(m);
		}
	}

	public void tick() {
		
		for (Entity e: this.getEntities()) {
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
		
		screenRectangle.setLocation(Game.player.getX() - 175, Game.player.getY() - 175);
		
		for (Entity entity: this.getEntities()) {
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
		for (Entity e: this.getEntities()) {
			if (!e.getBounds().intersects(screenRectangle)) {
				continue;
			}
			if (!(e instanceof Mob || e instanceof HealthBar || e instanceof Projectile)) {
				e.render(screen);

			}
		}
		for (Entity entity: this.getEntities()) {
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

	public void renderFont(String msg, Screen screen, int x, int y, int[] color,
			int scale) {
		JJFont.render(msg, screen, x, y, color, scale);
	}

	public Tile getTile(int x, int y) {
		if (0 > x || x >= width || 0 > y || y >= height)
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
		} else if (entity instanceof FireEntity) {
			this.fireList.add((FireEntity) entity);
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
		} else if (entity instanceof FireEntity) {
			this.fireList.add(index, (FireEntity) entity);
		}

	}

	public void remEntity(Entity entity) {
		this.entities.remove(entity);
		if (entity instanceof Mob) {
			this.mobs.remove((Mob) entity);
			if (entity instanceof Player) {
				this.players.remove((Player) entity);
			}
		} else if (entity instanceof FireEntity) {
			this.fireList.remove((FireEntity) entity);
		}
	}
	
	public void reset() {
		entities.clear();
		mobs.clear();
		players.clear();
		fireList.clear();
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

	public void addSpawner(int x, int y, String type) {
		this.addEntity(new Spawner(this, x, y, type));
	}

	public void addSpawner(int x, int y, String type, int amt) {
		this.addEntity(new Spawner(this, x, y, type, amt));
	}

	public void init() {
		if (this.mobs.size() <= 1) {
			initNPCPlacement();
			initSpawnerPlacement();
			initChestPlacement();
			otherEntityPlacement();
		}

	}
	
	public static void saveData()
	{
	 // Write to disk with FileOutputStream
	    FileOutputStream f_out;
        try
        {
            f_out = new 
                FileOutputStream("game.data");
        
	    // Write object with ObjectOutputStream
	    ObjectOutputStream obj_out;
            obj_out = new
                ObjectOutputStream (f_out);
        
	    // Write object out to disk
	        obj_out.writeObject (level1);
	        obj_out.close();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	}
	
	public static Level loadData()
	{
	 // Read from disk using FileInputStream
	    FileInputStream f_in;
        try
        {
            f_in = new 
                FileInputStream("game.data");

	    // Read object using ObjectInputStream
	    ObjectInputStream obj_in;
           obj_in = new ObjectInputStream (f_in);

	    // Read an object
	    Object obj = obj_in.readObject();

	    if (obj instanceof Level)
	    {
	        // Cast object to a Vector
	        Level lev = (Level) obj;
	        obj_in.close();
	        // Do something with Level....
	        return lev;
	    }
	    else
	    {
	        obj_in.close();
            return level1;
	    }
        }
	    catch (IOException | ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return level1;
	}
}
