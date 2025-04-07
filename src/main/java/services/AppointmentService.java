package services;

import dao.AppointmentDAO;
import dao.UserDAO;
import dao.MedicalServiceDAO;
import Data.Appointment;

import java.util.List;

public class AppointmentService {
    private AppointmentDAO appointmentDAO;

    public AppointmentService(AppointmentDAO dao) {
        this.appointmentDAO = dao;
    }

    public boolean addAppointment(Appointment a) {
        return appointmentDAO.insertAppointment(a);
    }

    public List<Appointment> getAllAppointments(UserDAO userDAO, MedicalServiceDAO serviceDAO) {
        return appointmentDAO.getAllAppointments(userDAO, serviceDAO);
    }

    public int getAppointmentCountByDoctorId(int doctorId) {
        return appointmentDAO.getAppointmentCountByDoctorId(doctorId);
    }

    public List<Appointment> getAppointmentsBetweenDates(String startDate, String endDate) {
        return appointmentDAO.findAppointmentsBetweenDates(startDate, endDate);
    }
}
