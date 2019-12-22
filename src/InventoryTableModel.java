import javax.swing.table.AbstractTableModel;
import java.util.List;
/**
 *
 * The InventoryTableModel class contains all Methods and data needed to construct a meaningful table in Main.java
 * It holds all column titles and item information displayed in the table.
 *
 * @author ...
 * @version 1.0
 */
public class InventoryTableModel extends AbstractTableModel {
    /**
     * column titles as an Object list
     */
    private static Object[] columns = {"Produktbezeichnung", "Kategorie", "Lagerbestand", "Lagerort", "Gewicht in g", "Preis in €"};

    /**
     * item table with one list per item
     */
    private static Object[][] data = { { "", "", "", "", "", "" } };

    /**
     *
     * @param columnIndex tablecolumn to get the class of
     * @return class of columnIndex; columnIndex=1 would be Category column with class String
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class returnValue;
        if ((columnIndex >= 0) && (columnIndex < getColumnCount())) {
            returnValue = getValueAt(0, columnIndex).getClass();
        } else {
            returnValue = Object.class;
        }
        return returnValue;
    }

    /**
     * @param inventory inventory from which data is adopted
     */
    public void setData(Inventory inventory) {
        Object[][] newContent = convertToObject(inventory);
        this.data = newContent;
        fireTableDataChanged();
    }

    /**
     * @return amount of rows
     */
    public int getRowCount() {
        return data.length;
    }

    /**
     * @return amount of columns
     */
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * @param column a column in the table
     * @return the title of the column
     */
    public String getColumnName(int column) {
        return columns[column].toString();
    }

    /**
     * @param rowIndex a row in the table
     * @param columnIndex a column in the table
     * @return data at rowIndex in columnIndex
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    /**
     * @param inventory data to put onto the table
     * @return a Object list of the inventory
     */
    private Object[][] convertToObject(Inventory inventory) {
        Object[][] newContent = new Object[inventory.getItems().size()][6];

        for(int i=0; i<inventory.getItems().size(); i++) {
            newContent[i][0] = inventory.getItems().get(i).description;
            newContent[i][1] = inventory.getItems().get(i).category;
            newContent[i][2] = inventory.getItems().get(i).stock;
            newContent[i][3] = inventory.getItems().get(i).location;
            newContent[i][4] = inventory.getItems().get(i).weight;
            newContent[i][5] = inventory.getItems().get(i).price;
        }

        return newContent;
    }
}
