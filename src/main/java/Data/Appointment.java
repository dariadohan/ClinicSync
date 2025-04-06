package Data;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private User pacient;
    private User doctor;
    private LocalDateTime dateTime;
    private MedicalService service;
    private String status;
    public Appointment(int id, User pacient, User doctor, LocalDateTime dateTime, MedicalService service, String status){
        this.id=id;
        this.pacient=pacient;
        this.doctor=doctor;
        this.dateTime=dateTime;
        this.service=service;
        this.status=status;
    }
}
