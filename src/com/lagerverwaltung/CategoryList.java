package com.lagerverwaltung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * CategoryListDialog is the window which shows all categories in a table. From here it is possible to create and
 * delete categories
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

        new ButtonCellEditor(jtable, new DeleteCategoryAction(), 2);

        updateTableModel(App.getInventory().getCategoryMap());

        JScrollPane scrollPane = new JScrollPane(jtable);
        inputPanel.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.NORTH);
        dialog.add(inputPanel, BorderLayout.CENTER);

        addCategoryButton.addActionListener(new AddCategoryListener());
        dialog.setVisible(true);
    }

    private class DeleteCategoryAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable)e.getSource();
            int modelRow = Integer.valueOf(e.getActionCommand());
            String categoryName = table.getModel().getValueAt(modelRow,0).toString();

            if (App.getInventory().getCategoryMap().get(categoryName).getCount() == 0) {
                final JDialog errorDialog = new JDialog();
                errorDialog.setAlwaysOnTop(true);
                if (JOptionPane.showConfirmDialog(errorDialog,"Wollen Sie die Kategorie \"" + categoryName +"\" wirklich löschen?", "Kategorie löschen", JOptionPane.YES_NO_OPTION) == 0) {
                    App.getInventory().removeCategory(new Category(categoryName));

                    updateTableModel(App.getInventory().getCategoryMap());

                    FileHandler fileHandler = new FileHandler();
                    fileHandler.storeInventoryInCSV(App.getInventory());
                }
            } else {
                final JDialog errorDialog = new JDialog();
                errorDialog.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(
                        errorDialog,
                        "Die Kategorie kann erst gelöscht werden, wenn sie keine Artikel mehr enthält!",
                        "Fehler beim Löschen einer Kategorie",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class AddCategoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddCategory addCategory = new AddCategory();
            if (addCategory.isAcceptButtonClicked()) {
                updateTableModel(App.getInventory().getCategoryMap());

                FileHandler fw = new FileHandler();
                fw.storeInventoryInCSV(App.getInventory());
            }
        }
    }

    private void updateTableModel(HashMap<String, Category> categoryMap) {
        while (categoryTableModel.getRowCount()>0) {
            categoryTableModel.removeRow(0);
        }

        for(Category cat : categoryMap.values()){
            String[] obj = { cat.getName(), String.valueOf(cat.getCount()), "Löschen" };
            categoryTableModel.addRow(obj);
        }
    }
}
