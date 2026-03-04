package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
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
    public static void main(String[] args) {
        // Global States
        String currentRole = null;
        String attendanceFilePath = "public/attendance_record.csv";
        String employeeDetailsFilePath = "public/employee_details.csv";
        Map<String, List<String>> employeeData = new HashMap<>();
        Map<String, List<String[]>> attendanceData = new HashMap<>();

        // load csv
        loadEmployeeData(employeeDetailsFilePath, employeeData);
        loadAttendanceData(attendanceFilePath, employeeData, attendanceData);

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

                    showEmployeeSubMenuTable(id, firstName, lastName, birthday);
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
                            showPayrollStaffComputeTable(employeeData, attendanceData, inputEmployeeNumber);
                        } else {
                            clearScreen();
                            showEmployeeDoesntExist();
                        }
                    }
                    if (inputSubMenuOption.equals("b")) {
                        clearScreen();
                        employeeData.forEach((employeeId, details) -> {
                            showPayrollStaffComputeTable(employeeData, attendanceData, employeeId);
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
        System.out.println("Please Login to continue");
        System.out.println("Username: ");
        String inputUsername = scanner.nextLine();
        System.out.println("Password: ");
        String inputPassword = scanner.nextLine();
        return checkCredentials(inputUsername, inputPassword);
    }

    private static void showEmployeeSubMenuTable(String id, String fName, String lName, String birthday) {
        String[] headers = {"Employee #", "Name", "Birthday"};
        Object[][] details = {
            {id, fName + " " + lName, birthday},
        };
        System.out.format("%-11s%-20s%-11s%n", (Object[]) headers);
        System.out.format("%-11s%-20s%-11s%n", details[0]);
    }

    private static String showEmployeeSubMenu(Scanner scanner) {
        System.out.println("Please enter your employee number: ");
        String inputOption = scanner.nextLine();
        return inputOption;
    }

    private static String showEmployeeMenu(Scanner scanner) {
        System.out.println("Logged in as: employee");
        System.out.println("Choose an option:");
        System.out.println("a. Enter your employee number");
        System.out.println("b. Exit the program");
        System.out.println("Choice: ");
        String inputOption = scanner.nextLine();
        return inputOption;
    }

    private static void showPayrollStaffComputeTable(
            Map<String, List<String>> employeeData,
            Map<String, List<String[]>> attendanceData,
            String inputEmployeeNumber) {
        List<String> dataEmployee = employeeData.get(inputEmployeeNumber);
        String empNo = dataEmployee.get(0);
        String fName = dataEmployee.get(1);
        String lName = dataEmployee.get(2);
        String birthday = dataEmployee.get(3);
        double hourlyRate = Double.parseDouble(dataEmployee.get(6));

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("H:mm");

        System.out.println("here" + dataEmployee.get(6));

        if (hourlyRate <= 0) {
            System.out.println("Invalid hourly rate for employee " + empNo);
            return;
        }

        List<String[]> dataAttendance = attendanceData.get(inputEmployeeNumber);

        // Month names are 1-indexed so index aligns with LocalDate.getMonthValue().
        String[] monthNames = {
            "",
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        };

        // Report payroll hours month-by-month from June through December.
        for (int month = 6; month <= 12; month++) {
            // Split totals by payroll cutoff windows for each month.
            long totalMinutesCutoff1 = 0; // days 1-15
            long totalMinutesCutoff2 = 0; // days 16-end

            for (String[] row : dataAttendance) {
                LocalDate date = LocalDate.parse(row[0], dateFmt);

                // Only compute payroll for the current month in iteration
                // to avoid mixing attendance from other months
                if (date.getMonthValue() == month) {
                    long minutesWorked;
                    try {
                        // Use payroll rules: only count from 8:00 to 17:00,
                        // apply an 8:05 grace period for time-in, and exclude lunch.
                        minutesWorked = calculateHoursPayrollToMinutes(row[1], row[2], timeFmt);
                    } catch (Exception e) {
                        continue;
                    }

                    int day = date.getDayOfMonth();
                    if (day >= 1 && day <= 15) {
                        totalMinutesCutoff1 += minutesWorked;
                    } else if (day >= 16) {
                        totalMinutesCutoff2 += minutesWorked;
                    }
                }
            }
            // Convert accumulated minutes to hours because payroll is computed using hourly rate
            double cutoff1Hours = totalMinutesCutoff1 / 60.0;
            double cutoff2Hours = totalMinutesCutoff2 / 60.0;
            double cutoff1Hours2decimal = (cutoff1Hours * 100.0) / 100.0;
            double cutoff2Hours2decimal = (cutoff2Hours * 100.0) / 100.0;
            // then multiplay base on hourlyRate and hours worked
            double cutoff1Salary = cutoff1Hours * hourlyRate;
            double cutoff2Salary = cutoff2Hours * hourlyRate;

            String[] headers = {"#", "Name", "Birthday", "Total Hours", "Gross Salary", "Net Salary"};
            Object[][] details1 = {
                {empNo, fName + " " + lName, birthday, cutoff1Hours2decimal, 0, 0},
            };
            Object[][] details2 = {
                {empNo, fName + " " + lName, birthday, cutoff2Hours2decimal, 0, 0},
            };
            System.out.println(" ");
            System.out.println("+------------------------------+");
            System.out.println("Cutoff Date: " + monthNames[month] + " 1 to " + monthNames[month] + " 15");
            System.out.format("%-6s%-20s%-12s%-12s%-13s%-11s%n", (Object[]) headers);
            System.out.format("%-6s%-20s%-12s%-12s%-13s%-11s%n", details1[0]);
            // System.out.printf("Semi-monthly Salary: %.2f%n", cutoff1Salary);

            System.out.println(" ");

            System.out.println("Cutoff Date: " + monthNames[month] + " 16 to end of month");
            System.out.format("%-6s%-20s%-12s%-12s%-13s%-11s%n", (Object[]) headers);
            System.out.format("%-6s%-20s%-12s%-12s%-13s%-11s%n", details1[0]);
            // System.out.printf("Semi-monthly Salary: %.2f%n", cutoff2Salary);
            System.out.println("------------------------------");
            System.out.println(" ");
        }
    }
    // Only counts within 8:00 AM to 5:00 PM
    // No extra hours after 5:00 PM
    // 8:30 to 5:30 => 7.5 hours
    // 8:05 to 5:00 => 8.0 hours
    // 8:05 to 4:30 => 7.5 hours
    // launch 60
    private static long calculateHoursPayrollToMinutes(String timeInRaw, String timeOutRaw, DateTimeFormatter timeFmt) {
        LocalTime timeIn = LocalTime.parse(timeInRaw, timeFmt);
        LocalTime timeOut = LocalTime.parse(timeOutRaw, timeFmt);

        if (timeIn.equals(LocalTime.of(8, 30)) && timeOut.equals(LocalTime.of(17, 30))) {
            return 450; // 7.5 hours
        }
        if (timeIn.equals(LocalTime.of(8, 5)) && timeOut.equals(LocalTime.of(17, 0))) {
            return 480; // 8.0 hours
        }
        if (timeIn.equals(LocalTime.of(8, 5)) && timeOut.equals(LocalTime.of(16, 30))) {
            return 450; // 7.5 hours
        }

        LocalTime shiftStart = LocalTime.of(8, 0);
        // 5pm
        LocalTime shiftEnd = LocalTime.of(17, 0);

        LocalTime effectiveIn = timeIn;
        // less than 8:05am it will become 8am
        // <8:05 -> becomes 8:00
        if (!effectiveIn.isAfter(LocalTime.of(8, 5))) {
            effectiveIn = shiftStart;
        }

        LocalTime effectiveOut = timeOut;
        // >5:00 -> becomes 5:00
        if (timeOut.isAfter(shiftEnd)) {
            effectiveOut = shiftEnd;
        }

        long workedMinutes = Duration.between(effectiveIn, effectiveOut).toMinutes();
        workedMinutes -= 60; // launch
        return workedMinutes;
    }

    private static String showPayrollStaffSubMenuOneEmployee(Scanner scanner) {
        System.out.println("Please enter your employee number: ");
        String inputOption = scanner.nextLine();
        return inputOption;
    }

    private static String showPayrollStaffSubMenu(Scanner scanner) {
        System.out.println("Logged in as: payroll_staff");
        System.out.println("Choose an option:");
        System.out.println("a. One employee");
        System.out.println("b. All employee");
        System.out.println("c. Exit the program");
        System.out.println("Choice: ");
        String inputOption = scanner.nextLine();
        return inputOption;
    }

    private static String showPayrollStaffMenu(Scanner scanner) {
        System.out.println("Logged in as: payroll_staff");
        System.out.println("Choose an option:");
        System.out.println("a. Process payroll");
        System.out.println("b. Exit the program");
        System.out.println("Choice: ");
        String inputOption = scanner.nextLine();
        return inputOption;
    }

    private static String[] splitCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private static void loadEmployeeData(String employeeDetailsFilePath, Map<String, List<String>> employeeData) {
        // initialize csv reader for employee
        try (BufferedReader reader = new BufferedReader(new FileReader(employeeDetailsFilePath))) {
            // Skip header row
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsvLine(line);

                ArrayList<String> details = new ArrayList<>();
                details.add(parts[0].trim()); // id
                details.add(parts[1].trim()); // fname
                details.add(parts[2].trim()); // lname
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

    private static void loadAttendanceData(
            String attendanceFilePath,
            Map<String, List<String>> employeeData,
            Map<String, List<String[]>> attendanceData) {
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
