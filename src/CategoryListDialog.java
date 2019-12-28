import jdk.nashorn.internal.scripts.JO;

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
     * column titles
     */
    String[] columnNames =  {
            "Kategorie", "Anzahl der Artikel", ""
    };

    /**
     * categoryTableModel of the table which is the DefaultTableModel with an overwriten getColumnClass Method
     */
    DefaultTableModel categoryTableModel = new DefaultTableModel(null, columnNames) {
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

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 2:
                    return true;
                default:
                    return false;
            }
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
        jtable.setCellSelectionEnabled(false);

        Action deleteAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                String categoryName = table.getModel().getValueAt(modelRow,0).toString();
                
                if (App.getInventory().getCategoryMap().get(categoryName).getCount() == 0) {
                    final JDialog dialog = new JDialog();
                    dialog.setAlwaysOnTop(true);
                    if (JOptionPane.showConfirmDialog(dialog,"Wollen Sie die Kategorie \"" + categoryName +"\" wirklich löschen?", "Kategorie löschen", JOptionPane.YES_NO_OPTION) == 0) {
                        App.getInventory().removeCategory(new Category(categoryName));

                        while (categoryTableModel.getRowCount()>0) {
                            categoryTableModel.removeRow(0);
                        }

                        for(Category cat : App.getInventory().getCategoryMap().values()){
                            String[] obj = { cat.getName(), String.valueOf(cat.getCount()), "Löschen" };
                            categoryTableModel.addRow(obj);
                        }

                        FileHandler fileHandler = new FileHandler();
                        fileHandler.storeInventoryInCSV(App.getInventory());
                    }
                } else {
                    final JDialog dialog = new JDialog();
                    dialog.setAlwaysOnTop(true);
                    JOptionPane.showMessageDialog(dialog,"Kategorie konnte nicht gelöscht werden!","Fehler beim Löschen einer Kategorie",JOptionPane.ERROR_MESSAGE);
                }


            }
        };

        new ButtonColumn(jtable, deleteAction, 2);

        for(Category cat : App.getInventory().getCategoryMap().values()){
            String[] obj = { cat.getName(), String.valueOf(cat.getCount()), "Löschen" };
            categoryTableModel.addRow(obj);
        }

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
                    while (categoryTableModel.getRowCount()>0) {
                        categoryTableModel.removeRow(0);
                    }

                    for(Category cat : App.getInventory().getCategoryMap().values()){
                        String[] obj = { cat.getName(), String.valueOf(cat.getCount()), "Löschen" };
                        categoryTableModel.addRow(obj);
                    }

                    //write Categories
                    FileHandler fw = new FileHandler();
                    fw.storeInventoryInCSV(App.getInventory());

                }
            }
        });
    }
}
