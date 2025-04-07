package dao;

import Data.Appointment;
import Data.MedicalService;
import Data.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentDAO {
    private Connection conn;

    public AppointmentDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertAppointment(Appointment a) {
        String sql = "INSERT INTO appointment (pacient, doctorID, date, time, serviceID, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getPacient());
            stmt.setInt(2, a.getDoctor());
            stmt.setString(3, a.getDate());
            stmt.setString(4, a.getTime());
            stmt.setInt(5, a.getService());
            stmt.setString(6, a.getStatus());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Appointment> getAllAppointments(UserDAO userDAO, MedicalServiceDAO serviceDAO) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointment";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("idappointment");
                String pacient = rs.getString("pacient");
                int doctorId = rs.getInt("doctorID");
                String date = rs.getString("date");
                String time = rs.getString("time");
                int serviceId = rs.getInt("serviceID");
                String status = rs.getString("status");

                int doctor = userDAO.findById(doctorId).getId();
                int service = serviceDAO.getServiceById(serviceId).getId();

                Appointment a = new Appointment(id, pacient, doctor, date, time, service, status);
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getAppointmentCountByDoctorId(int doctorId) {
        String sql = "SELECT COUNT(*) FROM appointment WHERE doctorID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Appointment> findAppointmentsBetweenDates(String startDate, String endDate) {
        List<Appointment> appointments = new ArrayList<>();
        DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate start = LocalDate.parse(startDate, dbFormat);
            LocalDate end = LocalDate.parse(endDate, dbFormat);

            String query = "SELECT * FROM appointment";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String dateStr = rs.getString("date");
                    LocalDate appointmentDate = LocalDate.parse(dateStr, dbFormat);

                    if ((appointmentDate.isEqual(start) || appointmentDate.isAfter(start)) &&
                            (appointmentDate.isEqual(end) || appointmentDate.isBefore(end))) {

                        appointments.add(new Appointment(
                                rs.getInt("idappointment"),
                                rs.getString("pacient"),
                                rs.getInt("doctorID"),
                                rs.getString("date"),
                                rs.getString("time"),
                                rs.getInt("serviceID"),
                                rs.getString("status")
                        ));
                    }
                }

            } catch (SQLException | DateTimeParseException e) {
                System.out.println("Error while querying DB: " + e.getMessage());
            }

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date input format. Use dd/MM/yyyy");
        }

        return appointments;
    }

}