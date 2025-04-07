package GUI;

import Data.MedicalService;
import services.MedicalServiceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DashBoardAMS extends JFrame {
    private JPanel dbPAMS;
    private JPanel mainP;
    private JTable table1;
    private JButton addButton;
    private JButton deleteButton;
    private JButton backButton;

    private final MedicalServiceService serviceService = new MedicalServiceService();

    public DashBoardAMS() {
        super("Medical Services Dashboard");
        this.setContentPane(mainP);
        mainP.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
        this.setSize(new Dimension(650, 530));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(backButton);
        mainP.add(topPanel, BorderLayout.NORTH);

        JScrollPane tableScroll = new JScrollPane(table1);
        tableScroll.setPreferredSize(new Dimension(600, 300));
        mainP.add(tableScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        mainP.add(buttonPanel, BorderLayout.SOUTH);

        loadServiceTable();

        addButton.addActionListener(e -> {
            dispose();
            new AddMS(DashBoardAMS.this);
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow >= 0) {
                int serviceId = (int) table1.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this service?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = serviceService.deleteServiceById(serviceId);
                    if (deleted) {
                        refreshTable();
                        JOptionPane.showMessageDialog(this, "Service deleted.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete service.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a service to delete.");
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new DashBoard();
        });

        this.setVisible(true);
    }

    private void loadServiceTable() {
        String[] columnNames = {"ID", "Name", "Price", "Duration"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        List<MedicalService> services = serviceService.getAllServices();
        for (MedicalService service : services) {
            model.addRow(new Object[]{
                    service.getId(),
                    service.getName(),
                    service.getPrice(),
                    service.getDuration()
            });
        }

        table1.setModel(model);
    }

    public void refreshTable() {
        loadServiceTable();
    }
}
