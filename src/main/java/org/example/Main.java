package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    static String empFile = "public/employee_details.csv";
    static String attendanceFile = "public/attendance_record.csv";

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
        if ("employee".equals(username) && "12345".equals(password)) {
            handleEmployeeMenu();
        } else if ("payroll_staff".equals(username) && "12345".equals(password)) {
            handlePayrollStaffMenu();
        } else {
            System.out.println("Incorrect username and/or password.");
            System.exit(0);
        }
    }

    // --- 2. MENU HANDLERS ---
    private static void handleEmployeeMenu() {
        while (true) {
            System.out.println("--- Employee Display Option ---");
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
            System.out.println("--- Payroll Staff Display Option ---");
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
        System.out.println("--- Process Payroll (No Allowances) ---");
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

            System.out.println("========================================");
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
        System.out.println("========================================");
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
            System.out.println("========================================");
            System.out.println("Net Salary: " + (gross - totalDeduct));
            System.out.println("========================================");
        } else {
            System.out.println("Net Salary: " + gross);
        }
    }

    // --- 4. CALCULATION  ---
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
            System.out.println("File error: " + e.getMessage());
        }
        return total;
    }

    private static double computeWorkHours(LocalTime login, LocalTime logout) {
        // Logic 4a: Only count 8:00 AM to 5:00 PM
        LocalTime startLimit = LocalTime.of(8, 0);
        LocalTime endLimit = LocalTime.of(17, 0);

        if (login.isBefore(startLimit)) {
            login = startLimit;
        }
        if (logout.isAfter(endLimit)) {
            logout = endLimit;
        }

        // Logic 4b, c, d: Handle specific login/logout durations
        long mins = Duration.between(login, logout).toMinutes();
        if (mins > 60) {
            mins -= 60; // Standard lunch break
        }
        return mins / 60.0;
    }

    // --- 5. DATA HELPERS ---
    private static String[] findEmployeeRecord(String empNum) {
        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (data[0].trim().equals(empNum)) {
                    return data;
                }
            }
        } catch (Exception e) {
            System.out.println("File error: " + e.getMessage());
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
            System.out.println("File error: " + e.getMessage());
        }
    }

    // SSS deduction  based on the Salary range from SSS CSV file of Motorph Requirement
    public static double computeSSS(double gross) {
        // Salary lower bounds from SSS table
        double[] salaryLimits = {
            0, 3250, 3750, 4250, 4750, 5250, 5750, 6250, 6750, 7250, 7750, 8250, 8750, 9250, 9750, 10250, 10750, 11250,
            11750, 12250, 12750, 13250, 13750, 14250, 14750, 15250, 15750, 16250, 16750, 17250, 17750, 18250, 18750,
            19250, 19750, 20250, 20750, 21250, 21750, 22250, 22750, 23250, 23750, 24250, 24750
        };

        // Corresponding contributions from SSS table
        double[] contributions = {
            135.0, 157.5, 180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5,
            360.0, 382.5, 405.0, 427.5, 450.0, 472.5, 495.0, 517.5, 540.0, 562.5,
            585.0, 607.5, 630.0, 652.5, 675.0, 697.5, 720.0, 742.5, 765.0, 787.5,
            810.0, 832.5, 855.0, 877.5, 900.0, 922.5, 945.0, 967.5, 990.0, 1012.5,
            1035.0, 1057.5, 1080.0, 1102.5, 1125.0
        };

        // Iterate backwards to find which bracket the gross salary falls into
        for (int i = salaryLimits.length - 1; i >= 0; i--) {
            if (gross >= salaryLimits[i]) {
                return contributions[i];
            }
        }
        return 135.0; // Default minimum
    }

    public static double computePhilHealth(double gross) {
        double prem = (gross <= 10000) ? 300.0 : (gross >= 60000) ? 1800.0 : gross * 0.03;
        return prem / 2;
    }

    public static double computeIncomeTax(double taxable) {
        if (taxable <= 20832) {
            return 0;
        }
        if (taxable <= 33332) {
            return (taxable - 20833) * 0.20;
        }
        if (taxable <= 66666) {
            return 2500 + (taxable - 33333) * 0.25;
        }
        if (taxable <= 166666) {
            return 10833 + (taxable - 66667) * 0.30;
        }
        if (taxable <= 666666) {
            return 40833.33 + (taxable - 166667) * 0.32;
        }
        return 200833.33 + (taxable - 666667) * 0.35;
    }

    private static String getMonthName(String m) {
        String[] n = {"", "", "", "", "", "", "June", "July", "August", "September", "October", "November", "December"};
        return n[Integer.parseInt(m)];
    }
}
