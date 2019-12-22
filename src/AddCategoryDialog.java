import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCategoryDialog extends JDialog {

    public Boolean acceptButtonClicked = false;

    private JLabel categoryNameLabel = new JLabel("Kategoriename");
    private JTextField categoryNameTextField = new JTextField(15);

    JButton dismissButton = new JButton("Abbrechen");
    JButton acceptButton = new JButton("Erstellen");

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

    private Boolean isValidInput() {
        String stringValue = categoryNameTextField.getText();

        if (stringValue.matches("[a-zA-ZöäüÖÄÜß0-9]{1,256}")) {
            return true;
        }
        return false;

    }
}
