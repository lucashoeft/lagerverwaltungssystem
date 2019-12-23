import javax.swing.table.AbstractTableModel;
import java.util.HashMap;

public class InventoryTableModel extends AbstractTableModel {

    // TODO Gewicht und Preis vom dg und cent in g und euro umrechenen
    private static Object[] columns = {"Produktbezeichnung", "Kategorie", "Lagerbestand", "Lagerort", "Gewicht in g", "Preis in €"};

    private static Object[][] data = { { "", "", "", "", "", "" } };

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

    public void setData(Inventory inventory) {
        Object[][] newContent = convertToObject(inventory);
        this.data = newContent;
        fireTableDataChanged();
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColumnCount() {
        return columns.length;
    }

    public String getColumnName(int column) {
        return columns[column].toString();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    private Object[][] convertToObject(Inventory inventory) {
        Object[][] newContent = new Object[inventory.getItemMap().size()][6];

        int j = 0;
        for(InventoryItem item : App.getInventory().getItemMap().values()){
            newContent[j][0] = item.getDescription();
            newContent[j][1] = item.getCategory();
            newContent[j][2] = item.getStock();
            newContent[j][3] = item.getLocation();
            newContent[j][4] = item.getWeight();
            newContent[j][5] = item.getPrice();
            j++;
        }
        return newContent;
    }
}
