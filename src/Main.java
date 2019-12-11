import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    static Object[][] databaseResults = {
            {"Stift (Schwarz)", "Stifte", 3, "000004", 2400, 0.99}
    };

    /*
    Lagerort:
    0 - 9 sind verfügbare Zeichen
    Nach Muster: ABCDDD
    A - Abteilung
    B - Sub-Abteilung
    C - Regalnummer
    DDD - Platznummer
    >> Ergibt max. 1.000.000 Einträge

    */

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

        table = new JTable(dTableModel);
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

                    List<InventoryItem> items = readBooksFromCSV(file);

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

    private static List<InventoryItem> readBooksFromCSV(File fileName) {
        List<InventoryItem> books = new ArrayList<>();
        Path pathToFile = fileName.toPath();

        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();

            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");

                InventoryItem book = createBook(attributes);

                // adding book into ArrayList
                books.add(book);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return books;
    }

    private static InventoryItem createBook(String[] metadata) {
        String description = metadata[0].replaceAll("^\"|\"$", "");
        String category = metadata[1].replaceAll("^\"|\"$", "");
        int stock = Integer.parseInt(metadata[2]);
        String location = metadata[3].replaceAll("^\"|\"$", "");
        Double weight = Double.parseDouble(metadata[4]);
        Double price = Double.parseDouble(metadata[5]);

        // create and return book of this metadata
        return new InventoryItem(description, category, stock, location, weight, price);
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