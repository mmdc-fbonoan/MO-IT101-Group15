package org.example.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import org.example.model.AttendanceRecordModel;
import org.example.model.EmployeeModel;
import org.example.store.StoreApp;
import org.example.utils.ReadFromTextFile;

public class UserInterface {

    Scanner scanner = new Scanner(System.in);
    StoreApp app = new StoreApp();

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void forceExit() {
        app.changePayrollStaffMenu(false);
        app.changeEmployeeMenu(false);
        app.changeApp(false);
    }

    private void getCSVData() {
        ReadFromTextFile readFromTextFile = new ReadFromTextFile();

        // employee details
        HashMap<String, EmployeeModel> employees = readFromTextFile.getEmployeeData(app.getEmployeeDetailsFilePath());
        app.setEmployeeData(employees);
        app.changeEmployeeDetailsFileValid(!employees.isEmpty());

        // attedance record
        HashMap<String, ArrayList<AttendanceRecordModel>> attendance =
                readFromTextFile.getAttendanceData(app.getAttendanceFilePath(), app.getAllEmployeeNumber());
        app.setAttendanceData(attendance);
        app.changeAttendanceFileValid(!attendance.isEmpty());
    }

    private void displayHeader() {

        getCSVData();

        // HashMap<String, EmployeeModel> employees = app.getEmployeeData();
        // HashMap<String, ArrayList<AttendanceRecordModel>> attendance = app.getAttendanceData();
        // System.out.println(employees);
        // System.out.println(attendance);

        System.out.println("------------------------------");
        System.out.println("Current Role: " + app.getUserRole());
        System.out.println("Attendance CSV Status: " + app.isAttendanceFileValid());
        System.out.println("Employee Details CSV Status: " + app.isEmployeeDetailsFileValid());
        System.out.println("------------------------------");
    }

    // KISS
    private void displayPayrollStaffMenu() {
        while (app.isPayrollStaffMenu()) {
            System.out.println("\nChoose an option:");
            System.out.println("a. Process Payroll");
            System.out.println(":q. Logout");
            System.out.println(":wq. Exit the program");
            System.out.println("Choice: ");

            String optionInput = scanner.nextLine();

            switch (optionInput) {
                case "a":
                    clearScreen();
                    System.out.println("Processing payroll...");
                    displayHeader();
                    break;
                case ":q":
                    clearScreen();
                    app.setUserRole("");
                    app.changePayrollStaffMenu(false);
                    break;
                case ":wq":
                    forceExit();
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    // KISS
    private void displayEmployeeMenu() {
        while (app.isEmployeeMenu()) {
            System.out.println("\nChoose an option:");
            System.out.println("a. Enter your employee number");
            System.out.println(":q. Logout");
            System.out.println(":wq. Exit the program");
            System.out.println("Choice: ");

            String optionInput = scanner.nextLine();

            switch (optionInput) {
                case "a":
                    clearScreen();
                    System.out.println("Enter your employee number");
                    displayHeader();
                    break;
                case ":q":
                    clearScreen();
                    app.setUserRole("");
                    app.changeEmployeeMenu(false);
                    break;
                case ":wq":
                    forceExit();
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public void checkUserCredentials(String username, String password) {
        if (username.equals("payroll_staff") && password.equals("12345")) {
            app.setUserRole("payroll_staff");
        } else if (username.equals("employee") && password.equals("12345")) {
            app.setUserRole("employee");
        } else {
            app.setUserRole("");
            System.out.println("------------------------------");
            System.out.println("Invalid credentials. Please try again.");
            System.out.println("------------------------------");
            forceExit();
        }
    }

    public void displayMainMenu() {
        while (app.isAppRunning()) {
            clearScreen();
            System.out.println("Please Login to continue");
            System.out.println("Username: ");
            String usernameInput = scanner.nextLine();
            System.out.println("Password: ");
            String passwordInput = scanner.nextLine();

            checkUserCredentials(usernameInput, passwordInput);

            // reset
            app.changePayrollStaffMenu(true);
            app.changeEmployeeMenu(true);

            // role menu
            switch (app.getUserRole()) {
                case "payroll_staff":
                    clearScreen();
                    displayHeader();
                    displayPayrollStaffMenu();
                    break;
                case "employee":
                    clearScreen();
                    displayHeader();
                    displayEmployeeMenu();
                    break;
            }
        }

        System.out.println("");
        System.out.println("Program exited.");
        System.out.println("");
    }
}
