import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The AddCategoryDialog is the window used to create new categories
 *
 * @author ...
 * @version 1.0
 */
public class AddCategoryDialog extends JDialog {

    /**
     * acceptButtonClicked is true when the acceptButton is pressed
     */
    public Boolean acceptButtonClicked = false;

    /**
     * Label that holds "Kategoriename"
     */
    private JLabel categoryNameLabel = new JLabel("Kategoriename");

    /**
     * text field that holds the name of the new category
     */
    private JTextField categoryNameTextField = new JTextField(15);

    /**
     * button to close dialog and cancel creating category
     */
    JButton dismissButton = new JButton("Abbrechen");

    /**
     * button to confirm input and create new category
     */
    JButton acceptButton = new JButton("Erstellen");

    /**
     * Constructor for a new AddCategoryDialog
     */
    AddCategoryDialog() {
        this.setTitle("Kategorie hinzufügen");
        this.setLayout(new GridLayout(2,1));
        this.setResizable(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new EmptyBorder(10,10,10,10));
        inputPanel.setLayout(new GridLayout(0,2,6,3));

        inputPanel.add(categoryNameLabel);
        inputPanel.add(categoryNameTextField);

        JPanel buttonPanel = new JPanel();

        ActionListener buttonListener = new ButtonListener();
        acceptButton.addActionListener(buttonListener);
        dismissButton.addActionListener(buttonListener);

        buttonPanel.add(dismissButton);
        buttonPanel.add(acceptButton);

        this.add(inputPanel, BorderLayout.CENTER);

        this.add(buttonPanel, BorderLayout.SOUTH);

        this.pack();
    }

    /**
     * ButtonListener listens for button presses. When a button is pressed ButtonListener checks which button has been
     * pressed and what should be done.
     */
    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == dismissButton) {
                acceptButtonClicked = false;
                dispose();
            }

            if (e.getSource() == acceptButton) {
                if (isValidInput()) {
                    // TODO: Check if description and location are unique

                    // TODO: If every case above is true, add category to category list

                    acceptButtonClicked = true;
                    dispose();
                } else {
                    System.out.println("Input not valid!");
                    /* final JDialog dialog = new JDialog();
                    dialog.setAlwaysOnTop(true);
                    JOptionPane.showMessageDialog(dialog,"Eingegebene Werte sind fehlerhaft oder nicht vorhanden. Bitte überprüfen Sie Ihre Eingabe!","Fehler beim Erstellen einer Kategorie",JOptionPane.ERROR_MESSAGE);
                    */
                }
            }
        }
    }

    /**
     * @return is the entered name a valid category name?
     */
    private Boolean isValidInput() {
        String stringValue = categoryNameTextField.getText();

        if (stringValue.matches("[a-zA-ZöäüÖÄÜß0-9]{1,256}")) {
            return true;
        }
        return false;

    }
}
