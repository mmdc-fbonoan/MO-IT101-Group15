package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserInterfaceTest {

    // - **Arrange**: setup data and objects
    // - **Act**: call the method
    // - **Assert**: verify result

    // sucessful login tests
    @Test
    void validPayrollStaffSetsRole() {
        UserInterface ui = new UserInterface();
        ui.checkUserCredentials("payroll_staff", "12345");
        assertEquals("payroll_staff", ui.app.getUserRole());
    }

    @Test
    void validEmployeeSetsRole() {
        UserInterface ui = new UserInterface();
        ui.checkUserCredentials("employee", "12345");
        assertEquals("employee", ui.app.getUserRole());
    }

    // invalid login tests
    @Test
    void invalidCredentialsClearsRoleAndStopsApp() {
        UserInterface ui = new UserInterface();
        ui.checkUserCredentials("bad", "bad");
        assertEquals("", ui.app.getUserRole());
        assertFalse(ui.app.isAppRunning());
    }
}
