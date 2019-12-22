import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryListDialog extends JDialog {

    JButton addCategoryButton = new JButton("Kategorie hinzufügen");
    JTable jtable;

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

    String[] columnNames =  {
            "Kategorie", "Anzahl der Artikel"
    };

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
