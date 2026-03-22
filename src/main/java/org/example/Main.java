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

public class Main {

    static String empFile = "public/employee_details.csv";
    static String attendanceFile = "public/attendance_record.csv";
    static Map<String, List<String>> employeeData = new HashMap<>();
    static Map<String, List<String[]>> attendanceData = new HashMap<>();

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
            initData();
            handleEmployeeMenu();
        } else if ("payroll_staff".equals(username) && "12345".equals(password)) {
            initData();
            handlePayrollStaffMenu();
        } else {
            System.out.println("Incorrect username and/or password.");
            System.exit(0);
        }
        scanner.close();
    }

    /**
     * This method call 2 method to store csv data to hashmap
     */
    private static void initData() {
        loadEmployeeData();
        loadAttendanceData();
    }

    /**
     * Displays the employee menu and allows the user to view basic employee details
     * by entering an employee number or exit the program.
     */
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

    /**
     * Displays the payroll staff menu and allows processing of payroll
     * or exiting the program.
     */
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

    /**
     * Displays payroll processing options such as processing a single employee
     * or all employees.
     */
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

    /**
     * Displays basic employee details such as employee number, name, and birthday.
     *
     * @param empNum the employee number used to retrieve employee data
     */
    private static void displayEmployeeBasicDetails(String empNum) {
        String[] empData = findEmployeeRecord(empNum);
        if (empData != null) {
            String employeeNumber = empData[0];
            String lastName = empData[1];
            String firstName = empData[2];
            String birthday = empData[3];

            System.out.println("========================================");
            System.out.println("         MOTORPH EMPLOYEE INFORMATION       ");
            System.out.println("========================================");
            System.out.println("Employee Number: " + employeeNumber);
            System.out.println("Employee Name: " + firstName + " " + lastName);
            System.out.println("Birthday: " + birthday);
            System.out.println("========================================");
        } else {
            System.out.println("Employee number does not exist.");
        }
    }
    /**
     * Processes the full payroll for a specific employee across all months
     * from June to December, including both cutoffs.
     *
     * @param empNum the employee number to process payroll for
     */
    private static void processFullPayroll(String empNum) {
        String[] empData = findEmployeeRecord(empNum);
        if (empData == null) {
            System.out.println("Employee number does not exist.");
            return;
        }

        String employeeNumber = empData[0];
        String lastName = empData[1];
        String firstName = empData[2];
        double hourlyRate = Double.parseDouble(empData[18]);
        String[] months = {"06", "07", "08", "09", "10", "11", "12"}; // June to December

        for (String month : months) {
            // Cutoff 1: 1st to 15th
            calculateAndDisplayCutoff(employeeNumber, firstName, lastName, month, 1, 15, hourlyRate, false);
            // Cutoff 2: 16th to end (Includes deductions)
            calculateAndDisplayCutoff(employeeNumber, firstName, lastName, month, 16, 31, hourlyRate, true);
        }
    }
    /**
     * Calculates and displays payroll details for a given cutoff period,
     * including gross salary and deductions if applicable.
     *
     * @param employeeNumber the employee number
     * @param firstName the employee's first name
     * @param lastName the employee's last name
     * @param month the month in MM format
     * @param start the starting day of the cutoff period
     * @param end the ending day of the cutoff period
     * @param rate the hourly rate of the employee
     * @param isSecondCutoff indicates if deductions should be applied
     */
    private static void calculateAndDisplayCutoff(
            String employeeNumber,
            String firstName,
            String lastName,
            String month,
            int start,
            int end,
            double rate,
            boolean isSecondCutoff) {
        double hours = getTotalHoursForPeriod(employeeNumber, month, start, end);
        double gross = hours * rate;

        // --- OUTPUT FORMATTING ---
        System.out.println("========================================");
        System.out.println("         MOTORPH PAYROLL SUMMARY        ");
        System.out.println("========================================");
        // System.out.println("\n----------------------------------------");
        System.out.println("Employee #: " + employeeNumber);
        System.out.println("Employee Name: " + firstName + " " + lastName);
        System.out.println("Cutoff Date: " + getMonthName(month) + " " + start + " to " + end);
        System.out.println("Total Hours Worked: " + hours);
        System.out.println("Gross Salary: " + gross);
        System.out.println("========================================");

        if (isSecondCutoff) {
            // Requirement 5: Add 1st and 2nd cutoff amounts first before computing deductions
            double firstCutoffHours = getTotalHoursForPeriod(employeeNumber, month, 1, 15);
            double monthlyGross = (firstCutoffHours + hours) * rate;

            double sss = computeSSS(monthlyGross);
            double ph = computePhilHealth(monthlyGross);
            double pi = computePagIBIG(monthlyGross);
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

    /**
     * Computes the total hours worked by an employee within a given date range.
     *
     * @param empNum the employee number
     * @param month the month in MM format
     * @param start the starting day of the period
     * @param end the ending day of the period
     * @return total hours worked within the specified period
     */
    private static double getTotalHoursForPeriod(String empNum, String month, int start, int end) {
        double total = 0;
        List<String[]> records = attendanceData.get(empNum);
        if (records == null) {
            return 0;
        }

        try {
            for (String[] record : records) {
                String[] dateParts = record[0].split("/"); // Expected format MM/DD/YYYY
                int dDay = Integer.parseInt(dateParts[1]);

                if (dateParts[0].equals(month) && dDay >= start && dDay <= end) {
                    LocalTime login = LocalTime.parse(record[1], timeFormat);
                    LocalTime logout = LocalTime.parse(record[2], timeFormat);
                    total += computeWorkHours(login, logout);
                }
            }
        } catch (Exception e) {
            System.out.println("File error: " + e.getMessage());
        }
        return total;
    }
    /**
     * Computes the number of work hours between login and logout times,
     * applying company rules such as working hours limits, grace period,
     * and lunch break deduction.
     *
     * @param login the login time
     * @param logout the logout time
     * @return total computed work hours
     */
    private static double computeWorkHours(LocalTime login, LocalTime logout) {
        // Logic 4a: Only count 8:00 AM to 5:00 PM
        LocalTime startLimit = LocalTime.of(8, 0);
        LocalTime endLimit = LocalTime.of(17, 0);

        // grace period, 10 min
        LocalTime graceLimit = LocalTime.of(8, 10);

        // Early login → normalize to 8:00
        if (login.isBefore(startLimit)) {
            login = startLimit;
        }
        // Within grace period → also normalize to 8:00
        else if (!login.isAfter(graceLimit)) {
            login = startLimit;
        }

        // Adjust logout
        if (logout.isAfter(endLimit)) {
            logout = endLimit;
        }

        // Prevent negative value
        if (logout.isBefore(login)) {
            return 0;
        }

        // Logic 4b, c, d: Handle specific login/logout durations
        long mins = Duration.between(login, logout).toMinutes();
        if (mins > 60) {
            mins -= 60; // Standard lunch break
        }

        return mins / 60.0;
    }

    /**
     * Finds and retrieves the employee record using the given employee number.
     *
     * @param empNum the employee number used as the key
     * @return an array containing employee details, or null if not found
     */
    private static String[] findEmployeeRecord(String empNum) {
        List<String> data = employeeData.get(empNum);
        if (data == null) {
            return null;
        }
        return data.toArray(new String[0]);
    }
    /**
     * Processes payroll for all employees stored in the employeeData map.
     * Iterates through each employee and computes their full payroll.
     */
    private static void processAllEmployees() {
        for (String empNum : employeeData.keySet()) {
            processFullPayroll(empNum);
        }
    }

    /**
     * Computes the SSS contribution based on the employee's gross salary
     * using predefined salary brackets.
     *
     * @param gross the employee's monthly gross salary
     * @return the corresponding SSS contribution
     */
    public static double computeSSS(double gross) {
        // Salary lower bounds from SSS table
        double[] salaryLimits = {
            0, 3250, 3750, 4250, 4750, 5250, 5750, 6250, 6750, 7250, 7750, 8250, 8750, 9250, 9750, 10250, 10750, 11250,
            11750, 12250, 12750, 13250, 13750, 14250, 14750, 15250, 15750, 16250, 16750, 17250, 17750, 18250, 18750,
            19250, 19750, 20250, 20750, 21250, 21750, 22250, 22750, 23250, 23750, 24250, 24750,
        };

        // Corresponding contributions from SSS table
        double[] contributions = {
            135.0, 157.5, 180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5, 360.0, 382.5, 405.0, 427.5, 450.0,
            472.5, 495.0, 517.5, 540.0, 562.5, 585.0, 607.5, 630.0, 652.5, 675.0, 697.5, 720.0, 742.5, 765.0, 787.5,
            810.0, 832.5, 855.0, 877.5, 900.0, 922.5, 945.0, 967.5, 990.0, 1012.5, 1035.0, 1057.5, 1080.0, 1102.5,
            1125.0,
        };

        // Iterate backwards to find which bracket the gross salary falls into
        for (int i = salaryLimits.length - 1; i >= 0; i--) {
            if (gross >= salaryLimits[i]) {
                return contributions[i];
            }
        }
        return 135.0; // Default minimum
    }
    /**
     * Computes the PhilHealth contribution based on the employee's gross salary,
     * applying minimum and maximum limits.
     *
     * @param gross the employee's monthly gross salary
     * @return the employee's share of PhilHealth contribution
     */
    public static double computePhilHealth(double gross) {
        double prem = (gross <= 10000) ? 300.0 : (gross >= 60000) ? 1800.0 : gross * 0.03;
        return prem / 2;
    }
    /**
     * Computes the Pag-IBIG contribution based on the employee's gross salary,
     * with a maximum contribution cap.
     *
     * @param gross the employee's monthly gross salary
     * @return the Pag-IBIG contribution
     */
    public static double computePagIBIG(double gross) {

        double contribution;

        if (gross <= 1500) {
            contribution = gross * 0.01;
        } else {
            contribution = gross * 0.02;
        }

        // Maximum employee contribution
        if (contribution > 100) {
            contribution = 100;
        }

        return contribution;
    }
    /**
     * Computes the income tax based on the employee's taxable income
     * using progressive tax brackets.
     *
     * @param taxable the taxable income after deductions
     * @return the computed income tax
     */
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
    /**
     * Return the name of the month from June to December
     *
     * @param month in MM format
     * @return Month Name
     */
    private static String getMonthName(String month) {
        String[] n = {
            "", "", "", "", "", "", "June", "July", "August", "September", "October", "November", "December",
        };
        return n[Integer.parseInt(month)];
    }

    /**
     * This method help employee csv format into [{key: [employee_number, fname, lname, birthday]}]
     * return type: array[hashmap{key: array[]}]
     */
    private static void loadEmployeeData() {
        // initialize csv reader for employee
        try (BufferedReader reader = new BufferedReader(new FileReader(empFile))) {
            // Skip header row
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsvLine(line);

                List<String> details = new ArrayList<>();
                for (String part : parts) {
                    details.add(part.trim());
                }

                employeeData.put(parts[0].trim(), details);
            }
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }
    }

    /**
     * This method help attedance csv format into [{key = [date, time-in, time-out]}]
     * return type: array[hashmap{key: array[]}]
     */
    private static void loadAttendanceData() {
        // initialize csv reader for employee attedance
        try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFile))) {
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

    /**
     * This method help remove character
     *
     * @param line a single line from the CSV file
     * @return an array of parsed CSV values
     */
    private static String[] splitCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }
}
