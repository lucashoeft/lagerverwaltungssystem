package com.lagerverwaltung;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Inventory class holds all information about the inventory items, the shelves and the categories.
 */
public class Inventory {

    /**
     * The path where the csv file of the inventory lies
     */
    private String path;

    /**
     * The hash map contains all inventory items and are keyed by their description as this description has to be
     * unqiue.
     */
    private HashMap<String, InventoryItem> itemMap;

    /**
     * The hash map contains all shelves and are keyed by their number as this number has to be unique.
     */
    private HashMap<Integer, Shelf> shelfMap;

    /**
     * The hash map  contains all categories and are keyed by their category name as this name has to be unique.
     */
    private HashMap<String, Category> categoryMap;

    /**
     * A logger to log messages
     */
    private static final Logger logger = Logger.getLogger(Inventory.class.getName());

    /**
     * The constructor of the Inventory class.
     *
     * <p>It initialises an empty path and the empty hash maps.
     */
    public Inventory(){
        path = "";
        itemMap = new HashMap<String, InventoryItem>();
        shelfMap = new HashMap<Integer, Shelf>();
        categoryMap = new HashMap<String, Category>();
    }

    /**
     * The constructor of the Inventory class, which initialises the path of the csv file.
     *
     * @param path the path of the csv file
     */
    public Inventory(String path){
        this.path = path;
    }

    /**
     * Updates the csv file path to a new file path.
     *
     * @param path new csv file path
     */
    public void setPath(String path){
        this.path = path;
    }

    /**
     * Returns the current file path where the inventory lies.
     *
     * @return The path where the csv file of the inventory lies
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the item map  and initialises the storage and the categories
     *
     * @param itemMap HashMap which contains all items
     */
    public void setItemMap(HashMap<String, InventoryItem> itemMap) {
        this.itemMap = itemMap;
        this.initStorage();
        this.initCategories();
    }

    /**
     * Tries to add a new inventory item to the item map.
     *
     * <p>This method succeeds if item does not exist yet with the description and will not make the shelf too heavy.
     *
     * @param item the inventory item to be added
     * @return true if successful
     */
    public boolean addNewItem(InventoryItem item) {
        // new item?
        for(InventoryItem itemCheck : getItemMap().values()) {
            if(item.checkUsage(itemCheck)){
                return false;
            }
        }
        // not to heavy?
        if (addItemToStorage(item, item.getStock())) {
            // add item
            // category exists?
            if (categoryMap.containsKey(item.getCategory())) {
                categoryMap.get(item.getCategory()).increaseCount();
            }
            // add new category
            else{
                if (!addCategory(new Category(item.getCategory()))) {
                    return false;
                }
            }
            itemMap.put(item.getDescription(), item);
            logger.log(Level.INFO,"New InventoryItem created");
            return true;
            }

        return false;
    }

