package org.example;

import java.util.Scanner;

public class UserInterface {

    Scanner scanner = new Scanner(System.in);
    StoreApp app = new StoreApp();

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void forceExit() {
        app.changePayrollStaffMenu(false);
        app.changeApp(false);
    }

    private void displayHeader() {
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
            System.out.println("a. Compute payroll (all employees)");
            System.out.println("b. Compute payroll (employee)");
            System.out.println("c. View Details (all employees)");
            System.out.println("d. View Details (employee)");
            System.out.println("e. Import employee records");
            System.out.println("q. Go back");
            System.out.println(":q. Exit");
            System.out.println("Choice: ");

            String optionInput = scanner.nextLine();

            switch (optionInput) {
                case "a":
                    clearScreen();
                    System.out.println("Compute payroll (all employees)");
                    displayHeader();
                    break;

                case "b":
                    clearScreen();
                    System.out.println("Compute payroll (employee)");
                    displayHeader();
                    break;

                case "c":
                    clearScreen();
                    System.out.println("View Details (all employees)");
                    displayHeader();
                    break;

                case "d":
                    clearScreen();
                    System.out.println("View Details (employee)");
                    displayHeader();
                    break;
                case "e":
                    clearScreen();
                    System.out.println("Import employee records");
                    displayHeader();
                    break;
                case "q":
                    clearScreen();
                    // go back
                    app.changePayrollStaffMenu(false);
                    break;
                case ":q":
                    clearScreen();
                    // exit program
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
            System.out.println("a. Compute payroll (all employees)");
            System.out.println("b. Compute payroll (employee)");
            System.out.println("c. View Details (all employees)");
            System.out.println("d. View Details (employee)");
            System.out.println("q. Go back");
            System.out.println(":q. Exit");
            System.out.println("Choice: ");

            String optionInput = scanner.nextLine();

            switch (optionInput) {
                case "a":
                    clearScreen();
                    System.out.println("Compute payroll (all employees)");
                    displayHeader();
                    break;

                case "b":
                    clearScreen();
                    System.out.println("Compute payroll (employee)");
                    displayHeader();
                    break;

                case "c":
                    clearScreen();
                    System.out.println("View Details (all employees)");
                    displayHeader();
                    break;

                case "d":
                    clearScreen();
                    System.out.println("View Details (employee)");
                    displayHeader();
                    break;
                case "q":
                    clearScreen();
                    // go back
                    app.changeEmployeeMenu(false);
                    break;
                case ":q":
                    clearScreen();
                    // exit program
                    forceExit();
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public void displayMainMenu() {
        while (app.isAppRunning()) {
            clearScreen();
            System.out.println("\nEnter your role:");
            System.out.println("a. Payroll staff");
            System.out.println("b. Employee");
            System.out.println(":q. Exit");
            System.out.println("Choice: ");
            String roleInput = scanner.nextLine();

            // reset
            app.changePayrollStaffMenu(true);
            app.changeEmployeeMenu(true);
            app.changePayrollMenu(true);
            app.changeViewDetailsMenu(true);
            app.changeImportEmployeeRecordsMenu(true);
            app.setUserRole("");

            // role menu
            switch (roleInput) {
                case "a":
                    app.setUserRole("payroll staff");
                    clearScreen();
                    displayHeader();
                    displayPayrollStaffMenu();
                    break;
                case "b":
                    app.setUserRole("employee");
                    clearScreen();
                    displayHeader();
                    displayEmployeeMenu();
                    break;
                case ":q":
                    clearScreen();
                    // exit program
                    forceExit();
                    break;
                default:
                    System.out.println("Invalid Role");
            }
        }

        System.out.println("");
        System.out.println("Program exited.");
        System.out.println("");
    }
}
