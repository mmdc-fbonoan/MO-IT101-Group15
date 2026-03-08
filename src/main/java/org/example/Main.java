package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// notes
// one file
// no oop
// make sure to add a guard clause if needed

public class Main {
    // Global States
    static String currentRole = null;
    static String attendanceFilePath = "public/attendance_record.csv";
    static String employeeDetailsFilePath = "public/employee_details.csv";
    static Map<String, List<String>> employeeData = new HashMap<>();
    static Map<String, List<String[]>> attendanceData = new HashMap<>();

    public static void main(String[] args) {

        // load csv
        loadEmployeeData();
        loadAttendanceData();

        // initialization for Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            clearScreen();
            currentRole = showLogin(scanner);

            // invalid roles
            if (currentRole == null) {
                showInvalidCredentials();
                return;
            }

            // employee role
            if ("employee".equals(currentRole)) {
                clearScreen();
                String inputMenuOption = showEmployeeMenu(scanner);

                // guard clause
                if (inputMenuOption.equals("")) {
                    clearScreen();
                    showInvalidOption();
                }
                if (inputMenuOption.equals("a")) {
                    clearScreen();
                    String inputEmployeeNumber = showEmployeeSubMenu(scanner);

                    List<String> data = employeeData.get(inputEmployeeNumber);

                    // guard clause
                    if (data == null) {
                        clearScreen();
                        System.out.flush();
                        showEmployeeDoesntExist();
                    }
                    clearScreen();

                    String id = data.get(0);
                    String firstName = data.get(1);
                    String lastName = data.get(2);
                    String birthday = data.get(3);

                    showEmployeeSubMenu(id, firstName, lastName, birthday);
                }
                if (inputMenuOption.equals("b")) {
                    showExit();
                }
            }

            // payroll staff flow
            if ("payroll_staff".equals(currentRole)) {
                clearScreen();
                String inputMenuOption = showPayrollStaffMenu(scanner);

                // guard clause
                if (inputMenuOption.equals("")) {
                    clearScreen();
                    showInvalidOption();
                }
                if (inputMenuOption.equals("a")) {
                    clearScreen();
                    String inputSubMenuOption = showPayrollStaffSubMenu(scanner);

                    // guard clause
                    if (inputSubMenuOption.equals("")) {
                        clearScreen();
                        showInvalidOption();
                    }
                    if (inputSubMenuOption.equals("a")) {
                        clearScreen();
                        String inputEmployeeNumber = showPayrollStaffSubMenuOneEmployee(scanner);

                        if (employeeData.containsKey(inputEmployeeNumber)) {
                            List<String> dataEmployee = employeeData.get(inputEmployeeNumber);
                            String empNo = dataEmployee.get(0);
                            String fName = dataEmployee.get(1);
                            String lName = dataEmployee.get(2);
                            double hourlyRate = Double.parseDouble(dataEmployee.get(6));
                            showPayrollStaffComputeTable(
                                    empNo, fName, lName, hourlyRate, attendanceData, inputEmployeeNumber);
                        } else {
                            clearScreen();
                            showEmployeeDoesntExist();
                        }
                    }
                    if (inputSubMenuOption.equals("b")) {
                        clearScreen();

                        employeeData.forEach((employeeId, details) -> {
                            String empNo = employeeId;
                            String fName = details.get(1);
                            String lName = details.get(2);
                            double hourlyRate = Double.parseDouble(details.get(6));
                            showPayrollStaffComputeTable(empNo, fName, lName, hourlyRate, attendanceData, employeeId);
                        });
                    }
                    if (inputSubMenuOption.equals("c")) {
                        clearScreen();
                        showExit();
                    }
                }
                if (inputMenuOption.equals("b")) {
                    clearScreen();
                    showExit();
                }
            }
        } catch (Exception e) {
            clearScreen();
            showError();
        }

        showExit();
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void showError() {
        System.out.println("------------------------------");
        System.out.println("An unexpected error occurred: ");
        System.out.println("------------------------------");
    }

