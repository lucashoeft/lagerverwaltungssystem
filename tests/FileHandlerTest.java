import com.lagerverwaltung.FileHandler;
import com.lagerverwaltung.Inventory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

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
        assertDoesNotThrow(() -> {
            FileHandler myParser = new FileHandler();
            String content =
                    "\n" + // emty line in category section -> ignore
                    "-1,Kat1,-1,-1,-1,-1\n" + // category entry in category section
                    // TODO "-1,KatFehler1,-1,-1,-1\n" + // invalid category -> ignore
                    ",KatFehler2,-1,-1,-1,-1\n" + // invalid category -> ignore
                    "Prod-1(ö!?.),Kat1,8,200000,116,1571\n" + // valid article entry
                    "Prod-2,Kat2,0,000001,100000000,1\n" + // valid article entry (0 article with 10t max weight)
                    "Prod-3,Kat1,100000000,000002,1,9999900\n" + // valid article entry (100.000.000 articles with 1g in storage => 10t in shelf)
                    "\n" + // empty line -> ignore
                    // TODO "Prod-2,Kat2,0,001000,375,799\n" + // duplicate description -> ignore
                    "fehler-art-1,test,Kat1,24,001001,375,799\n" + // description with embedded , -> invalid number of fields -> ignore
                    "fehler-art -2,Kat1,24,001001,375,799\n" + // description with embedded space -> ignore
                    "fehler-art_3,Kat1,24,001002,375,799\n" + // description with not allowed character (_) -> ignore
                    // TODO "fehler-art-4,KatUnbekannt,24,001003,375,799\n" + // unknown category used -> ignore
                    "fehler-art-5,Kat1,24,0001004,375,799\n" + // invalid storage-> ignore
                    "-1,Kat1,-1,-1,-1,-1\n" + // double category -> ignore
                    "fehler-art-6,Kat1,24,1005,375,799\n" + // invalid storage -> ignore
                    "fehler-art-7,Kat1,24,00A006,375,799\n" + // invalid storage -> ignore
                    "fehler-art-8,Kat1,24,,375,799\n" + // invalid storage -> ignore
                    "fehler-art-9,Kat1,-1,001007,375,799\n" + // invalid count -> ignore
                    "-1,Kat3,-1,-1,-1,-1\n" + // category entry in article section is allowed
                    "fehler-art-10,Kat1,a,001008,375,799\n" + // invalid count -> ignore
                    "fehler-art-11,Kat1,100000001,001009,375,799\n" + // invalid count -> ignore
                    "fehler-art-12,Kat1,-1,001010,375,799\n" + // invalid count -> ignore
                    "fehler-art-13,Kat1,0,001011,,799\n" + // no weight -> ignore
                    "fehler-art-14,Kat1,0,001012,0,799\n" + // zero weight -> ignore
                    "fehler-art-15,Kat1,0,001013,100000001,799\n" + // to heavy -> ignore
                    "fehler-art-16,Kat1,0,001014,1,\n" + // no price -> ignore
                    "fehler-art-17,Kat1,0,001015,1,0\n" + // zero price -> ignore
                    "fehler-art-18,Kat1,0,001016,1,9999901\n" + // price to high -> ignore
                    // TODO "fehler-art-19,Kat1,1,000017,1,1\n" + // shelf 000 overloaded -> ignore
                    "last,last,0,999999,1,1";
            String fileName = TestHelpers.createTmpFileWithContent("my_test_database_", ".csv", content);
            Inventory myInventory = myParser.readInventoryFromCSV(Paths.get(fileName));

            Assertions.assertEquals(4, myInventory.getItemMap().size());

            // check content of items
            Assertions.assertEquals("Prod-2", myInventory.getItemMap().get("Prod-2").getDescription());
            Assertions.assertEquals(8, myInventory.getItemMap().get("Prod-1(ö!?.)").getStock());
            Assertions.assertEquals("999999", myInventory.getItemMap().get("last").getLocation());
            Assertions.assertEquals(999, myInventory.getItemMap().get("last").getShelf());
            Assertions.assertEquals(116, myInventory.getItemMap().get("Prod-1(ö!?.)").getWeight());
            Assertions.assertEquals(9999900, myInventory.getItemMap().get("Prod-3").getPrice());

            // check shelfes
            Assertions.assertEquals(3, myInventory.getShelfMap().size());
            // TODO Assertions.assertEquals(100000000, myInventory.getShelfMap().get(0).getWeight());
            Assertions.assertEquals(928, myInventory.getShelfMap().get(200).getWeight());
            Assertions.assertEquals(0, myInventory.getShelfMap().get(999).getWeight());

            // check categories
            Assertions.assertEquals(4, myInventory.getCategoryMap().size());
            Assertions.assertEquals(2, myInventory.getCategoryMap().get("Kat1").getCount());
            Assertions.assertEquals(1, myInventory.getCategoryMap().get("Kat2").getCount());
            Assertions.assertEquals(0, myInventory.getCategoryMap().get("Kat3").getCount());
            Assertions.assertEquals(1, myInventory.getCategoryMap().get("last").getCount());

            TestHelpers.deleteTmpFile(fileName);
        });
    }
}
