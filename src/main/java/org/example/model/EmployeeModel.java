package org.example.model;

public class EmployeeModel {
    private String employeeId;
    private String employeeLastName;
    private String employeeFirstName;
    private String employeeBirthday;
    private String employeeAddress;
    private String employeePhoneNumber;
    private String employeeSSS;
    private String employeePhilhealth;
    private String employeeTin;
    private String employeePagibig;
    private String employeeStatus;
    private String employeePosition;
    private String employeeImmediateSupervisor;
    private String employeeBasicSalary;
    private String employeeRiceSubsidy;
    private String employeePhoneAllowance;
    private String employeeClothingAllowance;
    private String employeeGrossSemiMonthlyRate;
    private String employeeHourlyRate;

    // constructor
    public EmployeeModel(
            String employeeId,
            String employeeLastName,
            String employeeFirstName,
            String employeeBirthday,
            String employeeAddress,
            String employeePhoneNumber,
            String employeeSSS,
            String employeePhilhealth,
            String employeeTin,
            String employeePagibig,
            String employeeStatus,
            String employeePosition,
            String employeeImmediateSupervisor,
            String employeeBasicSalary,
            String employeeRiceSubsidy,
            String employeePhoneAllowance,
            String employeeClothingAllowance,
            String employeeGrossSemiMonthlyRate,
            String employeeHourlyRate) {
        this.employeeId = employeeId;
        this.employeeLastName = employeeLastName;
        this.employeeFirstName = employeeFirstName;
        this.employeeBirthday = employeeBirthday;
        this.employeeAddress = employeeAddress;
        this.employeePhoneNumber = employeePhoneNumber;
        this.employeeSSS = employeeSSS;
        this.employeePhilhealth = employeePhilhealth;
        this.employeeTin = employeeTin;
        this.employeePagibig = employeePagibig;
        this.employeeStatus = employeeStatus;
        this.employeePosition = employeePosition;
        this.employeeImmediateSupervisor = employeeImmediateSupervisor;
        this.employeeBasicSalary = employeeBasicSalary;
        this.employeeRiceSubsidy = employeeRiceSubsidy;
        this.employeePhoneAllowance = employeePhoneAllowance;
        this.employeeClothingAllowance = employeeClothingAllowance;
        this.employeeGrossSemiMonthlyRate = employeeGrossSemiMonthlyRate;
        this.employeeHourlyRate = employeeHourlyRate;
    }

    // getters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public String getEmployeeBirthday() {
        return employeeBirthday;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public String getEmployeePhoneNumber() {
        return employeePhoneNumber;
    }

    public String getEmployeeSSS() {
        return employeeSSS;
    }

    public String getEmployeePhilhealth() {
        return employeePhilhealth;
    }

    public String getEmployeeTin() {
        return employeeTin;
    }

    public String getEmployeePagibig() {
        return employeePagibig;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public String getEmployeeImmediateSupervisor() {
        return employeeImmediateSupervisor;
    }

    public String getEmployeeBasicSalary() {
        return employeeBasicSalary;
    }

    public String getEmployeeRiceSubsidy() {
        return employeeRiceSubsidy;
    }

    public String getEmployeePhoneAllowance() {
        return employeePhoneAllowance;
    }

    public String getEmployeeClothingAllowance() {
        return employeeClothingAllowance;
    }

    public String getEmployeeGrossSemiMonthlyRate() {
        return employeeGrossSemiMonthlyRate;
    }

    public String getEmployeeHourlyRate() {
        return employeeHourlyRate;
    }

    @Override
    public String toString() {
        return "{employeeId: \"" + employeeId + "\", employeeLastName: \"" + employeeLastName
                + "\", employeeFirstName: \"" + employeeFirstName + "\"}";
    }
}
