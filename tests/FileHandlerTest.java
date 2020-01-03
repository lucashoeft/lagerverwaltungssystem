import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FileHandlerTest {

    @Test
    void readInventoryFromNotExistingFile() {
        FileHandler myParser = new FileHandler();
        Assertions.assertEquals(0, myParser.readInventoryFromCSV(Paths.get("not_existing_file.csv")).getItemMap().size());
    }

    @Test
    void readInventoryFromEmptyFile() {
        assertDoesNotThrow(() -> {
            FileHandler myParser = new FileHandler();
            String fileName = TestHelpers.createTmpFileWithContent("my_test_database_", ".csv", "");
            Assertions.assertEquals(0, myParser.readInventoryFromCSV(Paths.get(fileName)).getItemMap().size());
            TestHelpers.deleteTmpFile(fileName);
        });
    }

    @Test
    void readInventoryFromFileWithContent() {
        /*
        assertDoesNotThrow(() -> {
            FileHandler myParser = new FileHandler();
            String content =
                    "\"Lamy Safari Füllhalter Kunststoff Umbra Feder M 1203065\",\"Füllfederhalter & Kugelschreiber\",8,\"000001\",116,1571\n" + // from example
                    "\n" + // empty line -> ignore
                    "ZIPIT, Wildlings,Federmäppchen,24,000023,375,799\n" + // description with embedded , -> invalid number of fields -> ignore
                    "ZIPIT Wildlings2,Federmäppchen,24,000023,,799\n" + // weight not specified -> ignore
                    "ZIPIT Wildlings3,Federmäppchen,24,000023,375,\n" + // price not specified -> ignore
                    "\"GirlZone Geschenke für Mädchen - Gelstifte Set für Mädchen\",Füllfederhalter & Kugelschreiber,24,000026,432,1299\n" +
                    // TODO "\"GirlZone Geschenke für Mädchen - \"Gelstifte Set für Mädchen\"\",Füllfederhalter & Kugelschreiber,24,000026,432,1299\n" + // description with embedded " -> ignore
                    "Dreikant-Buntstift - STABILO Trio dick kurz - 12er Pack - mit 12 verschiedenen Farben\",\"Stifte\",21,\"000012\",692,475\n";
            String fileName = TestHelpers.createTmpFileWithContent("my_test_database_", ".csv", content);
            Inventory myInventory = myParser.readInventoryFromCSV(Paths.get(fileName));
            Assertions.assertEquals(3, myInventory.getItemMap().size());

            // check content of items
            Assertions.assertEquals("Lamy Safari Füllhalter Kunststoff Umbra Feder M 1203065", myInventory.getItemMap().get("Lamy Safari Füllhalter Kunststoff Umbra Feder M 1203065").getDescription());
            Assertions.assertEquals(24, myInventory.getItemMap().get("GirlZone Geschenke für Mädchen - Gelstifte Set für Mädchen").getStock());
            Assertions.assertEquals("000012", myInventory.getItemMap().get("Dreikant-Buntstift - STABILO Trio dick kurz - 12er Pack - mit 12 verschiedenen Farben").getLocation());
            Assertions.assertEquals(0, myInventory.getItemMap().get("Dreikant-Buntstift - STABILO Trio dick kurz - 12er Pack - mit 12 verschiedenen Farben").getShelf());
            Assertions.assertEquals(692, myInventory.getItemMap().get("Dreikant-Buntstift - STABILO Trio dick kurz - 12er Pack - mit 12 verschiedenen Farben").getWeight());

            TestHelpers.deleteTmpFile(fileName);
        });

         */
    }
}
