import java.util.HashMap;
import java.util.Iterator;


public class Storage {


    private HashMap<Integer, Shelf> shelfMap;

    public void init(Inventory inventory){
        Iterator<InventoryItem> i = inventory.getItems().iterator();
        while (i.hasNext()){
            InventoryItem item = i.next();
            Shelf shelf = new Shelf(item.getShelf(), item.getWeight() * item.getStock());
            if (shelfMap.containsKey(shelf.getId())){
                shelfMap.get(shelf.getId()).addToShelf(item, item.getStock());
            }
            else{
                shelfMap.put(shelf.getId(), shelf);
            }
        }
    }
    public HashMap<Integer, Shelf> getShelfMap() {
        return shelfMap;
    }

    public void setShelfMap(HashMap<Integer, Shelf> shelfMap) {
        this.shelfMap = shelfMap;
    }


}
