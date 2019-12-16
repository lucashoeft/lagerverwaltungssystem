import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
        CSVParser csvParser = new CSVParser();          // static?
        items = csvParser.readInventoryFromCSV(Paths.get(path));
    }

    // auf disk als CSV datei abspeichern
    public void store(){
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

            //CSVWriter csvWriter = new CSVWriter();
            //csvWriter.writeInventoryCSV(file.toString());

            // write new file
            FileWriter fw = new FileWriter(path);
            fw.write(toStringCSV());
            fw.close();

            System.out.println("Data saved");
            // delete backup
            Files.delete(Paths.get(backup));
            System.out.println("Backup deleted");
        }
        catch (IOException e) {System.err.println(e.toString());}
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
        for (InventoryItem item : items) {
            string = string.concat(item.toStringCSV() + "\n");
        }
        return string;
    }

}
