package com.lagerverwaltung;

import javax.swing.table.AbstractTableModel;
/**
 * The InventoryTableModel class contains all Methods and data needed to construct a meaningful table in Main.java
 * It holds all column titles and item information displayed in the table.
 */
public class InventoryTableModel extends AbstractTableModel {

    /**
     * Column title array
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
     * Item table with one list per item
     */
    private static Object[][] data = { };

    /**
     *
     * @param columnIndex
     * @return
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
     * Checks if a cell is editable
     *
     * @param rowIndex row index of cell, which is going to be checked
     * @param columnIndex column index of cell, which is going to be checked
     * @return true if cell is editable, else return false
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
     * Sets new data and calls fireTableDataChanged to update the TableModel
     *
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
     * Converts an Inventory with its data into an Object[][]
     *
     * @param inventory data to put onto the table
     * @return a Object list of the inventory
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
