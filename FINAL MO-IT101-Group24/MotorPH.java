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

    //A single Scanner is declared at the class level and shared by all methods.
    static Scanner sc = new Scanner(System.in);

    //Program constants are defined here instead of writing directly inside the methods for accessibility purposes.
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


        // Prompts the user to enter their credentials
        System.out.println("\n==================================== ");
        System.out.println("        MotorPH Login System         ");
        System.out.println("==================================== ");
        System.out.print("\nEnter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String inputPassword = sc.nextLine();

        // Evaluate both username and password together in a single check per role.
        boolean isEmployee     = username.equals(EMP_USERNAME)     && inputPassword.equals(PASSWORD);
        boolean isPayrollStaff = username.equals(PAYROLL_USERNAME) && inputPassword.equals(PASSWORD);

        // If neither flag true, the program shall display an error message and terminate immediately.
        if (!isEmployee && !isPayrollStaff) {
            System.out.println("\nIncorrect username and/or password.\n");
            sc.close();
            System.exit(0);
        }

        if (isEmployee) { // Employee login block
            System.out.println("\nEmployee login successful.");
            String option = "";

            // Displays the employee menu after a successful login.
            System.out.println("\n==================================== ");
            System.out.println("\n1. View Employee Details ");
            System.out.println("2. Exit program");
            System.out.print("Choose Option: ");
            option = sc.nextLine();
            System.out.println("==================================== \n");

            // Option 1: Asks the user for a valid employee number and displays the employee details if found.
            if (option.equals("1")){
                System.out.print("Enter Your Employee Number: ");

                // These variables will hold the matched employee's data from CSV file once found.
                String employeeNo = sc.nextLine(); 
                String  lastName  = "";
                String  firstName = "";
                String  birthday  = "";
                boolean found     = false;
                                
                // Opens the employee CSV file and search for a row whose first column matches the employee number entered by the user
                try (BufferedReader br = new BufferedReader (new FileReader (EMP_FILE))){

                    br.readLine(); // skips header row
                    String line;

                    while ((line = br.readLine()) !=null){
                        if(line.trim().isEmpty()) continue; // skip blank lines in the CSV file

                        // Split the line by comma to access individual columns
                        String[] data = line.split(",");

                        //Column 0: Employee Number to be compared with what the user typed.
                        if (data[0].equals(employeeNo)){
                            lastName = data[1]; // Column 1: Last Name
                            firstName  = data[2]; // Column 2: First Name
                            birthday = data[3]; // Column 3: Birthday
                            found = true;
                            break; // stops searching once a match is found
                        }
                    }

                } catch (IOException e) {
                    System.out.println("\nEmployee file error.\n"); // If something goes wrong with the file, the program will print this instead of crashing.
                }
                
                if (found){
                    System.out.println("\n==================================== ");
                    System.out.println( "        Employee Information");
                    System.out.println("==================================== ");
                    System.out.println("\nEmployee #: " + employeeNo);
                    System.out.println("Employee Name: " + lastName + ", " + firstName);
                    System.out.println("Employee Birthday: " + birthday);
                    System.out.println("====================================\n");
                    
                    } else {
                        System.out.println("\nEmployee number does not exist.\n"); // If no record match was found, the program shall display this message instead of crashing.
                    }
                
                // Option 2: Closes the scanner and terminates the program.
            } else if (option.equals("2")){
                System.out.println("\nExiting program.\n");
                sc.close();
                System.exit(0);

                // Added feature: Any input other than "1" or "2" is not a valid menu option. 
                } else {
                    System.out.println("\nInvalid option. Please enter 1 or 2.\n");
                    sc.close();
                    System.exit(0);
                }
  
            } else { // Payroll staff login block.

                System.out.println("\nPayroll staff Login successful!");;

                // Display the payroll staff menu after a successful login.
                System.out.println("\n==================================== ");
                System.out.println("\n1. Process Payroll");
                System.out.println("2. Exit program");
                System.out.print("Choose Option: ");
                String option = sc.nextLine();
                System.out.println("\n====================================");
                
                // Option 1: Grants user an access to process the payroll of a single or all employees, and a choice to terminate the program.
                if (option.equals("1")) {
                    System.out.println("\n1. View One Employee");
                    System.out.println("2. View All Employees");
                    System.out.println("3. Exit program");
                    System.out.print("Choose Sub-option: ");
                    String subOption = sc.nextLine();
                    
                    // Sub-option 1: Asks the user for a valid employee number and displays the full payroll computation processed in oneEmployee() method.
                    if (subOption.equals("1")){
                        System.out.print("\nEnter Employee Number: ");
                        String employeeNo = sc.nextLine();
                        System.out.println("\n====================================\n");
                        oneEmployee(employeeNo); // standardized name: employeeNo

                    // Sub-option 2: Displays the full payroll computation of all employees processed in allEmployee() method.
                    } else if (subOption.equals("2")){
                        allEmployee(); // delegate payroll processing to allEmployee()

                    // Sub-option 3: Exits the program
                    } else if (subOption.equals("3")){
                        System.out.println("\nExiting program.\n");
                        sc.close();
                        System.exit(0);

                    // Added feature: Any input other than "1", "2", or "3" is not valid 
                    } else {
                        System.out.println("\nInvalid option. Please enter 1, 2, or 3.\n");
                    }

                // Option 2: Exits the program.
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

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue; // skip any blank lines in the file

                String[] data = line.split(",");
                double rangeFrom = Double.parseDouble(data[0].trim()); // column 0: lower bound of salary bracket
                String rangeToText = data[1].trim(); // column 1: upper bound, may be "Over" for the last bracket
                double employeeShare = Double.parseDouble(data[3].trim()); // column 3: SSS employee contribution for this bracket

                // Keep track of the current share so we have a value to return if we reach the end of the table without an exact bracket match.
                lastEmployeeShare = employeeShare;

                // "Over" means there is no upper limit — any salary at or above rangeFrom qualifies for this maximum bracket.
                if (rangeToText.equalsIgnoreCase("Over")) {
                    if (monthlyGross >= rangeFrom) {
                        return employeeShare;
                    }

                } else {
                    double rangeTo = Double.parseDouble(rangeToText);

                    if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                        return employeeShare; // return the matched contribution amount
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lastEmployeeShare; // Return the last bracket's share as a fallback if no range matched.
        
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

            br.readLine();
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
                    // Strips "At least" prefix, then split on " to " to get lower and upper bounds
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

        return Math.min(contribution, 100); // returns whichever is smaller — either the computed contribution or 100.
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

        // Apply the correct PhilHealth bracket based on the employee's monthly gross salary and divide in half to compute the employee share.
        if (monthlyGross <= 10000) {
            philhealthDeduction = 300/2;

        } else if (monthlyGross > 10000 && monthlyGross < 60000){
            philhealthDeduction =  monthlyGross*(0.03)/2;

        } else if (monthlyGross >= 60000) {
            philhealthDeduction = 1800/2; // over 60,000 gross salary has a fixed rate of 1800; employee pays half of it, hence the ' /2'.
            }

        return philhealthDeduction; // Returns the computed employee share of PhilHealth contribution
    }


    /* =======================================================================================
        Tax Computation (Method # 4) [ann]
    ==========================================================================================*/

        /**
        * Computes the monthly withholding tax of an employee using the BIR tax table.
        *
        * Algorithm:
        * The BIR withholding tax is computed after deducting all of the mandatory 
        * government contributions to the monthly gross salary.
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
        double taxableMonthlySalary = totalGross - totalContribution; 
        
        // Apply the BIR tax bracket that matches the computed taxable salary
        if (taxableMonthlySalary <= 20832) {
            tax = 0.00;

        } else if (taxableMonthlySalary >= 20833 && taxableMonthlySalary < 33333) {
            tax = (taxableMonthlySalary-20833)*0.2;

        } else if (taxableMonthlySalary >= 33333 && taxableMonthlySalary < 66667) {
            tax = 2500+(taxableMonthlySalary-33333)*0.25;

        } else if (taxableMonthlySalary >= 66667 && taxableMonthlySalary < 166667) {
            tax = 10833+(taxableMonthlySalary-66667)*0.30;

        } else if (taxableMonthlySalary >= 166667 && taxableMonthlySalary < 666667) {
            tax = 40833.33+(taxableMonthlySalary-166667)*0.32;

        } else if (taxableMonthlySalary >= 666667) {
            tax = 200833.33+(taxableMonthlySalary-666667)*0.35;
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

        long minutesWorked = Duration.between(logIn, logOut).toMinutes();

        // Rule 3: Subtract the mandatory lunch break; if 60 min or less was logged, result is 0
        if (minutesWorked > LUNCH_BREAK) {
            minutesWorked -= LUNCH_BREAK;
        } else {
            minutesWorked = 0;
        }

        return minutesWorked / 60.0;            
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

        // June (6) to December (12) per process flow requirement
        for (int month = 6; month <= 12; month++) {

            double firstHalf  = 0; // hours worked days 1–15
            double secondHalf = 0; // hours worked days 16–end

            // lengthOfMonth() gives the correct last day (e.g., 30 for June, 31 for July)
            int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();

            for (String[] data : attendanceRecords) {

                if (!data[0].equals(employeeNo)) continue; // Column 0 = employee number

                // Column 3 is the date in MM/DD/YYYY format — split to get month, day, and year
                String[] dateParts   = data[3].split("/");
                int      recordMonth = Integer.parseInt(dateParts[0]);
                int      day         = Integer.parseInt(dateParts[1]);
                int      year        = Integer.parseInt(dateParts[2]);

                if (year != 2024 || recordMonth != month) continue;

                // Column 4 = login time, Column 5 = logout time — both in H:mm format
                LocalTime login  = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);

                double hours = computeHoursWorked(login, logout);

                if (day <= 15) firstHalf  += hours; // days 1–15: first cutoff
                else           secondHalf += hours; // days 16–end: second cutoff
            }

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

            // Second cutoff — all four government deductions applied here
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

        String  lastName  = "";
        String  firstName = "";
        String  birthday  = "";
        boolean found     = false;
        double  rate      = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            br.readLine(); // skip header row
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Regex split handles commas inside quoted fields (e.g., addresses)
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Column 0 = Employee Number — compare against the input
                if (data[0].equals(employeeNo)) {
                    employeeNo = data[0];                             // Column 0: Employee Number
                    lastName   = data[1];                             // Column 1: Last Name
                    firstName  = data[2];                             // Column 2: First Name
                    birthday   = data[3];                             // Column 3: Birthday
                    rate       = Double.parseDouble(data[18].trim()); // Column 18: Hourly Rate
                    found      = true;
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }

        // Stop here if no matching employee number was found
        if (!found) {
            System.out.println("\nEmployee number does not exist.\n");
            return;
        }

        // Load all attendance records once — avoids reopening the file per month (June–December)
        List<String[]> attendanceRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ATT_FILE))) {
            br.readLine(); // skip header row
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    // Same regex split — attendance fields may also contain commas inside quotes
                    attendanceRecords.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            }

        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
        }

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
            br.readLine(); // skip header row
            String line;

            while ((line = br.readLine()) != null) {
                // Regex split handles commas inside quoted fields (e.g., addresses)
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                employees.add(data);
            }

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }

        // Step 2: Load attendance records once — reused for every employee in the loop below
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

        // Step 3: Process each employee using the pre-loaded attendance list
        for (String[] employeeData : employees) {
            String    employeeNo   = employeeData[0];                             // Column 0: Employee Number
            String    lastName     = employeeData[1];                             // Column 1: Last Name
            String    firstName    = employeeData[2];                             // Column 2: First Name
            String    birthday     = employeeData[3];                             // Column 3: Birthday
            double    rate         = Double.parseDouble(employeeData[18].trim()); // Column 18: Hourly Rate

            processPayroll(employeeNo, lastName, firstName, birthday, rate, attendanceRecords, timeFormat);
        }
    }
}