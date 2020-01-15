package com.lagerverwaltung;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The File Handler manages all actions concerning reading and writing to the csv file which is used for making the
 * inventory persistent.
 */
public class FileHandler {

    /**
     * A logger to log messages.
     */
    private static final Logger logger = Logger.getLogger(FileHandler.class.getName());

    /**
     * Creates an inventory from a csv file.
     *
     * <p>This method creates an empty inventory and then reads the csv file line by line. If it detects the pattern of
     * a category, it adds it to the category hash map, else it tries to add it as an inventory item and ignores it if
     * attributes are not valid or if description and location are not unique. In the end, it returns the entire
     * inventory.
     *
     * @param pathName the path where the csv file lies.
     * @return the inventory
     */
    public Inventory readInventoryFromCSV(Path pathName) {
        Inventory inventory = new Inventory();
        HashMap<String, InventoryItem> itemMap = new HashMap<String, InventoryItem>();
        inventory.setPath(pathName.toString());
        HashSet<String> checkSet = new HashSet<String>(1000000);

        try (BufferedReader br = Files.newBufferedReader(pathName, StandardCharsets.UTF_8)) {

            String line = br.readLine();

            // loop until all lines are read
            while (line != null) {
                String[] attributes = line.split(",");

                if (isCategory(attributes)) {
                    inventory.addCategory(new Category(attributes[1]));
                } else {
                    try {
                        InventoryItem inventoryItem = createInventoryItem(attributes);
                        if (!checkSet.add(inventoryItem.getLocation())) {
                            logger.log(Level.WARNING, "invalid item: " + inventoryItem.getDescription());
                        } else {
                            itemMap.put(inventoryItem.getDescription(), inventoryItem);
                        }
                    } catch (IllegalArgumentException iae) {
                        logger.log(Level.WARNING, iae.getMessage());
                    }
                }
                line = br.readLine();
            }

        } catch (IOException ioe) {
            logger.log(Level.WARNING, ioe.getMessage());
        }

        inventory.setItemMap(itemMap);
        return inventory;
    }

    /**
     * Saves the inventory to its file path.
     *
     * <p>This method creates a back up before it tries to save to the file path. It writes the categories first and
     * then the inventory items line by line to the file.
     *
     * @param inventory the inventory which shall be saved
     */
    public void storeInventoryInCSV(Inventory inventory) {
        try {
            String path = inventory.getPath();
            boolean check = false;

            if (!path.equals("")) {
                Path file = Paths.get(path);
                String backup = file.getParent().toString() + "/backup.csv";
                check = true;
                // delete old backup
                if (Files.exists(Paths.get(backup))) {
                    Files.delete(Paths.get(backup));
                    logger.log(Level.INFO,"Old backup deleted");
                }
                // create backup if old file exists
                if (Files.exists(file)) {
                    Files.move(file, Paths.get(backup));
                    logger.log(Level.INFO,"Backup created");
                } else {
                    logger.log(Level.INFO,"New File created");
                }
            }

            // write new file
            // if no path found, write at standard location
            if (path.equals("")){
                path = System.getProperty("user.dir") + "/Data/Lagersystem.csv";
                App.setConfigFile(App.getConfigPath(), path);
            }

            FileWriter fw = new FileWriter(path);
            for (Category cat : inventory.getCategoryMap().values()) {
                fw.write(cat.toStringCSV() + "\n");
            }
            for (InventoryItem inventoryItem : inventory.getItemMap().values()) {
                fw.write(inventoryItem.toStringCSV() + "\n");
            }
            fw.flush();
            fw.close();

            logger.log(Level.INFO,"Data saved");

            // delete backup
            if (check) {
                Files.delete(Paths.get(Paths.get(path).getParent().toString() + "/backup.csv"));
                logger.log(Level.INFO,"Backup deleted");
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    /**
     * Returns true if the array of attributes are matching to the category pattern.
     *
     * @param attributes the array of attributes of an object
     * @return true if it is a category
     */
    private Boolean isCategory(String[] attributes) {
        if (!attributes[0].equals("-1")) return false;
        if ((attributes[1].equals("-1")) || (attributes[1].indexOf(',') >= 0)) return false;
        if (!attributes[2].equals("-1")) return false;
        if (!attributes[3].equals("-1")) return false;
        if (!attributes[4].equals("-1")) return false;
        return (attributes[5].equals("-1"));
    }

    /**
     * Creates an inventory item from an array of attributes.
     *
     * @param metadata he array of attributes of an inventory item
     * @return the inventory item containing the attributes
     */
    private InventoryItem createInventoryItem(String[] metadata) throws IllegalArgumentException {
        if (metadata.length != 6) {
            throw new IllegalArgumentException("Fehlerhafte Zeile ignoriert.");
        }
        return new InventoryItem(metadata[0],
                metadata[1],
                Integer.parseInt(metadata[2]),
                metadata[3],
                Integer.parseInt(metadata[4]),
                Integer.parseInt(metadata[5]));
    }

}
