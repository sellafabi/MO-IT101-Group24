import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MotorPH {


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

    // Stores SSS and Pag-IBIG bracket data loaded from CSV files at startup
    static List<String[]> sssTable     = new ArrayList<>();
    static List<String[]> pagibigTable = new ArrayList<>();


    /*========================================================================================
        Main Method [rosella]
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
        loadTables();
        String role = handleLogin();

        if (role.equals(EMP_USERNAME)) {
            employeeMenu();
        } else if (role.equals(PAYROLL_USERNAME)) {
            payrollMenu();
        }

        sc.close();
    }


    /*========================================================================================
        Handle Login Method [rosella]
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
        // Display login interface
        System.out.println("\n==================================== ");
        System.out.println("        MotorPH Login System         ");
        System.out.println("==================================== ");

        // Get user input
        System.out.print("\nEnter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String inputPassword = sc.nextLine();

        // Validate credentials for each role.
        boolean isEmployee     = username.equals(EMP_USERNAME)     && inputPassword.equals(PASSWORD);
        boolean isPayrollStaff = username.equals(PAYROLL_USERNAME) && inputPassword.equals(PASSWORD);

        // Terminate program if credentials are invalid
        if (!isEmployee && !isPayrollStaff) {
            System.out.println("\nIncorrect username and/or password.\n");
            System.exit(0);
        }

        return username;
    }


    /*========================================================================================
        Print Employee Information Method [rosella]
    =========================================================================================*/

        /**
         * Displays basic employee details on the console.
         *
         * Algorithm:
         * 1. Prints a formatted header for the employee section.
         * 2. Displays employee number, full name, and birthday.
         * 3. Prints a closing separator for clarity.
         *
         * @param employeeNo employee identification number
         * @param lastName employee's last name
         * @param firstName employee's first name
         * @param birthday employee's date of birth
         */
    public static void printEmployeeInfo(String employeeNo, String lastName, String firstName, String birthday) {
        // Display employee information header
        System.out.println("\n==================================== ");
        System.out.println( "        Employee Information");
        System.out.println("==================================== ");

        // Output employee details
        System.out.println("\nEmployee #: " + employeeNo);
        System.out.println("Employee Name: " + lastName + ", " + firstName);
        System.out.println("Employee Birthday: " + birthday);

        // Closing separator
        System.out.println("====================================\n");
    }


    /*========================================================================================
        Employee Menu Method [rosella]
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
        // Display menu options
        System.out.println("\n==================================== ");
        System.out.println("\n1. View Employee Details ");
        System.out.println("2. Exit program");
        System.out.print("Choose Option: ");
        String option = sc.nextLine();
        System.out.println("==================================== \n");

        // Option 1: View Employee Details
        if (option.equals("1")){

            System.out.print("Enter Your Employee Number: ");
            String employeeNo         = sc.nextLine();
                
            // Variables to store employee data once found
            String  lastName          = "";
            String  firstName         = "";
            String  birthday          = "";
            boolean isEmpDetailsFound = false;
                                
            // Search employee record in CSV file
            try (BufferedReader br = new BufferedReader (new FileReader (EMP_FILE))){

                br.readLine(); // skip header row
                String line;

                while ((line = br.readLine()) !=null){
                    if(line.trim().isEmpty()) continue; // skip blank lines in the CSV file

                    // Split CSV row while handling quoted values
                    String[] employeeRow = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

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
                
            // Display result
            if (isEmpDetailsFound){
                printEmployeeInfo (employeeNo, lastName, firstName, birthday);
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
        Payroll Menu Method [rosella]
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
    public static void payrollMenu() {

        // Display main payroll menu
        System.out.println("\n==================================== ");
        System.out.println("\n1. Process Payroll");
        System.out.println("2. Exit program");
        System.out.print("Choose Option: ");
        String option = sc.nextLine();
        System.out.println("\n====================================");

        // Option 1: Process payroll
        if (option.equals("1")) {

            // Display sub-menu options
            System.out.println("\n1. View One Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Exit program");
            System.out.print("Choose Sub-option: ");
            String subOption = sc.nextLine();

            // Sub-option 1: Process payroll for one employee
            if (subOption.equals("1")) {
                System.out.print("\nEnter Employee Number: ");
                String employeeNo = sc.nextLine();
                System.out.println("\n====================================\n");
                oneEmployee(employeeNo); // standardized name: employeeNo

            // Sub-option 2: Process payroll for all employees
            } else if (subOption.equals("2")) {
                allEmployee(); // delegate payroll processing to allEmployee()

            // Sub-option 3: Exit program
            } else if (subOption.equals("3")) {
                System.out.println("\nExiting program.\n");

            // Invalid sub-option
            } else {
                System.out.println("\nInvalid option. Please enter 1, 2, or 3.\n");
            }

        // Option 2: Exit program
        } else if (option.equals("2")) {
            System.out.println("\nExiting program.\n");
        return;

        // Invalid option
        } else {
            System.out.println("\nInvalid option. Please enter 1 or 2.\n");
        }
    }            
    
   
    /*========================================================================================
        Load Tables Method [rosella]
    =========================================================================================*/

        /**
         * Loads SSS and Pag-IBIG contribution tables from CSV files into memory.
         *
         * Algorithm:
         * 1. Opens the SSS CSV file and skips the header row.
         * 2. Reads each line and stores non-empty rows into the SSS table list.
         * 3. Repeats the same process for the Pag-IBIG CSV file.
         * 4. Displays an error message if any file cannot be accessed.
         */
    public static void loadTables() {

        // Load SSS contribution table
        try (BufferedReader br = new BufferedReader(new FileReader(SSS_FILE))) {

            br.readLine(); // Skip header row
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Skip blank lines

                    // Split CSV row while handling quoted values
                    sssTable.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                }
            }

        } catch (IOException e) {
            System.out.println("Error: SSS table file not found. Check that sss.csv exists in src/.");
        }

        // Load Pag-IBIG contribution table
        try (BufferedReader br = new BufferedReader(new FileReader(PAGIBIG_FILE))) {

            br.readLine(); // Skip header row
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Skip blank lines

                    // Split CSV row while handling quoted values
                    pagibigTable.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error: Pag-IBIG table file not found. Check that pagibig.csv exists in src/.");
        }
    }


    /*========================================================================================
        SSS Computation Method [rosella]
    =========================================================================================*/

        /**
         * Computes the SSS contribution based on the employee's monthly gross salary.
         *
         * Algorithm:
         * 1. Iterates through the SSS table containing salary brackets.
         * 2. Checks if the monthly gross falls within a bracket range.
         * 3. Returns the corresponding employee share once a match is found.
         * 4. If no exact match is found, returns the last valid bracket value.
         *
         * @param monthlyGross combined gross salary for the month
         * @return employee's SSS contribution
         */
    public static double computeSSS(double monthlyGross) {

        // Stores last valid employee share as fallback
        double lastEmployeeShare = 0;

        for (String[] sssRow : sssTable) {

            // SSS Table Columns:
            // [0] Range From | [1] Range To | [3] Employee Share
            double rangeFrom     = Double.parseDouble(sssRow[0].trim());
            String rangeToText   = sssRow[1].trim();
            double employeeShare = Double.parseDouble(sssRow[3].trim());

            lastEmployeeShare    = employeeShare;

            // Handle "Over" bracket (no upper limit)
            if (rangeToText.equalsIgnoreCase("Over")) {
                if (monthlyGross >= rangeFrom) return employeeShare;
            } else {
                double rangeTo = Double.parseDouble(rangeToText);

                // Check if salary falls within range
                if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                    
                    return employeeShare;
                }
            }
        }

        return lastEmployeeShare; 
    }


    /*========================================================================================
        Pag-IBIG Computation Method [rosella]
    =========================================================================================*/

        /**
         * Computes the Pag-IBIG contribution based on the employee's monthly gross salary.
         *
         * Algorithm:
         * 1. Iterates through the Pag-IBIG table containing salary ranges and rates.
         * 2. Identifies the matching salary bracket.
         * 3. Computes contribution using the corresponding rate.
         * 4. Applies a maximum cap of PHP 100.00.
         *
         * @param monthlyGross combined gross salary for the month
         * @return employee's Pag-IBIG contribution (capped at PHP 100.00)
         */
    public static double computePagibig(double monthlyGross) {

        double contribution = 0;

        for (String[] pagibigRow : pagibigTable) {

            if (pagibigRow.length < 2) continue;

            // Pag-IBIG Table Columns:
            // [0] Salary Range | [1] Contribution Rate
            String salaryRange = pagibigRow[0].trim().replace("\"", "");
            String rateText    = pagibigRow[1].trim();

            // Skip invalid or incomplete rows
            if (salaryRange.isEmpty() || rateText.isEmpty() || !rateText.endsWith("%")) continue;

            double rate = Double.parseDouble(rateText.replace("%", "").trim()) / 100.0;

            // Handle "Over" range (no upper limit)
            if (salaryRange.toLowerCase().startsWith("over")) {

                String floorText = salaryRange.substring("over".length()).trim().replace(",", "");
                double floor     = Double.parseDouble(floorText);

                if (monthlyGross > floor) {
                    contribution = monthlyGross * rate;
                    break;
                }

            // Handle ranged values (e.g., "At least X to Y")
            } else if (salaryRange.toLowerCase().startsWith("at least")) {

                String rangeOnly = salaryRange.substring("at least".length()).trim();
                String[] parts   = rangeOnly.split("(?i)\\s+to\\s+");

                double rangeFrom = Double.parseDouble(parts[0].trim().replace(",", ""));
                double rangeTo   = Double.parseDouble(parts[1].trim().replace(",", ""));

                if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                    contribution = monthlyGross * rate;
                    break;
                }
            }
        }

        // Apply maximum contribution cap (PHP 100.00)
        return Math.min(contribution, 100);
    }


    /*========================================================================================
        PhilHealth Computation Method [ann]
    =========================================================================================*/

        /**
         * Computes the PhilHealth contribution based on the employee's monthly gross salary.
         *
         * Algorithm:
         * 1. Determines the applicable salary bracket.
         * 2. Applies the corresponding contribution rule.
         * 3. Returns the employee's share (50% of total premium).
         *
         * @param monthlyGross combined gross salary for the month
         * @return employee's PhilHealth contribution
         */
    public static double computePhilhealth (double monthlyGross) {

        double philhealthDeduction = 0.0;

        // Apply bracket-based computation
        if (monthlyGross <= 10000) {

            // Fixed contribution for lowest bracket
            philhealthDeduction = 300/2;

        } else if (monthlyGross > 10000 && monthlyGross < 60000){

            // 1.5% of monthly gross (half of 3% total rate)
            philhealthDeduction =  monthlyGross*(0.03)/2;

        } else if (monthlyGross >= 60000) {

            // Maximum contribution cap
            philhealthDeduction = 1800/2;
        }

        return philhealthDeduction; 
    }


    /* =======================================================================================
        Tax Computation (Method # 4) [ann]
    ==========================================================================================*/

        /**
        * Computes the monthly withholding tax of an employee using the BIR tax table.
        *
        * Algorithm:
        * The BIR (Bureau of Internal Revenue) withholding tax is computed after deducting all of the 
        * mandated government contributions (SSS + PhilHealth + Pag-IBIG) from the monthly gross salary.
        * Only the resulting taxable salary is matched against the six BIR brackets:
        *
        * Bracket 1: taxable ≤ 20,832 Tax = 0.00 (exempted from tax)
        * Bracket 2: 20,833 – 33,332 Tax = (taxable − 20,833) × 20%
        * Bracket 3: 33,333 – 66,666 Tax = 2,500 + (taxable − 33,333) × 25%
        * Bracket 4: 66,667 – 166,666 Tax = 10,833 + (taxable − 66,667) × 30%
        * Bracket 5: 166,667 – 666,666 Tax = 40,833.33 + (taxable − 166,667) × 32%
        * Bracket 6: 666,667 and above Tax = 200,833.33 + (taxable − 666,667) × 35%
        *
        * Process Flow (Government Deductions):
        * - Tax is deducted on the second cutoff only.
        * - Per process flow: the 1st and 2nd cutoff amounts are combined first,
        *   then SSS, PhilHealth, and Pag-IBIG are computed, and then tax is computed
        *   on the remaining taxable salary.
        *
        * @param monthlyGross      the combined gross salary of both cutoffs for the month
        * @param totalContribution the total of SSS + PhilHealth + Pag-IBIG contributions
        * @return the computed withholding tax amount
        */

    public static double withholdingTax (double totalGross, double totalContribution) {
        double tax = 0.00;

        // Withholding tax is applied after deducting the mandatory contributions to the monthly gross, leaving only the taxable salary.
        double taxableMonthlySalary = totalGross - totalContribution; 
        
        // Apply the BIR tax bracket that matches the computed taxable salary
        if (taxableMonthlySalary <= 20832) {
            tax = 0.00;  // Bracket 1: fully exempt — no withholding tax

        } else if (taxableMonthlySalary >= 20833 && taxableMonthlySalary < 33333) {
            tax = (taxableMonthlySalary-20833)*0.2;  // Bracket 2: 20% applied only to the amount exceeding the floor of 20,833

        } else if (taxableMonthlySalary >= 33333 && taxableMonthlySalary < 66667) {
            tax = 2500+(taxableMonthlySalary-33333)*0.25; // Bracket 3: fixed base of 2,500 plus 25% on the excess over 33,333

        } else if (taxableMonthlySalary >= 66667 && taxableMonthlySalary < 166667) {
            tax = 10833+(taxableMonthlySalary-66667)*0.30; // Bracket 4: fixed base of 10,833 plus 30% on the excess over 66,667

        } else if (taxableMonthlySalary >= 166667 && taxableMonthlySalary < 666667) {
            tax = 40833.33+(taxableMonthlySalary-166667)*0.32; // Bracket 5: fixed base of 40,833.33 plus 32% on the excess over 166,667

        } else if (taxableMonthlySalary >= 666667) {
            tax = 200833.33+(taxableMonthlySalary-666667)*0.35; // Bracket 6: fixed base of 200,833.33 plus 35% on the excess over 666,667
        }

        return tax; // Returns the final computed withholding tax amount
    }  


    /*========================================================================================
        Hours Worked Computation (Method #5) [ann]
    ==========================================================================================*/

        /**
         * Computes the total hours worked by an employee for a single attendance record (one day).
         *
         * Algorithm — three rules are applied in this order:
         *
         * Rule 1 — Overtime cap:
         *   Logout after 5:00 PM is capped at 5:00 PM before any calculation.
         *   Example: logout 5:30 PM → treated as 5:00 PM.
         *
         * Rule 2 — Grace period:
         *   Login at or before 8:10 AM is adjusted to 8:00 AM for computation.
         *   This prevents minor early arrivals from inflating hours and avoids
         *   penalizing logins between 8:01–8:10 AM.
         *   Example: login 8:05 AM → treated as 8:00 AM.
         *   Example: login 8:30 AM → used as-is (past grace period).
         *
         * Rule 3 — Lunch break deduction:
         *   A mandatory 60-minute unpaid break is always deducted, but only if
         *   the employee worked more than 60 minutes. Otherwise result is 0.
         *
         * Combined example (Rule 2 + Rule 3):
         *   Login 8:05 AM → 8:00 AM | Logout 4:30 PM → no cap needed
         *   510 min raw − 60 min lunch = 450 min = 7.5 hours worked
         *
         * @param logIn  raw login time from the attendance CSV
         * @param logOut raw logout time from the attendance CSV
         * @return       total hours worked as a decimal (e.g., 7.5 = 7 hours and 30 minutes)
         */
    public static double computeHoursWorked(LocalTime logIn, LocalTime logOut) {
        
        final LocalTime GRACE_PERIOD   = LocalTime.of(8, 10); // grace period ends at 8:10 AM (inclusive)
        final LocalTime STANDARD_START = LocalTime.of(8,  0); // official workday start
        final LocalTime CUTOFF_TIME    = LocalTime.of(17, 0); // official workday end
        final int       LUNCH_BREAK    = 60;                  // unpaid break in minutes

        // Guard against corrupted or reversed time entries in the CSV
        if (logOut.isBefore(logIn)) {
            return 0;
        }

        // Rule 1: Cap logout at 5:00 PM — time past this is not counted
        if (logOut.isAfter(CUTOFF_TIME)) {
            logOut = CUTOFF_TIME;
        }

        // Rule 2: Treat login as 8:00 AM if within the grace window (at or before 8:10 AM)
        if (!logIn.isAfter(GRACE_PERIOD)) {
            logIn = STANDARD_START;
        }

        // Calculate total minutes worked between login and logout times
        long minutesWorked = Duration.between(logIn, logOut).toMinutes();

        // Rule 3: Subtract the mandatory lunch break; if 60 min or less was logged, result is 0
        if (minutesWorked > LUNCH_BREAK) {
            minutesWorked -= LUNCH_BREAK;
        } else {
            minutesWorked = 0;
        }

        return minutesWorked / 60.0; // Convert minutes to hours     
    }


    /*========================================================================================
        Gross Computation (Method #6) [ann]
    ==========================================================================================*/

        /**
         * Computes the gross salary for a single cutoff period.
         *
         * Algorithm:
         * Gross salary = total hours worked × hourly rate.
         * Called twice per month — once for the first cutoff (days 1–15) and
         * once for the second cutoff (days 16–end). Allowances are excluded per process flow.
         *
         * @param hours total hours worked during the cutoff period
         * @param rate  employee's hourly rate (column 18 of details.csv)
         * @return gross salary for the cutoff period
         */
    static double computeGross(double hours, double rate) {
        return hours * rate;
    }


    /*========================================================================================
        Payroll Computation and Display (Method #7) [rosella]
    ==========================================================================================*/

        /**
         * Computes and displays the full payroll report for a single employee,
         * covering all months from June to December 2024.
         *
         * Algorithm — Per Month (June to December):
         * 1. Filter attendance records by employee number and current month.
         * 2. Parse login/logout times, compute hours via computeHoursWorked(), and
         *    accumulate into the correct cutoff: days 1–15 = firstHalf, days 16–end = secondHalf.
         * 3. Compute gross salary for each cutoff: hours × hourly rate.
         * 4. Combine both cutoffs into monthlyGross. Deductions (SSS, PhilHealth, Pag-IBIG, Tax)
         *    are all computed from this combined figure — not from the second cutoff alone.
         *    Per process flow: "add 1st and 2nd cutoff amounts first before computing deductions."
         * 5. Net salary on the second cutoff = secondHalf gross − totalDeductions.
         *    The first cutoff has no deductions — its net equals its gross.
         *
         * Process Flow Output Per Cutoff:
         *   First Cutoff  (1–15):  Total Hours Worked, Gross Salary, Net Salary
         *   Second Cutoff (16–30): Total Hours Worked, Gross Salary, SSS, PhilHealth,
         *                          Pag-IBIG, Tax, Total Deductions, Net Salary
         *
         * @param employeeNo        employee number — used to filter attendance records
         * @param lastName          employee's last name
         * @param firstName         employee's first name
         * @param birthday          employee's birthday
         * @param rate              hourly rate from column 18 of details.csv
         * @param attendanceRecords all attendance records pre-loaded into memory
         * @param timeFormat        formatter for parsing H:mm time values from the CSV
         */
    public static void processPayroll(String employeeNo, String lastName, String firstName,
                                      String birthday, double rate,
                                      List<String[]> attendanceRecords,
                                      DateTimeFormatter timeFormat) {

        System.out.println("\n==================================== ");
        System.out.println("           Employee Payroll          ");
        System.out.println("==================================== ");
        System.out.println("Employee # : "    + employeeNo);
        System.out.println("Employee Name : " + lastName + ", " + firstName);
        System.out.println("Birthday : "      + birthday);
        System.out.println("===================================\n");

        // includes months from: June (6) to December (12) per process flow requirement
        for (int month = 6; month <= 12; month++) {

            double firstHalf  = 0; // hours worked days 1–15
            double secondHalf = 0; // hours worked days 16–end

            // lengthOfMonth() gives the correct last day (e.g., 30 for June, 31 for July)
            int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();

            for (String[] attData : attendanceRecords) {

                if (!attData[0].equals(employeeNo)) continue; // Column 0 contains the employee number

                // Column 3 is the date in MM/DD/YYYY format — split to get month, day, and year
                String[] dateParts   = attData[3].split("/");
                int      recordMonth = Integer.parseInt(dateParts[0]);
                int      day         = Integer.parseInt(dateParts[1]);
                int      year        = Integer.parseInt(dateParts[2]);

                if (year != 2024 || recordMonth != month) continue;

                // Column 4 = login time, Column 5 = logout time — both in H:mm format
                LocalTime login  = LocalTime.parse(attData[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(attData[5].trim(), timeFormat);

                double hours = computeHoursWorked(login, logout);

                if (day <= 15) firstHalf  += hours; // days 1–15:   first cutoff
                else           secondHalf += hours; // days 16–end of the month: second cutoff
            }

            // Compute gross salary for the first and second cut-off.
            double grossFirst  = computeGross(firstHalf,  rate);
            double grossSecond = computeGross(secondHalf, rate);

            // Deductions are based on the combined monthly gross, not per-cutoff gross
            double monthlyGross = grossFirst + grossSecond;
            double sss          = computeSSS(monthlyGross);
            double pagibig      = computePagibig(monthlyGross);
            double philhealth   = computePhilhealth(monthlyGross);

            // BIR rule: contributions must be deducted from gross before tax is computed
            double totalContribution = sss + philhealth + pagibig;
            double tax               = withholdingTax(monthlyGross, totalContribution);
            double totalDeductions   = sss + pagibig + philhealth + tax;

            // Deductions are applied on the second cutoff payout only, per process flow.
            double netSalary = grossSecond - totalDeductions;

            // Convert numeric month (6-12) to its name; otherwise, returns to the default label
            String monthName = switch (month) {
                case 6  -> "June";
                case 7  -> "July";
                case 8  -> "August";
                case 9  -> "September";
                case 10 -> "October";
                case 11 -> "November";
                case 12 -> "December";
                default -> "Month " + month;
            };

            // First cutoff — no deductions; net equals gross
            System.out.println("\nFirst Cutoff");
            System.out.println("\nCutoff Date: "       + monthName + " 1 to 15");
            System.out.println("Total Hours Worked : " + firstHalf);
            System.out.println("Gross Salary: "        + grossFirst);
            System.out.println("Net Salary: "          + grossFirst);

            // Second cutoff — all four government deductions are applied here
            System.out.println("\nSecond Cutoff");
            System.out.println("\nCutoff Date: "       + monthName + " 16 to " + daysInMonth);
            System.out.println("Total Hours Worked : " + secondHalf);
            System.out.println("Gross Salary: "        + grossSecond);
            System.out.println("    SSS: "             + sss);
            System.out.println("    PhilHealth: "      + philhealth);
            System.out.println("    Pag-IBIG: "        + pagibig);
            System.out.println("    Tax: "             + tax);
            System.out.println("Total Deductions: "    + totalDeductions);
            System.out.println("Net Salary: "          + netSalary);
            System.out.println("-----------------------------------\n");
        }

        System.out.println("\n===================================");
        System.out.println("          END OF RECORD");
        System.out.println("=====================================");
    }
              
        
    /*========================================================================================
        For One Employee (Method #8) [rosella]
    ==========================================================================================*/

        /**
         * Processes and displays the payroll report for a single employee.
         *
         * Algorithm:
         * 1. Opens details.csv and searches line by line for the row matching employeeNo.
         *    Uses regex split to handle commas inside quoted fields (e.g., addresses).
         *    If no match is found, prints an error and returns — does not load attendance.
         * 2. Loads all attendance records into memory once before the monthly loop.
         *    Avoids reopening the file for each month (7 reads → 1 read).
         * 3. Delegates computation and display to processPayroll().
         *
         * Process Flow (payroll_staff → Process Payroll → One Employee):
         * - If employee not found: display "Employee number does not exist." and stop.
         * - If found: display payroll records from June to December with both cutoffs.
         *
         * @param employeeNo the employee number entered by the payroll staff
         */
    public static void oneEmployee(String employeeNo) {

        // H:mm handles both single-digit (e.g., 8:05) and double-digit hours (e.g., 17:00)
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

        String  lastName            = "";
        String  firstName           = "";
        String  birthday            = "";
        boolean isEmpDetailsFound   = false;
        double  rate                = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Regex split handles commas inside quoted fields (e.g., addresses)
                String[] empData = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Column 0 = Employee Number — compare against the input
                if (empData[0].equals(employeeNo)) {
                    employeeNo         = empData[0];                             // Column 0: Employee Number
                    lastName           = empData[1];                             // Column 1: Last Name
                    firstName          = empData[2];                             // Column 2: First Name
                    birthday           = empData[3];                             // Column 3: Birthday
                    rate               = Double.parseDouble(empData[18].trim()); // Column 18: Hourly Rate
                    isEmpDetailsFound  = true;
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }

        // Stop here if no matching employee number was found
        if (!isEmpDetailsFound) {
            System.out.println("\nEmployee number does not exist.\n");
            return;
        }

        // Load all attendance records once — avoids reopening the file per month (June–December)
        List<String[]> attendanceRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ATT_FILE))) {
            br.readLine(); 
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    // Same regex split — attendance fields may also contain commas inside quotes
                    attendanceRecords.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            }

        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
        }

        // calls the method that computes and displays full payroll report of the employee
        processPayroll(employeeNo, lastName, firstName, birthday, rate, attendanceRecords, timeFormat);
    }


    /*========================================================================================
        For All Employees (Method #9) [rosella]
    ==========================================================================================*/

        /**
         * Processes and displays the payroll report for every employee in the CSV file.
         *
         * Algorithm:
         * 1. Reads all employee records from details.csv into a List<String[]>.
         * 2. Loads all attendance records into a separate List<String[]> once, before
         *    the employee loop begins. This avoids reopening the file per employee —
         *    up to 34 employees × 7 months = 238 reads reduced to just 1.
         * 3. For each employee, delegates to processPayroll() which filters the
         *    pre-loaded attendance list internally.
         *
         * Process Flow (payroll_staff → Process Payroll → All Employees):
         * - Follows the same output format as oneEmployee() (Method #8).
         * - Automatically processes all employees without requiring an employee number.
         *
         * This method takes no parameters because it processes all employees by design.
         */
    public static void allEmployee() {

        // H:mm handles both single-digit (e.g., 8:05) and double-digit hours (e.g., 17:00)
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

        List<String[]> employees = new ArrayList<>();

        // Step 1: Read all employee records from details.csv
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                // Regex split handles commas inside quoted fields (e.g., addresses)
                String[] empData = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                employees.add(empData);
            }

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }

        // Step 2: Load attendance records once — reused for every employee in the loop below
        List<String[]> attendanceRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ATT_FILE))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    attendanceRecords.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            }

        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
        }

        // Step 3: Process each employee using the pre-loaded attendance list
        for (String[] empData : employees) {
            String    employeeNo   = empData[0];                             // Column 0: Employee Number
            String    lastName     = empData[1];                             // Column 1: Last Name
            String    firstName    = empData[2];                             // Column 2: First Name
            String    birthday     = empData[3];                             // Column 3: Birthday
            double    rate         = Double.parseDouble(empData[18].trim()); // Column 18: Hourly Rate

            // calls the method that computes and displays full payroll report of the employees
            processPayroll(employeeNo, lastName, firstName, birthday, rate, attendanceRecords, timeFormat);
        }
    }
}