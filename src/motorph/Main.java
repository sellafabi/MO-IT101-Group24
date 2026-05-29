package motorph;
import motorph.menu.MenuService;
import motorph.authenticator.AuthService;
import motorph.data.CSVService;

import motorph.gui.LoginFrame;


import java.util.List;
import java.util.Scanner;


public class Main {


    // Shared Scanner instance used across all methods for user input
    static Scanner sc = new Scanner(System.in);

    // File paths, usernames, and password — update here if any value changes

    static final String EMP_USERNAME     = "employee";
    static final String PAYROLL_USERNAME = "payroll_staff";
    static final String PASSWORD         = "12345";


    /*========================================================================================
        Main Method (Method #1) [rosella]
    ==========================================================================================*/

        /**
         * Entry point of the program.
         *
         * Algorithm:
         * 1. Loads SSS and Pag-IBIG bracket tables from CSV files into memory.
         * 2. Prompts the user to log in and returns their role.
         * 3. Routes to the correct menu based on role — employee or payroll staff.
         * 4. Closes the shared Scanner once the menu exits.
         *
         * @param args command-line arguments (not used)
         */
    public static void main(String[] args) {

        // List<String[]> sssTable = CSVService.SSSTable();
        // List<String[]> pagibigTable = CSVService.pagibigTable();
        
        // String role = AuthService.handleLogin();
        

        // if (role.equals(EMP_USERNAME)) {
        //     MenuService.employeeMenu();
        // } else if (role.equals(PAYROLL_USERNAME)) {
        //     MenuService.payrollMenu(sssTable, pagibigTable);
        // }

        new LoginFrame();
    }
}