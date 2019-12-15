import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main extends JFrame {

    private static Object[] columns = {"Produktbezeichnung", "Kategorie", "Lagerbestand", "Lagerort", "Gewicht in g", "Preis in €"};

    static DefaultTableModel dTableModel = new DefaultTableModel(null, columns) {
        public Class getColumnClass(int column) {
            Class returnValue;

            if ((column >= 0) && (column < getColumnCount())) {
                returnValue = getValueAt(0, column).getClass();
            } else {
                returnValue = Object.class;
            }

            return returnValue;
        }
    };

    CSVParser csvParser = new CSVParser();

    JFileChooser filechooser = new JFileChooser();
    JButton openButton = new JButton("Datei öffnen");
    JButton searchButton = new JButton("Suchen");
    JButton createInventoryItemButton = new JButton("Artikel erstellen");
    JButton manageCatagoryButton = new JButton("Kategorien verwalten");
    JTextField textField1 = new JTextField("", 15);
    JTable table;

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

        topPanel.add(manageCatagoryButton);

        this.add(topPanel, BorderLayout.NORTH);

        table = new JTable(dTableModel);
        dTableModel.setRowCount(0);
        table.setRowHeight(table.getRowHeight() + 6);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);

        if (App.getDatabase().getPath() != null && Files.exists(Paths.get(App.getDatabase().getPath()))) {
            List<InventoryItem> items = csvParser.readInventoryFromCSV(Paths.get(App.getDatabase().getPath()));

            Object[][] newContent = convertToObject(items);

            dTableModel.setDataVector(newContent,columns);
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
                    App.getDatabase().setPath(file.getPath());

                    List<InventoryItem> items = csvParser.readInventoryFromCSV(file.toPath());

                    Object[][] newContent = convertToObject(items);

                    dTableModel.setDataVector(newContent, columns);

                    System.out.println(filechooser.getSelectedFile().toString());
                }
            }
        }
    }

    private Object[][] convertToObject(List<InventoryItem> items) {
        Object[][] newContent = new Object[items.size()][6];

        for(int i=0; i<items.size(); i++) {
            newContent[i][0] = items.get(i).description;
            newContent[i][1] = items.get(i).category;
            newContent[i][2] = items.get(i).stock;
            newContent[i][3] = items.get(i).location;
            newContent[i][4] = items.get(i).weight;
            newContent[i][5] = items.get(i).price;
        }

        return newContent;
    }
}