import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public Inventory readInventoryFromCSV(Path pathName) {
        Inventory inventory = new Inventory();
        HashMap<String, InventoryItem> itemMap = new HashMap<String, InventoryItem>();
        inventory.setPath(pathName.toString());

        try (BufferedReader br = Files.newBufferedReader(pathName,
                StandardCharsets.UTF_8)) {

            String line = br.readLine();

            // loop until all lines are read
            while (line != null) {
                String[] attributes = line.split(",");

                InventoryItem inventoryItem = createInventoryItem(attributes);

                // adding inventoryItem into itemMap
                if (inventoryItem.isValid()) {
                    itemMap.put(inventoryItem.getDescription(), inventoryItem); // only add valid items to inventory
                }

                // if Category found, add Category into categoryMap
                if (inventoryItem.isCategory()) {
                    inventory.addCategory(new Category(inventoryItem.getCategory()));
                }

                line = br.readLine();
            }

        } catch (IOException ioe) {
            //ioe.printStackTrace();
        }

        inventory.setItemMap(itemMap);
        return inventory;
    }

    /**
     * Creates a InventoryItem object from an Array that has been read from a .CSV file
     * @param metadata String Array with all needed attributes of an InventoryItem object
     * @return new InventoryItem containing metadata
     */
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

    /**
     * Takes an Inventory object and writes it to a .CSV file
     * @param inventory inventory which is to be saved
     */
    public void storeInventoryInCSV(Inventory inventory) {
        try {
            String path = inventory.getPath();
            boolean check = false;
            //Path file = Paths.get(path);
            //String backup = file.getParent().toString() + "/backup.csv";
            if (!path.equals("")) {
                Path file = Paths.get(path);
                String backup = file.getParent().toString() + "/backup.csv";
                check = true;
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
            if (check) {
                Files.delete(Paths.get(Paths.get(path).getParent().toString() + "/backup.csv"));
                System.out.println("Backup deleted");
            }
        }
        catch (IOException e) {System.err.println(e.toString()+"test");}
    }



}
