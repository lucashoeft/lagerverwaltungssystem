import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.*;
import java.io.FileWriter;
import java.util.*;

public class App {

    static DataBase dataBase = new DataBase();
    static String configPath = System.getProperty("user.home") + "\\.Lagerverwaltung\\config.cfg";

    public static void main(String[] args) throws IOException {


        read_config_file(configPath);
        GUI.run();

        //Testausgabe des aktuellen Pfades der verwendeten Datenbank/.csv Datei
        System.out.println(dataBase.get_path());
    }

    public static void read_config_file(String path) {
        try {
            Path cfgFile = Paths.get(path);
            if (Files.exists(cfgFile)) {
                System.out.println("Config file found");
                if(clear_config_file(path) == 0) {
                    Scanner scan = new Scanner(cfgFile);
                    dataBase.set_path(scan.nextLine());
                    System.out.println("Config file loaded");
                }
                else {
                    read_config_file(path);
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

    public static void set_config_file(String config_path, String new_path) {
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

    public static int clear_config_file(String path){
        File cfgFile = new File(path);
        if (cfgFile.length() == 0) {
            cfgFile.delete();
            System.out.println("empty config file deleted");
            return 1;
        }
        return 0;
    }

    public static DataBase get_dataBase(){
        return dataBase;
    }

    public static String get_configPath(){
        return configPath;
    }
}
