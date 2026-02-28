package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        // File paths and data structures
        String attendanceFilePath = "public/attendance_record.csv";
        String employeeDetailsFilePath = "public/employee_details.csv";
        boolean isAttendanceFileValid = false;
        boolean isEmployeeDetailsFileValid = false;
        HashMap<String, ArrayList> employeeData = new HashMap<>();
        Map<String, List<String[]>> attendanceData = new HashMap<>();
        ArrayList<String> allEmployeeNumbers = new ArrayList<>();

        // initialization for Scanner
        Scanner scanner = new Scanner(System.in);

        // start ui
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // login
        System.out.println("Please Login to continue");
        System.out.println("Username: ");
        String inputUsername = scanner.nextLine();
        System.out.println("Password: ");
        String inputPassword = scanner.nextLine();

        // guard clause
        if (inputUsername.equals("") && inputPassword.equals("")) {
            System.out.println("------------------------------");
            System.out.println("Incorrect username and/or passowrd");
            System.out.println("------------------------------");
        }

        // initialize csv reader for employee
        try (BufferedReader reader = new BufferedReader(new FileReader(employeeDetailsFilePath))) {
            // Skip header row
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                ArrayList<String> details = new ArrayList<>();
                details.add(parts[0].trim()); // id
                details.add(parts[1].trim()); // fname
                details.add(parts[2].trim()); // lname
                details.add(parts[3].trim()); // birthday

                employeeData.put(parts[0].trim(), details);
            }

            System.out.println("Loaded employees: " + employeeData.size());
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
        }

        // create an array of employee number that has no data
        for (String employeeNumber : employeeData.keySet()) {
            allEmployeeNumbers.add(employeeNumber);
            attendanceData.put(employeeNumber, new ArrayList<>());
        }

        // initialize csv reader for employee attedance
        try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFilePath))) {
            // Skip header row
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String employeeNumber = parts[0].trim();
                String timeIn = parts[4].trim();
                String timeOut = parts[5].trim();
                String date = parts[3].trim();

                // check the allEmployeeNumbers if its same with employeeNumber
                if (allEmployeeNumbers.contains(employeeNumber)) {

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

        // convert into not hex
        // System.out.println(employeeData.size());
        // System.out.println(
        //     employeeData.entrySet().stream()
        //         .map(e -> e.getKey() + "=" + e.getValue())
        //         .toList()
        // );

        // System.out.println(attendanceData.size());
        // System.out.println(attendanceData.entrySet().stream()
        //         .map(e -> e.getKey() + "="
        //                 + e.getValue().stream().map(Arrays::toString).toList())
        //         .toList());

        // employee flow
        if (inputUsername.equals("employee") && inputPassword.equals("12345")) {
            // clear screen
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("Choose an option:");
            System.out.println("a. Enter your employee number");
            System.out.println("b. Exit the program");
            System.out.println("Choice: ");

            String inputOption = scanner.nextLine();

            //
            if (inputOption.equals("")) {
                System.out.println("Invalid option");
            }
            if (inputOption.equals("a")) {
                // clear screen
                System.out.print("\033[H\033[2J");
                System.out.flush();

                System.out.println("Please enter your employee number: ");
                String inputEmployeeNumber = scanner.nextLine();

                ArrayList<String> data = employeeData.get(inputEmployeeNumber);

                // guard clause
                if (data == null) {
                    // clear screen
                    System.out.print("\033[H\033[2J");
                    System.out.flush();

                    System.out.println("------------------------------");
                    System.out.println("Employee number does not exist.");
                    System.out.println("------------------------------");
                } else {
                    // clear screen
                    System.out.print("\033[H\033[2J");
                    System.out.flush();

                    String id = data.get(0);
                    String firstName = data.get(1);
                    String lastName = data.get(2);
                    String birthday = data.get(3);

                    String[] headers = {"Employee #", "Name", "Birthday"};

                    Object[][] details = {
                        {id, firstName + " " + lastName, birthday},
                    };

                    System.out.format("%-11s%-20s%-11s%n", (Object[]) headers);

                    System.out.format("%-11s%-20s%-11s%n", details[0]);
                }
            }

            if ("b".equals(inputOption)) {
                System.out.println("------------------------------");
                System.out.println("Program exited.");
                System.out.println("------------------------------");
                System.exit(0);
            }
        }

        // payroll staff flow
        // if (inputUsername.equals("payroll_staff") && inputPassword.equals("12345")) {
        //     // clear screen
        //     System.out.print("\033[H\033[2J");
        //     System.out.flush();

        //     System.out.println("Choose an option:");
        //     System.out.println("a. Process Payroll");
        //     System.out.println("Choice: ");

        //     String inputOption = scanner.nextLine();
        // }

        System.out.println("------------------------------");
        System.out.println("Program exited.");
        System.out.println("------------------------------");
    }
}
