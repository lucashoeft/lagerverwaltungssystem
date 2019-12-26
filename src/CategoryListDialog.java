import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * CategoryListDialog is the window which shows all categories in a table. From here it is possible to create and
 * delete categories
 *
 * @author ...
 * @version 1.0
 */
public class CategoryListDialog extends JDialog {

    /**
     * Button to open the AddCategoryDialog window
     */
    JButton addCategoryButton = new JButton("Kategorie hinzufügen");

    /**
     * Table which holds all categories
     */
    JTable jtable;

    /**
     * Data inside the table currently only hardcoded
     */
    String[][] rowData = {
            { "Anspitzer", "4" },
            { "Federmäppchen", "5" },
            { "Füllfederhalter & Kugelschreiber", "3" },
            { "Marker & Filzstifte", "2" },
            { "Minen Patronen & Tintenlöscher", "1"},
            { "Radiergummies & Korrekturtools", "3" },
            { "Stifte", "4" },
            { "Technisches Zeichnen", "4" }
    };

    /**
     * column titles
     */
    String[] columnNames =  {
            "Kategorie", "Anzahl der Artikel"
    };

    /**
     * categoryTableModel of the table which is the DefaultTableModel with an overwriten getColumnClass Method
     */
    DefaultTableModel categoryTableModel = new DefaultTableModel(rowData, columnNames) {
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
    };

    /**
     * Constructor for a new CategoryListDialog
     */
    public CategoryListDialog() {
        this.setResizable(false);
        this.setTitle("Kategorien verwalten");

        // TODO get list of all existing categories and update table

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(5,10,10,10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));
        buttonPanel.add(addCategoryButton, BorderLayout.CENTER);

        jtable = new JTable(categoryTableModel);
        jtable.setRowHeight(jtable.getRowHeight() + 6);
        jtable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(jtable);
        inputPanel.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(inputPanel, BorderLayout.CENTER);

        addCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
                addCategoryDialog.setLocationRelativeTo(null);
                addCategoryDialog.setModal(true);
                addCategoryDialog.setSize(340,120);
                addCategoryDialog.setAlwaysOnTop(true);
                addCategoryDialog.setVisible(true);

                if (addCategoryDialog.acceptButtonClicked) {
                    // TODO: Update TableModel
                }
            }
        });
    }
}
