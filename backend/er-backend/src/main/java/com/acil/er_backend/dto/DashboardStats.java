package com.acil.er_backend.dto;

import java.util.Map;

public class DashboardStats {
    private int totalToday;
    private int waiting;
    private int called;
    private int inProgress;
    private int done;
    private int noShow;
    private Map<String, Integer> triageLevels;
    private Double avgWaitTime;

    public int getTotalToday() { return totalToday; }
    public void setTotalToday(int totalToday) { this.totalToday = totalToday; }
    public int getWaiting() { return waiting; }
    public void setWaiting(int waiting) { this.waiting = waiting; }
    public int getCalled() { return called; }
    public void setCalled(int called) { this.called = called; }
    public int getInProgress() { return inProgress; }
    public void setInProgress(int inProgress) { this.inProgress = inProgress; }
    public int getDone() { return done; }
    public void setDone(int done) { this.done = done; }
    public int getNoShow() { return noShow; }
    public void setNoShow(int noShow) { this.noShow = noShow; }
    public Map<String, Integer> getTriageLevels() { return triageLevels; }
    public void setTriageLevels(Map<String, Integer> triageLevels) { this.triageLevels = triageLevels; }
    public Double getAvgWaitTime() { return avgWaitTime; }
    public void setAvgWaitTime(Double avgWaitTime) { this.avgWaitTime = avgWaitTime; }
}
