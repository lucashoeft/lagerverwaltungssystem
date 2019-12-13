import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public List<InventoryItem> readInventoryFromCSV(Path pathName) {
        List<InventoryItem> inventory = new ArrayList<>();

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
                String[] attributes = line.split(",");

                InventoryItem inventoryItem = createInventoryItem(attributes);

                // adding invenotryItem into ArrayList
                inventory.add(inventoryItem);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return inventory;
    }

    private InventoryItem createInventoryItem(String[] metadata) {
        InventoryItem inventoryItem = new InventoryItem();
        try {
            inventoryItem.description = metadata[0].replaceAll("^\"|\"$", "");
            inventoryItem.category = metadata[1].replaceAll("^\"|\"$", "");
            inventoryItem.stock = Integer.parseInt(metadata[2]);
            inventoryItem.location = metadata[3].replaceAll("^\"|\"$", "");
            inventoryItem.weight = Double.parseDouble(metadata[4]);
            inventoryItem.price = Double.parseDouble(metadata[5]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create and return book of this metadata
        return inventoryItem;
    }
}
