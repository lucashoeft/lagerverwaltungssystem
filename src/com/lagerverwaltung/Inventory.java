package com.lagerverwaltung;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Inventory class holds all information about the inventory items, the shelfs and the categories.
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
     * The constructor for the Inventory class, which initialises the path of the csv file.
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
                addCategory(new Category(item.getCategory()));
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
     * Increases the stock of an inventory item.
     *
     * @param name name of the item
     * @param count new stock = old stock + count
     * @return true if successful else return false
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
     * Decrease the stock of an item
     *
     * @param name name of the item
     * @param count new stock= old stock - count
     * @return true if successful else return false
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
     * @return all shelves Map
     */
    public HashMap<Integer, Shelf> getShelfMap() {
        return shelfMap;
    }

    /**
     * @param shelfMap new Map of all shelves
     */
    public void setShelfMap(HashMap<Integer, Shelf> shelfMap) {
        this.shelfMap = shelfMap;
    }

    /**
     * Add count new items to Storage if it doesn't make shelf too heavy
     *
     * @param item item to be added
     * @param count amount of items
     * @return true if successful, else return false
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
     * Remove count items from Storage
     *
     * @param item item to be removed
     * @param count amount of items to be removed
     * @return true if successful, else return false
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
     * @return Map of all categories
     */
    public HashMap<String, Category> getCategoryMap(){
        return categoryMap;
    }

    /**
     * Add new category, if it doesn't exist already
     *
     * @param cat new categorie which is added to categoryMap
     * @return true if successful, else return false
     */
    public boolean addCategory(Category cat){
        // category already exists?
        if (categoryMap.containsKey(cat.getName())){
            return false;
        }
        else {
            categoryMap.put(cat.getName(), cat);
            // category added
            return true;
        }
    }

    /**
     * Remove category, if it exists
     *
     * @param cat category which is to be removed
     * @return true if successful, else return false
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
     * Initialize storage with all valid shelves
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
     * Find all categories in itemMap and set categoryMap to all used categories
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
