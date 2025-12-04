package com.acil.er_backend.dto;

import com.acil.er_backend.model.Appointment;
import com.acil.er_backend.model.DoctorNote;
import com.acil.er_backend.model.Patient;
import com.acil.er_backend.model.TriageRecord;
import java.util.List;

public class AppointmentDetailResponse {
    private Appointment appointment;
    private Patient patient;
    private List<TriageRecord> triageRecords;
    private List<DoctorNote> doctorNotes;

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public List<TriageRecord> getTriageRecords() { return triageRecords; }
    public void setTriageRecords(List<TriageRecord> triageRecords) { this.triageRecords = triageRecords; }
    public List<DoctorNote> getDoctorNotes() { return doctorNotes; }
    public void setDoctorNotes(List<DoctorNote> doctorNotes) { this.doctorNotes = doctorNotes; }
}
