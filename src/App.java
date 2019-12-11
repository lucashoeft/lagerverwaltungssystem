import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.*;
import java.io.FileWriter;
import java.util.*;

public class App {

    static Database dataBase = new Database();
    static String configPath = System.getProperty("user.home") + "/.Lagerverwaltung/config.cfg";

    public static void main(String[] args) throws IOException {


        readConfigFile(configPath);
        GUI.run();

        //Testausgabe des aktuellen Pfades der verwendeten Datenbank/.csv Datei
        System.out.println(dataBase.getPath());
    }

    public static void readConfigFile(String path) {
        try {
            Path cfgFile = Paths.get(path);
            if (Files.exists(cfgFile)) {
                System.out.println("Config file found");
                if(clearConfigFile(path) == 0) {
                    Scanner scan = new Scanner(cfgFile);
                    dataBase.setPath(scan.nextLine());
                    System.out.println("Config file loaded");
                }
                else {
                    readConfigFile(path);
                }
            }
            else {
                System.out.println("Config file not found");
                Files.createDirectories(cfgFile.getParent());
                Files.createFile(cfgFile);
                System.out.println("new Config file created");
            }
        }
        catch (IOException e) {System.err.println(e);}
    }

    public static void setConfigFile(String config_path, String new_path) {
        try {
            Path cfgFile = Paths.get(config_path);
            if (Files.exists(cfgFile)) {
                System.out.println("Config file found");
                FileWriter fw = new FileWriter(config_path);
                fw.write(new_path);
                fw.close();
                System.out.println("Config file updated");
            }
            else {
                System.out.println("Config file not found");
                // File not found Exception
            }
        }
        catch (IOException e) {System.err.println(e);}
    }

    public static int clearConfigFile(String path){
        File cfgFile = new File(path);
        if (cfgFile.length() == 0) {
            cfgFile.delete();
            System.out.println("empty config file deleted");
            return 1;
        }
        return 0;
    }

    public static Database getDatabase(){
        return dataBase;
    }

    public static String getConfigPath(){
        return configPath;
    }
}