    /**
     * Removes item from item map.
     *
     * @param name the description of the inventory item
     * @return true if item was removed
     */
    public boolean deleteItem(String name) {
        // item exists?
        if (itemMap.containsKey(name)) {
            if (removeItemFromStorage(itemMap.get(name), itemMap.get(name).getStock())) {
                // remove item
                categoryMap.get(itemMap.get(name).getCategory()).decreaseCount();
                itemMap.remove(name);
                logger.log(Level.INFO,"InventoryItem removed");
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a hash map of all inventory items in the inventory.
     *
     * @return hash map of all inventory items
     */
    public HashMap<String, InventoryItem> getItemMap() {
        return itemMap;
    }

    /**
     * Returns the inventory item with the matching description.
     *
     * @param name the description of the inventory item
     * @return the inventory item
     */
    public InventoryItem getItem(String name)
    {
        return itemMap.get(name);
    }

    /**
     * Increases the stock of an inventory item by a certain number.
     *
     * @param name the description of the inventory item
     * @param count the number the stock shall be increased by
     * @return true if successful
     */
    public boolean increaseStockBy(String name, int count){
        if (itemMap.containsKey(name) && categoryMap.containsKey(itemMap.get(name).getCategory())){
            if (addItemToStorage(itemMap.get(name), count)){
                itemMap.get(name).setStock(itemMap.get(name).getStock() + count);
                return true;
            }
        }
        return false;
    }

    /**
     * Decreases the stock of an item inventory item by a certain number.
     *
     * @param name the description of the inventory item
     * @param count the number the stock shall be decreased by
     * @return true if successful
     */
    public boolean decreaseStockBy(String name, int count){
        if (itemMap.containsKey(name) && categoryMap.containsKey(itemMap.get(name).getCategory())){
            if (itemMap.get(name).getStock() - count >= 0) {
                if (removeItemFromStorage(itemMap.get(name), count)) {
                    itemMap.get(name).setStock(itemMap.get(name).getStock() - count);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the hash map with all shelves of the inventory.
     *
     * @return hash map of shelves
     */
    public HashMap<Integer, Shelf> getShelfMap() {
        return shelfMap;
    }

    /**
     * Sets the hash map of shelves in the inventory to a new hash map.
     *
     * @param shelfMap the new hash map of shelves
     */
    public void setShelfMap(HashMap<Integer, Shelf> shelfMap) {
        this.shelfMap = shelfMap;
    }

    /**
     * Tries to update the shelf with the new weight of an inventory item.
     *
     * <p>This method adds the inventory item weight to the shelf if the total weight of the shelf is equal or below
     * 100000000 decigram which equals 10 tons.
     *
     *
     * @param item the inventory item to be added
     * @param count the new or current stock of the inventory item
     * @return true if successful
     */
    public boolean addItemToStorage(InventoryItem item, int count){
        // if shelf exists
        if (shelfMap.containsKey(item.getShelf())){
            // try to add Items
            return shelfMap.get(item.getShelf()).addToShelf(item, count);
            // change to heavy
        }
        else{
            // create new shelf with items
            if(item.getWeight() * item.getStock() <= 100000000) {
                shelfMap.put(item.getShelf(), new Shelf(item.getShelf(), item.getWeight() * count));
                //item.setStock(item.getStock() + count);
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the shelf with the weight of an inventory item by removing the weight.
     *
     * @param item the inventory item to be removed
     * @param count the amount of items to be removed
     * @return true if successful
     */
    public boolean removeItemFromStorage(InventoryItem item, int count){
        // if shelf exists
        if (shelfMap.containsKey(item.getShelf())){
            // try to remove items
            return shelfMap.get(item.getShelf()).removeFromShelf(item, count);
            // error in removal
        }
        else{
            // shelf doesnt exists
            return false;
        }
    }

    /**
     * Returns the hash map of all categories.
     *
     * @return the hash map of all categories.
     */
    public HashMap<String, Category> getCategoryMap(){
        return categoryMap;
    }

    /**
     * Tries to add a new category to the inventory.
     *
     * <p>This method returns false if a category already exists with this name.
     *
     * @param cat the new category
     * @return true if successful
     */
    public boolean addCategory(Category cat) {
        // category already exists?
        if ((categoryMap.containsKey(cat.getName())) || !cat.getName().matches("[a-zA-ZöäüÖÄÜß0-9]{1,256}")) {
            return false;
        } else {
            categoryMap.put(cat.getName(), cat);
            // category added
            return true;
        }
    }

    /**
     * Removes a category if it exists.
     *
     * @param cat the category which is to be removed
     * @return true if successful
     */
    public boolean removeCategory(Category cat){
        // category exists and is empty?
        if (categoryMap.containsKey(cat.getName()) && categoryMap.get(cat.getName()).getCount() == 0){
            categoryMap.remove(cat.getName());
            // category removed
            return true;
        }
        else {
            // category not found/removed
            return false;
        }
    }

    /**
     * Initialises the storage with all valid shelves.
     */
    private void initStorage(){
        // get all used shelves
        for(InventoryItem item : getItemMap().values()){
            // if shelf already exists
            if (shelfMap.containsKey(item.getShelf())){
                // add weight of new items
                // ignored if to heavy/invalid
                shelfMap.get(item.getShelf()).addToShelf(item, item.getStock());
            }
            else{
                // create new shelf, ignore if to heavy/invalid
                if(item.getWeight() * item.getStock() <= 100000000){     // 10t = 10 000kg = 10 000 000g = 100 000 000
                    shelfMap.put(item.getShelf(), new Shelf(item.getShelf(), item.getWeight() * item.getStock()));
                }
            }
        }
    }

    /**
     * Initialises all categories that are used by inventory items.
     */
    private void initCategories(){
        // get all used categories
        for(InventoryItem item : getItemMap().values()){
            // if category already exists
            if (categoryMap.containsKey(item.getCategory())){
                // add new item
                categoryMap.get(item.getCategory()).increaseCount();
            }
            else{
                // create new category
                categoryMap.put(item.getCategory(), item.getCategoryObj());
            }
        }
    }

}
