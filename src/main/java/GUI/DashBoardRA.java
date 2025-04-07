package GUI;

import Data.MedicalService;
import Data.Role;
import Data.User;
import controller.AppointmentController;
import controller.UserController;
import dao.AppointmentDAO;
import dao.DatabaseConnection;
import dao.MedicalServiceDAO;
import dao.UserDAO;
import services.AppointmentService;
import services.MedicalServiceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DashBoardRA extends JFrame {
    private JTable tableDoctors;
    private JTable tableMedServices;
    private JTable tableAppoint;
    private JTextField textFieldName;
    private JComboBox<String> comboBoxTime;
    private JFormattedTextField dateTF;
    private JButton addButton;
    private JPanel dbPRA;
    private JPanel mainP;

    private final MedicalServiceService serviceService = new MedicalServiceService();


    public DashBoardRA() throws SQLException {
        setTitle("Receptionist Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // TOP Panel - Doctors, Med Services, Form
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        AppointmentController.setDependencies(
                new AppointmentService(new AppointmentDAO(DatabaseConnection.getConnection())),
                new UserDAO(),
                new MedicalServiceDAO()
        );


        // Doctors Table
        String[] columnNames = {"ID", "Name", "Specialty", "Email", "Phone Number"};
        Object[][] data = fetchAllUsers();

        tableDoctors = new JTable(data, columnNames);
        JScrollPane scrollDoctors = new JScrollPane(tableDoctors);
        topPanel.add(scrollDoctors);

        // Medical Services Table
        String[] columnNamesMs = {"ID", "Name", "Price", "Duration"};
        tableMedServices = new JTable(new String[][]{{"square", "green", "red","purple"}}, columnNamesMs);
        JScrollPane scrollServices = new JScrollPane(tableMedServices);
        topPanel.add(scrollServices);
        loadServiceTable();

        // Right Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int row = 0;

        // Name Label and Field
        gbc.gridx = 0;
        gbc.gridy = row++;
        formPanel.add(new JLabel("Name"), gbc);

        textFieldName = new JTextField();
        gbc.gridy = row++;
        formPanel.add(textFieldName, gbc);

        // Time Label and Combo
        gbc.gridy = row++;
        formPanel.add(new JLabel("Time"), gbc);

        comboBoxTime = new JComboBox<>();
        for (int i = 0; i < 24; i++) {
            String hour = String.format("%02d:00", i);
            comboBoxTime.addItem(hour);
        }
        gbc.gridy = row++;
        formPanel.add(comboBoxTime, gbc);

        // Date Label and Field
        gbc.gridy = row++;
        formPanel.add(new JLabel("Date"), gbc);

        dateTF = new JFormattedTextField("dd/mm/2025");
        gbc.gridy = row++;
        formPanel.add(dateTF, gbc);

        // Add Button
        addButton = new JButton("Add Appointment");
        gbc.gridy = row;
        formPanel.add(addButton, gbc);

        topPanel.add(formPanel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel appointmentModel = new DefaultTableModel(
                new Object[]{"ID", "Patient Name", "Doctor Name", "Service Name", "Date", "Time", "Status"}, 0
        );
        tableAppoint = new JTable(appointmentModel);
        JScrollPane scrollAppointments = new JScrollPane(tableAppoint);
        scrollAppointments.setPreferredSize(new Dimension(800, 200));
        mainPanel.add(scrollAppointments, BorderLayout.CENTER);
        AppointmentController.reloadAppointmentsTable(tableAppoint);

        setContentPane(mainPanel);

        addButton.addActionListener(e -> {
            String patientName = textFieldName.getText().trim();
            int selectedRowDoctor = tableDoctors.getSelectedRow();
            int selectedRowMS = tableMedServices.getSelectedRow();
            String date = dateTF.getText().trim();
            String time = (String) comboBoxTime.getSelectedItem();

            // Validate fields
            if (patientName.isEmpty() || selectedRowDoctor == -1 || selectedRowMS == -1 || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields and select a doctor and a service.");
                return;
            }

            // Get doctor and service ID
            int doctorId = (int) tableDoctors.getValueAt(selectedRowDoctor, 0);
            int serviceId = (int) tableMedServices.getValueAt(selectedRowMS, 0);

            AppointmentController.handleAddAppointment(
                    patientName,
                    doctorId,
                    serviceId,
                    date,
                    time,
                    tableAppoint
            );
        });


        setVisible(true);
    }

    public Object[][] fetchAllUsers() {
        List<User> users = UserController.fetchUsersByRole(Role.DOCTOR);
        if (users == null || users.isEmpty()) {
            return new Object[0][0];
        }

        Object[][] data = new Object[users.size()][];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i] = new Object[]{
                    user.getId(),
                    user.getName(),
                    user.getSpecialty() != null ? user.getSpecialty().name() : "",
                    user.getEmail(),
                    user.getPhoneNo()
            };
        }

        return data;
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

        tableMedServices.setModel(model);
    }
}
