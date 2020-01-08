package com.lagerverwaltung;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.*;
import java.io.FileWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The App class contains the runnable main method and manages all actions before opening the main window. This means
 * to read the config file to locate the csv file containing the inventory.
 */
public class App {

    /**
     * The inventory which represents the model and is manipulated by methods at runtime.
     */
    private static Inventory inventory = new Inventory();

    /**
     * The configPath contains the location of the config file which contains the path for the .csv database
     */
    private static String configPath = System.getProperty("user.dir") + "/Data/config.cfg";

    private static final Logger logger = Logger.getLogger(App.class.getName());

    /**
     * Starts the application.
     *
     * <p>1. Read the config file.
     * <p>2. Start the Main class containing the graphical user interface.
     *
     *
     * @param args the arguments which could be given over to this main method
     */
    public static void main(String[] args) {

        readConfigFile(configPath);
        SwingUtilities.invokeLater(() -> {
            new Main();
        });

        logger.log(Level.INFO,"Config Path: " + configPath);
        logger.log(Level.INFO,"Inventory Path: " + inventory.getPath());
    }

    /**
     * Reads existing config file
     *
     * <p>This method tries to read the config file. It creates a new one if it could not find the config file at the
     * path location.
     *
     * @param path the path of the config file
     */
    public static void readConfigFile(String path) {
        try {
            Path cfgFile = Paths.get(path);
            if (Files.exists(cfgFile)) {
                logger.log(Level.INFO,"Config file found");
                if(clearConfigFile(path) == 0) {
                    Scanner scan = new Scanner(cfgFile);
                    inventory.setPath(scan.nextLine());
                    logger.log(Level.INFO,"Config file loaded");
                }
                else {
                    readConfigFile(path);
                }
            }
            else {
                logger.log(Level.INFO,"Config file not found");
                Files.createDirectories(cfgFile.getParent());
                Files.createFile(cfgFile);
                logger.log(Level.INFO,"New Config file created");
            }
        } catch (IOException e) {
            logger.log(Level.WARNING,e.getMessage());
        }
    }

    /**
     * Updates the config file with a new path pointing at csv file containing the inventory.
     *
     * @param config_path the path of the config file
     * @param new_path the new path of the inventory csv file
     */
    public static void setConfigFile(String config_path, String new_path) {
        try {
            Path cfgFile = Paths.get(config_path);
            if (Files.exists(cfgFile)) {
                logger.log(Level.INFO,"Config file found");
                FileWriter fw = new FileWriter(config_path);
                fw.write(new_path);
                fw.close();
                logger.log(Level.INFO,"Config file updated");
            } else {
                logger.log(Level.INFO,"Config file not found");
            }
        } catch (IOException e) {
            logger.log(Level.WARNING,e.getMessage());
        }
    }

    /**
     * Deletes an empty config file at the location of the path.
     *
     * @param path the path of the config file
     * @return 1 if config file at path was deleted
     */
    public static int clearConfigFile(String path){
        File cfgFile = new File(path);
        if (cfgFile.length() == 0) {
            cfgFile.delete();
            logger.log(Level.INFO,"Empty config file deleted");
            return 1;
        }
        return 0;
    }

    /**
     * Returns the current inventory.
     *
     * @return the inventory
     */
    public static Inventory getInventory(){
        return inventory;
    }

    /**
     * Updates the current inventory to a new inventory.
     *
     * @param newInventory the new inventory
     */
    public static void setInventory(Inventory newInventory) {
        inventory = newInventory;
    }

    /**
     * Returns the location of the current config file.
     *
     * @return the path of the config file
     */
    public static String getConfigPath(){
        return configPath;
    }
}
