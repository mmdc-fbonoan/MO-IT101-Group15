package org.example.model;

public class AttendanceRecordModel {
    private String date;
    private String timeIn;
    private String timeOut;

    public AttendanceRecordModel(String date, String timeIn, String timeOut) {
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public String getDate() {
        return date;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    @Override
    public String toString() {
        return "{date: \"" + date + "\", timeIn: \"" + timeIn + "\", timeOut: \"" + timeOut + "\"}";
    }
}
