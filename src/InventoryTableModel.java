import javax.swing.table.AbstractTableModel;
import java.util.List;

public class InventoryTableModel extends AbstractTableModel {

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
