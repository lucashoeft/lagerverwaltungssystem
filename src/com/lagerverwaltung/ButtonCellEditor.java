package com.lagerverwaltung;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * The ButtonCellEditor class adds the functionality to have buttons in table cells. It extends the AbstractCellEditor
 * class and implements the interfaces of TableCellRenderer, TableCellEditor, ActionListener and MouseListener.
 */
public class ButtonCellEditor extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {

    /**
     * The table which shall contain the buttons.
     */
    private JTable table;

    /**
     * The action which invokes the button behavior.
     */
    private Action action;

    /**
     * The button which is used for the TableCellRenderer interface.
     */
    private JButton renderButton;

    /**
     * The button which is used for the TableCellEditor interface.
     */
    private JButton editButton;

    /**
     * The content of the cell respectively the button.
     */
    private Object editorValue;

    /**
     * The flag to check if button column was pressed.
     */
    private boolean isButtonColumnEditor;

    /**
     * The constructor of the ButtonCellEditor class.
     *
     * @param table the table which shall be manipulated.
     * @param action the action added to buttons
     * @param column the column the buttons shall be added
     */
    public ButtonCellEditor(JTable table, Action action, int column) {
        this.table = table;
        this.action = action;

        renderButton = new JButton();
        editButton = new JButton();
        editButton.setFocusPainted( false );
        editButton.addActionListener( this );

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer( this );
        columnModel.getColumn(column).setCellEditor( this );
        table.addMouseListener( this );
    }

    /**
     * Returns the component that should be added to the component hierarchy. Once installed in the hierarchy this
     * component will then be able to draw and receive user input.
     *
     * @param table the table that is asking the editor to edit
     * @param value the value of the cell to be edited
     * @param isSelected true if the cell is to be rendered with highlighting
     * @param row the row of the cell being edited
     * @param column the column of the cell being edited
     * @return the component for editing
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editButton.setText(value.toString());
        editButton.setIcon(null);

        this.editorValue = value;

        return editButton;
    }

    /**
     * Returns the value contained in the editor.
     *
     * @return the value contained in the editor
     */
    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }

    /**
     * Returns the component used for drawing the cell. This method is used to configure the renderer appropriately
     * before drawing.
     *
     * @param table the table that is asking the renderer to render
     * @param value the value of the cell to be rendered
     * @param isSelected true if the cell is to be rendered with highlighting
     * @param hasFocus true if the cell is to be rendered with focus
     * @param row the row of the cell being rendered
     * @param column the column of the cell being rendered
     * @return the component for rendering
     */
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            renderButton.setForeground(table.getSelectionForeground());
            renderButton.setBackground(table.getSelectionBackground());
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }

        renderButton.setText(value.toString());
        renderButton.setIcon(null);

        return renderButton;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event that occurred
     */
    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        fireEditingStopped();

        ActionEvent event = new ActionEvent(table, ActionEvent.ACTION_PERFORMED,"" + row);
        action.actionPerformed(event);
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event that occured
     */
    public void mousePressed(MouseEvent e) {
        if (table.isEditing() && table.getCellEditor() == this) {
            isButtonColumnEditor = true;
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event that occured
     */
    public void mouseReleased(MouseEvent e) {
        if (isButtonColumnEditor && table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        isButtonColumnEditor = false;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {}

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {}
}