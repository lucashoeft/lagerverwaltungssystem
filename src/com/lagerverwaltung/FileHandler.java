package com.lagerverwaltung;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The File Handler manages all actions concerning reading and writing the database
 *
 * @author ...
 * @version 1.0
 */
public class FileHandler {

    private static final Logger logger = Logger.getLogger(FileHandler.class.getName());

    /**
     * Create Inventory object from a .CSV file at pathName
     *
     * @param pathName the path where the file is used from
     * @return Inventory object
     * @see Inventory
     */
    public Inventory readInventoryFromCSV(Path pathName) {
        Inventory inventory = new Inventory();
        HashMap<String, InventoryItem> itemMap = new HashMap<String, InventoryItem>();
        inventory.setPath(pathName.toString());

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
                        itemMap.put(inventoryItem.getDescription(), inventoryItem);
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
     * Takes an Inventory object and writes it to a .CSV file
     * <p>
     *  creates backup, writes new file and then deletes backup
     * </p>
     *
     * @param inventory inventory which is to be saved
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
            fw.write(inventory.toStringCSV());
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

    private Boolean isCategory(String[] attributes) {
        if (!attributes[0].equals("-1")) return false;
        if ((attributes[1].equals("-1")) || (attributes[1].indexOf(',') >= 0)) return false;
        if (!attributes[2].equals("-1")) return false;
        if (!attributes[3].equals("-1")) return false;
        if (!attributes[4].equals("-1")) return false;
        return (attributes[5].equals("-1"));
    }

    /**
     * Creates a InventoryItem object from an Array that has been read from a .CSV file
     *
     * @param metadata String Array with all needed attributes of an InventoryItem object
     * @return new InventoryItem containing metadata
     */
    private InventoryItem createInventoryItem(String[] metadata) throws IllegalArgumentException {
        return new InventoryItem(metadata[0],
                    metadata[1],
                    Integer.parseInt(metadata[2]),
                    metadata[3],
                    Integer.parseInt(metadata[4]),
                    Integer.parseInt(metadata[5]));
    }

}