    private static void showExit() {
        System.out.println("------------------------------");
        System.out.println("Program exited.");
        System.out.println("------------------------------");
        System.exit(0);
    }

    private static void showInvalidCredentials() {
        System.out.println("------------------------------");
        System.out.println("Incorrect username and/or password");
        System.out.println("------------------------------");
    }

    private static void showInvalidOption() {
        System.out.println("------------------------------");
        System.out.println("Invalid option");
        System.out.println("------------------------------");
    }

    private static void showEmployeeDoesntExist() {
        System.out.println("------------------------------");
        System.out.println("Employee number does not exist.");
        System.out.println("------------------------------");
    }

    private static String checkCredentials(String username, String password) {
        if ("employee".equals(username) && "12345".equals(password)) {
            return "employee";
        }
        if ("payroll_staff".equals(username) && "12345".equals(password)) {
            return "payroll_staff";
        }
        return null;
    }

    private static String showLogin(Scanner scanner) {
        System.out.println("=== MotorPH Login ===");
        System.out.print("Username: ");
        String inputUsername = scanner.nextLine();
        System.out.print("Password: ");
        String inputPassword = scanner.nextLine();
        return checkCredentials(inputUsername, inputPassword);
    }

    private static void showEmployeeSubMenu(String id, String fName, String lName, String birthday) {
        System.out.println("========================================");
        System.out.println("         MOTORPH EMPLOYEE INFORMATION       ");
        System.out.println("========================================");
        System.out.println("Employee Number: " + id);
        System.out.println("Employee Name: " + fName + " " + lName);
        System.out.println("Birthday: " + birthday);
        System.out.println("========================================");
    }

    private static String showEmployeeSubMenu(Scanner scanner) {
        System.out.print("Enter Employee #: ");
        String inputOption = scanner.nextLine();
        return inputOption;
    }

