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
 * The App Class contains the runnable main method and manages all actions before opening the main window and after
 * closing
 *
 * @author ...
 */
public class App {

    /**
     * inventory contains all items in the warehouse
     */
    private static Inventory inventory = new Inventory();

    /**
     * The configPath contains the location of the config file which contains the path for the .csv database
     */
    private static String configPath = System.getProperty("user.dir") + "/Data/config.cfg";

    private static final Logger logger = Logger.getLogger(App.class.getName());

    /**
     * Starts the software
     * <p>
     * 1. read Config
     * 2. start GUI
     * </p>
     *
     * @param args arguments which could be given over to this main method
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
     * Reads existing config file, if it doesn't exist, create one
     *
     * @param path path of the config file
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
     * Rewrites the config file to contain new_path as a new path
     *
     * @param config_path path of the config file
     * @param new_path new path inside the config file
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
     * Deletes an empty config file at path
     *
     * @param path path of config file
     * @return return 1 if config file at path was deleted
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
     * @return Inventory object
     */
    public static Inventory getInventory(){
        return inventory;
    }

    public static void setInventory(Inventory newInventory) {
        inventory = newInventory;
    }

    /**
     * @return path of the configuration file
     */
    public static String getConfigPath(){
        return configPath;
    }
}
