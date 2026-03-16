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


    /*------------------------------------------------------------------------
        SHARED SCANNER INSTANCE
        A single Scanner object is declared at the class level so that all
        methods share the same input stream.
    -------------------------------------------------------------------------*/
    static Scanner sc = new Scanner(System.in);

    /*------------------------------------------------------------------------
        PROGRAM CONSTANTS
        All file paths, usernames, and the password are defined here as
        static final constants instead of being written directly inside methods.
        This way, if a path or credential ever changes, it only needs to be
        updated in one place.
    -------------------------------------------------------------------------*/
    static final String EMP_FILE         = "FINAL MO-IT101-Group24/src/details.csv";
    static final String ATT_FILE         = "FINAL MO-IT101-Group24/src/attendance.csv";
    static final String SSS_FILE         = "FINAL MO-IT101-Group24/src/sss.csv";
    static final String PAGIBIG_FILE     = "FINAL MO-IT101-Group24/src/pagibig.csv";
    static final String EMP_USERNAME     = "employee";
    static final String PAYROLL_USERNAME = "payroll_staff";
    static final String PASSWORD         = "12345";



    /*============================================================================
        Login System (MAIN METHOD) [ann]
    ==============================================================================*/

        /**
         * The main entry point of the MotorPH Payroll System.
         *
         * Process Flow:
         * 1. The program asks for a username and password upon launch.
         * 2. Valid usernames are: "employee" and "payroll_staff". Password is "12345".
         * 3. If credentials are incorrect, display an error message and terminate the program.
         * 4. If credentials are correct, proceed based on the username:
         *    - "employee"      → Display options: (1) View Employee Details, (2) Exit
         *    - "payroll_staff" → Display options: (1) Process Payroll, (2) Exit
         *
         * Login Validation Algorithm:
         * Both the username and password must be correct at the same time for
         * access to be granted. Two boolean flags (isEmployee, isPayrollStaff)
         * are evaluated once before any branching. This avoids the bug where
         * only the password is wrong but the program still proceeds, and it
         * eliminates the need to repeat the password check in each login branch.
         */
    
    public static void main(String[] args) {


        // Prompt the user to enter their credentials
        System.out.println("\n==================================== ");
        System.out.println("        MotorPH Login System         ");
        System.out.println("==================================== ");
        System.out.print("\nEnter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String inputPassword = sc.nextLine();

        // Evaluate both username and password together in a single check per role.
        // Using && ensures that both fields must be correct — if only one is wrong,
        // neither flag will be true, and the invalid credentials block below will trigger.
        boolean isEmployee     = username.equals(EMP_USERNAME)     && inputPassword.equals(PASSWORD);
        boolean isPayrollStaff = username.equals(PAYROLL_USERNAME) && inputPassword.equals(PASSWORD);

        /*------------------------------------------------------------------------
            INVALID CREDENTIALS
            If neither flag is true, it means the username, the password, or
            both are incorrect. Per process flow, the program must display an
            error message and terminate immediately — no retry is allowed.
        -------------------------------------------------------------------------*/
        if (!isEmployee && !isPayrollStaff) {
            System.out.println("\nIncorrect username and/or password.\n");
            sc.close();
            System.exit(0);
        }

        /*------------------------------------------------------------------------
            EMPLOYEE LOGIN
            The "employee" role grants access to self-service information lookup
            only. The employee can view their own details or exit the program.
            They cannot access payroll processing or view other employees' data.
        -------------------------------------------------------------------------*/
        if (isEmployee) {
            System.out.println("\nEmployee login successful.");
            String option = "";

            // Display the employee menu after successful login
            System.out.println("\n==================================== ");
            System.out.println("\n1. View Employee Details ");
            System.out.println("2. Exit program");
            System.out.print("Choose Option: ");
            option = sc.nextLine();
            System.out.println("==================================== \n");

            /*--------------------------------------------------------------------
                Option 1 — View Employee Details
                The employee enters their employee number. The program searches
                the employee CSV file line by line until it finds a row whose
                first column (employee number) matches the input.
                If found, display employee's number, name, and birthday.
                If not found, display "Employee number does not exist."
            ---------------------------------------------------------------------*/
            if (option.equals("1")){
                System.out.print("Enter Your Employee Number: ");

                // These variables will hold the matched employee's data from CSV file once found
                String employeeNo = sc.nextLine(); 
                String  lastName  = "";
                String  firstName = "";
                String  birthday  = "";
                boolean found     = false;
                                
                // Open the employee CSV file and search for a row whose first column
                // matches the employee number entered by the user
                try (BufferedReader br = new BufferedReader (new FileReader (EMP_FILE))){

                    br.readLine(); // skip header row
                    String line;

                    while ((line = br.readLine()) !=null){
                        if(line.trim().isEmpty()) continue; // skip blank lines in the CSV file

                        // Split the line by comma to access individual columns
                        String[] data = line.split(",");

                        // Column 0 = Employee Number; compare against what the user typed
                        if (data[0].equals(employeeNo)){
                            lastName = data[1]; // Column 1: Last Name
                            firstName  = data[2]; // Column 2: First Name
                            birthday = data[3]; // Column 3: Birthday
                            found = true;
                            break; // stop searching once a match is found
                        }
                    }

                } catch (IOException e) {
                    System.out.println("\nEmployee file error.\n");
                }
                


                // Per process flow: display employee's number, name, and birthday if found
                if (found){
                    System.out.println("\n==================================== ");
                    System.out.println( "        Employee Information");
                    System.out.println("==================================== ");
                    System.out.println("\nEmployee #: " + employeeNo);
                    System.out.println("Employee Name: " + lastName + ", " + firstName);
                    System.out.println("Employee Birthday: " + birthday);
                    System.out.println("====================================\n");
                    
                    // Per process flow: if no matching record was found, display this message
                } else {
                    System.out.println("\nEmployee number does not exist.\n");
                }
                
            /*--------------------------------------------------------------------
                Option 2: Exit the program
                If the employee chose to exit. Close the scanner to release the
                input stream resource before terminating the program.
            ---------------------------------------------------------------------*/
            } else if (option.equals("2")){
                System.out.println("\nExiting program.\n");
                sc.close();
                System.exit(0); // program terminated
                
                // Added feature - Any input other than "1" or "2" is not a valid menu option 
            } else {
                System.out.println("\nInvalid option. Please enter 1 or 2.\n");
                sc.close();
                System.exit(0); // program terminated
            }
  
        /*------------------------------------------------------------------------
            PAYROLL STAFF LOGIN
            The "payroll_staff" role grants access to payroll processing.
            Staff can compute and view payroll records for one specific employee
            or for all employees at once, covering June to December 2024.
        -------------------------------------------------------------------------*/
        }else { 

            System.out.println("\nPayroll staff Login successful!");;

            // Display the payroll staff menu after successful login
            System.out.println("\n==================================== ");
            System.out.println("\n1. Process Payroll");
            System.out.println("2. Exit program");
            System.out.print("Choose Option: ");
            String option = sc.nextLine();
            System.out.println("\n====================================");
            
            /*--------------------------------------------------------------------
                Option 1: Process Payroll
                Allowances are NOT included in gross salary computation, per the
                process flow requirement. The payroll staff is then shown a
                sub-menu to choose between one employee or all employees.
            ---------------------------------------------------------------------*/
            if (option.equals("1")) {
                System.out.println("\n1. View One Employee");
                System.out.println("2. View All Employees");
                System.out.println("3. Exit program");
                System.out.print("Choose Sub-option: ");
                String subOption = sc.nextLine();
                
                                
                /*----------------------------------------------------------------
                    Sub-option 1: One Employee
                    The payroll staff enters a specific employee number.
                    The program searches for that employee and, if found,
                    delegates the full payroll computation and display to
                    the oneEmployee() method, which covers June to December.
                      Per cutoff:
                        First Cutoff  (1–15):  Total Hours Worked, Gross, Net Salary
                        Second Cutoff (16–30): Total Hours Worked, Gross, 
                                               Each Deduction (SSS, PhilHealth, Pag-IBIG, Tax), 
                                               Total Deductions, Net Salary
                -----------------------------------------------------------------*/
                if (subOption.equals("1")){
                System.out.print("\nEnter Employee Number: ");
                String employeeNo = sc.nextLine();
                System.out.println("\n====================================\n");
                oneEmployee(employeeNo); // standardized name: employeeNo

                /*------------------------------------------------------------
                    Sub-option 2: All Employees
                    Delegates to allEmployee(), which processes every employee
                    in the CSV file using the same display format as Sub-option 1.
                -------------------------------------------------------------*/
                } else if (subOption.equals("2")){
                    allEmployee(); // delegate payroll processing to allEmployee()

                /*------------------------------------------------------------
                    Sub-option 3: Exit the program
                -------------------------------------------------------------*/
                } else if (subOption.equals("3")){
                    System.out.println("\nExiting program.\n");
                    sc.close();
                    System.exit(0);

                // Added feature - Any input other than "1", "2", or "3" is not valid 
                } else {
                    System.out.println("\nInvalid option. Please enter 1, 2, or 3.\n");
                }

            /*--------------------------------------------------------------------
                Option 2 — Exit
            ---------------------------------------------------------------------*/
            } else if (option.equals("2")) {
                    System.out.println("\nExiting program.\n");
                    sc.close();
                    System.exit(0);

            // Added feature - Invalid option — only 1 or 2 are accepted 
            } else {
                System.out.println("\nInvalid option. Please enter 1 or 2.\n");
            }
        } 
        sc.close();
    } 



    /*========================================================================================
        SSS Computation (Method # 1) [rosella]
    ==========================================================================================*/

        /**
        * Computes the SSS contribution of an employee based on their combined
        * monthly gross salary.
        *
        * Algorithm:
        * The SSS contribution table is stored in sss.csv file. Each row in the file
        * defines a salary bracket (rangeFrom to rangeTo) and the corresponding
        * employee share for that bracket. The method reads the table row by row
        * and returns the employee share as soon as a bracket match is found.
        * The last bracket uses the keyword "Over" instead of a numeric upper bound,
        * meaning any salary at or above that floor falls into the maximum bracket.
        * 'lastEmployeeShare' is kept as a fallback in case no exact bracket matches,
        * which should not happen with a correctly formatted SSS table.
        *
        * Process Flow (Government Deductions):
        * - SSS is deducted on the second cutoff only.
        * - The 1st and 2nd cutoff gross amounts are added together first to get
        *   the monthly gross, which is then used to look up the correct bracket.
        *
        * @param monthlyGross the combined gross salary of both cutoffs for the month
        * @return the employee's SSS contribution amount based on their salary bracket
        */
   
    public static double computeSSS(double monthlyGross) {

        // Holds the most recently read employee share while looping through brackets.
        // This acts as a safety fallback if the loop finishes without a bracket match.
        double lastEmployeeShare = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(SSS_FILE))) {

            br.readLine(); // skip header row of the SSS table
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue; // skip any blank lines in the file

                String[] data = line.split(",");
                double rangeFrom = Double.parseDouble(data[0].trim()); // column 0: lower bound of salary bracket
                String rangeToText = data[1].trim(); // column 1: upper bound, may be "Over" for the last bracket
                double employeeShare = Double.parseDouble(data[3].trim()); // column 3: SSS employee contribution for this bracket

                // Keep track of the current share so we have a value to return
                // if we reach the end of the table without an exact bracket match
                lastEmployeeShare = employeeShare;

                // "Over" means there is no upper limit — any salary at or above
                // rangeFrom qualifies for this maximum bracket
                if (rangeToText.equalsIgnoreCase("Over")) {
                    if (monthlyGross >= rangeFrom) {
                        return employeeShare;
                    }

                } else {
                    double rangeTo = Double.parseDouble(rangeToText);

                    // Check if the monthly gross falls within this SSS salary bracket
                    if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                        return employeeShare; // return the matched contribution amount
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the last bracket's share as a fallback if no range matched
        return lastEmployeeShare;
        
    }



    /*========================================================================================
        Pag-ibig Computation (Method # 2) [rosella]
    ==========================================================================================*/

        /**
        * Computes the Pag-IBIG contribution of an employee based on their combined
        * monthly gross salary.
        *
        * Algorithm:
        * The Pag-IBIG contribution table is stored in pagibig.csv file. Each row defines
        * a salary range and a contribution rate. The method multiplies the monthly
        * gross by the rate of the matching bracket to get the raw contribution.
        * Per government rules, the employee's Pag-IBIG contribution is capped at
        * a maximum of PHP 100.00 per month regardless of how high the salary is.
        * 'Math.min()' enforces this cap without needing a separate if-condition.
        *
        * Process Flow (Government Deductions):
        * - Pag-IBIG is deducted on the second cutoff only.
        * - The 1st and 2nd cutoff gross amounts are added together first to get
        *   the monthly gross, which is then used to find the applicable rate.
        *
        * @param monthlyGross the combined gross salary of both cutoffs for the month
        * @return the employee's Pag-IBIG contribution, capped at PHP 100.00
        */

    public static double computePagibig(double monthlyGross) {

        double contribution = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(PAGIBIG_FILE))) {

            br.readLine(); // skip header row of the Pag-IBIG table
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue; // skip any blank lines in the file

                // Regex split handles quoted fields that contain commas (e.g., "At least 1,000 to 1,500")
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (data.length < 2) continue; // skip incomplete rows (e.g., NOTE line)

                // Column 0: salary range text — strip surrounding quotes before parsing
                String salaryRange = data[0].trim().replace("\"", "");

                // Column 1: employee contribution rate — e.g., "1%" or "2%"
                String rateText = data[1].trim();

                // Skip rows that are not valid bracket entries (empty cells, NOTE line, etc.)
                if (salaryRange.isEmpty() || rateText.isEmpty() || !rateText.endsWith("%")) continue;

                // Convert rate from percentage string to decimal: "1%" → 0.01
                double rate = Double.parseDouble(rateText.replace("%", "").trim()) / 100.0;

                // --- Parse the salary range text and check if monthlyGross falls in it ---

                if (salaryRange.toLowerCase().startsWith("over")) {
                    // Format: "Over 1,500"
                    // Extract the floor value and check if gross exceeds it
                    String floorText = salaryRange.substring("over".length()).trim().replace(",", "");
                    double floor = Double.parseDouble(floorText);

                    if (monthlyGross > floor) {
                        contribution = monthlyGross * rate; // raw contribution before cap
                        break; // stop once correct bracket is found
                    }

                } else if (salaryRange.toLowerCase().startsWith("at least")) {
                    // Format: "At least 1,000 to 1,500"
                    // Strip "At least" prefix, then split on " to " to get lower and upper bounds
                    String rangeOnly = salaryRange.substring("at least".length()).trim();
                    String[] parts = rangeOnly.split("(?i)\\s+to\\s+");
                    double rangeFrom = Double.parseDouble(parts[0].trim().replace(",", ""));
                    double rangeTo   = Double.parseDouble(parts[1].trim().replace(",", ""));

                    if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                        contribution = monthlyGross * rate; // raw contribution before cap
                        break; // stop once correct bracket is found
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Per government rule: Pag-IBIG employee contribution cannot exceed PHP 100.00/month.
        // 'Math.min' returns whichever is smaller — either the computed contribution or 100.
        return Math.min(contribution, 100); 
    }



    /* =======================================================================================
        PhilHealth Computation (Method # 3) [ann]
    ==========================================================================================*/

        /**
        * Computes the PhilHealth contribution of an employee based on their combined
        * monthly gross salary.
        *
        * Algorithm:
        * PhilHealth uses a fixed-rate premium system with three salary brackets.
        * The employee pays only half of the total premium — the other half is
        * covered by the employer. The three brackets and their employee shares are:
        *   - Monthly gross ≤ 10,000 : fixed PHP 150.00 (half of PHP 300 flat rate)
        *   - Monthly gross 10,001 – 59,999 : 1.5% of monthly gross (half of the 3% total rate)
        *   - Monthly gross ≥ 60,000 : fixed PHP 900.00 (half of PHP 1,800 ceiling)
        *
        * Process Flow (Government Deductions):
        * - PhilHealth is deducted on the second cutoff only.
        * - The 1st and 2nd cutoff gross amounts are added together first before
        *   determining which bracket applies.
        *
        * @param monthlyGross the combined gross salary of both cutoffs for the month
        * @return the employee's share of the PhilHealth contribution
        */

    public static double computePhilhealth (double monthlyGross) {

        // philhealthDeduction holds the result to be returned.
        double philhealthDeduction = 0.0;

        // Apply the correct PhilHealth bracket based on the employee's monthly gross salary
        if (monthlyGross <= 10000) {

            // Bracket 1: salaries at or below 10,000 pay a flat premium of 300;
            // employee pays half = 150
            philhealthDeduction = 300/2;

        } else if (monthlyGross > 10000 && monthlyGross < 60000){

            // Bracket 2: salaries between 10,001 and 59,999 are charged 3% of gross;
            // employee pays half of that = 1.5% of gross
            philhealthDeduction =  monthlyGross*(0.03)/2;

        } else if (monthlyGross >= 60000) {

            // Bracket 3: salaries at or above 60,000 hit the ceiling premium of 1,800;
            // employee pays half = 900 (contribution does not increase beyond this)
            philhealthDeduction = 1800/2;
            }

        // Returns the computed employee share of PhilHealth contribution
        return philhealthDeduction; 
    }



    /* =======================================================================================
        Tax Computation (Method # 4) [ann]
    ==========================================================================================*/

        /**
        * Computes the monthly withholding tax of an employee using the BIR tax table.
        *
        * Algorithm:
        * The BIR (Bureau of Internal Revenue) withholding tax is NOT computed on the
        * gross salary directly. The taxable salary must first be derived by subtracting
        * all government contributions (SSS + PhilHealth + Pag-IBIG) from the monthly
        * gross. Only the resulting taxable salary is matched against the six BIR brackets:
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

        // Taxable salary = monthly gross minus all mandatory government contributions.
        // This step follows the BIR rule that contributions are pre-tax deductions —
        // the employee is only taxed on what remains after contributions are removed.
        double taxableMonthlySalary = totalGross - totalContribution; 
        
        // Apply the BIR tax bracket that matches the computed taxable salary
        if (taxableMonthlySalary <= 20832) {

            // Bracket 1: fully exempt — no withholding tax
            tax = 0.00;

        } else if (taxableMonthlySalary >= 20833 && taxableMonthlySalary < 33333) {

            // Bracket 2: 20% applied only to the amount exceeding the floor of 20,833
            tax = (taxableMonthlySalary-20833)*0.2;

        } else if (taxableMonthlySalary >= 33333 && taxableMonthlySalary < 66667) {

            // Bracket 3: fixed base of 2,500 plus 25% on the excess over 33,333
            tax = 2500+(taxableMonthlySalary-33333)*0.25; // 2,500 + 25% on the excess over 33,333

        } else if (taxableMonthlySalary >= 66667 && taxableMonthlySalary < 166667) {

            // Bracket 4: fixed base of 10,833 plus 30% on the excess over 66,667
            tax = 10833+(taxableMonthlySalary-66667)*0.30;

        } else if (taxableMonthlySalary >= 166667 && taxableMonthlySalary < 666667) {

            // Bracket 5: fixed base of 40,833.33 plus 32% on the excess over 166,667
            tax = 40833.33+(taxableMonthlySalary-166667)*0.32;

        } else if (taxableMonthlySalary >= 666667) {

            // Bracket 6: fixed base of 200,833.33 plus 35% on the excess over 666,667
            tax = 200833.33+(taxableMonthlySalary-666667)*0.35;
        }

        // Returns the final computed withholding tax amount
        return tax;
    }  
 


    /* =======================================================================================
        Hours Worked Computation (Method # 5) [ann]
    ==========================================================================================*/

        /**
        * Computes the total hours worked by an employee for a single attendance record (one day).
        *
        * Algorithm — three rules are applied in this order:
        *
        * Rule 1 — Overtime cap:
        *   The workday officially ends at 5:00 PM. Any time logged after 5:00 PM is
        *   not counted. If the employee logs out after 5:00 PM, the logout time is
        *   replaced with 5:00 PM before any calculation is done.
        *   Example: if employee logs out 5:30 PM, treated as 5:00 PM.
        *
        * Rule 2 — Grace period:
        *   Employees who log in at or before 8:10 AM are considered on time and their
        *   login time is adjusted to the standard start of 8:00 AM for computation.
        *   This prevents minor early arrivals from inflating hours and prevents logins
        *   between 8:01–8:10 from being penalized.
        *   Example: if employee logs in 8:05 AM, treated as 8:00 AM.
        *   Example: if employee logs in 8:30 AM, actual time used (after grace period, no adjustment).
        *
        * Rule 3 — Lunch break deduction:
        *   A mandatory 1-hour (60-minute) unpaid lunch break is always deducted.
        *   The deduction only applies if the employee worked more than 60 minutes.
        *   If total time is 60 minutes or less, the result is 0 productive hours.
        *
        * Combined example (Rule 2 + Rule 3 applied together):
        *   Login: 8:05 AM adjusted to 8:00 AM (within grace period)
        *   Logout: 4:30 PM no cap needed (before 5:00 PM)
        *   Raw duration: 8:00 AM to 4:30 PM = 8 hours 30 minutes = 510 minutes
        *   After lunch deduction: 510 − 60 = 450 minutes = 7.5 hours worked
        *
        * @param logIn  the employee's raw login time from the attendance CSV
        * @param logOut the employee's raw logout time from the attendance CSV
        * @return total hours worked as a decimal (e.g., 7.5 = 7 hours and 30 minutes)
        */

    public static double computeHoursWorked(LocalTime logIn, LocalTime logOut) {
        
        LocalTime gracePeriod = LocalTime.of(8, 10); // grace period ends at 8:10 AM (inclusive)
        LocalTime cutoffTime = LocalTime.of(17, 0); // official end of workday: 5:00 PM
        LocalTime standardStart = LocalTime.of(8, 0); // standard start of workday: 8:00 AM

        // Rule 1: If the employee logged out after 5:00 PM, cap the logout at 5:00 PM.
        // This ensures overtime is not counted in the hours worked calculation.
        if (logOut.isAfter(cutoffTime)) {
            logOut = cutoffTime;
        }

        // Rule 2: If the employee logged in at or before the grace period (8:10 AM),
        // treat the login as the standard start time (8:00 AM). This covers both
        // exact 8:00 AM logins and early arrivals up to 8:10 AM.
        if (!logIn.isAfter(gracePeriod)) {
            logIn = standardStart;
        }

        // Calculate the raw duration between the (adjusted) login and logout times
        long  minutesWorked = Duration.between(logIn, logOut).toMinutes();

        // Rule 3: Deduct the mandatory 60-minute lunch break.
        // The deduction only applies if the employee was present for more than an hour.
        // If 60 minutes or less were logged, no productive work time is counted.
        int lunchBreak = 60;
        if (minutesWorked > lunchBreak) {
            minutesWorked -= lunchBreak;
        } else {
            minutesWorked = 0;
        }

        // Convert remaining minutes to hours and return
        return minutesWorked / 60.0;
            
    }



    /*========================================================================================
        Gross Computation (Method # 6) [ann]
    ==========================================================================================*/

        /**
        * Computes the gross salary for a single cutoff period.
        *
        * Algorithm:
        * Gross salary is calculated by multiplying the total hours worked in the
        * cutoff period by the employee's hourly rate. This method is called twice
        * per month — once for the first cutoff (days 1–15) and once for the second
        * cutoff (days 16–end of month).
        *
        * Per process flow: allowances are not included in the gross salary.
        * Only hours worked multiplied by the hourly rate is counted.
        *
        * @param hours the total number of hours worked during the cutoff period
        * @param rate  the employee's hourly rate (read from column 18 of details.csv)
        * @return the gross salary for the cutoff period
        */

    static double computeGross(double hours, double rate) {
        return hours * rate;

    }



    /*========================================================================================
        Payroll Computation and Display (Method # 7) [rosella]
    ==========================================================================================*/

        /**
        * Computes and displays the full payroll report for a single employee,
        * covering all months from June to December 2024.
        *
        * Algorithm — Per Month (June to December):
        * 1. Loop through all attendance records and filter those belonging to this
        *    employee in the current month.
        * 2. For each matching record, parse the login and logout times, compute
        *    hours worked using computeHoursWorked(), and accumulate into the correct
        *    cutoff period: days 1–15 = firstHalf, days 16–end = secondHalf.
        * 3. Compute gross salary for each cutoff: hours × hourly rate.
        * 4. Combine both cutoff gross amounts into monthlyGross. Government deductions
        *    (SSS, PhilHealth, Pag-IBIG, Tax) are all computed from this combined figure,
        *    not from the second cutoff gross alone. This follows the process flow rule
        *    that says "add the 1st and 2nd cutoff amounts first before computing deductions."
        * 5. Net salary on the second cutoff = secondHalf gross − totalDeductions.
        *    The first cutoff has no deductions, so its net equals its gross.
        *
        * Process Flow Output Per Cutoff:
        *   First Cutoff  (1–15):  Total Hours Worked, Gross Salary, Net Salary
        *   Second Cutoff (16–30): Total Hours Worked, Gross Salary, SSS, PhilHealth,
        *                          Pag-IBIG, Tax, Total Deductions, Net Salary
        *
        * @param employeeNo is the employee number (consistent name used throughout)
        * @param lastName is the employee's last name
        * @param firstName is the employee's first name
        * @param birthday is the employee's birthday
        * @param rate is the employee's hourly rate from column 18 of details.csv
        * @param attendanceRecords all attendance records pre-loaded into memory
        * @param timeFormat is DateTimeFormatter for parsing H:mm time values from the CSV
        */


    public static void processPayroll(String employeeNo, String lastName, String firstName,
                                      String birthday, double rate,
                                      List<String[]> attendanceRecords,
                                      DateTimeFormatter timeFormat) {

        // Display the employee's basic header information
        System.out.println("\n==================================== ");
        System.out.println("           Employee Payroll          ");
        System.out.println("==================================== ");
        System.out.println("Employee # : "    + employeeNo);
        System.out.println("Employee Name : " + lastName + ", " + firstName);
        System.out.println("Birthday : "      + birthday);
        System.out.println("===================================\n");

        // Iterate over each month from June (6) to December (12) per process flow requirement
        for (int month = 6; month <= 12; month++) {

            double firstHalf  = 0; // accumulates hours worked from days 1–15 (first cutoff)
            double secondHalf = 0; // accumulates hours worked from days 16–end (second cutoff)

            // 'YearMonth.lengthOfMonth()' returns the actual last calendar day of the month
            // (e.g., 30 for June, 31 for July) — used for the second cutoff end date display
            int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();

            // Scan all attendance records and pick only those that belong to this
            // employee and fall within the current month of 2024
            for (String[] data : attendanceRecords) {

                // Column 0 of the attendance CSV is the employee number —
                // skip this record if it does not belong to the current employee
                if (!data[0].equals(employeeNo)) continue;

                // Column 3 is the date in MM/DD/YYYY format — split by "/" to extract parts
                String[] dateParts = data[3].split("/");
                int recordMonth = Integer.parseInt(dateParts[0]); // month portion of the date
                int day = Integer.parseInt(dateParts[1]); // day portion of the date
                int year = Integer.parseInt(dateParts[2]); // year portion of the date

                // Skip records that are outside the current loop's month or year
                if (year != 2024 || recordMonth != month) continue;

                // Column 4 = login time, Column 5 = logout time — both in H:mm format
                LocalTime login  = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);

                // Apply grace period, overtime cap, and lunch break rules for this day
                double hours = computeHoursWorked(login, logout);

                // Add to the correct cutoff bucket based on the day of the month
                if (day <= 15) firstHalf  += hours; // days 1–15 belong to the first cutoff
                else secondHalf += hours; // days 16 and beyond belong to the second cutoff
            }

            // Compute gross salary for each cutoff: total hours × hourly rate
            double grossFirst = computeGross(firstHalf,  rate);
            double grossSecond = computeGross(secondHalf, rate);

            // Combine both cutoffs into the monthly gross.
            // Government deductions are computed from this combined figure, not from either
            // cutoff alone. This follows the process flow rule: "add 1st and 2nd cutoff
            // amounts first before computing deductions."
            double monthlyGross = grossFirst + grossSecond;

            // Compute each government deduction using the combined monthly gross
            double sss = computeSSS(monthlyGross);
            double pagibig = computePagibig(monthlyGross);
            double philhealth = computePhilhealth(monthlyGross);

            // totalContribution is used as the input to withholdingTax() because BIR rules
            // require that contributions be deducted from gross before tax is computed
            double totalContribution = sss + philhealth + pagibig;
            double tax = withholdingTax(monthlyGross, totalContribution);

            // Total deductions = all four government contributions added together
            double totalDeductions = sss + pagibig + philhealth + tax;

            // Net salary = second cutoff gross minus all deductions.
            // Deductions are applied on the second payout only, per process flow.
            double netSalary = grossSecond - totalDeductions;

            // Convert the numeric month to its full name string for display purposes
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

            /*--------------------------------------------------------------------
                Display First Cutoff (e.g., June 1 to June 15)
                No government deductions are applied on the first cutoff.
                Net Salary equals Gross Salary for this payout period.
            ---------------------------------------------------------------------*/
            System.out.println("\nFirst Cutoff");
            System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
            System.out.println("Total Hours Worked : " + firstHalf);
            System.out.println("Gross Salary: " + grossFirst);
            System.out.println("Net Salary: " + grossFirst); // no deductions on first cutoff

            /*--------------------------------------------------------------------
                Display Second Cutoff (e.g., June 16 to June 30)
                All government deductions (SSS, PhilHealth, Pag-IBIG, Tax) are
                applied here. daysInMonth is used for the correct end date.
            ---------------------------------------------------------------------*/
            System.out.println("\nSecond Cutoff");
            System.out.println("\nCutoff Date: " + monthName + " 16 to " + daysInMonth);
            System.out.println("Total Hours Worked : " + secondHalf);
            System.out.println("Gross Salary: " + grossSecond);
            System.out.println("    SSS: " + sss);
            System.out.println("    PhilHealth: " + philhealth);
            System.out.println("    Pag-IBIG: " + pagibig);
            System.out.println("    Tax: " + tax);
            System.out.println("Total Deductions: " + totalDeductions);
            System.out.println("Net Salary: " + netSalary);
            System.out.println("-----------------------------------\n");
        }

        System.out.println("\n===================================");
        System.out.println("          END OF RECORD");
        System.out.println("=====================================");
    }


              
        
    /*========================================================================================
        For One Employee (Method # 8) [rosella]
    ==========================================================================================*/

        /**
        * Processes and displays the payroll report for a single employee.
        *
        * Algorithm:
        * 1. Opens the employee CSV and searches line by line for the row whose
        *    first column matches the given employeeNo.
        *    - The regex split pattern ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)" is used
        *      instead of a plain "," split to handle fields that contain commas
        *      inside quoted values (e.g., "Dela Cruz, Juan" in a name field).
        *    - If no match is found, the method prints an error and returns early
        *      to stop execution — it does not proceed to load attendance data.
        * 2. Loads all attendance records from the attendance CSV into a List once.
        *    Loading into memory before the monthly loop avoids reopening the file
        *    for every month (June through December), which would be 7 file reads
        *    per employee instead of just 1.
        * 3. Delegates the actual payroll computation and display to 'processPayroll()'.
        *
        * Process Flow (payroll_staff to Process Payroll to One Employee):
        * - If employee not found: display "Employee number does not exist." and stop.
        * - If found: display payroll records from June to December with both cutoffs.
        *
        * @param employeeNo the employee number entered by the payroll staff in main()
        */


    public static void oneEmployee(String employeeNo) {

        // H:mm handles both single-digit hours (e.g., 8:05) and double-digit (e.g., 17:00)
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

        // Variables to store the matched employee's details from the employee CSV file
        String lastName = "";
        String firstName = "";
        String birthday = "";
        boolean found = false;
        double rate = 0;

        // Search the employee file for a record matching the entered employee number
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            br.readLine(); // skip header row
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip blank lines in the CSV

                // This regex split handles commas inside quoted fields correctly.
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Column 0 = Employee Number — compare against the input
                if (data[0].equals(employeeNo)) {
                    employeeNo = data[0]; // Column 1: Last Name
                    lastName = data[1]; // Column 2: First Name
                    firstName = data[2]; // Column 3: Birthday
                    birthday = data[3];
                    rate = Double.parseDouble(data[18].trim()); // Column 18: Hourly Rate
                    found = true;
                    break; // if match found, stop reading the file
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }

        // Per process flow: if the employee number does not exist, show the message
        // and return immediately — do not proceed to load attendance or compute payroll
        if (!found) {
            System.out.println("\nEmployee number does not exist.\n");
            return;
        }

        // Load all attendance records into a List before the monthly loop.
        // (avoids repeatedly reopening the file for each month — June to December)
        List<String[]> attendanceRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ATT_FILE))) {
            br.readLine(); // skip header row
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())

                    // Same regex split as above — needed because attendance fields
                    // may also contain quoted values with embedded commas
                    attendanceRecords.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            }

        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
        }

        // Hand off to the shared payroll method which handles the monthly loop,
        // deduction computations, and all output display
        processPayroll(employeeNo, lastName, firstName, birthday, rate, attendanceRecords, timeFormat);
    }



    /* =======================================================================================
        For All Employee (Method # 9) [rosella]
    ==========================================================================================*/

        /**
        * Processes and displays the payroll report for every employee in the CSV file.
        *
        * Algorithm:
        * 1. Reads all employee records from details.csv into a 'List<String[]>'.
        *    Each element of the list is one employee's row of data.
        * 2. Loads all attendance records from attendance.csv into a separate
        *    'List<String[]>' once, before the employee loop begins.
        *    This is a deliberate performance decision: if attendance were loaded
        *    inside the loop, the file would be reopened for every employee AND
        *    for every month (up to 34 × 7 = 238 file reads). Loading once and
        *    passing the in-memory list to 'processPayroll()' reduces this to 1 read.
        * 3. For each employee, delegates to 'processPayroll()' which filters the
        *    pre-loaded attendance list to find that employee's records.
        *
        * Process Flow (payroll_staff to Process Payroll to All Employees):
        * - Follows the same output format as 'oneEmployee()' from (Method #8).
        * - Automatically processes all employees without requiring an employee number.
        *
        * This method takes no parameters because it processes all employees by design.
        */


    public static void allEmployee() {

        // H:mm handles both single-digit hours (e.g., 8:05) and double-digit (e.g., 17:00)
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

        // This list will hold one String[] per employee row read from details.csv file
        List<String[]> employees = new ArrayList<>();

        // Step 1: Read and store all employee records from the employee file
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            br.readLine(); // skip header row
            String line;

            while ((line = br.readLine()) != null) {

                // Regex split handles fields that contain commas inside quoted values
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                employees.add(data);
            }

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }

        // Step 2: Load all attendance records into memory once before the employee loop.
        // Passing this pre-loaded list to processPayroll() for the attendance file
        // to be read only once for all employees, not once per employee per month.
        List<String[]> attendanceRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ATT_FILE))) {
            br.readLine(); // skip header row
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    attendanceRecords.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
        }

        // Step 3: Loop through each employee and delegate payroll computation
        // and display to the shared processPayroll() method
        for (String[] employeeData : employees) {
            String employeeNo = employeeData[0]; // Column 0: Employee Number
            String lastName = employeeData[1]; // Column 1: Last Name
            String firstName = employeeData[2]; // Column 2: First Name
            String birthday = employeeData[3]; // Column: Birthday
            double rate = Double.parseDouble(employeeData[18].trim()); // hourly rate at column index 18

            processPayroll(employeeNo, lastName, firstName, birthday, rate, attendanceRecords, timeFormat);
        }
    }
}
