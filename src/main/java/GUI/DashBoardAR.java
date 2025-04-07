package GUI;

import Data.Appointment;
import Data.Role;
import Data.User;
import controller.AppointmentController;
import controller.MedicalServiceController;
import controller.UserController;
import dao.AppointmentDAO;
import dao.DatabaseConnection;
import dao.MedicalServiceDAO;
import dao.UserDAO;
import services.AppointmentService;
import services.MedicalServiceService;
import services.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DashBoardAR extends JFrame {
    private JPanel dbP;
    private JButton viewButton;
    private JButton exportButton;
    private JPanel mainP;
    private JTable table1;
    private JTable table2;
    private JFormattedTextField ddMmYyyyFormattedTextField;
    private JFormattedTextField ddMmYyyyFormattedTextField1;
    private JButton backButton;

    public DashBoardAR() throws SQLException {
        super();
        this.setContentPane(mainP);
        this.setSize(new Dimension(650, 530));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        try {
            AppointmentController.setDependencies(
                    new AppointmentService(new AppointmentDAO(DatabaseConnection.getConnection())),
                    new UserDAO(),
                    new MedicalServiceDAO()
            );
            MedicalServiceController.setDependencies(new MedicalServiceService());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String[] columnNames = {"ID", "Name", "Specialty", "Email", "Phone Number", "Appointments"};
        Object[][] data = fetchAllUsers();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table1.setModel(model);

        backButton.addActionListener( e -> {
            dispose();
            new DashBoard();
        });

        viewButton.addActionListener(e -> {
            String startDate = ddMmYyyyFormattedTextField.getText();
            String endDate = ddMmYyyyFormattedTextField1.getText();

            List<Appointment> appointments = AppointmentController.fetchAppointmentsBetweenDates(startDate, endDate);

            String[] columnNames2 = {"Date", "Time", "Patient ID", "Doctor ID", "Service ID"};
            Object[][] data2 = new Object[appointments.size()][];

            for (int i = 0; i < appointments.size(); i++) {
                var a = appointments.get(i);

                data2[i] = new Object[] {
                        a.getDate(),
                        a.getTime(),
                        a.getPacient(),
                        UserController.getDoctorNameById(a.getDoctor()),
                        MedicalServiceService.getMedicalServiceNameById((a.getService()))
                };
            }

            DefaultTableModel model2 = new DefaultTableModel(data2, columnNames2);
            table2.setModel(model2);
        });


        this.setVisible(true);
    }

    private void refreshTableData() {
        Object[][] newData = fetchAllUsers();
        String[] columnNames = {"ID", "Name", "Specialty", "Email", "Phone Number", "Appointments"};
        DefaultTableModel newModel = new DefaultTableModel(newData, columnNames);
        table1.setModel(newModel);
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
                    user.getPhoneNo(),
                    AppointmentController.getAppointmentCountByDoctorId(user.getId())
            };
        }

        return data;
    }
}
