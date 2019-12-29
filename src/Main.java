import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * This is the main Window of the Storage-Management-System.
 * It contains a filterable table with all items on stock.
 *
 * @author ...
 * @version 1.0
 */
public class Main extends JFrame {

    /**
     * FileHandler for loading and saving of the database
     */
    FileHandler fileHandler = new FileHandler();

    /**
     * FileChooser to choose a .CSV file
     */
    JFileChooser filechooser = new JFileChooser();

    /**
     * Button to open a new .CSV file
     */
    JButton openButton = new JButton("Datei öffnen");

    /**
     * search Button
     */
    JButton searchButton = new JButton("Suchen");

    /**
     * create item button
      */
    JButton createInventoryItemButton = new JButton("Artikel erstellen");

    /**
     * manage Categories Button
     */
    JButton manageCategoryButton = new JButton("Kategorien verwalten");

    /**
     * search Field
     */
    JTextField textField1 = new JTextField("", 15);

    /**
     * table to show item data
     */
    JTable table;

    /**
     * data model which holds data for the table
     */
    InventoryTableModel inventoryTableModel = new InventoryTableModel();

    /**
     * Constructor for the main window of this Software
     */
    public Main() {

        // minimum window size : 620x420
        this.setMinimumSize(new Dimension(620, 420));

        this.setSize(800, 600);

        // window centered on screen
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("Lagerverwaltungssystem");

        // area above table as Panel
        JPanel topPanel = new JPanel();

        // Button Listener
        // Decides what happens after button press
        ListenForButton listenForButton = new ListenForButton();

        // Button for opening a .CSV file
        openButton.addActionListener(listenForButton);

        // adding of components to the window
        topPanel.add(openButton);
        topPanel.add(textField1);
        topPanel.add(searchButton);
        topPanel.add(createInventoryItemButton);

        manageCategoryButton.addActionListener(listenForButton);
        topPanel.add(manageCategoryButton);

        // Adding of Button Listener to buttons
        createInventoryItemButton.addActionListener(listenForButton);
        searchButton.addActionListener(listenForButton);

        this.add(topPanel, BorderLayout.NORTH);

        // table creation
        // data is stored in InvetoryTableModel
        table = new JTable(inventoryTableModel);
        table.setRowHeight(table.getRowHeight() + 6);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(4).setCellRenderer(new WeightTableCellRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new PriceTableCellRenderer());
        table.setCellSelectionEnabled(false);

        Action manageAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                String itemDescription = table.getModel().getValueAt(modelRow,0).toString();
                System.out.println(itemDescription);

                // TODO: Open filled out JDialog
            }
        };

        new ButtonColumn(table, manageAction, 6);

        // If .CSV file exists load it into table
        if (App.getInventory().getPath() != "" && Files.exists(Paths.get(App.getInventory().getPath()))) {
            App.getInventory().loadData();
            App.getInventory().initStorage();
            App.getInventory().initCategories();
            inventoryTableModel.setData(App.getInventory());
        }

        // Scrollable area which contains the table
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    /**
     * Listener class for Button clicks. When a button is clicked the ListenForButton class checks which button has
     * been clicked and then decides what method to run
     */
    private class ListenForButton implements ActionListener {

        // Wird aufgerufen wenn ein Button gedrückt wird

        /**
         *
         * @param e Contains information of the button press
         */
        public void actionPerformed(ActionEvent e) {

            // action for searchButton
            // Search table for string in textField1
            if (e.getSource() == searchButton) {
                String text = textField1.getText();
                TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
                table.setRowSorter(sorter);
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text + "|" + text.replace(",","").replace(".","")));
                }
            }

            // Action for openButton
            // open Filechooser, set new Path of .CSV file to selected file, update table
            if (e.getSource() == openButton) {
                filechooser.setCurrentDirectory(new java.io.File("."));
                filechooser.setDialogTitle("Datei öffnen");
                filechooser.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));

                if (filechooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    // update config file and data path
                    App.setConfigFile(App.getConfigPath(), file.getPath());
                    App.getInventory().setPath(file.getPath());

                    App.getInventory().loadData();
                    App.getInventory().initStorage();
                    App.getInventory().initCategories();
                    inventoryTableModel.setData(App.getInventory());

                    System.out.println(filechooser.getSelectedFile().toString());
                }
            }
            // action for createInventroyItemButton
            // open new InventoryItemDialog window
            if (e.getSource() == createInventoryItemButton) {
                AddInventoryItemDialog addInventoryItemDialog = new AddInventoryItemDialog();
                addInventoryItemDialog.setSize(340,260);
                addInventoryItemDialog.setLocationRelativeTo(null);
                addInventoryItemDialog.setModal(true);
                addInventoryItemDialog.setVisible(true);

                if (addInventoryItemDialog.acceptButtonClicked) {
                    inventoryTableModel.setData(App.getInventory());
                    fileHandler.storeInventoryInCSV(App.getInventory());
                }
            }

            if (e.getSource() == manageCategoryButton) {
                CategoryListDialog categoryDialog = new CategoryListDialog();
                categoryDialog.setSize(600,400);
                categoryDialog.setLocationRelativeTo(null);
                categoryDialog.setModal(true);
                categoryDialog.setVisible(true);
            }
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

}