import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Inventory {

    private String path;
    private List<InventoryItem> items;

    public Inventory(){
        path = "";
        items = new ArrayList<>();
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


    // check uniqueness
    public boolean checkUnique(InventoryItem item) {
        Iterator<InventoryItem> i = items.iterator();
        while (i.hasNext()){
            // description and location unique?
            if (!i.next().isUnique(item)){
                return false;
            }
        }
        // unique
        return true;
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

}
