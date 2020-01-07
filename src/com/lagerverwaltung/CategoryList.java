package com.lagerverwaltung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * The CategoryList class is responsible for showing all available categories to the user. From here, the user can also
 * create new categories and remove existing categories if no inventory items exist in this category.
 */
public class CategoryList {

    /**
     * The button to add new categories
     */
    private JButton addCategoryButton = new JButton("Kategorie hinzufügen");

    /**
     * The table which shows all available categories
     */
    private JTable jtable;

    /**
     * The column names of the table
     */
    private String[] columnNames =  { "Kategorie", "Anzahl der Artikel", "" };

    /**
     * The table model which manages the data of the table
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

        /**
         * Returns boolean value of queried cell if it is editable.
         *
         * @param rowIndex the row whose value is to be queried
         * @param columnIndex the column whose value is to be queried
         * @return true if editable
         */
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
     * The dialog which holds all components of the graphical user interface
     */
    private JDialog dialog;

    /**
     * The constructor of the CategoryList class.
     *
     * <p>It creates the dialog, adds the components to the dialog and in the end connects the button to the action
     * listener and fills the table with the category data from the inventory.
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

    /**
     * The DeleteCategoryAction class extends the AbstractAction class and implements the functionality to delete
     * specific categories.
     */
    private class DeleteCategoryAction extends AbstractAction {
        /**
         * Invoked when an action occurs.
         *
         * <p>This method detects which button was pressed and then tries to delete the category. It is only possible to
         * delete a category if the amount of inventory items that exist with this category as a selected value is zero.
         *
         * @param e the event that occurred
         */
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

    /**
     * The AddCategoryListener class implements the ActionListener interface and adds the functionality to create
     * new categories.
     */
    private class AddCategoryListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * <p>This method creates a new instance of the AddCategory class which opens a JDialog. When the JDialog
         * was closed it checks if the dialog was closed by the accept button and then updates the table model and saves
         * the current inventory to the csv file.
         *
         * @param e the event that occurred
         */
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

    /**
     * Updates the table model with new categories which then updates the table.
     *
     * @param categoryMap the categories to be shown in the table
     */
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
