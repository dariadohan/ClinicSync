package Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Report {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Appointment> appointments;
    private Map<User, Integer> doctorStatistics;
    private Map<MedicalService, Integer> serviceStatistics;
    private String reportFormat;

    public Report(LocalDate startDate, LocalDate endDate, List<Appointment> appointments,
                  Map<User, Integer> doctorStatistics, Map<MedicalService, Integer> serviceStatistics,
                  String reportFormat) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.appointments = appointments;
        this.doctorStatistics = doctorStatistics;
        this.serviceStatistics = serviceStatistics;
        this.reportFormat = reportFormat;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public Map<User, Integer> getDoctorStatistics() {
        return doctorStatistics;
    }

    public Map<MedicalService, Integer> getServiceStatistics() {
        return serviceStatistics;
    }

    public String getReportFormat() {
        return reportFormat;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void setDoctorStatistics(Map<User, Integer> doctorStatistics) {
        this.doctorStatistics = doctorStatistics;
    }

    public void setServiceStatistics(Map<MedicalService, Integer> serviceStatistics) {
        this.serviceStatistics = serviceStatistics;
    }

    public void setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
    }
}

