package com.acil.er_backend.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class DoctorNoteRequest {

    @NotNull(message = "appointmentId boş olamaz.")
    private Long appointmentId;

    @NotBlank(message = "Tanı boş olamaz.")
    private String diagnosis;

    private String secondaryDiagnosis;

    @NotBlank(message = "Plan boş olamaz.")
    private String plan;

    private String prescription;
    private String labOrders;
    private LocalDate followUpDate;
    private String followUpNotes;
    private Boolean referralNeeded;
    private String referralDepartment;

    @Min(0) @Max(365)
    private Integer restDays;

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getSecondaryDiagnosis() { return secondaryDiagnosis; }
    public void setSecondaryDiagnosis(String secondaryDiagnosis) { this.secondaryDiagnosis = secondaryDiagnosis; }
    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    public String getLabOrders() { return labOrders; }
    public void setLabOrders(String labOrders) { this.labOrders = labOrders; }
    public LocalDate getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDate followUpDate) { this.followUpDate = followUpDate; }
    public String getFollowUpNotes() { return followUpNotes; }
    public void setFollowUpNotes(String followUpNotes) { this.followUpNotes = followUpNotes; }
    public Boolean getReferralNeeded() { return referralNeeded; }
    public void setReferralNeeded(Boolean referralNeeded) { this.referralNeeded = referralNeeded; }
    public String getReferralDepartment() { return referralDepartment; }
    public void setReferralDepartment(String referralDepartment) { this.referralDepartment = referralDepartment; }
    public Integer getRestDays() { return restDays; }
    public void setRestDays(Integer restDays) { this.restDays = restDays; }
}
