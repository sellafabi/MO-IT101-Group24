package motorph;
import motorph.menu.MenuService;
import motorph.authenticator.AuthService;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {


    // Shared Scanner instance used across all methods for user input
    static Scanner sc = new Scanner(System.in);

    // File paths, usernames, and password — update here if any value changes
    static final String EMP_FILE         = "src/details.csv";
    static final String ATT_FILE         = "src/attendance.csv";
    static final String SSS_FILE         = "src/sss.csv";
    static final String PAGIBIG_FILE     = "src/pagibig.csv";
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

        List<String[]> sssTable = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(SSS_FILE))) {

            br.readLine();
            String sssLine;

            while ((sssLine = br.readLine()) != null) {
                if (!sssLine.trim().isEmpty()) {

                    // Split CSV row while handling quoted values
                    sssTable.add(sssLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                }
            }

        } catch (IOException e) {
            System.out.println("Error: SSS table file not found. Check that sss.csv exists in src/.");
        }

        List<String[]> pagibigTable = new ArrayList<>();
        // Load Pag-IBIG contribution table
        try (BufferedReader br = new BufferedReader(new FileReader(PAGIBIG_FILE))) {

            br.readLine();
            String pagibigLine;

            while ((pagibigLine = br.readLine()) != null) {
                if (!pagibigLine.trim().isEmpty()) {

                    // Split CSV row while handling quoted values
                    pagibigTable.add(pagibigLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error: Pag-IBIG table file not found. Check that pagibig.csv exists in src/.");
        }
        
        String role = AuthService.handleLogin();
        

        if (role.equals(EMP_USERNAME)) {
            MenuService.employeeMenu();
        } else if (role.equals(PAYROLL_USERNAME)) {
            MenuService.payrollMenu(sssTable, pagibigTable);
        }

        sc.close();
    }
}