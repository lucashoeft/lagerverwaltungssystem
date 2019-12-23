import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Inventory {

    private String path;
    private HashMap<String, InventoryItem> itemMap;
    private HashMap<Integer, Shelf> shelfMap;
    private HashMap<String, Category> categoryMap;

    public Inventory(){
        path = "";
        itemMap = new HashMap<String, InventoryItem>();
        shelfMap = new HashMap<Integer, Shelf>();
        categoryMap = new HashMap<String, Category>();
    }

    public Inventory(String path){
        this.path = path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    // read file, parse and store
    public void loadData() {
        FileHandler fileHandler = new FileHandler();
        itemMap = fileHandler.readInventoryFromCSV(Paths.get(path));
    }

    // add item
    public boolean addNewItem(InventoryItem item) {
        // new item?
        // TODO testen
        if (!itemMap.containsValue(item)) {         // does containsValue use o.equals()?
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
                System.out.println("new InventoryItem created");
                return true;
            }
        }
        return false;
    }

    // remove item
    public boolean deleteItem(String name) {
        // item exists?
        if (itemMap.containsKey(name)) {
            if (removeItemFromStorage(itemMap.get(name), itemMap.get(name).getStock())) {
                // remove item
                categoryMap.get(itemMap.get(name).getCategory()).decreaseCount();
                itemMap.remove(name);
                System.out.println("InventoryItem removed");
                return true;
            }
        }
        return false;
    }

    // return all items
    public HashMap<String, InventoryItem> getItemMap() {
        return itemMap;
    }

    // return all items matching the search mask
    public HashMap<String, InventoryItem> getItems(String searchMask) {
        return itemMap;
    }

    // change item stock
    public boolean increaseStockBy(String name, int count){
        if (itemMap.containsKey(name) && categoryMap.containsKey(itemMap.get(name).getCategory())){
            if (addItemToStorage(itemMap.get(name), count)){
                itemMap.get(name).setStock(itemMap.get(name).getStock() + count);
                return true;
            }
        }
        return false;
    }

    // change item stock
    public boolean decreaseStockBy(String name, int count){
        if (itemMap.containsKey(name) && categoryMap.containsKey(itemMap.get(name).getCategory())){
            if (removeItemFromStorage(itemMap.get(name), count)){
                itemMap.get(name).setStock(itemMap.get(name).getStock() - count);
                return true;
            }
        }
        return false;
    }


    // converting item list into csv-compatible string
    public String toStringCSV() {
        String string = "";
        for(InventoryItem item : getItemMap().values()){
            string = string.concat(item.toStringCSV()+"\n");
        }
        return string;
    }

    // initialize storage with all needed shelves
    public void initStorage(){
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
                if(item.getWeight() * item.getStock() < 100000000){     // 10t = 10 000kg = 10 000 000g = 100 000 000
                    shelfMap.put(item.getShelf(), new Shelf(item.getShelf(), item.getWeight() * item.getStock()));
                }
            }
        }
    }

    public HashMap<Integer, Shelf> getShelfMap() {
        return shelfMap;
    }

    public void setShelfMap(HashMap<Integer, Shelf> shelfMap) {
        this.shelfMap = shelfMap;
    }

    //fügt Storage count neue Artikel hinzu, falls nicht zu schwer
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
            if(item.getWeight() * item.getStock() < 100000000) {
                shelfMap.put(item.getShelf(), new Shelf(item.getShelf(), item.getWeight() * count));
                //item.setStock(item.getStock() + count);
                return true;
            }
        }
        return false;
    }

    //entfernt count Artikel aus Storage
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

    public HashMap<String, Category> getCategoryMap(){
        return categoryMap;
    }

    //
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

    //public boolean addToCategory()

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

    public void initCategories(){
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
