package org.example;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    // Global configurations
    // static String empFile =
    // "C:/Users/DEL/Documents/NetBeansProjects/Motorph_Test/src/motorph_test/EmployeeDetails.csv";
    // static String attendanceFile =
    // "C:/Users/DEL/Documents/NetBeansProjects/Motorph_Test/src/motorph_test/AttendanceRecord.csv";

    static String empFile = "public/employee_details.csv";
    static String attendanceFile = "public/attendance_record.csv";

    // static String empFile = "src/motorph_test - EmployeeDetails.csv";
    // static String attendanceFile = "src/motorph_test - AttendanceRecord.csv";

    static Scanner scanner = new Scanner(System.in);
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

    public static void main(String[] args) {
        // --- 1. LOGIN SYSTEM ---
        System.out.println("=== MotorPH Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Validate credentials
        if (password.equals("12345") && (username.equals("employee") || username.equals("payroll_staff"))) {
            if (username.equals("employee")) {
                handleEmployeeMenu();
            } else {
                handlePayrollStaffMenu();
            }
        } else {
            System.out.println("Incorrect username and/or password.");
            System.exit(0); // Terminate program
        }
    }

    // --- 2. MENU HANDLERS ---

    private static void handleEmployeeMenu() {
        while (true) {
            System.out.println("\n--- Employee Display Option ---");
            System.out.println("1. Enter your employee number");
            System.out.println("2. Exit the program");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter Employee #: ");
                String empNum = scanner.nextLine();
                displayEmployeeBasicDetails(empNum);
            } else {
                break;
            }
        }
    }
    // If user is Payroll_Staff proceed to display option
    private static void handlePayrollStaffMenu() {
        while (true) {
            System.out.println("\n--- Payroll Staff Display Option ---");
            System.out.println("1. Process Payroll");
            System.out.println("2. Exit the program");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                processPayrollMenu();
            } else {
                break;
            }
        }
    }
    // Display Sub-Option One Employee or All Employee
    private static void processPayrollMenu() {
        System.out.println("\n--- Process Payroll (No Allowances) ---");
        System.out.println("1. One employee");
        System.out.println("2. All employees");
        System.out.println("3. Exit");
        System.out.print("Choice: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            System.out.print("Enter Employee #: ");
            String empNum = scanner.nextLine();
            processFullPayroll(empNum);
        } else if (choice.equals("2")) {
            processAllEmployees();
        }
    }

    // --- 3. CORE LOGIC ---

    private static void displayEmployeeBasicDetails(String empNum) {
        String[] data = findEmployeeRecord(empNum);
        if (data != null) {

            System.out.println("\n========================================");
            System.out.println("         MOTORPH EMPLOYEE INFORMATION       ");
            System.out.println("========================================");
            System.out.println("Employee Number: " + data[0]);
            System.out.println("Employee Name: " + data[2] + " " + data[1]);
            System.out.println("Birthday: " + data[3]);
            System.out.println("========================================");
        } else {
            System.out.println("Employee number does not exist.");
        }
    }

    private static void processFullPayroll(String empNum) {
        String[] empData = findEmployeeRecord(empNum);
        if (empData == null) {
            System.out.println("Employee number does not exist.");
            return;
        }

        double hourlyRate = Double.parseDouble(empData[18]);
        String[] months = {"06", "07", "08", "09", "10", "11", "12"}; // June to December

        for (String month : months) {
            // Cutoff 1: 1st to 15th
            calculateAndDisplayCutoff(empData, month, 1, 15, hourlyRate, false);
            // Cutoff 2: 16th to end (Includes deductions)
            calculateAndDisplayCutoff(empData, month, 16, 31, hourlyRate, true);
        }
    }

    private static void calculateAndDisplayCutoff(
            String[] emp, String month, int start, int end, double rate, boolean isSecondCutoff) {
        double hours = getTotalHoursForPeriod(emp[0], month, start, end);
        double gross = hours * rate;

        // --- OUTPUT FORMATTING ---
        System.out.println("\n========================================");
        System.out.println("         MOTORPH PAYROLL SUMMARY        ");
        System.out.println("========================================");
        // System.out.println("\n----------------------------------------");
        System.out.println("Employee #: " + emp[0]);
        System.out.println("Employee Name: " + emp[2] + " " + emp[1]);
        System.out.println("Cutoff Date: " + getMonthName(month) + " " + start + " to " + end);
        System.out.println("Total Hours Worked: " + hours);
        System.out.println("Gross Salary: " + gross);
        System.out.println("========================================");

        if (isSecondCutoff) {
            // Requirement 5: Add 1st and 2nd cutoff amounts first before computing deductions
            double firstCutoffHours = getTotalHoursForPeriod(emp[0], month, 1, 15);
            double monthlyGross = (firstCutoffHours + hours) * rate;

            double sss = computeSSS(monthlyGross);
            double ph = computePhilHealth(monthlyGross);
            double pi = 100.0;
            double tax = computeIncomeTax(monthlyGross - (sss + ph + pi));
            double totalDeduct = sss + ph + pi + tax;

            System.out.println("Each Deduction:");
            System.out.println("  SSS: " + sss);
            System.out.println("  PhilHealth: " + ph);
            System.out.println("  Pag-IBIG: " + pi);
            System.out.println("  Tax: " + tax);
            System.out.println("Total Deductions: " + totalDeduct);
            System.out.println("Net Salary: " + (gross - totalDeduct));
        } else {
            System.out.println("Net Salary: " + gross);
        }
    }

    // --- 4. CALCULATION HELPERS ---

    private static double getTotalHoursForPeriod(String empNum, String month, int start, int end) {
        double total = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(attendanceFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                String[] dateParts = d[3].split("/"); // Expected format MM/DD/YYYY
                int dDay = Integer.parseInt(dateParts[1]);

                if (d[0].equals(empNum) && dateParts[0].equals(month) && dDay >= start && dDay <= end) {
                    LocalTime login = LocalTime.parse(d[4], timeFormat);
                    LocalTime logout = LocalTime.parse(d[5], timeFormat);
                    total += computeWorkHours(login, logout);
                }
            }
        } catch (Exception e) {
        }
        return total;
    }

    private static double computeWorkHours(LocalTime login, LocalTime logout) {
        // Logic 4a: Only count 8:00 AM to 5:00 PM
        LocalTime startLimit = LocalTime.of(8, 0);
        LocalTime endLimit = LocalTime.of(17, 0);

        if (login.isBefore(startLimit)) login = startLimit;
        if (logout.isAfter(endLimit)) logout = endLimit;

        // Logic 4b, c, d: Handle specific login/logout durations
        long mins = Duration.between(login, logout).toMinutes();
        if (mins > 60) mins -= 60; // Standard lunch break

        return mins / 60.0;
    }

    // --- 5. DATA HELPERS ---

    private static String[] findEmployeeRecord(String empNum) {
        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (data[0].trim().equals(empNum)) return data;
            }
        } catch (Exception e) {
        }
        return null;
    }

    private static void processAllEmployees() {
        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                processFullPayroll(data[0].trim());
            }
        } catch (Exception e) {
        }
    }

    // Reuse existing deduction logic from Milestone 1
    public static double computeSSS(double gross) {
        if (gross >= 24750) return 1125.00;
        if (gross < 3250) return 135.00;
        double contribution = 157.50;
        double floor = 3250;
        while (floor + 500 <= gross) {
            contribution += 22.50;
            floor += 500;
        }
        return contribution;
    }

    public static double computePhilHealth(double gross) {
        double prem = (gross <= 10000) ? 300.0 : (gross >= 60000) ? 1800.0 : gross * 0.03;
        return prem / 2;
    }

    public static double computeIncomeTax(double taxable) {
        if (taxable <= 20832) return 0;
        if (taxable <= 33332) return (taxable - 20833) * 0.20;
        if (taxable <= 66666) return 2500 + (taxable - 33333) * 0.25;
        if (taxable <= 166666) return 10833 + (taxable - 66667) * 0.30;
        if (taxable <= 666666) return 40833.33 + (taxable - 166667) * 0.32;
        return 200833.33 + (taxable - 666667) * 0.35;
    }

    private static String getMonthName(String m) {
        String[] n = {"", "", "", "", "", "", "June", "July", "August", "September", "October", "November", "December"};
        return n[Integer.parseInt(m)];
    }
}
