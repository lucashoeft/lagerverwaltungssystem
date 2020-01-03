import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * This Dialog class is opened when a user wants to create a new InventoryItem.
 *
 * @author ...
 * @version 1.0
 */
import java.util.Iterator;

/**
 *
 * InventoryItemDialog is the window which is used to create new items
 *
 * @author ...
 * @version 1.0
 */
public class AddInventoryItem {

    /**
     * is true when acceptButton has been clicked
     */
    public Boolean acceptButtonClicked = false;

    private InventoryItemInputPanel inputPanel = new InventoryItemInputPanel(App.getInventory().getCategoryMap());

    /**
     * Button to close dialog without any changes to the item
     */
    private JButton dismissButton = new JButton("Abbrechen");

    /**
     * Button to close dialog but save all changes if possible
     */
    private JButton acceptButton = new JButton("Erstellen");

    JDialog dialog;

    /**
     * Constructor for the InventoryItemDialog
     */
    public AddInventoryItem() {
        dialog = new JDialog();
        dialog.setResizable(false);
        dialog.setTitle("Artikel erstellen");
        dialog.setSize(340,260);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);


        JPanel buttonPanel = new JPanel();

        ActionListener buttonListener = new ButtonListener();
        acceptButton.addActionListener(buttonListener);
        dismissButton.addActionListener(buttonListener);

        buttonPanel.add(dismissButton);
        buttonPanel.add(acceptButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * ButtonListener contains all actions that are taken when certain buttons are clicked
     */
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == dismissButton) {
                acceptButtonClicked = false;
                dialog.dispose();
            }

            if (e.getSource() == acceptButton) {
                try {
                    InventoryItem item = new InventoryItem(
                            inputPanel.getDescription(),
                            inputPanel.getCategory(),
                            inputPanel.getStock(),
                            inputPanel.getItemLocation(),
                            inputPanel.getWeight(),
                            inputPanel.getPrice()
                    );
                    if (App.getInventory().addNewItem(item)) {
                        acceptButtonClicked = true;
                        dialog.dispose();
                    } else {
                        showErrorOptionPane("Der Artikel konnte dem Lagerbestand nicht hinzugefügt werden. Die Produktbezeichnung und der Lagerort müssen eindeutig sein.");
                    }
                } catch (IllegalArgumentException iae){
                    showErrorOptionPane(iae.getMessage());
                }
            }
        }
    }

    private void showErrorOptionPane(String message) {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(dialog,message,"Fehler beim Erstellen eines Artikels",JOptionPane.ERROR_MESSAGE);
    }
}
