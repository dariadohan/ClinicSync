package GUI;

import services.MedicalServiceService;

import javax.swing.*;
import java.awt.*;

public class AddMS extends JFrame {
    private JPanel panel1;
    private JTextField textField1; // name
    private JTextField textField2; // price
    private JTextField textField3; // duration
    private JButton backButton;
    private JButton addButton;

    private final MedicalServiceService serviceService = new MedicalServiceService();
    private final DashBoardAMS dashboard; // to refresh the table

    public AddMS(DashBoardAMS dashboard) {
        super("Add Medical Service");
        this.dashboard = dashboard;
        this.setContentPane(panel1);
        this.setSize(new Dimension(650, 530));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        backButton.addActionListener(e -> {
            dispose();
            dashboard.setVisible(true);
        });

        addButton.addActionListener(e -> {
            String name = textField1.getText().trim();
            String priceText = textField2.getText().trim();
            String durationText = textField3.getText().trim();

            if (name.isEmpty() || priceText.isEmpty() || durationText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            try {
                float price = Float.parseFloat(priceText);
                int duration = Integer.parseInt(durationText);

                boolean success = serviceService.createMedicalService(name, price, duration);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Service added successfully!");
                    dashboard.refreshTable();
                    dispose();
                    dashboard.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add service. Check if the name is unique.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Price and duration must be valid numbers.");
            }
        });
    }
}
