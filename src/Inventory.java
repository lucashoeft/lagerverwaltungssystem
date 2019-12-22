import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Inventory {

    private String path;
    private List<InventoryItem> items;
    private HashMap<Integer, Shelf> shelfMap;
    private HashMap<String, Category> categoryMap;

    public Inventory(){
        path = "";
        items = new ArrayList<>();
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
        items = fileHandler.readInventoryFromCSV(Paths.get(path));
    }

    // add item
    public boolean addItem(InventoryItem item) {
        // new item?
        if (!items.contains(item)) {
            // not to heavy?
            if (addItemToStorage(item, item.getStock())) {
                // add item
                categoryMap.get(item.getCategory()).increaseCount();
                items.add(item);
                System.out.println("new InventoryItem created");
                return true;
            }
        }
        return false;
    }

    // remove item
    public boolean removeItem(InventoryItem item) {
        // item exists?
        if (items.contains(item)) {
            if (removeItemFromStorage(item, item.getStock())) {
                // remove item
                categoryMap.get(item.getCategory()).decreaseCount();
                items.remove(item);
                System.out.println("InventoryItem removed");
                return true;
            }
        }
        return false;
    }

    // return all items
    public List<InventoryItem> getItems() {
        return items;
    }

    // return all items matching the search mask
    public List<InventoryItem> getItems(String searchMask) {
        return items;
    }

    // converting item list into csv-compatible string
    public String toStringCSV() {
        String string = "";
        Iterator<InventoryItem> i = items.iterator();
        while (i.hasNext()){
            string = string.concat(i.next().toStringCSV()+"\n");
        }
        return string;
    }

    // initialize storage with all needed shelves
    public void initStorage(){
        Iterator<InventoryItem> i = getItems().iterator();
        // get all used shelves
        while (i.hasNext()){
            InventoryItem item = i.next();
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
                item.setStock(item.getStock() + count);
                return true;
            }
            // change to heavy
            return false;
        }
        else{
            // create new shelf with items
            shelfMap.put(item.getShelf(), new Shelf(item.getShelf(), item.getWeight() * (item.getStock() + count)));
            item.setStock(item.getStock() + count);
            return true;
        }
    }

    //entfernt count Artikel aus Storage
    public boolean removeItemFromStorage(InventoryItem item, int count){
        // if shelf exists
        if (shelfMap.containsKey(item.getShelf())){
            // try to remove items
            if (shelfMap.get(item.getShelf()).removeFromShelf(item,  count)){
                item.setStock(item.getStock() - count);
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
            return false;
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
        Iterator<InventoryItem> i = getItems().iterator();
        // get all used categories
        while (i.hasNext()){
            InventoryItem item = i.next();
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
