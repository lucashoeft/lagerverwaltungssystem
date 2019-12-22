import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Inventory {

    private String path;
    private List<InventoryItem> items;
    private HashMap<Integer, Shelf> shelfMap;
    private List<String> categories;

    public Inventory(){
        path = "";
        items = new ArrayList<>();
        shelfMap = new HashMap<Integer, Shelf>();
        categories = new ArrayList<>();
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

    // add item, checks required by caller
    public boolean addItem(InventoryItem item) {  return items.add(item); }

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

    public List<String> getCategories(){
        return categories;
    }

    public boolean addCategory(String name){
        // category already exists?
        if (!categories.contains(name)){
            return categories.add(name);
            // category added
        }
        else {
            // category not added
            return false;
        }
    }

    public void initCategories(){
        Iterator<InventoryItem> i = getItems().iterator();
        // get all used categories
        while (i.hasNext()){
            InventoryItem item = i.next();
            // category already exists?
            if (!categories.contains(item.getCategory())){
                // add category
                categories.add(item.getCategory());
            }
        }
    }

}
