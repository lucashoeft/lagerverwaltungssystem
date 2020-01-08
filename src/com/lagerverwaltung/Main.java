package com.lagerverwaltung;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * The Main class is the center of the program and also the main window the user interacts with. It holds all buttons
 * and the main table which shows the data of the inventory. It controls all other classes, events and updates the
 * inventory and the table model based on the changes of the inventory.
 */
public class Main {

    /**
     * A file handler for getting the data and saving the changes of the inventory item.
     */
    private FileHandler fileHandler = new FileHandler();

    /**
     * The button triggers the search.
     */
    private JButton searchButton = new JButton("Suchen");

    /**
     * The button triggers the event to create new articles.
     */
    private JButton createInventoryItemButton = new JButton("Artikel erstellen");

    /**
     * The button triggers the event to manage the categories of the inventory.
     */
    private JButton manageCategoryButton = new JButton("Kategorien verwalten");

    /**
     * The text field is the input field for the search.
     */
    private JTextField searchTextField = new JTextField("", 15);

    /**
     * The table shows all attributes of the inventory items.
     */
    private JTable table;

    /**
     * The table model holds the inventory data that is displayed in the table.
     */
    private InventoryTableModel inventoryTableModel = new InventoryTableModel();

    /**
     * The frame which holds all buttons and the table.
     */
    private JFrame frame;

    /**
     * A logger to log messages.
     */
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * The constructor of the Main class.
     *
     * <p>It creates the frame and adds all components to it and connects the buttons and the table to custom action
     * listeners. It then tries to get the data out of the csv file and assigns it to the inventory and updates the
     * table.
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


