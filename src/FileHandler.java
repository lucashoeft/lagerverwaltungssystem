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

/**
 * The File Handler manages all actions concerning reading and writing the database
 * @author ...
 * @version 1.0
 */
public class FileHandler {
    /**
     * create Inventory object from a .CSV file at pathName
     * @param pathName the path where the file is used from
     * @return Inventory object
     */
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
                //String[] attributes = line.split(",");
                String[] attributes = new String[6];
                int i = 0;
                for (String piece: line.split(",")) {
                    if (!piece.equals("") && !piece.equals(null)) {
                        attributes[i] = piece;
                    }
                    i++;
                }

                InventoryItem inventoryItem = createInventoryItem(attributes);

                // adding inventoryItem into itemMap
                if (inventoryItem.isValid()) {
                    itemMap.put(inventoryItem.getDescription(), inventoryItem); // only add valid items to inventory
                }
                // if Category found
                // add Category into categoryMap
                if (inventoryItem.isCategory()) {
                    App.getInventory().addCategory(new Category(inventoryItem.getCategory()));
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

    /**
     * Creates a InventoryItem object from an Array that has been read from a .CSV file
     * @param metadata String Array with all needed attributes of an InventoryItem object
     * @return new InventoryItem containing metadata
     */
    private InventoryItem createInventoryItem(String[] metadata) {
        InventoryItem inventoryItem = new InventoryItem(null, null, null, null, null, null);
        try {
            if(metadata[0] != null) {
                inventoryItem.description = metadata[0].replaceAll("^\"|\"$", "");
            }
            if(metadata[1] != null) {
                inventoryItem.category = metadata[1].replaceAll("^\"|\"$", "");
            }
            if(metadata[2] != null) {
                inventoryItem.stock = Integer.parseInt(metadata[2]);
            }
            if(metadata[3] != null) {
                inventoryItem.location = metadata[3].replaceAll("^\"|\"$", "");
            }
            if(metadata[4] != null) {
                inventoryItem.weight = Integer.parseInt(metadata[4]);
            }
            if(metadata[5] != null) {
                inventoryItem.price = Integer.parseInt(metadata[5]);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        // create and return book of this metadata
        return inventoryItem;
    }

    /**
     * Takes an Inventory object and writes it to a .CSV file
     * @param inventory inventory which is to be saved
     */
    public void storeInventoryInCSV(Inventory inventory) {
        try {
            String path = inventory.getPath();
            Path file = Paths.get(path);
            String backup = file.getParent().toString() + "/backup.csv";
            if (!path.equals("")) {
                // delete old backup
                if (Files.exists(Paths.get(backup))) {
                    Files.delete(Paths.get(backup));
                    System.out.println("old backup deleted");
                }
                // create backup if old file exists
                if (Files.exists(file)) {
                    Files.move(file, Paths.get(backup));
                    System.out.println("Backup created");
                } else {
                    System.out.println("New File created");
                }
            }

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
            if (Files.exists(Paths.get(backup))) {
                Files.delete(Paths.get(backup));
                System.out.println("Backup deleted");
            }
        }
        catch (IOException e) {System.err.println(e.toString()+"test");}
    }



}
