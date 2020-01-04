package com.lagerverwaltung;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * This is the main Window of the Storage-Management-System.
 * It contains a filterable table with all items on stock.
 */
public class Main {

    /**
     * FileHandler for loading and saving of the database
     */
    private FileHandler fileHandler = new FileHandler();

    /**
     * search Button
     */
    private JButton searchButton = new JButton("Suchen");

    /**
     * create item button
      */
    private JButton createInventoryItemButton = new JButton("Artikel erstellen");

    /**
     * manage Categories Button
     */
    private JButton manageCategoryButton = new JButton("Kategorien verwalten");

    /**
     * search Field
     */
    private JTextField searchTextField = new JTextField("", 15);

    /**
     * table to show item data
     */
    private JTable table;

    /**
     * data model which holds data for the table
     */
    private InventoryTableModel inventoryTableModel = new InventoryTableModel();

    private JFrame frame;

    /**
     * Constructor for the main window of this Software
     */
    public Main() {
        frame = new JFrame("Lagerverwaltungssystem");
        frame.setMinimumSize(new Dimension(620, 420));
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        manageCategoryButton.addActionListener(new ManageCategoryListener());
        createInventoryItemButton.addActionListener(new CreateInventoryListener());
        searchButton.addActionListener(new SearchListener());

        // table creation
        // data is stored in InventoryTableModel
        table = new JTable(inventoryTableModel);
        table.setRowHeight(table.getRowHeight() + 6);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(2).setCellRenderer(new StockTableCellRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new LocationTableCellRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new WeightTableCellRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new PriceTableCellRenderer());
        table.setCellSelectionEnabled(false);

        new ButtonCellEditor(table, new AddAction(), 6);
        new ButtonCellEditor(table, new SubAction(), 7);
        new ButtonCellEditor(table, new ManageAction(), 8);

        // If .CSV file exists load it into table
        if (App.getInventory().getPath() != "" && Files.exists(Paths.get(App.getInventory().getPath()))) {
            Inventory inventory = fileHandler.readInventoryFromCSV(Paths.get(App.getInventory().getPath()));
            App.setInventory(inventory);

            if (App.getInventory().getItemMap().size() > 0) {
                inventoryTableModel.setData(App.getInventory());
            }
        }

        // area above table as Panel
        JPanel topPanel = new JPanel();
        // adding of components to the window
        topPanel.add(searchTextField);
        topPanel.add(searchButton);
        topPanel.add(createInventoryItemButton);
        topPanel.add(manageCategoryButton);

        // Scrollable area which contains the table
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private class AddAction extends AbstractAction {

        private String exceptionMessage = "Eingegebener Wert ist fehlerhaft. Bitte überprüfen Sie Ihre Eingabe!";

        @Override
        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable)e.getSource();
            int modelRow = Integer.valueOf(e.getActionCommand());
            String itemDescription = table.getModel().getValueAt(modelRow,0).toString();

            final JDialog dialog = new JDialog();
            dialog.setAlwaysOnTop(true);
            String add = JOptionPane.showInputDialog(dialog,"Um welchen Betrag soll der aktuelle Lagerbestand ("+ App.getInventory().getItem(itemDescription).getStock() + ") erhöht werden?","Lagerbestand erhöhen",JOptionPane.OK_CANCEL_OPTION);

            if (add != null) {
                try {
                    Integer numValue = Integer.parseInt(add);
                    if (numValue > 0) {
                        if (App.getInventory().increaseStockBy(itemDescription,numValue)) {
                            inventoryTableModel.setData(App.getInventory());
                            fileHandler.storeInventoryInCSV(App.getInventory());
                        } else {
                            showErrorOptionPane("Das Regalgewicht darf 10.000.000 g nicht überschreiten.");
                        }
                    } else {
                        showErrorOptionPane("Der eingegebene Wert muss größer als 0 sein.");
                    }
                } catch (Exception err) {
                    showErrorOptionPane("Es werden nur ganzzahlige Werte akzeptiert.");
                }
            }
        }
    }

    private class SubAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable)e.getSource();
            int modelRow = Integer.valueOf(e.getActionCommand());
            String itemDescription = table.getModel().getValueAt(modelRow,0).toString();

            final JDialog dialog = new JDialog();
            dialog.setAlwaysOnTop(true);
            String sub = JOptionPane.showInputDialog(dialog,"Um welchen Betrag soll der aktuelle Lagerbestand ("+ App.getInventory().getItem(itemDescription).getStock() + ") verringert werden?","Lagerbestand verringern",JOptionPane.OK_CANCEL_OPTION);

            if (sub != null) {
                try {
                    Integer numValue = Integer.parseInt(sub);
                    if (numValue > 0) {
                        if (App.getInventory().decreaseStockBy(itemDescription,numValue)) {
                            inventoryTableModel.setData(App.getInventory());
                            fileHandler.storeInventoryInCSV(App.getInventory());
                        } else {
                            showErrorOptionPane("Der Lagerbestand darf nicht kleiner als 0 sein.");
                        }
                    } else {
                        showErrorOptionPane("Der eingegebene Wert muss größer als 0 sein.");
                    }
                } catch (Exception err) {
                    showErrorOptionPane("Es werden nur ganzzahlige Werte akzeptiert.");
                }
            }
        }
    }

    private class ManageAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable)e.getSource();
            int modelRow = Integer.valueOf(e.getActionCommand());
            String itemDescription = table.getModel().getValueAt(modelRow,0).toString();

            ViewInventoryItem viewInventoryItem = new ViewInventoryItem(App.getInventory().getItem(itemDescription));

            if (viewInventoryItem.isInventoryUpdated()) {
                inventoryTableModel.setData(App.getInventory());
            }
        }
    }

    private class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = searchTextField.getText();
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
            sorter.setSortable(6, false);
            sorter.setSortable(7, false);
            sorter.setSortable(8, false);
            table.setRowSorter(sorter);
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    if (table.getRowCount() > 0) {
                        int[] indices = {0, 1, 2, 3, 4, 5};
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text + "|" + text.replace(",","").replace(".",""),indices));
                    }
                }
        }
    }

    private class CreateInventoryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AddInventoryItem addInventoryItem = new AddInventoryItem();
            if (addInventoryItem.isAcceptButtonClicked()) {
                inventoryTableModel.setData(App.getInventory());
                fileHandler.storeInventoryInCSV(App.getInventory());
            }
        }
    }

    private class ManageCategoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new CategoryList();
        }
    }

    private class StockTableCellRenderer extends DefaultTableCellRenderer {

        public StockTableCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Number) {
                value = NumberFormat.getIntegerInstance(Locale.GERMANY).format(value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private class LocationTableCellRenderer extends DefaultTableCellRenderer {

        public LocationTableCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }
    }

    private class WeightTableCellRenderer extends DefaultTableCellRenderer {

        public WeightTableCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Number) {
                Double valueX = ((Number) value).doubleValue() / 10.0;
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
                nf.setMinimumFractionDigits(1);
                value = nf.format(valueX);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private class PriceTableCellRenderer extends DefaultTableCellRenderer {

        public PriceTableCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Number) {
                Double valueX = ((Number) value).doubleValue() / 100.0;
                value = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(valueX);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private void showErrorOptionPane(String message) {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(dialog,message,"Fehler beim Bearbeiten des Lagerbestandes",JOptionPane.ERROR_MESSAGE);
    }
}