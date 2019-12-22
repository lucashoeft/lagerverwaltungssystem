import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * One inventory object contains the whole database during runtime
 * @author ...
 * @version 1.0
 */
public class Inventory {

    /**
     * path stores the path where the .csv file of the database lies
     */
    private String path;

    /**
     * items contains a list of all items currently on stock
     */
    private List<InventoryItem> items;

    /**
     * Constructor for a new Inventory object
     */
    public Inventory(){
        path = "";
        items = new ArrayList<>();
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
     */
    public void loadData() {
        FileHandler fileHandler = new FileHandler();
        items = fileHandler.readInventoryFromCSV(Paths.get(path));
    }

    // nach FileHandler verschoben
    // auf disk als CSV datei abspeichern
    /*public void store(){
        try {
            Path file = Paths.get(path);
            String backup = file.getParent().toString() + "/backup.csv";
            // delete old backup
            if (Files.exists(Paths.get(backup))) {
                Files.delete(Paths.get(backup));
                System.out.println("old backup deleted");
            }
            // create backup if old file exists
            if (Files.exists(file)) {
                Files.move(file, Paths.get(backup));
                System.out.println("Backup created");
            }
            else {
                System.out.println("New File created");
            }
            Files.createFile(file);

            // write new file
            // if no path found, write at standard location
            if (path == ""){
                path = System.getProperty("user.dir") + "/Data/Lagersystem.csv";
                App.setConfigFile(App.getConfigPath(), path);
            }
            FileWriter fw = new FileWriter(path);
            fw.write(toStringCSV());
            fw.close();

            System.out.println("Data saved");
            // delete backup
            Files.delete(Paths.get(backup));
            System.out.println("Backup deleted");
        }
        catch (IOException e) {System.err.println(e);}
    }*/

    // add an item
    // TODO currently only used for testing; does not check any constraints
    public boolean addItem(InventoryItem item) {  return items.add(item); }

    /**
     * @return all items in Inventory
     */
    public List<InventoryItem> getItems() {
        return items;
    }

    /**
     * @return all items matching the search mask
      */
    public List<InventoryItem> getItems(String searchMask) {
        return items;
    }

    /**
     * converting item list into csv-compatible string
     * @return csv-compatible string of the inventory
     */
    public String toStringCSV() {
        String string = "";
        Iterator<InventoryItem> i = items.iterator();
        while (i.hasNext()){
            string = string.concat(i.next().toStringCSV()+"\n");
        }
        return string;
    }

}
