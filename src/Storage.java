import java.util.HashMap;
import java.util.Iterator;


public class Storage {


    private HashMap<Integer, Shelf> shelfMap;

    public Storage() {
        shelfMap = new HashMap<Integer, Shelf>();
    }

    public void init(Inventory inventory){
        Iterator<InventoryItem> i = inventory.getItems().iterator();
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
                // TODO einheit noch in gramm
                if(item.getWeight() * item.getStock() < 10000000){
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


}
