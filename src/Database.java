import java.util.List;

public class Database {
    //public static String path;
    private String path;
    List<InventoryItem> items;

    public Database(){
        this.path = null;
    }

    public Database(String path){
        this.path = path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    //load(); // file lesen, parsen und lokal abspeichern

    //store(); // auf disk als CSV datei abspeichern

    // liefert alle Items
    public List<InventoryItem> getItems() {
        return items;
    }

    // liefert alle Items mit einer Bezeichnung, die dem Suchbegriff entspricht
    public List<InventoryItem> getItems(String searchMask) {
        return items;
    }
}
