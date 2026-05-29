package motorph.menu;
import motorph.report.ReportPrinter;
import motorph.payroll.PayrollService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MenuService {

    // Shared Scanner instance used across all methods for user input
    static Scanner sc = new Scanner(System.in);

    static final String EMP_FILE         = "src/details.csv";

    /*========================================================================================
        Employee Menu (Method #4) [ann]
    =========================================================================================*/

        /**
         * Displays the employee menu and processes selected actions.
         *
         * Algorithm:
         * 1. Displays menu options for the employee.
         * 2. Accepts user input for menu selection.
         * 3. If option 1 is selected:
         *    - Prompts for employee number.
         *    - Searches the employee CSV file for a matching record.
         *    - Displays employee details if found; otherwise shows an error message.
         * 4. If option 2 is selected, exits the program.
         * 5. Displays an error message for invalid input.
         */
    public static void employeeMenu() {

        System.out.println("\n--------------------------------------");
        System.out.println("\n1. Enter you employee number");
        System.out.println("2. Exit program");
        System.out.print("Choose Option: ");
        String option = sc.nextLine();

        // Option 1: View Employee Details
        if (option.equals("1")){

            System.out.print("\nEnter Your Employee Number: ");
            String employeeNo         = sc.nextLine();
                
            // Variables to hold the matched employee's fields
            String  lastName          = "";
            String  firstName         = "";
            String  birthday          = "";
            boolean isEmpDetailsFound = false;
                                
            try (BufferedReader br = new BufferedReader (new FileReader (EMP_FILE))){

                br.readLine();
                String employeeLine;

                while ((employeeLine = br.readLine()) !=null){
                    if(employeeLine.trim().isEmpty()) continue;

                    // Split CSV row while handling quoted values
                    String[] employeeRow = employeeLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                    // Column mapping:
                    // [0] Employee Number | [1] Last Name | [2] First Name | [3] Birthday
                    if (employeeRow[0].equals(employeeNo)){
                        lastName          = employeeRow[1];
                        firstName         = employeeRow[2];
                        birthday          = employeeRow[3];
                        isEmpDetailsFound = true;
                        break; // Stop once match is found
                    }
                }

            } catch (IOException e) {
                System.out.println("\nEmployee file error.\n");
            }
                
            if (isEmpDetailsFound){
                ReportPrinter.printEmployeeInfo (employeeNo, lastName, firstName, birthday);
            } else {
                System.out.println("\nEmployee number does not exist.\n");
            }
                
        // Option 2: Exit program
        } else if (option.equals("2")){
            System.out.println("\nExiting program.\n");
        return;
                
        // Invalid input handling
        } else {
            System.out.println("\nInvalid option. Please enter 1 or 2.\n");
        }
    }

    /*========================================================================================
        Payroll Menu (Method #5) [ann]
    =========================================================================================*/

        /**
         * Displays the payroll staff menu and processes selected actions.
         *
         * Algorithm:
         * 1. Displays main payroll menu options.
         * 2. Accepts user input for menu selection.
         * 3. If option 1 is selected:
         *    - Displays sub-options for payroll processing.
         *    - Processes one employee, all employees, or exits based on input.
         * 4. If option 2 is selected, exits the program.
         * 5. Displays an error message for invalid input.
         */
    public static void payrollMenu(List<String[]> sssTable,
                               List<String[]> pagibigTable) {

        System.out.println("\n--------------------------------------");
        System.out.println("\n1. Process Payroll");
        System.out.println("2. Exit program");
        System.out.print("Choose Option: ");
        String option = sc.nextLine();
        System.out.println("\n--------------------------------------");

        // Option 1: Process payroll
        if (option.equals("1")) {

            System.out.println("\n1. View One Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Exit program");
            System.out.print("Choose Sub-option: ");
            String subOption = sc.nextLine();

            // Sub-option 1: Process payroll for one employee
            if (subOption.equals("1")) {
                System.out.print("\nEnter Employee Number: ");
                String employeeNo = sc.nextLine();
                PayrollService.oneEmployee(employeeNo, sssTable, pagibigTable);

            // Sub-option 2: Process payroll for all employees
            } else if (subOption.equals("2")) {
                PayrollService.allEmployee(sssTable, pagibigTable); // delegate payroll processing to allEmployee()

            // Sub-option 3: Exit program
            } else if (subOption.equals("3")) {
                System.out.println("\nExiting program.\n");

            // Invalid sub-option handling
            } else {
                System.out.println("\nInvalid option. Please enter 1, 2, or 3.\n");
            }

        // Option 2: Exit program
        } else if (option.equals("2")) {
            System.out.println("\nExiting program.\n");
        return;

        // Invalid option handling
        } else {
            System.out.println("\nInvalid option. Please enter 1 or 2.\n");
        }
    }
    
}
