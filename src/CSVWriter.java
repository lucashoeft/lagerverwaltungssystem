import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Iterator;


public class CSVWriter {

    public void writeInventoryCSV(String path) {
        try {
            FileWriter fw = new FileWriter(path);
            fw.write(App.getInventory().toStringCSV());


            fw.close();
        }
        catch (IOException e) {System.err.println(e);}
    }
}
