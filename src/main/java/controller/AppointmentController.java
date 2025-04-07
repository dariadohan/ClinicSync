package controller;

import dao.UserDAO;
import dao.MedicalServiceDAO;
import Data.Appointment;
import Data.MedicalService;
import Data.User;
import services.AppointmentService;
import services.MedicalServiceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalTime;
import java.util.List;

public class AppointmentController {
    private static AppointmentService service;
    private static UserDAO userDAO;
    private static MedicalServiceDAO serviceDAO;

    // Setup method to initialize dependencies
    public static void setDependencies(AppointmentService s, UserDAO uDao, MedicalServiceDAO sDao) {
        service = s;
        userDAO = uDao;
        serviceDAO = sDao;
    }

    public static void handleAddAppointment(String pacientName, int doctor, int serviceSelected,
                                            String date, String time, JTable appointmentTable) {
        try {
            LocalTime parsedTime = LocalTime.parse(time);
            if (parsedTime.isBefore(LocalTime.of(10, 0)) || parsedTime.isAfter(LocalTime.of(18, 0))) {
                JOptionPane.showMessageDialog(null, "Time must be between 10:00 and 18:00.");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid time format.");
            return;
        }

        Appointment newAppt = new Appointment(0, pacientName, doctor, date, time, serviceSelected, "Pending");

        if (service.addAppointment(newAppt)) {
            JOptionPane.showMessageDialog(null, "Appointment added.");
            reloadAppointmentsTable(appointmentTable);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to add appointment.");
        }
    }

    public static void reloadAppointmentsTable(JTable table) {
        List<Appointment> appointments = service.getAllAppointments(userDAO, serviceDAO);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Appointment a : appointments) {
            model.addRow(new Object[]{
                    a.getId(), a.getPacient(), new UserController().getDoctorNameById(a.getDoctor()),  MedicalServiceService.getMedicalServiceNameById((a.getService())),
                    a.getDate(), a.getTime(), a.getStatus()
            });
        }
    }

    public static int getAppointmentCountByDoctorId(int doctorId) {
        return service.getAppointmentCountByDoctorId(doctorId);
    }

    public static List<Appointment> fetchAppointmentsBetweenDates(String startDate, String endDate) {
        return service.getAppointmentsBetweenDates(startDate, endDate);
    }
}
