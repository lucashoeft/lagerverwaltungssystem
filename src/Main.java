import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends JFrame {

    FileHandler fileHandler = new FileHandler();

    JFileChooser filechooser = new JFileChooser();
    JButton openButton = new JButton("Datei öffnen");
    JButton searchButton = new JButton("Suchen");
    JButton createInventoryItemButton = new JButton("Artikel erstellen");
    JButton manageCategoryButton = new JButton("Kategorien verwalten");
    JTextField textField1 = new JTextField("", 15);
    JTable table;
    InventoryTableModel inventoryTableModel = new InventoryTableModel();

    public Main() {

        this.setMinimumSize(new Dimension(620, 420));
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("Lagerverwaltungssystem");

        JPanel topPanel = new JPanel();

        ListenForButton listenForButton = new ListenForButton();

        openButton.addActionListener(listenForButton);
        topPanel.add(openButton);

        topPanel.add(textField1);

        topPanel.add(searchButton);

        createInventoryItemButton.addActionListener(listenForButton);
        topPanel.add(createInventoryItemButton);

        topPanel.add(manageCategoryButton);

        this.add(topPanel, BorderLayout.NORTH);

        table = new JTable(inventoryTableModel);
        table.setRowHeight(table.getRowHeight() + 6);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);

        if (App.getInventory().getPath() != "" && Files.exists(Paths.get(App.getInventory().getPath()))) {
            App.getInventory().loadData();
            App.getInventory().initStorage();
            App.getInventory().initCategories();
            inventoryTableModel.setData(App.getInventory());
        }

        // Search Table

        searchButton.addActionListener(listenForButton);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private class ListenForButton implements ActionListener {

        public void actionPerformed(ActionEvent e) {

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
        }
    }


}