package org.example;

import java.time.LocalDateTime;
import java.util.Date;

public class Appoinment {
    private int id;
    private Pacient pacient;
    private Doctor doctor;
    private LocalDateTime dateTime;
    private MedicalService service;
    private String status;
    public Appoinment(int id, Pacient pacient, Doctor doctor, LocalDateTime dateTime, MedicalService service, String status){
        this.id=id;
        this.pacient=pacient;
        this.doctor=doctor;
        this.dateTime=dateTime;
        this.service=service;
        this.status=status;
    }
}
