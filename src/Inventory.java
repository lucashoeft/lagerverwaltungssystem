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
        path = null;
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
            if (Files.exists(file)) {
                String backup = file.getParent().toString()+"/backup.csv";
                Files.move(file, Paths.get(backup));
                System.out.println("Backup created");
                Files.createFile(file);

                CSVWriter csvWriter = new CSVWriter();
                csvWriter.writeInventoryCSV(file.toString());

                System.out.println("Data saved");
                Files.delete(Paths.get(backup));
                System.out.println("Backup deleted");
            }
            else {
                System.out.println("ERROR");
                // File not found Exception
            }
        }
        catch (IOException e) {System.err.println(e);}
    }

    // liefert alle Items
    public List<InventoryItem> getItems() {
        return items;
    }

    // liefert alle Items mit einer Bezeichnung, die dem Suchbegriff entspricht
    public List<InventoryItem> getItems(String searchMask) {
        return items;
    }

    public String toStringCSV() {
        String string = "";
        Iterator<InventoryItem> i = items.iterator();
        while (i.hasNext()){
            string = string.concat(i.next().toStringCSV()+"\n");      //new line am ende!!
        }
        return string;
    }

}
