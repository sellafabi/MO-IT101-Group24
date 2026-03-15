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
         */

    
    public static void main(String[] args) {

        // File path to the employee details CSV (contains Employee #, Name, Birthday, Hourly Rate, etc.)
        String empInfo = "FINAL MO-IT101-Group24/src/details.csv";
            
        Scanner sc = new Scanner(System.in);

        // Credentials for both user roles — per process flow requirements
        String payrollUsername = "payroll_staff";   
        String employeeUsername = "employee";
        String password = "12345";

        // Ask for username and password
        System.out.println("\n==================================== ");
        System.out.println("        MotorPH Login System         ");
        System.out.println("==================================== ");
        System.out.print("\nEnter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String inputPassword = sc.nextLine();

        
        /*------------------------------------------------------------------------
            EMPLOYEE LOGIN
            - Valid username: "employee", password: "12345"
            - Grants access to self-service information lookup
            - Options displayed: (1) View Employee Details, (2) Exit
        -------------------------------------------------------------------------*/

        if (username.equals(employeeUsername) && inputPassword.equals(password)) {
            System.out.println("\nEmployee login successful.");
            String option = "";

            // If credentials correct — display employee menu options
            System.out.println("\n==================================== ");
            System.out.println("\n1. View Employee Details ");
            System.out.println("2. Exit program");
            System.out.print("Choose Option: \n");
            option = sc.nextLine();
            System.out.println("==================================== \n");


            /*--------------------------------------------------------------------
                Option 1: Enter your employee number
                - Ask the employee to enter their employee number.
                - Search the CSV file for a matching record.
                - If found: display Employee #, Employee Name, and Birthday.
                - If not found: display "Employee number does not exist."
            ---------------------------------------------------------------------*/

            if (option.equals("1")){
                System.out.print("Enter Your Employee Number: ");

                // Reusing the existing sc Scanner instance instead of creating a new one
                String inputEmployeeNumber = sc.nextLine(); 

                // Variables to store the matched employee's information from the CSV file
                String employeeNo = ""; 
                String employeeLastName = "";
                String employeeFirstName = "";
                String employeeBirthday = "";
                boolean found = false;
                                

                // Read the employee CSV file and search for the entered employee number
                try (BufferedReader br = new BufferedReader (new FileReader (empInfo))){
                    br.readLine(); // skip header row
                    String line;

                    while ((line = br.readLine()) !=null){
                        if(line.trim().isEmpty()) continue; // skip blank lines
                        String[] data = line.split(",");

                        // Check if this row's employee number matches the input
                        if (data[0].equals(inputEmployeeNumber)){
                            employeeNo = data[0];
                            employeeLastName = data[1];
                            employeeFirstName  = data[2];
                            employeeBirthday = data[3];
                            found = true;
                            break; // stop searching once a match is found
                        }
                    }

                } catch (IOException e) {
                    System.out.println("\nEmployee file error.\n");

                
                // Display employee details if found, otherwise show "Employee number does not exist" message

                // Per process flow: display Employee Number, Employee Name, and Birthday
                } if (found){
                    System.out.println("\n==================================== ");
                    System.out.println( "        Employee Information");
                    System.out.println("==================================== ");
                    System.out.println("\nEmployee #: " + employeeNo);
                    System.out.println("Employee Name: " + employeeLastName + ", " + employeeFirstName);
                    System.out.println("Employee Birthday: " + employeeBirthday);
                    System.out.println("====================================\n");
                    
                    // Per process flow: if employee number does not exist, display this message
                    } else {
                        System.out.println("\nEmployee number does not exist.\n");
                    }
                
            /*--------------------------------------------------------------------
                Option 2: Exit the program
                - Terminate the program immediately.
            ---------------------------------------------------------------------*/

            } else if (option.equals("2")){
                System.out.println("\nExiting program.\n");
                System.exit(0); // program terminated
                
                // Added feature - Invalid option — only 1 or 2 are accepted 
                } else {
                System.out.println("\nInvalid option. Please enter 1 or 2.\n");
                System.exit(0); // program terminated
                }
                        
            }


        /*------------------------------------------------------------------------
            PAYROLL STAFF LOGIN
            - Valid username: "payroll_staff", password: "12345"
            - Grants access to payroll processing for one or all employees
            - Options displayed: (1) Process Payroll, (2) Exit
        -------------------------------------------------------------------------*/

        else if (username.equals(payrollUsername) && inputPassword.equals(password)) { 

            System.out.println("\nPayroll staff Login successful!");
            String option; 
            String subOption;

            // If credentials correct — display payroll staff menu options
            System.out.println("\n==================================== ");
            System.out.println("\n1. Process Payroll");
            System.out.println("2. Exit program");
            System.out.print("Choose Option: ");
            option = sc.nextLine();
            System.out.println("\n====================================");
            
            
            /*--------------------------------------------------------------------
                Option 1: Process Payroll (allowances are NOT included)
                - Display sub-options: (1) One Employee, (2) All Employees, (3) Exit
                - Per process flow: Do not include allowances in payroll computation.
            ---------------------------------------------------------------------*/

            if (option.equals("1")) {
                System.out.println("\n1. View One Employee");
                System.out.println("2. View All Employees");
                System.out.println("3. Exit program");
                System.out.print("Choose Sub-option: ");
                subOption = sc.nextLine();
                System.out.println("\n====================================\n");
                                
                /*----------------------------------------------------------------
                    Sub-option 1: One Employee
                    - Ask payroll staff to enter an employee number.
                    - If incorrect: display "Employee number does not exist."
                    - If correct: display payroll records from June to December.
                      Per cutoff:
                        First Cutoff  (1–15):  Total Hours Worked, Gross, Net Salary
                        Second Cutoff (16–30): Total Hours Worked, Gross, Each
                                               Deduction (SSS, PhilHealth, Pag-IBIG,
                                               Tax), Total Deductions, Net Salary
                -----------------------------------------------------------------*/

                if (subOption.equals("1")){
                System.out.print("Enter Employee Number: ");
                String inputEmployeeNumber = sc.nextLine();
                oneEmployee(inputEmployeeNumber); // delegate payroll processing to oneEmployee()


                    /*------------------------------------------------------------
                        Sub-option 2: All Employees
                        - Follow the same display format as "One Employee" above,
                          but process and display records for every employee.
                    -------------------------------------------------------------*/

                    } else if (subOption.equals("2")){
                        allEmployee(); // delegate payroll processing to allEmployee()


                    /*------------------------------------------------------------
                        Sub-option 3: Exit the program
                    -------------------------------------------------------------*/

                    } else if (subOption.equals("3")){
                        System.out.println("\nExiting program.\n");
                        System.exit(0);

                    // Added feature - Invalid option — only 1 or 2 are accepted 
                    } else {
                        System.out.println("\nInvalid option. Please enter 1, 2, or 3.\n");
                    }

            } else if (option.equals("2")) {
                    System.out.println("\nExiting program.\n");
                    System.exit(0);

            // Added feature - Invalid option — only 1 or 2 are accepted 
            } else {
                System.out.println("\nInvalid option. Please enter 1 or 2.\n");
            }


        /*------------------------------------------------------------------------
            INVALID CREDENTIALS
            - Per process flow: if username and/or password is incorrect,
              display an error message and terminate the program.
        -------------------------------------------------------------------------*/

        } else { 
            System.out.println("\nIncorrect username and/or password.\n");
            System.exit(0); // program terminated

        } 
        sc.close();
    } 



    /*========================================================================================
        SSS Computation (Method # 1) [rosella]
    ==========================================================================================*/

        /**
        * Computes the SSS contribution of an employee based on their monthly gross salary.
        *
        * Process Flow (Government Deductions):
        * - SSS is part of the second cutoff deductions.
        * - The 1st and 2nd cutoff amounts are added first to get the monthly gross,
        *   which is then used to look up the correct SSS bracket.
        * - Reads the SSS contribution table from a CSV file and matches the employee's
        *   gross salary against the salary brackets to return the correct employee share.
        *
        * @param monthlyGross the combined gross salary of both cutoffs for the month
        * @return the employee's SSS contribution amount based on their salary bracket
        */
   
 
    public static double computeSSS(double monthlyGross) {

        String file = "FINAL MO-IT101-Group24/src/sss.csv";
        double lastEmployeeShare = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            br.readLine(); // skip header row of the SSS table
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue; // skip blank lines

                String[] data = line.split(",");
                double rangeFrom = Double.parseDouble(data[0].trim()); // lower bound of salary bracket
                String rangeToText = data[1].trim(); // upper bound, may be "Over" for the last bracket
                double employeeShare = Double.parseDouble(data[3].trim()); // SSS employee share for this bracket

                // Store the current share in case it becomes the last applicable bracket
                lastEmployeeShare = employeeShare;

                // Handle the last bracket which has no upper limit ("Over")
                if (rangeToText.equalsIgnoreCase("Over")) {

                    // If gross meets or exceeds the minimum of the "Over" bracket, apply it
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

        // Fallback: return the last stored share if no bracket was matched
        return lastEmployeeShare;
        
    }



    /*========================================================================================
        Pag-ibig Computation (Method # 2) [rosella]
    ==========================================================================================*/

        /**
        * Computes the Pag-IBIG contribution of an employee based on their monthly gross salary.
        *
        * Process Flow (Government Deductions):
        * - Pag-IBIG is part of the second cutoff deductions.
        * - The 1st and 2nd cutoff amounts are added first to get the monthly gross,
        *   which is then used to determine the applicable Pag-IBIG rate.
        * - Reads the Pag-IBIG table from a CSV file, multiplies the gross by the
        *   applicable rate, and caps the contribution at a maximum of 100 pesos.
        *
        * @param monthlyGross the combined gross salary of both cutoffs for the month
        * @return the employee's Pag-IBIG contribution, capped at 100 pesos
        */


    public static double computePagibig(double monthlyGross) {

        String file = "FINAL MO-IT101-Group24/src/pagibig.csv";
        double contribution = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            br.readLine(); // skip header row of the Pag-IBIG table
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue; // skip blank lines

                String[] data = line.split(",");
                double rangeFrom = Double.parseDouble(data[0].trim()); // lower bound of salary bracket
                double rangeTo = Double.parseDouble(data[1].trim()); // upper bound of salary bracket
                double rate = Double.parseDouble(data[2].trim()); // contribution rate for this bracket

                // Check if the monthly gross falls within this Pag-IBIG salary bracket
                if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {

                    contribution = monthlyGross * rate; // compute contribution by applying the rate
                    break; // stop searching once the correct bracket is found

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Pag-IBIG contribution is capped at a maximum of 100 pesos per month
        return Math.min(contribution, 100); 
    }



    /* =======================================================================================
        PhilHealth Computation (Method # 3) [ann]
    ==========================================================================================*/

        /**
        * Computes the PhilHealth contribution of an employee based on their monthly gross salary.
        *
        * Process Flow (Government Deductions):
        * - PhilHealth is part of the second cutoff deductions.
        * - The 1st and 2nd cutoff amounts are added first to get the monthly gross,
        *   which is then used to determine the PhilHealth bracket.
        * - The employee only shoulders half of the total PhilHealth premium:
        *     - 10,000 and below : fixed 150 pesos (half of 300)
        *     - Between 10,000 and 60,000 : 1.5% of gross salary (half of 3%)
        *     - 60,000 and above : fixed 900 pesos (half of 1,800)
        *
        * @param totalGross the combined gross salary of both cutoffs for the month
        * @return the employee's share of the PhilHealth contribution
        */


    public static double computePhilhealth (double totalGross) {
        double PHdeduction = 0.0;

        // Apply the correct PhilHealth bracket based on the monthly gross salary
        if (totalGross <= 10000) {
            PHdeduction = 300/2; // Fixed premium of 300; employee pays half = 150

            } else if (totalGross > 10000 && totalGross < 60000){
                PHdeduction =  totalGross*(0.03)/2; // 3% of gross salary; employee pays half

            } else if (totalGross >= 60000) {
                PHdeduction = 1800/2; // Fixed maximum premium of 1800; employee pays half = 900
            }

        // Returns the computed employee share of PhilHealth contribution
        return PHdeduction; 
    }



    /* =======================================================================================
        Tax Computation (Method # 4) [ann]
    ==========================================================================================*/

        /**
        * Computes the monthly withholding tax of an employee using the BIR tax table.
        *
        * Process Flow (Government Deductions):
        * - Tax is part of the second cutoff deductions.
        * - Per process flow: add the 1st and 2nd cutoff amounts first, then compute
        *   all deductions (SSS + PhilHealth + Pag-IBIG) before computing the tax.
        * - The taxable salary is derived by subtracting all government contributions
        *   (SSS, PhilHealth, Pag-IBIG) from the total monthly gross.
        * - The six BIR tax brackets are then applied to the taxable salary:
        *     - 20,832 and below       : 0% (exempt)
        *     - 20,833 – 33,332        : 20% on excess over 20,833
        *     - 33,333 – 66,666        : 2,500 + 25% on excess over 33,333
        *     - 66,667 – 166,666       : 10,833 + 30% on excess over 66,667
        *     - 166,667 – 666,666      : 40,833.33 + 32% on excess over 166,667
        *     - 666,667 and above      : 200,833.33 + 35% on excess over 666,667
        *
        * @param totalGross        the combined gross salary of both cutoffs for the month
        * @param totalContribution the total of SSS, PhilHealth, and Pag-IBIG contributions
        * @return the computed withholding tax amount
        */


    public static double withholdingTax (double totalGross, double totalContribution) {
        double tax = 0.00;

        // Step 1: Derive taxable salary — gross minus all government contributions
        // Per process flow: add 1st and 2nd cutoff deductions before computing tax
        double taxableMonthlySalary = totalGross - totalContribution; 
        
        // Step 2: Apply BIR tax brackets to the taxable monthly salary
        if (taxableMonthlySalary <= 20832) {
            tax = 0.00; // exempt from withholding tax

        } else if (taxableMonthlySalary >= 20833 && taxableMonthlySalary < 33333) {
            tax = (taxableMonthlySalary-20833)*0.2; // 20% on the excess over 20,833

        } else if (taxableMonthlySalary >= 33333 && taxableMonthlySalary < 66667) {
            tax = 2500+(taxableMonthlySalary-33333)*0.25; // 2,500 + 25% on the excess over 33,333

        } else if (taxableMonthlySalary >= 66667 && taxableMonthlySalary < 166667) {
            tax = 10833+(taxableMonthlySalary-66667)*0.30; // 10,833 + 30% on the excess over 66,667

        } else if (taxableMonthlySalary >= 166667 && taxableMonthlySalary < 666667) {
            tax = 40833.33+(taxableMonthlySalary-166667)*0.32; // 40,833.33 + 32% on the excess over 166,667

        } else if (taxableMonthlySalary >= 666667) {
            tax = 200833.33+(taxableMonthlySalary-666667)*0.35; // 200,833.33 + 35% on the excess over 666,667
        }

        // Returns the final computed withholding tax amount
        return tax;
    }  
    


    /* =======================================================================================
        Hours Worked Computation (Method # 5) [ann]
    ==========================================================================================*/

        /**
        * Computes the total hours worked by an employee for a single day.
        *
        * Process Flow (Hours Worked Rules):
        * - Do not count extra/overtime hours — workday ends at 5:00 PM.
        * - If the employee logs in at or before 8:10 AM (grace period), login
        *   time is treated as 8:00 AM (not marked as late).
        *   Example: logs in 8:05 AM, logs out 5:00 PM → 8 hours worked.
        * - If the employee logs in after the grace period, the actual login time is used.
        *   Example: logs in 8:30 AM, logs out 5:30 PM → 7.5 hours worked (cap at 5 PM).
        * - A mandatory 1-hour lunch break is always deducted from the total duration.
        *   Example: logs in 8:05 AM, logs out 4:30 PM → 7.5 hours worked.
        *
        * @param logIn  the time the employee logged in
        * @param logOut the time the employee logged out
        * @return the total hours worked as a decimal (e.g., 7.5 for 7 hours 30 minutes)
        */


    public static double computeHoursWorked(LocalTime logIn, LocalTime logOut) {
        
        LocalTime gracePeriod = LocalTime.of(8, 10); // grace period ends at 8:10 AM — logins at/before this are not late
        LocalTime cutoffTime = LocalTime.of(17, 0); // workday ends at 5:00 PM — no overtime counted
        LocalTime standardStart = LocalTime.of(8, 0); // standard start of workday

        // Rule #1: overtime is not counted — cap logout time at 5:00 PM
        if (logOut.isAfter(cutoffTime)) {
            logOut = cutoffTime; // treat 5:00 PM as the effective logout time
        }

        // Rule #2: if employee logged in within the grace period (≤ 8:10 AM), treat as 8:00 AM
        if (!logIn.isAfter(gracePeriod)) {
            logIn = standardStart; // use 8:00 AM as effective login time
        }

        // Calculate total minutes between login and logout
        long  minutesWorked = Duration.between(logIn, logOut).toMinutes();
        int lunchBreak = 60; // mandatory 1-hour lunch break deducted from every workday

            // Deduct lunch break only if the employee worked more than 1 hour
            if (minutesWorked > lunchBreak) {
                minutesWorked -= lunchBreak;

            // If total time is 1 hour or less, no productive hours are counted
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
        * Computes the gross salary for a given cutoff period.
        *
        * Process Flow:
        * - Gross salary = Total Hours Worked × Hourly Rate.
        * - Called separately for the first cutoff (days 1–15) and the second
        *   cutoff (days 16–end of month).
        * - Allowances are NOT included, per process flow requirement.
        *
        * @param hours the total number of hours worked in the cutoff period
        * @param rate  the employee's hourly rate read from the CSV file (column 18)
        * @return the gross salary for the cutoff period
        */


    static double computeGross(double hours, double rate) {

        // Gross = hours worked × hourly rate (allowances excluded per process flow)
        return hours * rate;

    }

              
        
    /*========================================================================================
        For One Employee (Method # 7) [rosella]
    ==========================================================================================*/

        /**
        * Processes and displays the payroll report for a single employee.
        *
        * Process Flow (payroll_staff → Process Payroll → One Employee):
        * 1. Receives the employee number entered by payroll staff in main().
        * 2. Searches the employee CSV file for a matching record.
        *    - If not found: display "Employee number does not exist." and stop.
        *    - If found: display Employee #, Employee Name, and Birthday.
        * 3. Loads all attendance records into memory.
        * 4. For each month from June to December, compute payroll for two cutoffs:
        *
        *    First Cutoff  (June 1 to June 15):
        *      - Total Hours Worked
        *      - Gross Salary
        *      - Net Salary (no deductions on first cutoff)
        *
        *    Second Cutoff (June 16 to June 30) — all deductions applied here:
        *      - Total Hours Worked
        *      - Gross Salary
        *      - SSS deduction
        *      - PhilHealth deduction
        *      - Pag-IBIG deduction
        *      - Tax (withholding tax)
        *      - Total Deductions
        *      - Net Salary
        *
        * Note: Government deductions use the combined monthly gross (1st + 2nd cutoff).
        * @param employeeNumber the employee number entered by the payroll staff in main()
        */


    public static void oneEmployee(String employeeNumber) {

        String empFile = "FINAL MO-IT101-Group24/src/details.csv";
        String attFile = "FINAL MO-IT101-Group24/src/attendance.csv";
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm"); // format for parsing time values in the CSV

        // Variables to store the matched employee's details from the employee CSV file
        String employeeNo = "";
        String firstName = "";
        String lastName = "";
        String birthday = "";
        boolean found = false;
        double rate = 0;

        // Search the employee file for a record matching the entered employee number
        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {
            br.readLine(); // skip header row
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // If a match is found, store employee details from CSV and stop searching
                if (data[0].equals(employeeNumber)) {
                    employeeNo = data[0];
                    lastName = data[1];
                    firstName = data[2];
                    birthday = data[3];
                    rate = Double.parseDouble(data[18].trim()); // hourly rate is at column index 18
                    found = true;
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }

        // Per process flow: if employee number does not exist, display message and stop
        if (!found) {
            System.out.println("Employee number does not exist.");
            return;
        }

        // Display the matched employee's basic information
        System.out.println("\n==================================== ");
        System.out.println("        Employee's Payroll         ");
        System.out.println("==================================== ");
        System.out.println("Employee # : " + employeeNo);
        System.out.println("Employee Name : " + lastName + ", " + firstName);
        System.out.println("Birthday : " + birthday);
        System.out.println("===================================\n");
        

        // Load all attendance records into memory once before the monthly loop
        // (avoids repeatedly reopening the file for each month — June to December)
        List<String[]> attendanceRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(attFile))) {
            br.readLine(); // skip header row
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    attendanceRecords.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            }

        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
        }

        // Process payroll for each month from June (6) to December (12)
        // Per process flow: display all records from June to December
        for (int month = 6; month <= 12; month++) {
            double firstHalf = 0; // total hours worked: days 1–15 (first cutoff)
            double secondHalf = 0; // total hours worked: days 16–end of month (second cutoff)
            int daysInMonth = YearMonth.of(2024, month).lengthOfMonth(); // last day of the month (example: 30th for June)

            // Filter attendance records: match this employee's records for the current month
            for (String[] data : attendanceRecords) {
                if (!data[0].equals(employeeNo)) continue; // skip records not belonging to this employee

                String[] dateParts = data[3].split("/");
                int recordMonth = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);

                if (year != 2024 || recordMonth != month) continue; // skip records outside current month

                // Parse login and logout times from the attendance CSV file
                LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);

                // Compute hours worked for this day applying grace period, cap, and lunch break rules
                double hours = computeHoursWorked(login, logout);

                // Accumulate hours into the correct cutoff period
                if (day <= 15) firstHalf += hours; // first cutoff: days 1–15
                else secondHalf += hours; // second cutoff: days 16–end
            }

            // Compute gross salary for each cutoff period (hours × hourly rate)
            double grossFirst = computeGross(firstHalf, rate);
            double grossSecond = computeGross(secondHalf, rate);

            // Per process flow: add 1st and 2nd cutoff amounts first before computing deductions
            double monthlyGross = grossFirst + grossSecond;

            // Compute each government deduction using the combined monthly gross
            double sss = computeSSS(monthlyGross);
            double pagibig = computePagibig(monthlyGross);
            double philhealth = computePhilhealth(monthlyGross);
            double totalContribution = sss + philhealth + pagibig; // sum of contributions (used for tax)
            double tax = withholdingTax(monthlyGross, totalContribution);
            double totalDeductions = sss + pagibig + philhealth + tax; // total amount deducted on second cutoff

            // Net salary is computed on the second cutoff where deductions are applied
            double netSalary = grossSecond - totalDeductions;

            // Convert month number to month name for display
            String monthName = switch (month) {
                case 6 -> "June";
                case 7 -> "July";
                case 8 -> "August";
                case 9 -> "September";
                case 10 -> "October";
                case 11 -> "November";
                case 12 -> "December";
                default -> "Month " + month;
            };


            
            /*--------------------------------------------------------------------
                Display First Cutoff (June 1 to June 15)
                - Per process flow: show Total Hours Worked, Gross Salary, Net Salary.
                - No deductions on the first cutoff.
            ---------------------------------------------------------------------*/

            System.out.println("\nFirst Cutoff");
            System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
            System.out.println("Total Hours Worked : " + firstHalf);
            System.out.println("Gross Salary: " + grossFirst);
            System.out.println("Net Salary: " + grossFirst); // net equals gross — no deductions on first cutoff


            /*--------------------------------------------------------------------
                Display Second Cutoff (June 16 to June 30)
                - Per process flow: show Total Hours Worked, Gross Salary, each
                  deduction (SSS, PhilHealth, Pag-IBIG, Tax), Total Deductions,
                  and Net Salary. Second payout includes all deductions.
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




    /* =======================================================================================
        For All Employee (Method # 8) [rosella]
    ==========================================================================================*/

        /**
        * Processes and displays the payroll report for all employees.
        *
        * Process Flow (payroll_staff → Process Payroll → All Employees):
        * - Follows the same display format as oneEmployee() (Method #7),
        *   but automatically processes every employee in the CSV file.
        * 1. Reads all employee records from the employee CSV.
        * 2. Loads all attendance records into memory once (avoids repeated file reads).
        * 3. For each employee, computes the bi-monthly payroll (first and second cutoff)
        *    for each month from June to December 2024.
        *
        *    First Cutoff  (1–15):  Total Hours Worked, Gross Salary, Net Salary
        *    Second Cutoff (16–30): Total Hours Worked, Gross Salary, SSS, PhilHealth,
        *                           Pag-IBIG, Tax, Total Deductions, Net Salary
        *
        * Note: Government deductions use the combined monthly gross (1st + 2nd cutoff).
        * This method takes no parameters as it automatically processes all employees.
        */


    public static void allEmployee() {

        String empFile = "FINAL MO-IT101-Group24/src/details.csv";
        String attFile = "FINAL MO-IT101-Group24/src/attendance.csv";
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm"); // time format used in the attendance CSV file
        List<String[]> employees = new ArrayList<>(); // list to hold all employee records from the CSV file

        // Step 1: Read and store all employee records from the employee file
        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {
            br.readLine(); // skip header row
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                employees.add(data); // add each employee row to the list
            }
        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }

        // Step 2: Load all attendance records into memory once before the employee loop
        // (avoids repeatedly opening the attendance file for each employee and each month)
        List<String[]> attendanceRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(attFile))) {
            br.readLine(); // skip header row
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    attendanceRecords.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
        }

        // Step 3: Loop through each employee record and process their payroll
        for (String[] employeeData : employees) {
            String employeeNo = employeeData[0];
            String lastName = employeeData[1];
            String firstName = employeeData[2];
            String birthday = employeeData[3];
            double rate = Double.parseDouble(employeeData[18].trim()); // hourly rate at column index 18

            // Display the employee's basic information as required by process flow
            System.out.println("\n==================================== ");
            System.out.println("        All Employee's Payroll         ");
            System.out.println("==================================== ");
            System.out.println("Employee # : " + employeeNo);
            System.out.println("Employee Name : " + lastName + ", " + firstName);
            System.out.println("Birthday : " + birthday);
            System.out.println("===================================\n");

            // Process payroll for each month from June (6) to December (12)
            // Per process flow: display all records June to December
            for (int month = 6; month <= 12; month++) {
                double firstHalf = 0; // total hours worked: days 1–15 (first cutoff)
                double secondHalf = 0; // total hours worked: days 16–end of month (second cutoff)
                int daysInMonth = YearMonth.of(2024, month).lengthOfMonth(); // last day of the month

                // Filter attendance records: match this employee's records for the current month
                for (String[] data : attendanceRecords) {
                    if (!data[0].equals(employeeNo)) continue; // skip records not belonging to this employee

                    String[] dateParts = data[3].split("/");
                    int recordMonth = Integer.parseInt(dateParts[0]);
                    int day = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);

                    if (year != 2024 || recordMonth != month) continue; // skip records outside current month

                    // Parse login and logout times from the attendance CSV file
                    LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                    LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);

                    // Compute hours worked for this day applying grace period, cap, and lunch break rules
                    double hours = computeHoursWorked(login, logout);

                    // Accumulate hours into the correct cutoff period
                    if (day <= 15) firstHalf += hours; // first cutoff: days 1–15
                    else secondHalf += hours; // second cutoff: days 16–end
                }


                // Compute gross salary for each cutoff period (hours × hourly rate)
                double grossFirst = computeGross(firstHalf, rate);
                double grossSecond = computeGross(secondHalf, rate);

                // Per process flow: add 1st and 2nd cutoff amounts first before computing deductions
                double monthlyGross = grossFirst + grossSecond;

                // Compute government deductions using the monthly gross
                double sss = computeSSS(monthlyGross);
                double pagibig = computePagibig(monthlyGross);
                double philhealth = computePhilhealth(monthlyGross);
                double totalContribution = sss + philhealth + pagibig; // sum of contributions (used for tax)
                double tax = withholdingTax(monthlyGross, totalContribution);
                double totalDeductions = sss + pagibig + philhealth + tax; // total amount deducted on second cutoff

                // Net salary is computed on the second cutoff where deductions are applied
                double netSalary = grossSecond - totalDeductions;

                // Convert month number to month name for display
                String monthName = switch (month) {
                    case 6 -> "June";
                    case 7 -> "July";
                    case 8 -> "August";
                    case 9 -> "September";
                    case 10 -> "October";
                    case 11 -> "November";
                    case 12 -> "December";
                    default -> "Month " + month;
                };


                /*--------------------------------------------------------------------
                    Display First Cutoff (e.g., June 1 to June 15)
                    - Per process flow: show Total Hours Worked, Gross Salary, Net Salary.
                    - No deductions on the first cutoff.
                ---------------------------------------------------------------------*/

                System.out.println("\nFirst Cutoff");
                System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
                System.out.println("Total Hours Worked : " + firstHalf);
                System.out.println("Gross Salary: " + grossFirst);
                System.out.println("Net Salary: " + grossFirst); // net equals gross — no deductions on first cutoff


                /*--------------------------------------------------------------------
                    Display Second Cutoff (e.g., June 16 to June 30)
                    - Per process flow: second payout includes all government deductions.
                    - Show: Total Hours Worked, Gross Salary, SSS, PhilHealth, Pag-IBIG,
                      Tax, Total Deductions, and Net Salary.
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
    }
}
