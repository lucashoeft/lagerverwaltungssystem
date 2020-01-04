package com.lagerverwaltung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * CategoryListDialog is the window which shows all categories in a table. From here it is possible to create and
 * delete categories
 *
 * @author ...
 * @version 1.0
 */
public class CategoryList {

    /**
     * Button to open the AddCategoryDialog window
     */
    private JButton addCategoryButton = new JButton("Kategorie hinzufügen");

    /**
     * Table which holds all categories
     */
    private JTable jtable;

    /**
     * column titles
     */
    private String[] columnNames =  {
            "Kategorie", "Anzahl der Artikel", ""
    };

    /**
     * categoryTableModel of the table which is the DefaultTableModel with an overwriten getColumnClass Method
     */
    private DefaultTableModel categoryTableModel = new DefaultTableModel(null, columnNames) {
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

    private JDialog dialog;

    /**
     * Constructor for a new CategoryListDialog
     */
    public CategoryList() {
        dialog = new JDialog();
        dialog.setResizable(false);
        dialog.setTitle("Kategorien verwalten");
        dialog.setSize(600,400);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

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

        new ButtonCellEditor(jtable, deleteAction, 2);

        for(Category cat : App.getInventory().getCategoryMap().values()){
            String[] obj = { cat.getName(), String.valueOf(cat.getCount()), "Löschen" };
            categoryTableModel.addRow(obj);
        }

        JScrollPane scrollPane = new JScrollPane(jtable);
        inputPanel.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.NORTH);
        dialog.add(inputPanel, BorderLayout.CENTER);

        addCategoryButton.addActionListener((ActionEvent e) -> {
            AddCategory addCategory = new AddCategory();
            if (addCategory.isAcceptButtonClicked()) {
                while (categoryTableModel.getRowCount()>0) {
                    categoryTableModel.removeRow(0);
                }

                for(Category cat : App.getInventory().getCategoryMap().values()){
                    String[] obj = { cat.getName(), String.valueOf(cat.getCount()), "Löschen" };
                    categoryTableModel.addRow(obj);
                }

                FileHandler fw = new FileHandler();
                fw.storeInventoryInCSV(App.getInventory());
            }
        });
        dialog.setVisible(true);
    }
}
