package org.example.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.example.model.AttendanceRecordModel;
import org.example.model.EmployeeModel;

public class ReadFromTextFile {

    // data layer
    public HashMap<String, EmployeeModel> getEmployeeData(String filePath) {

        HashMap<String, EmployeeModel> employeeData = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            // skip header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                // create employee object
                EmployeeModel emp = new EmployeeModel(
                        parts[0], // ID
                        parts[1], // Last Name
                        parts[2], // First Name
                        parts[3], // Birthday
                        parts[4], // Address
                        parts[5], // Phone
                        parts[6], // SSS
                        parts[7], // Philhealth
                        parts[8], // TIN
                        parts[9], // Pagibig
                        parts[10], // Status
                        parts[11], // Position
                        parts[12], // Immediate Supervisor
                        parts[13], // Basic Salary
                        parts[14], // Rice Subsidy
                        parts[15], // Phone Allowance
                        parts[16], // Clothing Allowance
                        parts[17], // Gross SemiMonthly Rate
                        parts[18] // Hourly Rate
                        );

                // store in hashmap
                employeeData.put(parts[0], emp);
            }

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
        }

        return employeeData;
    }

    // data layer
    public HashMap<String, ArrayList<AttendanceRecordModel>> getAttendanceData(
            String filePath, ArrayList<String> allEmployeeNumbers) {

        HashMap<String, ArrayList<AttendanceRecordModel>> attendanceData = new HashMap<>();
        Set<String> validEmployeeNumbers = new HashSet<>(allEmployeeNumbers);

        // create an empty arraylist for each key
        for (String employeeNumber : allEmployeeNumbers) {
            attendanceData.put(employeeNumber, new ArrayList<>());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            // skip header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                String employeeNumber = parts[0].trim();
                String timeIn = parts[4].trim();
                String timeOut = parts[5].trim();
                String date = parts[3].trim();

                // similar to filter method in javascript
                if (validEmployeeNumbers.contains(employeeNumber)) {
                    attendanceData.get(employeeNumber).add(new AttendanceRecordModel(date, timeIn, timeOut));
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
        }

        return attendanceData;
    }
}