    private static String showEmployeeMenu(Scanner scanner) {
        System.out.println("--- Employee Display Option ---");
        System.out.println("Logged in as: employee");
        System.out.println("Choose an option:");
        System.out.println("a. Enter your employee number");
        System.out.println("b. Exit the program");
        System.out.print("Choice: ");
        String inputOption = scanner.nextLine();
        return inputOption;
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

    private static long calculateHoursPayrollToMinutes(String timeInRaw, String timeOutRaw, DateTimeFormatter timeFmt) {

        LocalTime login = LocalTime.parse(timeInRaw, timeFmt);
        LocalTime logout = LocalTime.parse(timeOutRaw, timeFmt);

        LocalTime startLimit = LocalTime.of(8, 0);
        LocalTime endLimit = LocalTime.of(17, 0);

        if (login.isBefore(startLimit)) login = startLimit;
        if (logout.isAfter(endLimit)) logout = endLimit;

        long mins = Duration.between(login, logout).toMinutes();
        if (mins > 60) mins -= 60; // lunch break

        return mins;
    }

    private static double getTotalHoursForPeriod(String empNum, String month, int start, int end) {
        List<String[]> dataAttendance = attendanceData.get(empNum);
        if (dataAttendance == null) return 0.0;

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("H:mm");
        double total = 0.0;

        for (String[] row : dataAttendance) {
            // row[0]=date (MM/dd/yyyy), row[1]=timeIn, row[2]=timeOut
            String[] dateParts = row[0].split("/");
            int dDay = Integer.parseInt(dateParts[1]);

            if (dateParts[0].equals(month) && dDay >= start && dDay <= end) {
                total += calculateHoursPayrollToMinutes(row[1], row[2], timeFmt) / 60.0;
            }
        }

        return total;
    }

    // calculateAndDisplayCutoff(empNo, fName, lName, month, 1, 15, hourlyRate, false);
    private static void calculateAndDisplayCutoff(
            String empNo,
            String fName,
            String lName,
            String month,
            int start,
            int end,
            double rate,
            boolean isSecondCutoff) {
        double hours = getTotalHoursForPeriod(empNo, month, start, end);
        double gross = hours * rate;

        // --- OUTPUT FORMATTING ---
        System.out.println("========================================");
        System.out.println("         MOTORPH PAYROLL SUMMARY        ");
        System.out.println("========================================");
        System.out.println("Employee #: " + empNo);
        System.out.println("Employee Name: " + fName + " " + lName);
        System.out.println("Cutoff Date: " + getMonthName(month) + " " + start + " to " + end);
        System.out.println("Total Hours Worked: " + hours);
        System.out.println("Gross Salary: " + gross);
        System.out.println("========================================");

        if (isSecondCutoff) {
            // Requirement 5: Add 1st and 2nd cutoff amounts first before computing deductions
            double firstCutoffHours = getTotalHoursForPeriod(empNo, month, 1, 15);
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
            System.out.println(" ");
        } else {
            System.out.println("Net Salary: " + gross);
            System.out.println(" ");
        }
    }

    private static void showPayrollStaffComputeTable(
            String empNo,
            String fName,
            String lName,
            double hourlyRate,
            Map<String, List<String[]>> attendanceData,
            String inputEmployeeNumber) {
        if (hourlyRate <= 0) {
            System.out.println("Invalid hourly rate for employee " + empNo);
            return;
        }
        String[] months = {"06", "07", "08", "09", "10", "11", "12"};

        for (String month : months) {
            // Cutoff 1: 1st to 15th
            calculateAndDisplayCutoff(empNo, fName, lName, month, 1, 15, hourlyRate, false);
            // Cutoff 2: 16th to end (Includes deductions)
            calculateAndDisplayCutoff(empNo, fName, lName, month, 16, 31, hourlyRate, true);
        }
    }

    private static String showPayrollStaffSubMenuOneEmployee(Scanner scanner) {
        System.out.print("Enter Employee #: ");
        String inputOption = scanner.nextLine();
        return inputOption;
    }

    private static String showPayrollStaffSubMenu(Scanner scanner) {
        System.out.println("--- Process Payroll (No Allowances) ---");
        System.out.println("Choose an option:");
        System.out.println("a. One employee");
        System.out.println("b. All employee");
        System.out.println("c. Exit the program");
        System.out.print("Choice: ");
        String inputOption = scanner.nextLine();
        return inputOption;
    }

    private static String showPayrollStaffMenu(Scanner scanner) {
        System.out.println("--- Payroll Staff Display Option ---");
        System.out.println("Choose an option:");
        System.out.println("a. Process payroll");
        System.out.println("b. Exit the program");
        System.out.print("Choice: ");
        String inputOption = scanner.nextLine();
        return inputOption;
    }

    private static String[] splitCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private static void loadEmployeeData() {
        // initialize csv reader for employee
        try (BufferedReader reader = new BufferedReader(new FileReader(employeeDetailsFilePath))) {
            // Skip header row
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsvLine(line);

                ArrayList<String> details = new ArrayList<>();
                details.add(parts[0].trim()); // id
                details.add(parts[2].trim()); // fname
                details.add(parts[1].trim()); // lname
                details.add(parts[3].trim()); // birthday
                details.add(parts[13].trim()); // Basic Salary
                details.add(parts[17].trim()); // Gross Semi-monthly Rate
                details.add(parts[18].trim()); // Hourly Rate

                employeeData.put(parts[0].trim(), details);
            }

        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }

    private static void loadAttendanceData() {
        // initialize csv reader for employee attedance
        try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFilePath))) {
            // Skip header row
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsvLine(line);

                String employeeNumber = parts[0].trim();
                String timeIn = parts[4].trim();
                String timeOut = parts[5].trim();
                String date = parts[3].trim();

                // only include attendance rows for known employee IDs
                if (employeeData.containsKey(employeeNumber)) {

                    attendanceData
                            // computeIfAbsent
                            //  - get the employee’s list if already present
                            //  - otherwise create a new empty list for that employee
                            .computeIfAbsent(employeeNumber, k -> new ArrayList<>())
                            // appends the array
                            .add(new String[] {date, timeIn, timeOut});
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }
}
