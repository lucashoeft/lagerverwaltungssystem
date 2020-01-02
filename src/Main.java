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
 *
 * @author ...
 * @version 1.0
 */
public class Main {

    /**
     * FileHandler for loading and saving of the database
     */
    FileHandler fileHandler = new FileHandler();

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

    JFrame frame;

    /**
     * Constructor for the main window of this Software
     */
    public Main() {
        frame = new JFrame("Lagerverwaltungssystem");

        // minimum window size : 620x420
        frame.setMinimumSize(new Dimension(620, 420));

        frame.setSize(800, 600);

        // window centered on screen
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // area above table as Panel
        JPanel topPanel = new JPanel();

        // Button Listener
        // Decides what happens after button press
        ListenForButton listenForButton = new ListenForButton();

        // adding of components to the window
        topPanel.add(textField1);
        topPanel.add(searchButton);
        topPanel.add(createInventoryItemButton);

        manageCategoryButton.addActionListener(listenForButton);
        topPanel.add(manageCategoryButton);

        // Adding of Button Listener to buttons
        createInventoryItemButton.addActionListener(listenForButton);
        searchButton.addActionListener(listenForButton);

        frame.add(topPanel, BorderLayout.NORTH);

        // table creation
        // data is stored in InvetoryTableModel
        table = new JTable(inventoryTableModel);
        table.setRowHeight(table.getRowHeight() + 6);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(4).setCellRenderer(new WeightTableCellRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new PriceTableCellRenderer());
        table.setCellSelectionEnabled(false);

        Action addAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                String itemDescription = table.getModel().getValueAt(modelRow,0).toString();

                final JDialog dialog = new JDialog();
                dialog.setAlwaysOnTop(true);
                String add = JOptionPane.showInputDialog(dialog,"Welcher Betrag soll zum aktuellen Lagerbestand ("+ App.getInventory().getItem(itemDescription).stock + ") addiert werden?","Lagerbestand anpassen",JOptionPane.OK_CANCEL_OPTION);

                if (add != null) {
                    try {
                        Integer numValue = Integer.parseInt(add);
                        if (numValue >= 0) {
                            if (App.getInventory().increaseStockBy(itemDescription,numValue)) {
                                inventoryTableModel.setData(App.getInventory());
                                fileHandler.storeInventoryInCSV(App.getInventory());
                            } else {
                                showErrorOptionPane();
                            }
                        } else {
                            showErrorOptionPane();
                        }
                    } catch (Exception err) {
                        showErrorOptionPane();
                    }
                }
            }
        };

        Action subAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                String itemDescription = table.getModel().getValueAt(modelRow,0).toString();

                final JDialog dialog = new JDialog();
                dialog.setAlwaysOnTop(true);
                String sub = JOptionPane.showInputDialog(dialog,"Welcher Betrag soll vom aktuellen Lagerbestand ("+ App.getInventory().getItem(itemDescription).stock + ") subtrahiert werden?","Lagerbestand anpassen",JOptionPane.OK_CANCEL_OPTION);

                if (sub != null) {
                    try {
                        Integer numValue = Integer.parseInt(sub);
                        if (numValue >= 0) {
                            if (App.getInventory().decreaseStockBy(itemDescription,numValue)) {
                                inventoryTableModel.setData(App.getInventory());
                                fileHandler.storeInventoryInCSV(App.getInventory());
                            } else {
                                showErrorOptionPane();
                            }
                        } else {
                            showErrorOptionPane();
                        }
                    } catch (Exception err) {
                        showErrorOptionPane();
                    }
                }
            }
        };

        Action manageAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                String itemDescription = table.getModel().getValueAt(modelRow,0).toString();

                ViewInventoryItem viewInventoryItem = new ViewInventoryItem(App.getInventory().getItem(itemDescription));

                if (viewInventoryItem.inventoryUpdated) {
                    inventoryTableModel.setData(App.getInventory());
                }
            }
        };

        new ButtonCellEditor(table, addAction, 6);
        new ButtonCellEditor(table, subAction, 7);
        new ButtonCellEditor(table, manageAction, 8);


        // If .CSV file exists load it into table
        if (App.getInventory().getPath() != "" && Files.exists(Paths.get(App.getInventory().getPath()))) {
            Inventory inventory = fileHandler.readInventoryFromCSV(Paths.get(App.getInventory().getPath()));
            App.setInventory(inventory);

            if (App.getInventory().getItemMap().size() > 0) {
                inventoryTableModel.setData(App.getInventory());
            }
        }

        // Scrollable area which contains the table
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * Listener class for Button clicks. When a button is clicked the ListenForButton class checks which button has
     * been clicked and then decides what method to run
     */
    private class ListenForButton implements ActionListener {

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
                sorter.setSortable(6, false);
                sorter.setSortable(7, false);
                sorter.setSortable(8, false);
                table.setRowSorter(sorter);
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    int[] indices = {0, 1, 2, 3, 4, 5};
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text + "|" + text.replace(",","").replace(".",""),indices));
                }
            }

            // action for createInventroyItemButton
            // open new InventoryItemDialog window
            if (e.getSource() == createInventoryItemButton) {
                AddInventoryItem addInventoryItem = new AddInventoryItem();
                if (addInventoryItem.acceptButtonClicked) {
                    inventoryTableModel.setData(App.getInventory());
                    fileHandler.storeInventoryInCSV(App.getInventory());
                }
            }

            if (e.getSource() == manageCategoryButton) {
                new CategoryList();
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

    private void showErrorOptionPane() {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(dialog,"Eingegebener Werte ist fehlerhaft. Bitte überprüfen Sie Ihre Eingabe!","Fehler beim Bearbeiten des Lagerbestandes",JOptionPane.ERROR_MESSAGE);
    }

}