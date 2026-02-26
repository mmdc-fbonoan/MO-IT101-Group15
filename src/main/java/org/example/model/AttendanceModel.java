package org.example.model;

import java.util.ArrayList;

public class AttendanceModel {
    private String employeeId;
    private ArrayList<AttendanceRecordModel> attendanceTime = new ArrayList<>();

    public AttendanceModel(String employeeId) {
        this.employeeId = employeeId;
    }

    public void addAttendance(String date, String timeIn, String timeOut) {
        attendanceTime.add(new AttendanceRecordModel(date, timeIn, timeOut));
    }

    public ArrayList<AttendanceRecordModel> getAttendanceTime() {
        return attendanceTime;
    }
}
