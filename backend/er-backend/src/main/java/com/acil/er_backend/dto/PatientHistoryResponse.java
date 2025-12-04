package com.acil.er_backend.dto;

import com.acil.er_backend.model.Appointment;
import com.acil.er_backend.model.DoctorNote;
import com.acil.er_backend.model.Patient;
import com.acil.er_backend.model.TriageRecord;
import java.util.List;

public class PatientHistoryResponse {
    private Patient patient;
    private List<Appointment> appointments;
    private List<TriageRecord> triageRecords;
    private List<DoctorNote> doctorNotes;
    private int totalAppointments;
    private int totalTriageRecords;
    private int totalDoctorNotes;

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
        this.totalAppointments = appointments != null ? appointments.size() : 0;
    }

    public List<TriageRecord> getTriageRecords() { return triageRecords; }
    public void setTriageRecords(List<TriageRecord> triageRecords) {
        this.triageRecords = triageRecords;
        this.totalTriageRecords = triageRecords != null ? triageRecords.size() : 0;
    }

    public List<DoctorNote> getDoctorNotes() { return doctorNotes; }
    public void setDoctorNotes(List<DoctorNote> doctorNotes) {
        this.doctorNotes = doctorNotes;
        this.totalDoctorNotes = doctorNotes != null ? doctorNotes.size() : 0;
    }

    public int getTotalAppointments() { return totalAppointments; }
    public int getTotalTriageRecords() { return totalTriageRecords; }
    public int getTotalDoctorNotes() { return totalDoctorNotes; }
}
