package org.example.store;

import java.util.ArrayList;
import java.util.HashMap;
import org.example.model.AttendanceRecordModel;
import org.example.model.EmployeeModel;

public class StoreApp {
    // UI state variables
    private boolean appRunning = true;
    private boolean payrollStaffMenu = true;
    private boolean employeeMenu = true;
    private String userRole = "";

    // getters
    public boolean isAppRunning() {
        return appRunning;
    }

    public boolean isPayrollStaffMenu() {
        return payrollStaffMenu;
    }

    public boolean isEmployeeMenu() {
        return employeeMenu;
    }

    public String getUserRole() {
        return userRole;
    }

    // setters
    public void changeApp(boolean input) {
        this.appRunning = input;
    }

    public void changePayrollStaffMenu(boolean input) {
        this.payrollStaffMenu = input;
    }

    public void changeEmployeeMenu(boolean input) {
        this.employeeMenu = input;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    // CSV file paths and validation status
    private String attendanceFilePath = "public/attendance_record.csv";
    private String employeeDetailsFilePath = "public/employee_details.csv";
    private boolean isAttendanceFileValid = false;
    private boolean isEmployeeDetailsFileValid = false;
    private HashMap<String, EmployeeModel> employeeData = new HashMap<>();
    private HashMap<String, ArrayList<AttendanceRecordModel>> attendanceData = new HashMap<>();

    // getters
    public String getAttendanceFilePath() {
        return attendanceFilePath;
    }

    public String getEmployeeDetailsFilePath() {
        return employeeDetailsFilePath;
    }

    public boolean isAttendanceFileValid() {
        return isAttendanceFileValid;
    }

    public boolean isEmployeeDetailsFileValid() {
        return isEmployeeDetailsFileValid;
    }

    public HashMap<String, EmployeeModel> getEmployeeData() {
        return employeeData;
    }

    public HashMap<String, ArrayList<AttendanceRecordModel>> getAttendanceData() {
        return attendanceData;
    }

    // setters
    public void setAttendanceFilePath(String attendanceFilePath) {
        this.attendanceFilePath = attendanceFilePath;
    }

    public void setEmployeeDetailsFilePath(String employeeDetailsFilePath) {
        this.employeeDetailsFilePath = employeeDetailsFilePath;
    }

    public void changeAttendanceFileValid(boolean input) {
        this.isAttendanceFileValid = input;
    }

    public void changeEmployeeDetailsFileValid(boolean input) {
        this.isEmployeeDetailsFileValid = input;
    }

    public void setEmployeeData(HashMap<String, EmployeeModel> employeeData) {
        this.employeeData = employeeData;
    }

    public void setAttendanceData(HashMap<String, ArrayList<AttendanceRecordModel>> attendanceData) {
        this.attendanceData = attendanceData;
    }

    // custom methods
    public ArrayList<String> getAllEmployeeNumber() {
        ArrayList<String> employeeNumbers = new ArrayList<>();
        for (String employeeNumber : employeeData.keySet()) {
            employeeNumbers.add(employeeNumber);
        }
        return employeeNumbers;
    }
}
