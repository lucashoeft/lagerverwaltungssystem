import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    static Object[][] databaseResults = {
            {"Stift (Schwarz)", "Stifte", 3, "000004", 2400, 0.99}
    };

    static Object[] columns = {"Produktbezeichnung", "Kategorie", "Lagerbestand", "Lagerort", "Gewicht in g", "Preis in €"};

    static DefaultTableModel dTableModel = new DefaultTableModel(databaseResults, columns) {
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
    int buttonClicked;

    /*public static void main(String[] args) {
        new Main();
    }*/
    /*public static void my_exit(){
        App.clear_config_file();
        JFrame.EXIT_ON_CLOSE();
    }*/

    public Main() {

        this.setMinimumSize(new Dimension(620, 420));
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.out.println("was" + App.dataBase.getPath());

        this.setTitle("Lagerverwaltungssystem");

        JPanel topPanel = new JPanel();

        ListenForButton listenForButton = new ListenForButton();

        openButton.addActionListener(listenForButton);
        topPanel.add(openButton);

        ListenForKeys listenForKeys = new ListenForKeys();
        textField1.addKeyListener(listenForKeys);
        topPanel.add(textField1);

        topPanel.add(searchButton);

        createInventoryItemButton.addActionListener(listenForButton);
        topPanel.add(createInventoryItemButton);

        topPanel.add(manageCatagoryButton);

        this.add(topPanel, BorderLayout.NORTH);

        //

        if (App.dataBase.path != null) {
            List<InventoryItem> items = csvParser.readInventoryFromCSV(Paths.get(App.dataBase.path));


            for (InventoryItem b : items) {
                System.out.println(b);
            }

            Object[][] newContent = new Object[items.size()][6];

            for(int i=0; i<items.size(); i++) {
                newContent[i][0] = items.get(i).description;
                newContent[i][1] = items.get(i).category;
                newContent[i][2] = items.get(i).stock;
                newContent[i][3] = items.get(i).location;
                newContent[i][4] = items.get(i).weight;
                newContent[i][5] = items.get(i).price;
            }
            table = new JTable(dTableModel);
            table.setModel(new DefaultTableModel(newContent, columns));

        } else {
            table = new JTable(dTableModel);
        }


        table.setRowHeight(table.getRowHeight() + 6);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);

        // Search Table

        searchButton.addActionListener(listenForButton);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        // this.add(panel);

        ListenForWindow listenforWindow = new ListenForWindow();
        this.addWindowListener(listenforWindow);

        this.setVisible(true);
    }

    private class ListenForButton implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == createInventoryItemButton) {
                buttonClicked++;
                System.out.println(buttonClicked);
            }

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

                    //System.out.println(file.getPath());
                    // update config file and data path
                    App.setConfigFile(App.getConfigPath(), file.getPath());
                    App.getDatabase().setPath(file.getPath());

                    List<InventoryItem> items = csvParser.readInventoryFromCSV(file.toPath());


                    for (InventoryItem b : items) {
                        System.out.println(b);
                    }

                    Object[][] newContent = new Object[items.size()][6];

                    for(int i=0; i<items.size(); i++) {
                        newContent[i][0] = items.get(i).description;
                        newContent[i][1] = items.get(i).category;
                        newContent[i][2] = items.get(i).stock;
                        newContent[i][3] = items.get(i).location;
                        newContent[i][4] = items.get(i).weight;
                        newContent[i][5] = items.get(i).price;
                    }

                    table.setModel(new DefaultTableModel(newContent, columns));

                    System.out.println(filechooser.getSelectedFile().toString());
                }
            }
        }
    }

    private class ListenForKeys implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private class ListenForWindow implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {

        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}