        try {
            if (!App.getInventory().getPath().equals("")) {
                String backupPath = Paths.get(App.getInventory().getPath()).getParent().toString() + "/backup.csv";
                Path inventoryPath = Paths.get(App.getInventory().getPath());
                // backup recovery
                if (Files.exists(Paths.get(backupPath))) {
                    logger.log(Level.INFO, "Backup found");
                    if (Files.exists(inventoryPath)) {
                        Files.delete(inventoryPath);
                    }
                    Files.move(Paths.get(backupPath), inventoryPath);
                    logger.log(Level.INFO, "recovered from backup");
                }
                // load existing .csv data
                if (Files.exists(inventoryPath)) {
                    Inventory inventory = fileHandler.readInventoryFromCSV(inventoryPath);
                    App.setInventory(inventory);
                    logger.log(Level.INFO, "Inventory found");

                    if (App.getInventory().getItemMap().size() > 0) {
                        inventoryTableModel.setData(App.getInventory());
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
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

    /**
     * The AddAction class extends the AbstractAction class and implements the functionality to increase the stock value
     * by a value that was entered by the user.
     */
    private class AddAction extends AbstractAction {

        /**
         * Invoked when an action occurs.
         *
         * <p>This method detects which button was pressed and then opens an option pane which let the user enter a
         * number. It then detects if increasing the stock value is possible and saves the changes. If not, it shows
         * an appropriate error message to the user.
         *
         * @param e the event that occurred
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable)e.getSource();
            int modelRow = Integer.valueOf(e.getActionCommand());
            String itemDescription = table.getModel().getValueAt(modelRow,0).toString();

            final JDialog inputDialog = new JDialog();
            inputDialog.setAlwaysOnTop(true);
            String add = JOptionPane.showInputDialog(inputDialog,"Um welchen Betrag soll der aktuelle Lagerbestand ("+ App.getInventory().getItem(itemDescription).getStock() + ") erhöht werden?","Lagerbestand erhöhen",JOptionPane.OK_CANCEL_OPTION);

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

    /**
     * The SubAction class extends the AbstractAction class and implements the functionality to decrease the stock value
     * by a value that was entered by the user.
     */
    private class SubAction extends AbstractAction {

        /**
         * Invoked when an action occurs.
         *
         * <p>This method detects which button was pressed and then opens an option pane which let the user enter a
         * number. It then detects if decreasing the stock value is possible and saves the changes. If not, it shows
         * an appropriate error message to the user.
         *
         * @param e the event that occurred
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable)e.getSource();
            int modelRow = Integer.valueOf(e.getActionCommand());
            String itemDescription = table.getModel().getValueAt(modelRow,0).toString();

            final JDialog inputDialog = new JDialog();
            inputDialog.setAlwaysOnTop(true);
            String sub = JOptionPane.showInputDialog(inputDialog,"Um welchen Betrag soll der aktuelle Lagerbestand ("+ App.getInventory().getItem(itemDescription).getStock() + ") verringert werden?","Lagerbestand verringern",JOptionPane.OK_CANCEL_OPTION);

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

    /**
     * The ManageAction class extends the AbstractAction class and implements the functionality to open the JDialog with
     * the belonging inventory item of the table row.
     */
    private class ManageAction extends AbstractAction {

        /**
         * Invoked when an action occurs.
         *
         * <p>This method detects which button was pressed and then opens the JDialog with the inventory item as a
         * parameter. After the JDialog was closed, this method detects if the inventory updated and then updates the
         * content of the table.
         *
         * @param e the event that occurred
         */
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

    /**
     * The SearchListener class implements the ActionListener interface and adds the functionality to search the table
     * by text.
     */
    private class SearchListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * <p>This method takes the input from the search text field, filters the rows and updates the graphical user
         * interface. If there is not input, it shows all rows of the table.
         *
         * @param e the event that occurred
         */
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
                        String textWithoutDots = text.replace(",","").replace(".","");
                        System.out.println(escapeRegexCharacters(text));
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + escapeRegexCharacters(text) + "|" + escapeRegexCharacters(textWithoutDots),indices));
                    }
                }
        }
    }

    /**
     * The CreateInventoryListener class implements the ActionListener interface and adds the functionality to create
     * new inventory items.
     */
    private class CreateInventoryListener implements ActionListener {

        /**
         * Invoked when an action occurs.
         *
         * <p>This method creates a new instance of the AddInventoryItem class which opens a JDialog. When the JDialog
         * was closed it checks if the dialog was closed by the accept button and then updates the table model and saves
         * the current inventory to the csv file.
         *
         * @param e the event that occurred
         */
        public void actionPerformed(ActionEvent e) {
            AddInventoryItem addInventoryItem = new AddInventoryItem();
            if (addInventoryItem.isAcceptButtonClicked()) {
                inventoryTableModel.setData(App.getInventory());
                fileHandler.storeInventoryInCSV(App.getInventory());
            }
        }
    }

    /**
     * The ManageCategoryListener class implements the ActionListener interface and adds the functionality to show
     * the category list.
     */
    private class ManageCategoryListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * <p>This method creates a new instance of the CategoryList class which opens a JDialog with the current list
         * of all categories.
         *
         * @param e the event that occurred
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            new CategoryList();
        }
    }

    /**
     * The StockTableCellRenderer class extends DefaultTableCellRenderer by rendering the stock value with the semantic
     * which is known in Germany.
     */
    private class StockTableCellRenderer extends DefaultTableCellRenderer {

        /**
         * Aligns the content of the cell to the right side.
         */
        public StockTableCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }

        /**
         * Returns the default table cell renderer.
         *
         * <p>During a printing operation, this method will be called to let it customize the value that is shown to the
         * user. This method specifically renders the provided stock value to the same stock value, but with the
         * semantic which is known in Germany.
         *
         * @param table  the table
         * @param value  the value to assign to the cell
         * @param isSelected true if cell is selected
         * @param hasFocus true if cell has focus
         * @param row  the row of the cell to render
         * @param column the column of the cell to render
         * @return the default table cell renderer
         * @return the constructor of default table cell renderer with changed value
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Number) {
                value = NumberFormat.getIntegerInstance(Locale.GERMANY).format(value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    /**
     * The LocationTableCellRenderer class extends DefaultTableCellRenderer and aligns the location number to the right
     * side.
     */
    private class LocationTableCellRenderer extends DefaultTableCellRenderer {

        /**
         * Aligns the content of the cell to the right side.
         */
        public LocationTableCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }
    }

    /**
     * The WeightTableCellRenderer class extends DefaultTableCellRenderer by rendering the decigram value to the gram
     * value with the semantic which is known in Germany.
     */
    private class WeightTableCellRenderer extends DefaultTableCellRenderer {

        /**
         * Aligns the content of the cell to the right side.
         */
        public WeightTableCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }

        /**
         * Returns the default table cell renderer.
         *
         * <p>During a printing operation, this method will be called to let it customize the value that is shown to the
         * user. This method specifically renders the provided decigram value to the gram value with the semantic which
         * is known in Germany.
         *
         * @param table  the table
         * @param value  the value to assign to the cell
         * @param isSelected true if cell is selected
         * @param hasFocus true if cell has focus
         * @param row  the row of the cell to render
         * @param column the column of the cell to render
         * @return the default table cell renderer
         * @return the constructor of default table cell renderer with changed value
         */
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

    /**
     * The PriceTableCellRenderer class extends DefaultTableCellRenderer by rendering the cent value to an euro value
     * with the semantic which is known in Germany.
     */
    private class PriceTableCellRenderer extends DefaultTableCellRenderer {

        /**
         * Aligns the content of the cell to the right side.
         */
        public PriceTableCellRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }

        /**
         * Returns the default table cell renderer.
         *
         * <p>During a printing operation, this method will be called to let it customize the value that is shown to the
         * user. This method specifically renders the provided cent value to an euro value with the semantic which is
         * known in Germany.
         *
         * @param table  the table
         * @param value  the value to assign to the cell
         * @param isSelected true if cell is selected
         * @param hasFocus true if cell has focus
         * @param row  the row of the cell to render
         * @param column the column of the cell to render
         * @return the default table cell renderer
         * @return the constructor of default table cell renderer with changed value
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Number) {
                Double valueX = ((Number) value).doubleValue() / 100.0;
                value = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(valueX);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    /**
     * Shows the custom error message to the user above every other JDialog or JFrame.
     *
     * @param message the error message that shall be shown
     */
    private void showErrorOptionPane(String message) {
        final JDialog messageDialog = new JDialog();
        messageDialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(messageDialog,message,"Fehler beim Bearbeiten des Lagerbestandes",JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Returns a text which does not contain any characters that could be interpreted as regex characters.
     *
     * @param text the text which contains regex meta characters
     * @return the text with escaped all regex characters
     */
    private String escapeRegexCharacters(String text) {
        final String[] regexCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&","%"};

        for (String regexCharacter : regexCharacters) {
            if (text.contains(regexCharacter)) {
                text = text.replace(regexCharacter, "\\" + regexCharacter);
            }
        }
        return text;
    }
}