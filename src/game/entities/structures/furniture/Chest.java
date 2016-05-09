package game.entities.structures.furniture;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import game.entities.Entity;
import game.entities.SolidEntity;
import game.graphics.Screen;
import game.graphics.SpriteSheet;
import items.Gun;
import items.Item;
import items.Sword;
import level.Level;

/*
 * Creates a chest that stores items for the player to use
 */
public class Chest extends Entity implements SolidEntity {

	private static final long serialVersionUID = 1L;

	// color set of the chest
	private static final int[] color = new int[] { 0xFF111111, 0xFF452909, 0xFFFFE011 };

	// whether or not the chest is open
	private boolean isOpen;

	// what items are in the chest
	private Item[] contents;

	// randomizes items
	private static final Random random = new Random();

	// the size of the chest
	private static final int SIZE = 8;

	/**
	 * Creates an empty chest
	 * 
	 * @param level
	 *            the level it is on
	 * @param x
	 *            the x coord
	 * @param y
	 *            the y coord
	 */
	public Chest(Level level, int x, int y) {
		this(level, x, y, new Item[] {});

	}

	/**
	 * Creates a chest filled with certain items
	 * 
	 * @param level
	 *            the level it is on
	 * @param x
	 *            the x coord
	 * @param y
	 *            the y coord
	 * @param items
	 *            the items it contains
	 */
	public Chest(Level level, int x, int y, Item... items) {
		super(level, x, y);

		this.contents = items;
		setBounds(getX(), getY(), SIZE, SIZE);
	}
	
	/**
	 * Creates a chest filled with certain items
	 * 
	 * @param level
	 *            the level it is on
	 * @param x
	 *            the x coord
	 * @param y
	 *            the y coord
	 * @param items
	 *            the items it contains
	 */
	public Chest(Level level, int x, int y, List<Item> items) {
		super(level, x, y);

		this.contents = (Item[]) items.toArray();
		setBounds(getX(), getY(), SIZE, SIZE);
	}

	/**
	 * Creates a chest with random items
	 * 
	 * @param level
	 *            level it is on
	 * @param x
	 *            x coord
	 * @param y
	 *            y cord
	 * @param type
	 *            the type of item it should have
	 * @param amt
	 *            the amount of items it should have
	 */
	public Chest(Level level, int x, int y, String type, int amt) {
		super(level, x, y);
		contents = new Item[amt];
		fillRandomItems(type, amt, 0);
		setBounds(getX(), getY(), SIZE, SIZE);
	}

	/**
	 * @return a random Gun
	 */
	private Gun getRandomGun() {
		switch (random.nextInt(5)) {
		case 0:
			return (Gun) Item.revolver;
		case 1:
			return (Gun) Item.laserRevolver;
		case 2:
			return (Gun) Item.shotgun;
		case 3:
			return (Gun) Item.assaultRifle;
		case 4:
			return (Gun) Item.crossBow;
		default:
			return (Gun) Item.bazooka;
		}
	}

	/**
	 * @return a random sword
	 */
	private Sword getRandomSword() {
		switch (random.nextInt(5)) {
		case 0:
			return (Sword) Item.longSword;

		case 1:
			return (Sword) Item.claymore;
		case 2:
			return (Sword) Item.sabre;
		case 3:
			return (Sword) Item.heavenlyShortSword;
		case 4:
			return (Sword) Item.heavenlySword;
		default:
			return (Sword) Item.shortSword;
		}
	}

	/**
	 * @return a random Item
	 */
	private Item getRandomItem() {
		switch (random.nextInt(4)) {
		case 0:
			return Item.apple;
		case 1:
			return Item.banana;
		case 2:
			return Item.orange;
		default:
			return Item.feather;

		}
	}

	/**
	 * Fills the contents of the chest with random items
	 * 
	 * @param string
	 *            the type of item
	 * @param amt
	 *            the amount of items
	 * @param index
	 *            the index of the item array to fill
	 */
	private void fillRandomItems(String string, int amt, int index) {

		// fills a random item into the contents array
		switch (string) {
		case "gun":
			contents[index] = getRandomGun();
			break;
		case "sword":
			contents[index] = getRandomSword();
			break;
		case "item":
			contents[index] = getRandomItem();
			break;
		default:
			switch (random.nextInt(3)) {
			case 0:
				contents[index] = getRandomGun();
				break;
			case 1:
				contents[index] = getRandomSword();
				break;
			default:
				contents[index] = getRandomItem();
				break;
			}
		}

		// recursive case if more items need to be filled in the array
		if (amt > 1)
			fillRandomItems(string, amt - 1, index + 1);
	}

	/**
	 * Opens the chest
	 */
	public void open() {
		isOpen = true;
	}

	/**
	 * @return the items in the chest
	 */
	public Item[] getContents() {
		return contents;
	}

	/**
	 * Display the chest on the screen
	 */
	public void render(Screen screen) {

		if (!isOpen) {
			screen.render(getX(), getY(), 0 + 20 * 32, color, SpriteSheet.tiles);
			screen.render(getX() + 8, getY(), 1 + 20 * 32, color, SpriteSheet.tiles);
		} else {
			screen.render(getX(), getY(), 2 + 20 * 32, color, SpriteSheet.tiles);
			screen.render(getX() + 8, getY(), 3 + 20 * 32, color, SpriteSheet.tiles);
		}

	}

	@Override
	public Rectangle getShadow() {
		return null;
	}

	@Override
	public void tick() {

	}

}
