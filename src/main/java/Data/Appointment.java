package Data;

import java.util.Optional;

public class Appointment {
    private int id;
    private String pacientName;
    private int doctor;
    private String date;
    private String time;
    private int service;
    private String status;
    public Appointment(int id, String pacientName, int doctor, String date, String time, int service, String status){
        this.id=id;
        this.pacientName=pacientName;
        this.doctor=doctor;
        this.date=date;
        this.time=time;
        this.service=service;
        this.status=status;
    }

    public int getId() {
        return id;
    }

    public int getService() {
        return service;
    }

    public String getDate() {
        return date;
    }

    public String getPacient() {
        return pacientName;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    public void setPacientName(String pacientName) {
        this.pacientName = pacientName;
    }

    public void setService(int service) {
        this.service = service;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
