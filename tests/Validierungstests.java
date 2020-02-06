import com.lagerverwaltung.Category;
import com.lagerverwaltung.FileHandler;
import com.lagerverwaltung.Inventory;
import com.lagerverwaltung.InventoryItem;
import com.lagerverwaltung.InventoryTableModel;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class Validierungstests {

    private Inventory inventory = new Inventory();

    @Test
    public void persistentStorageTest() {
        String fileName = createTmpFileWithContent("my_test_database_", ".csv", "");

        inventory.addNewItem(new InventoryItem("Rote-Stifte", "Buntstifte", 24, "000000", 10, 199));
        inventory.setPath(fileName);
        FileHandler fileHandler = new FileHandler();
        fileHandler.storeInventoryInCSV(inventory);

        Inventory newInventory = fileHandler.readInventoryFromCSV(Paths.get(inventory.getPath()));
        assertEquals(inventory.getItemMap().size(), newInventory.getItemMap().size());
        assertEquals(inventory.getShelfMap().size(), newInventory.getShelfMap().size());
        assertEquals(inventory.getCategoryMap().size(), newInventory.getCategoryMap().size());
        assertEquals(inventory.getPath(), newInventory.getPath());
        assertEquals("Rote-Stifte",newInventory.getItem("Rote-Stifte").getDescription());
        assertEquals("Buntstifte",newInventory.getItem("Rote-Stifte").getCategory());
        assertEquals(24,newInventory.getItem("Rote-Stifte").getStock());
        assertEquals("000000",newInventory.getItem("Rote-Stifte").getLocation());
        assertEquals(10,newInventory.getItem("Rote-Stifte").getWeight().intValue());
        assertEquals(199,newInventory.getItem("Rote-Stifte").getPrice());

        deleteTmpFile(fileName);
    }

    @Test
    public void uniqueInventoryItemDescriptionTest() {
        inventory.addNewItem(new InventoryItem("Rote-Stifte", "Buntstifte", 24, "000000", 10, 199));
        assertFalse(inventory.addNewItem(new InventoryItem("Rote-Stifte", "Filzer", 12, "111111", 12, 299)));
    }

    @Test
    public void uniqueInventoryItemLocationTest() {
        inventory.addNewItem(new InventoryItem("Blaue-Stifte", "Kugelschreiber", 21, "222222", 57, 99));
        assertFalse(inventory.addNewItem(new InventoryItem("Grüne-Stifte", "Wachsmalstifte", 100, "222222", 100, 199)));
    }

    @Test
    public void uniqueCategoryNameTest() {
        inventory.addCategory(new Category("Stifte"));
        assertFalse(inventory.addCategory(new Category("Stifte")));
    }

    @Test
    public void searchTest() {
        inventory.addNewItem(new InventoryItem("Stift-blau-0.05mm","Schreibgeräte",360,"000012",100,60));
        inventory.addNewItem(new InventoryItem("Stift-schwarz-0.03mm","Schreibgeräte",130,"000013",100,60));
        inventory.addNewItem(new InventoryItem("Tippex-3x","Korrekturflüssigkeit",97,"951167",160,495));
        inventory.addNewItem(new InventoryItem("Tippex-10x","Korrekturflüssigkeit",48,"461168",550,1550));
        inventory.addNewItem(new InventoryItem("Locher-25-Blatt-rot","Büromaterial",37,"332471",240,625));
        inventory.addNewItem(new InventoryItem("Post-it-Block-weiß","Notizzettel",460,"123483",300,215));
        inventory.addNewItem(new InventoryItem("Hefter-100x-bunt","Hefter",179,"513513",45000,250));
        inventory.addNewItem(new InventoryItem("Textmarker-3x-neongrün","Schreibgeräte",255,"986722",20000,350));
        inventory.addNewItem(new InventoryItem("Kopierpapier-10x-500","Papier",513,"002001",25000,3595));
        inventory.addNewItem(new InventoryItem("Trennstreifen-5x-100-bunt","Register",83,"501993",24,2500));

        assertEquals(2, searchHelper(inventory,"Tippex").intValue());
        assertEquals(1,searchHelper(inventory,"Locher-25-Blatt-rot").intValue());
        assertEquals(1,searchHelper(inventory,"Kopierpapier-10x-500").intValue());
        assertEquals(0,searchHelper(inventory,"Tuppex-10x").intValue());
        assertEquals(9,searchHelper(inventory,"3").intValue());
    }

    // Helper method that implements the search from the Main class
    private Integer searchHelper(Inventory inventory, String text) {
        InventoryTableModel inventoryTableModel = new InventoryTableModel();
        inventoryTableModel.setData(inventory);
        JTable table = new JTable(inventoryTableModel);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            if (table.getRowCount() > 0) {
                int[] indices = {0, 1, 2, 3, 4, 5};
                String textWithoutDots = text.replace(",","").replace(".","");
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + escapeRegexCharacters(text) + "|" + escapeRegexCharacters(textWithoutDots),indices));
            }
        }

        return table.getRowCount();
    }

    // Implemented method from the Main class
    private String escapeRegexCharacters(String text) {
        final String[] regexCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&","%"};

        for (String regexCharacter : regexCharacters) {
            if (text.contains(regexCharacter)) {
                text = text.replace(regexCharacter, "\\" + regexCharacter);
            }
        }
        return text;
    }

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

}
