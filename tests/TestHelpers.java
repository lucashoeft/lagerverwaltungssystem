import com.lagerverwaltung.Inventory;
import com.lagerverwaltung.InventoryItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestHelpers {

    static String createTmpFileWithContent(String prefix, String suffix, String content) {
        String fileName = "";
        try {
            File tmpFile = File.createTempFile(prefix, suffix);
            PrintWriter pw = new PrintWriter(new FileWriter(tmpFile));
            pw.print(content);
            pw.close();
            fileName = tmpFile.getCanonicalPath();
        }
        catch (IOException ignored) { }
        System.out.printf("create tmp file: %s%n", fileName);
        return fileName;
    }

    static void deleteTmpFile(String fileName) {
        System.out.printf("delete tmp file: %s%n", fileName);
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException ignored) { }
    }

    static Inventory createInventory() {
        Inventory myInventory = new Inventory();
        myInventory.addNewItem(new InventoryItem("item11", "cat1", 1100, "001001", 100, 199));
        myInventory.addNewItem(new InventoryItem("item12", "cat1", 1200, "001002", 100, 199));
        //myInventory.addItem(new InventoryItem("item13", "cat1", 1300, "001001", 10.0, 1.99)); // location already in use -> ignore in init()
        myInventory.addNewItem(new InventoryItem("item21", "cat1", 2, "002001", (10000 * 1000) / 2, 199));
        myInventory.addNewItem(new InventoryItem("item31", "cat1", 1, "003001", (10000 * 1000 * 10) + 1, 199)); // to heavy item -> ignore in init()
        return myInventory;
    }
}
