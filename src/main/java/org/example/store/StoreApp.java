package org.example;

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
    private String attendanceFilePath = "public/employee_attendance.csv";
    private String employeeDetailsFilePath = "public/employee_details.csv";
    private boolean isAttendanceFileValid = false;
    private boolean isEmployeeDetailsFileValid = false;

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
}
