package com.lagerverwaltung;

import javax.swing.table.AbstractTableModel;

/**
 * The InventoryTableModel extends the AbstractTableModel class by implementing its core methods and adding additional
 * methods to better handle the inventory. It holds the names of the columns as the table model is used only for the
 * purpose of showing the inventory.
 */
public class InventoryTableModel extends AbstractTableModel {

    /**
     * The array of the names of the columns that are shown to the user.
     */
    private static Object[] columns = {
            "Produktbezeichnung",
            "Kategorie",
            "Lagerbestand",
            "Lagerort",
            "Gewicht in g",
            "Preis in €",
            "Lagerbestand erhöhen",
            "Lagerbestand verringern",
            ""
    };

    /**
     * The two dimensional array that holds the data of the table.
     */
    private static Object[][] data = { };

    /**
     * Returns the most specific superclass for all the cell values in the column.
     *
     * @param columnIndex the index of the column
     * @return the class of the object values in the model
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
     * Returns the boolean value if cell is editable based on the row index and the column index.
     *
     * <p>This method allows column six, seven and eight to be editable. These columns are the columns containing the
     * buttons.
     *
     * @param rowIndex the row whose value to be queried
     * @param columnIndex the column whose value to be queried
     * @return true if the cell is editable
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 6:
            case 7:
            case 8:
                return true;
            default:
                return false;
        }
    }

    /**
     * Sets the inventory to the two dimensional data array and calls the method which redraws the content of the table.
     *
     * @param inventory the inventory that shall be shown
     */
    public void setData(Inventory inventory) {
        Object[][] newContent = convertToObject(inventory);
        this.data = newContent;
        fireTableDataChanged();
    }

    /**
     * @return the number of rows
     */
    public int getRowCount() {
        return data.length;
    }

    /**
     * @return the number of columns
     */
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * Returns the column name as a string value.
     *
     * @param column the index of the column
     * @return the name of the column
     */
    public String getColumnName(int column) {
        return columns[column].toString();
    }

    /**
     * Returns the value for the cell at columnIndex and rowIndex.
     *
     * @param rowIndex the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    /**
     * Converts the items of an inventory into a two dimensional Object array.
     *
     * @param inventory the inventory
     * @return the two dimensional array filled with inventory items
     */
    private Object[][] convertToObject(Inventory inventory) {
        Object[][] newContent = new Object[inventory.getItemMap().size()][9];

        int j = 0;
        for(InventoryItem item : inventory.getItemMap().values()){
            newContent[j][0] = item.getDescription();
            newContent[j][1] = item.getCategory();
            newContent[j][2] = item.getStock();
            newContent[j][3] = item.getLocation();
            newContent[j][4] = item.getWeight();
            newContent[j][5] = item.getPrice();
            newContent[j][6] = "+";
            newContent[j][7] = "-";
            newContent[j][8] = "Verwalten";
            j++;
        }
        return newContent;
    }
}
