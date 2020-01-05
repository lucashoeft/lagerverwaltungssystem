package com.lagerverwaltung;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * during runtime an Inventory object contains all items of the database
 */
public class Inventory {

    /**
     * path stores the path where the .csv file of the database lies
     */
    private String path;

    /**
     * itemMap contains all items at the key of their description
     */
    private HashMap<String, InventoryItem> itemMap;

    /**
     * shelfMap contains all shelves at the key of their ID
     */
    private HashMap<Integer, Shelf> shelfMap;

    /**
     * categoryMap contains all categories at the key of their name
     */
    private HashMap<String, Category> categoryMap;

    private static final Logger logger = Logger.getLogger(Inventory.class.getName());

    /**
     * Constructor for a new Inventory object
     */
    public Inventory(){
        path = "";
        itemMap = new HashMap<String, InventoryItem>();
        shelfMap = new HashMap<Integer, Shelf>();
        categoryMap = new HashMap<String, Category>();
    }

    /**
     * Constructor for a new Inventory object
     * @param path the path of the database .CSV file
     */
    public Inventory(String path){
        this.path = path;
    }

    /**
     * @param path new .CSV database file path
     */
    public void setPath(String path){
        this.path = path;
    }

    /**
     * @return .CSV database file path of this Inventory object
     */
    public String getPath() {
        return path;
    }

    /**
     * read file, parse file to object and save locally
     * @param itemMap HashMap
     */
    public void setItemMap(HashMap<String, InventoryItem> itemMap) {
        this.itemMap = itemMap;
        this.initStorage();
        this.initCategories();
    }

    /**
     * add a new item to itemMap.
     * is successful when item doesn't exist yet and won't make shelf too heavy
     *
     * @param item item to be added
     * @return true if successful else return false
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
     * remove item from itemMap
     *
     * @param name name of the item
     * @return true if item removed else return false
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
     * @return all existing items
     */
    public HashMap<String, InventoryItem> getItemMap() {
        return itemMap;
    }

    /**
     * @param name name of an item
     * @return item object with name
     */
    public InventoryItem getItem(String name)
    {
        return itemMap.get(name);
    }

    /**
     * @param searchMask String which needs to be in all found items
     * @return all items fitting the search mask
     */
    public HashMap<String, InventoryItem> getItems(String searchMask) {
        return itemMap;
    }

    /**
     * increase the stock of an item
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
     * decrease the stock of an item
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
     * converting item list into csv-compatible string
     * @return csv-compatible string of the inventory
     */
    public String toStringCSV() {
        String string = "";
        for(Category cat : getCategoryMap().values()){
            string = string.concat(cat.toStringCSV()+"\n");
        }
        for(InventoryItem item : getItemMap().values()){
            string = string.concat(item.toStringCSV()+"\n");
        }
        return string;
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
     * add count new items to Storage if it doesn't make shelf too heavy
     *
     * @param item item to be added
     * @param count amount of items
     * @return true if successful, else return false
     */
    public boolean addItemToStorage(InventoryItem item, int count){
        // if shelf exists
        if (shelfMap.containsKey(item.getShelf())){
            // try to add Items
            if (shelfMap.get(item.getShelf()).addToShelf(item, count)){
                return true;
            }
            // change to heavy
            return false;
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
     * remove count items from Storage
     *
     * @param item item to be removed
     * @param count amount of items to be removed
     * @return true if successful, else return false
     */
    public boolean removeItemFromStorage(InventoryItem item, int count){
        // if shelf exists
        if (shelfMap.containsKey(item.getShelf())){
            // try to remove items
            if (shelfMap.get(item.getShelf()).removeFromShelf(item,  count)){
                return true;
            }
            // error in removal
            return false;
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
     * initialize storage with all valid shelves
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
