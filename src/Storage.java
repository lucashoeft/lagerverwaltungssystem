import java.util.HashMap;
import java.util.Iterator;

/**
 * A storage contains a list of all shelves inside the Warehouse
 *
 * @author ...
 * @version 1.0
 */
public class Storage {


    /**
     * HashMap of all shelves
      */

    private HashMap<Integer, Shelf> shelfMap;

    /**
     * Constructor for Storage objects
      */
    public Storage() {
        shelfMap = new HashMap<Integer, Shelf>();
    }

    /**
     * Initialization function
     * Find all shelves and add them to shelfMap
     * @param inventory holds all items which a Storage is built out of
      */
    public void init(Inventory inventory){
        Iterator<InventoryItem> i = inventory.getItems().iterator();
        // get all used shelves
        while (i.hasNext()){
            InventoryItem item = i.next();
            // if shelf already exists
            if (shelfMap.containsKey(item.getShelf())){
                // add weight of new items
                shelfMap.get(item.getShelf()).addToShelf(item, item.getStock());
            }
            else{
                // create new shelf
                shelfMap.put(item.getShelf(), new Shelf(item.getShelf(), item.getWeight() * item.getStock()));
            }
        }
    }
    // Setter und Getter

    /**
     * @return a Map of all shelves in a storage
     */
    public HashMap<Integer, Shelf> getShelfMap() {
        return shelfMap;
    }

    /**
     * @param shelfMap new Map of all shelves in a storage
     */
    public void setShelfMap(HashMap<Integer, Shelf> shelfMap) {
        this.shelfMap = shelfMap;
    }

    /**
     * adds Storage count new articles, if not too heavy
     * @param item item to be added to a storage
     * @param count count of item to be added to a storage
     * @return true if adding was successful, else false
     */
    public boolean addItemToStorage(InventoryItem item, int count){
        // if shelf exists
        if (shelfMap.containsKey(item.getShelf())){
            // try to add articles
            if (shelfMap.get(item.getShelf()).addToShelf(item, count)){
                item.setStock(item.getStock() + count);
                return true;
            }
            // Change exceeds maximum shelf weight
            return false;
        }
        else{
            // Create new shelf with item
            shelfMap.put(item.getShelf(), new Shelf(item.getShelf(), item.getWeight() * (item.getStock() + count)));
            item.setStock(item.getStock() + count);
            return true;
        }
    }

    /**
     * removes count articles from storage
     * @param item item to be removed from a storage
     * @param count count of item to be removed from a storage
     * @return true if successful
     */
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


}
