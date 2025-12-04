package com.acil.er_backend.service;

import com.acil.er_backend.dto.*;
import com.acil.er_backend.model.Appointment;
import com.acil.er_backend.model.AppointmentStatus;
import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(String patientTc, String chiefComplaint, Integer urgencySelfAssessment);
    List<Appointment> getTodayAppointments();
    List<Appointment> getTodayAppointmentsByStatus(AppointmentStatus status);
    Appointment updateStatus(Long id, AppointmentStatus status);
    AppointmentDetailResponse getDetail(Long id);
    List<Appointment> getAppointmentsByPatientTc(String tc);
    PatientHistoryResponse getPatientHistory(String tc);
    DashboardStats getDashboardStats();
    WaitingRoomDisplay getWaitingRoomDisplay();
    MobileQueueStatus getMobileQueueStatus(String tc);

    class MobileQueueStatus {
        private boolean found;
        private Integer queueNumber;
        private String status;
        private int waitingAhead;
        private Integer estimatedWaitMinutes;
        private String patientName;
        private String message;

        public boolean isFound() { return found; }
        public void setFound(boolean found) { this.found = found; }
        public Integer getQueueNumber() { return queueNumber; }
        public void setQueueNumber(Integer queueNumber) { this.queueNumber = queueNumber; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getWaitingAhead() { return waitingAhead; }
        public void setWaitingAhead(int waitingAhead) { this.waitingAhead = waitingAhead; }
        public Integer getEstimatedWaitMinutes() { return estimatedWaitMinutes; }
        public void setEstimatedWaitMinutes(Integer estimatedWaitMinutes) { this.estimatedWaitMinutes = estimatedWaitMinutes; }
        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
