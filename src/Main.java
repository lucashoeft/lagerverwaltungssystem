import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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
     * Search Button
      */
    JButton searchButton = new JButton("Suchen");

    /**
     * create item button
      */
    JButton createInventoryItemButton = new JButton("Artikel erstellen");

    /**
     * Manage Categories Button
     */
    JButton manageCategoryButton = new JButton("Kategorien verwalten");

    /**
     * Search Field
     */
    JTextField textField1 = new JTextField("", 15);

    /**
     * table to show item data
      */
    JTable table;

    /**
     * Data model which holds data for the table
      */
    InventoryTableModel inventoryTableModel = new InventoryTableModel();

    /**
     * Constructor for the main window of this Software
     */
    public Main() {

        // minimale Fenstergröße 620x420
        this.setMinimumSize(new Dimension(620, 420));

        this.setSize(800, 600);

        // Fenster mittig auf dem Bildschirm
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("Lagerverwaltungssystem");

        // Bereich über der Tabelle als Panel
        JPanel topPanel = new JPanel();

        // Button Listener
        // Entscheidet was nach einem Knopfdruck passiert
        ListenForButton listenForButton = new ListenForButton();

        // Button zum öffnen einer CSV Datei
        openButton.addActionListener(listenForButton);

        // Hinzufügen der Komponenten zum Fenster
        topPanel.add(openButton);
        topPanel.add(textField1);
        topPanel.add(searchButton);
        topPanel.add(createInventoryItemButton);

        manageCategoryButton.addActionListener(listenForButton);
        topPanel.add(manageCategoryButton);

        // Hinzufügen des ButtonListeners zu den Buttons
        createInventoryItemButton.addActionListener(listenForButton);
        searchButton.addActionListener(listenForButton);

        this.add(topPanel, BorderLayout.NORTH);

        // Erstellen der Tabelle
        // Daten sind in inventoryTableModel gespeichert
        table = new JTable(inventoryTableModel);
        table.setRowHeight(table.getRowHeight() + 6);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);

        // Wenn CSV Datei existiert, dann lade die CSV Datei in die Tabelle
        if (App.getInventory().getPath() != "" && Files.exists(Paths.get(App.getInventory().getPath()))) {
            App.getInventory().loadData();
            App.getInventory().initStorage();
            App.getInventory().initCategories();
            inventoryTableModel.setData(App.getInventory());
        }

        // Bereich auf dem Fenster mit der Tabelle
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private class ListenForButton implements ActionListener {

        // Wird aufgerufen wenn ein Button gedrückt wird

        /**
         *
         * @param e Contains information of the button press
         */
        public void actionPerformed(ActionEvent e) {

            // Aktion für Suchbutton
            // Durchsuche Tabelle nach Schlüsselwort in textField1
            if (e.getSource() == searchButton) {
                String text = textField1.getText();
                TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
                table.setRowSorter(sorter);
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            // Aktion für Öffnenbutton
            // Öffne Filechooser, setze neuen Pfad der CSV Datei auf die ausgewählte Datei, aktualisiere die Tabelle
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
            // Aktion für Erstellen Button
            // Öffne neues InventoryItemDialog Fenster
            if (e.getSource() == createInventoryItemButton) {
                InventoryItemDialog inventoryItemDialog = new InventoryItemDialog();
                inventoryItemDialog.setSize(340,260);
                inventoryItemDialog.setLocationRelativeTo(null);
                inventoryItemDialog.setModal(true);
                inventoryItemDialog.setVisible(true);

                if (inventoryItemDialog.acceptButtonClicked) {
                    inventoryTableModel.setData(App.getInventory());
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


}