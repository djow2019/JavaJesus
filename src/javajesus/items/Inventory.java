package javajesus.items;

import java.io.Serializable;

import javajesus.SoundHandler;
import javajesus.entities.Pickup;

/*
 * A container that holds various items 
 */
public class Inventory implements Serializable {

	// serialization
	private static final long serialVersionUID = 1L;
	
	// the list of all the real items
	private Item[] items;
	
	// inventory space
	private static final int INVENTORY_SIZE = 36;
	
	/**
	 * Inventory ctor()
	 */
	public Inventory() {
		
		// initialize items
		items = new Item[INVENTORY_SIZE];
		
	}
	
	/**
	 * get()
	 * Gets an item in the inventory
	 * 
	 * @param index - index of item
	 * @return the item
	 */
	public Item get(int index) {
		return items[index];
	}

	/**
	 * Adds an item to the inventory
	 * 
	 * @param item - the new item
	 */
	public void add(Item item) {

		// iterate through the inventory list
		for (int i = 0; i < items.length; i++) {
			
			if (items[i] != null) {
				
				// check if it is already in the inventory
				if (items[i].equals(item)) {
					items[i].add();
					return;
				}
				
				// first null space in inventory
			} else {
				items[i] = item;
				return;
			}
			
		}
	}
	
	/**
	 * add()
	 * adds the contents of a pickup
	 * Plays a pickup Sound
	 * Removes the pickup afterwards
	 * 
	 * @param pickup - the pickup item
	 */
	public void add(Pickup pickup) {
		
		SoundHandler.play(SoundHandler.click);
		
		// add the contents of the pickup
		for (int i = 0; i < pickup.getQuantity(); i++) {
			add(pickup.getItem());
		}
		
		// now remove the pickup
		pickup.remove();
	}
	
	/**
	 * Removes an item exactly once
	 * 
	 * @param item - item to remove
	 */
	public void remove(Item item) {
		
		// remove an item once else remove it altogether
		if (item.getQuantity() > 1) {
			item.remove();
		} else {
			discard(item);
		}
		
	}
	
	/**
	 * Removes an item num times
	 * 
	 * @param item - item to remove
	 * @param num - quantity to remove
	 */
	public void remove(Item item, int num) {
		
		// remove the item num times
		for (int i = 0; i < num; i++) {
			remove(item);
		}
		
	}

	/**
	 * Removes an item from the inventory
	 * 
	 * @param item - item to discard
	 * @return successfully or not
	 */
	public boolean discard(Item item) {
		
		// iterate through the item list
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].equals(item)) {
				items[i] = null;
				
				// now sort the items
				sortItemsByID();
				
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * Finds an item in the inventory
	 * 
	 * @param item - item to find
	 * @return the item, or null if not found
	 */
	private Item find(Item item) {

		// iterate through the inventory list
		for (int i = 0; i < items.length; i++) {

			if (items[i] != null && items[i].equals(item)) {
				return items[i];
			}
		}
		
		// no item found
		return null;
	}
	
	/**
	 * @param ammo - the ammo type to find
	 * @return the amount of that ammo in the inventory
	 */
	public int getAmmoAmount(Item ammo) {

		// now search for that item
		if (ammo != null && find(ammo) != null) {

			// return number of bullets
			return ammo.getQuantity();

		} else {

			// item does not exist
			return 0;
		}

	}
	
	/**
	 * @return First item in inventory that restores health
	 * Returns NULL if none found
	 */
	public Item findHealthItem() {
		
		// iterate through inventory
		for (int i = 0; i < items.length; i++) {
			
			// grab the item at i
			if (items[i] != null) {
				
				// check if it contains health
				if (items[i].containsHealth()) {
					return items[i];
				}
				
				// rest of inventory is null
			} else {
				break;
			}
		}
		
		// none found
		return null;
	}
	
	/**
	 * @return First item in inventory that restores Stamina
	 * Returns NULL if none found
	 */
	public Item findStaminaItem() {
		// iterate through Player's inventory
		for (int i = 0; i < items.length; i++) {
			//check item at index i
			if(items[i] != null) {
				//checks if item contains stamina
				if (items[i].containsStamina()) {
					return items[i];
				}
				//rest of inventory is null
			} else {
				break;
			}
		}
		return null;
	}

	/**
	 * Sorts items alphabetically
	 */
	public void sortItemsAlphabetically() {
		
		// remove any blank spots in between
		condense();

		// like a bubble sort
		for (int i = 0; i < items.length; i++) {
			
			// invalid item
			int low = i;

			// assume all previous entries are sorted
			for (int j = i + 1; j < items.length; j++) {
				
				// skip null items
				if (items[j] == null) {
					break;
				}

				// moves the smallest name to the bottom
				if (items[j].getName().compareToIgnoreCase(items[low].getName()) < 0) {
					low = j;
				}
			}

			// swap the elements
			Item temp = items[i];
			items[i] = items[low];
			items[low] = temp;

		}
	}

	/**
	 * Sorts items by ID
	 */
	public void sortItemsByID() {
		
		// remove any blank spots in between
		condense();

		// like a bubble sort
		for (int i = 0; i < items.length; i++) {

			// index of the item with the smallest ID
			int low = i;

			// assume all previous entries are sorted
			for (int j = i + 1; j < items.length; j++) {
				
				// skip null items
				if (items[j] == null) {
					break;
				}

				// moves the smallest ID to the bottom
				if (items[j].getId() < items[low].getId()) {
					low = j;
				}
			}

			// swap the elements
			Item temp = items[i];
			items[i] = items[low];
			items[low] = temp;

		}
	}
	
	/**
	 * Removes blank items in between other items
	 */
	private void condense() {
		
		// special selection sort checking for null cases
		for (int i = 0; i < items.length; i++) {
			
			// swap with first non null
			if (items[i] == null) {
				
				// search for non null
				for (int j = i + 1; j < items.length; j++) {
					
					// swap
					if (items[j] != null) {
						items[i] = items[j];
						items[j] = null;
						break;
					}
					
				}
				
			}
			
		}
		
	}

}
