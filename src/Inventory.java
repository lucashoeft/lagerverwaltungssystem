import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Iterator;

public class Inventory {

    private String path;
    private List<InventoryItem> items;

    public Inventory(){
        path = "";
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

    // file lesen, parsen und lokal abspeichern
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
