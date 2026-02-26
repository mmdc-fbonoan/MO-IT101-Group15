package org.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import org.example.model.AttendanceRecordModel;
import org.example.model.EmployeeModel;
import org.example.utils.ReadFromTextFile;
import org.junit.jupiter.api.Test;

public class ReadFromTextFileTest {

    private static String EMPLOYEE_FILE = "public/employee_details.csv";
    private static String ATTENDANCE_FILE = "public/attendance_record.csv";

    // sucessful tests
    @Test
    void validPathReturnsData() {
        ReadFromTextFile reader = new ReadFromTextFile();
        HashMap<String, EmployeeModel> employees = reader.getEmployeeData(EMPLOYEE_FILE);
        HashMap<String, ArrayList<AttendanceRecordModel>> attendance =
                reader.getAttendanceData(ATTENDANCE_FILE, new ArrayList<>(employees.keySet()));

        assertFalse(attendance.isEmpty());
        assertFalse(employees.isEmpty());
    }

    // invalid tests
    @Test
    void invalidPathReturnsEmptyMap() {
        ReadFromTextFile reader = new ReadFromTextFile();
        HashMap<String, EmployeeModel> employees = reader.getEmployeeData("public/sample.csv");

        assertTrue(employees.isEmpty());
    }

    @Test
    void invalidPathReturnsPreinitializedEmptyLists() {
        ReadFromTextFile reader = new ReadFromTextFile();
        HashMap<String, EmployeeModel> employees = reader.getEmployeeData(EMPLOYEE_FILE);
        ArrayList<String> employeeNumbers = new ArrayList<>(employees.keySet());

        HashMap<String, ArrayList<AttendanceRecordModel>> attendance =
                reader.getAttendanceData("public/sample.csv", employeeNumbers);

        assertTrue(attendance.values().stream().allMatch(ArrayList::isEmpty));
    }
}
