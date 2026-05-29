package motorph.authenticator;

import java.util.Scanner;

public class AuthService {

    // Shared Scanner instance used across all methods for user input
    static Scanner sc = new Scanner(System.in);

    static final String EMP_USERNAME     = "employee";
    static final String PAYROLL_USERNAME = "payroll_staff";
    static final String PASSWORD         = "12345";

    /*========================================================================================
        Handle Login (Method #2) [ann]
    =========================================================================================*/

        /**
         * Prompts the user to enter login credentials and validates access.
         *
         * Algorithm:
         * 1. Displays the login interface.
         * 2. Accepts username and password input.
         * 3. Checks credentials against predefined roles (employee or payroll staff).
         * 4. Terminates the program if credentials are invalid.
         *
         * @return the validated username used for role identification
         */
    public static String handleLogin() {

        System.out.println("\n======================================");
        System.out.println("        MotorPH Login System         ");
        System.out.println("======================================");

        System.out.print("\nEnter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String inputPassword = sc.nextLine();

        boolean isEmployee     = username.equals(EMP_USERNAME)     && inputPassword.equals(PASSWORD);
        boolean isPayrollStaff = username.equals(PAYROLL_USERNAME) && inputPassword.equals(PASSWORD);

        if (!isEmployee && !isPayrollStaff) {
            System.out.println("\nIncorrect username and/or password.\n");
            System.exit(0);
        }
    return username;
    }
    
}
