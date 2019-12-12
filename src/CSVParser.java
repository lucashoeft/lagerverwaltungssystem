import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public static List<InventoryItem> readInventoryFromCSV(Path pathName) {
        List<InventoryItem> inventory = new ArrayList<>();
        Path pathToFile = pathName;

        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
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

    private static InventoryItem createInventoryItem(String[] metadata) {
        String description = metadata[0].replaceAll("^\"|\"$", "");
        String category = metadata[1].replaceAll("^\"|\"$", "");
        int stock = Integer.parseInt(metadata[2]);
        String location = metadata[3].replaceAll("^\"|\"$", "");
        Double weight = Double.parseDouble(metadata[4]);
        Double price = Double.parseDouble(metadata[5]);

        // create and return book of this metadata
        return new InventoryItem(description, category, stock, location, weight, price);
    }
}
