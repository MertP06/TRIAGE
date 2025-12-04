package com.acil.er_backend.dto;

import jakarta.validation.constraints.*;

public class CreateTriageRequest {

    @NotNull(message = "appointmentId boş olamaz.")
    private Long appointmentId;

    @NotBlank(message = "Semptom listesi boş olamaz.")
    private String nurseSymptomsCsv;

    @DecimalMin(value = "30.0") @DecimalMax(value = "45.0")
    private Double temperature;

    @Min(20) @Max(240)
    private Integer pulse;

    @Min(50) @Max(260)
    private Integer bpHigh;

    @Min(30) @Max(200)
    private Integer bpLow;

    @Min(50) @Max(100)
    private Integer oxygenSaturation;

    @Min(5) @Max(60)
    private Integer respiratoryRate;

    @Min(0) @Max(10)
    private Integer painLevel;

    @Min(20) @Max(600)
    private Integer bloodGlucose;

    @NotBlank(message = "Triage seviyesi boş olamaz.")
    private String triageLevel;

    private String notes;

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public String getNurseSymptomsCsv() { return nurseSymptomsCsv; }
    public void setNurseSymptomsCsv(String nurseSymptomsCsv) { this.nurseSymptomsCsv = nurseSymptomsCsv; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Integer getPulse() { return pulse; }
    public void setPulse(Integer pulse) { this.pulse = pulse; }
    public Integer getBpHigh() { return bpHigh; }
    public void setBpHigh(Integer bpHigh) { this.bpHigh = bpHigh; }
    public Integer getBpLow() { return bpLow; }
    public void setBpLow(Integer bpLow) { this.bpLow = bpLow; }
    public Integer getOxygenSaturation() { return oxygenSaturation; }
    public void setOxygenSaturation(Integer oxygenSaturation) { this.oxygenSaturation = oxygenSaturation; }
    public Integer getRespiratoryRate() { return respiratoryRate; }
    public void setRespiratoryRate(Integer respiratoryRate) { this.respiratoryRate = respiratoryRate; }
    public Integer getPainLevel() { return painLevel; }
    public void setPainLevel(Integer painLevel) { this.painLevel = painLevel; }
    public Integer getBloodGlucose() { return bloodGlucose; }
    public void setBloodGlucose(Integer bloodGlucose) { this.bloodGlucose = bloodGlucose; }
    public String getTriageLevel() { return triageLevel; }
    public void setTriageLevel(String triageLevel) { this.triageLevel = triageLevel; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
