import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class FileHandler {

    public HashMap<String, InventoryItem> readInventoryFromCSV(Path pathName) {
        HashMap<String, InventoryItem> itemMap = new HashMap<String, InventoryItem>();

        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathName,
                StandardCharsets.UTF_8)) {

            // read the first line from the text file
            String line = br.readLine();

            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");

                InventoryItem inventoryItem = createInventoryItem(attributes);

                // adding inventoryItem into ArrayList
                if (inventoryItem.isValid()) {
                    itemMap.put(inventoryItem.getDescription(), inventoryItem); // only add valid items to inventory
                }

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        } catch (IOException ioe) {
            //ioe.printStackTrace();
        }

        return itemMap;
    }

    private InventoryItem createInventoryItem(String[] metadata) {
        InventoryItem inventoryItem = new InventoryItem(null, null, null, null, null, null);
        try {
            inventoryItem.description = metadata[0].replaceAll("^\"|\"$", "");
            inventoryItem.category = metadata[1].replaceAll("^\"|\"$", "");
            inventoryItem.stock = Integer.parseInt(metadata[2]);
            inventoryItem.location = metadata[3].replaceAll("^\"|\"$", "");
            inventoryItem.weight = Integer.parseInt(metadata[4]);
            inventoryItem.price = Integer.parseInt(metadata[5]);
        } catch (Exception e) {
            //e.printStackTrace();
        }

        // create and return book of this metadata
        return inventoryItem;
    }

    public void storeInventoryInCSV(Inventory inventory) {
        try {
            String path = inventory.getPath();
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
            if (path.equals("")){
                path = System.getProperty("user.dir") + "/Data/Lagersystem.csv";
                App.setConfigFile(App.getConfigPath(), path);
            }
            FileWriter fw = new FileWriter(path);
            fw.write(inventory.toStringCSV());
            fw.close();

            System.out.println("Data saved");
            // delete backup
            Files.delete(Paths.get(backup));
            System.out.println("Backup deleted");
        }
        catch (IOException e) {System.err.println(e.toString());}
    }



}
