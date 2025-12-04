package com.acil.er_backend.dto;

import java.util.List;

public class WaitingRoomDisplay {
    private CurrentCall currentCall;
    private List<WaitingPatient> waitingList;
    private int totalWaiting;
    private String lastUpdated;

    public static class CurrentCall {
        private Integer queueNumber;
        private String patientName;
        private String message;

        public Integer getQueueNumber() { return queueNumber; }
        public void setQueueNumber(Integer queueNumber) { this.queueNumber = queueNumber; }
        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class WaitingPatient {
        private Integer queueNumber;
        private String status;
        private int aheadCount;

        public Integer getQueueNumber() { return queueNumber; }
        public void setQueueNumber(Integer queueNumber) { this.queueNumber = queueNumber; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getAheadCount() { return aheadCount; }
        public void setAheadCount(int aheadCount) { this.aheadCount = aheadCount; }
    }

    public CurrentCall getCurrentCall() { return currentCall; }
    public void setCurrentCall(CurrentCall currentCall) { this.currentCall = currentCall; }
    public List<WaitingPatient> getWaitingList() { return waitingList; }
    public void setWaitingList(List<WaitingPatient> waitingList) { this.waitingList = waitingList; }
    public int getTotalWaiting() { return totalWaiting; }
    public void setTotalWaiting(int totalWaiting) { this.totalWaiting = totalWaiting; }
    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
}
