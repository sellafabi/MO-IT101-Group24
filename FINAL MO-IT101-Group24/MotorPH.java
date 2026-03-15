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
         * Handles user authentication for two roles: employee and payroll staff.
         * Employees can look up their own information, while payroll staff
         * can process and view payroll computations for one or all employees.
         */

    
    public static void main(String[] args) {

        String empInfo = "FINAL MO-IT101-Group24/src/details.csv";
            
        Scanner sc = new Scanner(System.in);

        // Credentials for both user roles
        String payrollUsername = "payroll_staff";   
        String employeeUsername = "employee";
        String password = "12345";

        System.out.println("\n==================================== ");
        System.out.println("        MotorPH Login System         ");
        System.out.println("==================================== ");
        System.out.print("\nEnter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String inputPassword = sc.nextLine();

        


        // EMPLOYEE LOGIN — grants access to employee self-service information lookup
        if (username.equals(employeeUsername) && inputPassword.equals(password)) {
            System.out.println("\nEmployee login successful.");
            String option = "";

            System.out.println("\n==================================== ");
            System.out.println("\n1. View Employee Details ");
            System.out.println("2. Exit program");
            System.out.print("Choose Option: \n");
            option = sc.nextLine();
            System.out.println("==================================== \n");

            if (option.equals("1")){
                // System.out.println("\n");
                System.out.print("Enter Your Employee Number: ");

                // Reusing the existing sc Scanner instance instead of creating a new one
                String inputEmployeeNumber = sc.nextLine(); 

                // Variables to store the matched employee's information from the CSV file
                String employeeNo = ""; 
                String employeeLastName = "";
                String employeeFirstName = "";
                String employeeBirthday = "";
                boolean found = false;
                                

                try (BufferedReader br = new BufferedReader (new FileReader (empInfo))){
                    br.readLine();
                    String line;

                    while ((line = br.readLine()) !=null){
                        if(line.trim().isEmpty()) continue;
                        String[] data = line.split(",");

                        if (data[0].equals(inputEmployeeNumber)){
                            employeeNo = data[0];
                            employeeLastName = data[1];
                            employeeFirstName  = data[2];
                            employeeBirthday = data[3];
                            found = true;
                            break;
                        }
                    }

                } catch (IOException e) {
                    System.out.println("\nEmployee file error.\n");

                
                // Display employee information if found, otherwise notify the user
                } if (found){
                    System.out.println("\n==================================== ");
                    System.out.println( "        Employee Information");
                    System.out.println("==================================== ");
                    System.out.println("\nEmployee #: " + employeeNo);
                    System.out.println("Employee Name: " + employeeLastName + ", " + employeeFirstName);
                    System.out.println("Employee Birthday: " + employeeBirthday);
                    System.out.println("====================================\n");
                                                
                    } else {
                        System.out.println("\nEmployee number does not exist.\n");
                    }
                       
                } else if (option.equals("2")){
                    System.out.println("\nExiting program.\n");
                    System.exit(0); // program terminated
                
                } else {
                // If the user enters anything other than 1 or 2, notify them it is invalid
                System.out.println("\nInvalid option. Please enter 1 or 2.\n");
                System.exit(0); // program terminated
                }
                        
            }
                    
        // PAYROLL LOGIN — grants access to payroll processing for one or all employees
        else if (username.equals(payrollUsername) && inputPassword.equals(password)) { 

            System.out.println("\nPayroll staff Login successful!");
            String option; 
            String subOption;

            System.out.println("\n==================================== ");
            System.out.println("\n1. Process Payroll");
            System.out.println("2. Exit program");
            System.out.print("Choose Option: ");
            option = sc.nextLine();
            System.out.println("\n====================================");
            
            

            if (option.equals("1")) {
                System.out.println("\n1. View One Employee");
                System.out.println("2. View All Employees");
                System.out.println("3. Exit program");
                System.out.print("Choose Sub-option: ");
                subOption = sc.nextLine();
                System.out.println("\n====================================\n");
                                
                // Route to the appropriate payroll method based on the sub-option selected
                if (subOption.equals("1")){
                System.out.print("Enter Employee Number: ");
                String inputEmployeeNumber = sc.nextLine();
                oneEmployee(inputEmployeeNumber);  

                    } else if (subOption.equals("2")){

                        allEmployee();

                    } else if (subOption.equals("3")){

                        System.out.println("\nExiting program.\n");
                        System.exit(0);

                    } else {
                        // If the user enters anything other than 1, 2, or 3, notify them it is invalid
                        System.out.println("\nInvalid option. Please enter 1, 2, or 3.\n");
                    }

            } else if (option.equals("2")) {

                    System.out.println("\nExiting program.\n");
                    System.exit(0);

            } else {
                // If the user enters anything other than 1 or 2, notify them it is invalid
                System.out.println("\nInvalid option. Please enter 1 or 2.\n");
            }

        // If either username or password is incorrect, deny access and terminate the program
        } else { 

            System.out.println("\nIncorrect username and/or password.\n");
            System.exit(0);

        } 
        sc.close();
    } 



    /*========================================================================================
        SSS Computation (Method # 1) [rosella]
    ==========================================================================================*/

        /**
        * Computes the SSS contribution of an employee based on their monthly gross salary.
        * Reads the SSS contribution table from a CSV file and matches the employee's
        * gross salary against the salary brackets to return the correct employee share. 
        * @param monthlyGross the total gross salary of the employee for the month
        * @return the employee's SSS contribution amount based on their salary bracket
        */
   
 
    public static double computeSSS(double monthlyGross) {

        String file = "FINAL MO-IT101-Group24/src/sss.csv";
        double lastEmployeeShare = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            br.readLine(); // skip header row of the SSS table
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                double rangeFrom = Double.parseDouble(data[0].trim()); // lower bound of salary bracket
                String rangeToText = data[1].trim(); // upper bound, may be "Over"
                double employeeShare = Double.parseDouble(data[3].trim()); // corresponding SSS contribution

                // Store the current share in case it becomes the last applicable bracket
                lastEmployeeShare = employeeShare;

                // Handle the last bracket which has no upper limit ("Over")
                if (rangeToText.equalsIgnoreCase("Over")) {

                    if (monthlyGross >= rangeFrom) {

                        return employeeShare; // gross exceeds the minimum of the last bracket

                    }

                } else {

                    double rangeTo = Double.parseDouble(rangeToText);

                    // Check if the gross salary falls within this bracket
                    if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {

                        return employeeShare; 

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lastEmployeeShare; // Return the last stored share as a fallback if no bracket matched
        
    }



    /*========================================================================================
        Pag-ibig Computation (Method # 2) [rosella]
    ==========================================================================================*/

        /**
        * Computes the Pag-IBIG contribution of an employee based on their monthly gross salary.
        * Reads the Pag-IBIG contribution table from a CSV file, multiplies the gross salary
        * by the applicable rate, and caps the contribution at a maximum of 100 pesos.
        * @param monthlyGross the total gross salary of the employee for the month
        * @return the employee's Pag-IBIG contribution, capped at 100 pesos
        */


    public static double computePagibig(double monthlyGross) {

        String file = "FINAL MO-IT101-Group24/src/pagibig.csv";
        double contribution = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            br.readLine(); // skip header row of the Pag-IBIG table
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                double rangeFrom = Double.parseDouble(data[0].trim()); // lower bound of salary bracket
                double rangeTo = Double.parseDouble(data[1].trim()); // upper bound of salary bracket
                double rate = Double.parseDouble(data[2].trim()); // contribution rate for this bracket

                // Check if the gross salary falls within this bracket
                if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {

                    contribution = monthlyGross * rate; // compute contribution based on rate
                    break; // stop searching once the correct bracket is found

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Math.min(contribution, 100); // Pag-IBIG contribution is capped at a maximum of 100 pesos per month
    }



    /* =======================================================================================
        PhilHealth Computation (Method # 3) [ann]
    ==========================================================================================*/

        /**
        * Computes the PhilHealth contribution of an employee based on their monthly gross salary.
        * The employee only shoulders half of the total PhilHealth premium, with the
        * other half covered by the employer. Three salary brackets are applied:
        * - 10,000 and below: fixed 150 pesos (half of 300)
        * - Between 10,000 and 60,000: 1.5% of gross salary (half of 3%)
        * - 60,000 and above: fixed 900 pesos (half of 1,800)
        * @param totalGross the total gross salary of the employee for the month
        * @return the employee's share of the PhilHealth contribution
        */


    public static double computePhilhealth (double totalGross) {
        double PHdeduction = 0.0;

        // Apply the correct PhilHealth bracket based on the employee's gross salary
        if (totalGross <= 10000) {
            PHdeduction = 300/2; // Fixed premium of 300; employee pays half = 150
            } else if (totalGross > 10000 && totalGross < 60000){
                PHdeduction =  totalGross*(0.03)/2; // 3% of gross salary; employee pays half
            } else if (totalGross >= 60000) {
                PHdeduction = 1800/2; // Fixed maximum premium of 1800; employee pays half = 900
            }

        return PHdeduction; // Returns the computed employee share of PhilHealth contribution
    }



    /* =======================================================================================
        Tax Computation (Method # 4) [ann]
    ==========================================================================================*/

        /**
        * Computes the monthly withholding tax of an employee using the BIR tax table.
        * The taxable monthly salary is first derived by subtracting all government
        * contributions (SSS, PhilHealth, Pag-IBIG) from the gross salary. The resulting
        * taxable salary is then matched against the six BIR tax brackets to compute
        * the correct withholding tax amount.
        * @param totalGross the total gross salary of the employee for the month
        * @param totalContribution the total of SSS, PhilHealth, and Pag-IBIG contributions
        * @return the computed withholding tax amount
        */


    public static double withholdingTax (double totalGross, double totalContribution) {
        double tax = 0.00;

        // Taxable salary is gross minus all government contributions (SSS + PhilHealth + Pag-IBIG)
        double taxableMonthlySalary = totalGross - totalContribution; 
        
        // BIR tax brackets applied to the taxable monthly salary
        if (taxableMonthlySalary <= 20832) {
            tax = 0.00;// Exempt from tax

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
        return tax; // Returns the final computed withholding tax amount
    }  
    


    /* =======================================================================================
        Hours Worked Computation (Method # 5) [ann]
    ==========================================================================================*/

        /**
        * Computes the total hours worked by an employee for a single day.
        * Applies three rules:
        * 1. If the employee logs in at or before 8:10 AM (grace period), login
        *    time is treated as 8:00 AM for computation purposes.
        * 2. Logout time is capped at 5:00 PM — overtime is not counted.
        * 3. A mandatory 1-hour lunch break is deducted from the total duration.
        * @param logIn the time the employee logged in
        * @param logOut the time the employee logged out
        * @return the total hours worked as a decimal (example: 7.5 for 7 hours 30 minutes)
        */


    public static double computeHoursWorked(LocalTime logIn, LocalTime logOut) {
        
        LocalTime gracePeriod = LocalTime.of(8, 10); // grace period ends at 8:10 AM
        LocalTime cutoffTime = LocalTime.of(17, 0); // workday ends at 5:00 PM
        LocalTime standardStart = LocalTime.of(8, 0); // standard start time 8:00 AM

        // Overtime is not counted — cap logout time at 5:00 PM
        if (logOut.isAfter(cutoffTime)) {
            logOut = cutoffTime; // limits logout time at 5:00 PM
        }

        // If employee logged in within the grace period, treat login as 8:00 AM
        if (!logIn.isAfter(gracePeriod)) {
            logIn = standardStart; // use 8:00 AM as effective login time
        }

        // Calculate total minutes between login and logout
        long  minutesWorked = Duration.between(logIn, logOut).toMinutes();
        int lunchBreak = 60; // mandatory 1-hour lunch break in minutes

            // Deduct lunch break only if the employee worked more than 1 hour
            if (minutesWorked > lunchBreak) {
            minutesWorked -= lunchBreak;

            } else {

                minutesWorked = 0; // If total time is 1 hour or less, no productive hours are counted

            }

        return minutesWorked / 60.0; // Convert remaining minutes to hours and return
            
    }



    /*========================================================================================
        Gross Computation (Method # 6) [ann]
    ==========================================================================================*/

        /**
        * Computes the gross salary for a given period by multiplying the total
        * hours worked by the employee's hourly rate.
        * @param hours the total number of hours worked in the cutoff period
        * @param rate the employee's hourly rate
        * @return the gross salary for the cutoff period
        */


    static double computeGross(double hours, double rate) {

        return hours * rate; // Gross salary for the cutoff period = total hours worked × hourly rate

    }

              
        
    /*========================================================================================
        For One Employee (Method # 7) [rosella]
    ==========================================================================================*/

        /**
        * Processes and displays the payroll report for a single employee.
        * Prompts the payroll staff to enter an employee number, searches the
        * employee file for a match, then loads all attendance records once into
        * memory and computes the bi-monthly payroll (first and second cutoff)
        * for each month from June to December 2024.
        * Government deductions (SSS, PhilHealth, Pag-IBIG, and withholding tax)
        * are applied on the second cutoff only.
        * @param employeeNumber the employee number entered by the payroll staff in main()
        */


    public static void oneEmployee(String employeeNumber) {

        String empFile = "FINAL MO-IT101-Group24/src/details.csv";
        String attFile = "FINAL MO-IT101-Group24/src/attendance.csv";
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm"); // format for parsing time values in the CSV

        // Variables to store the matched employee's details from the employee file
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
                    rate = Double.parseDouble(data[18].trim()); // hourly rate is at column 18 from CSV
                    found = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }

        // Stop execution if no matching employee record was found
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
        

        // Load all attendance records into memory once before the loop to avoid repeatedly opening the file for each month
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

        // Process payroll for each month from June to December
        for (int month = 6; month <= 12; month++) {
            double firstHalf = 0; // total hours worked from day 1 to 15
            double secondHalf = 0; // total hours worked from day 16 to end of month
            int daysInMonth = YearMonth.of(2024, month).lengthOfMonth(); // get total days in the month

            // Filter attendance records for this employee and current month
            for (String[] data : attendanceRecords) {
                if (!data[0].equals(employeeNo)) continue; // skip records not belonging to this employee

                String[] dateParts = data[3].split("/");
                int recordMonth = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);

                if (year != 2024 || recordMonth != month) continue; // skip records outside current month

                LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);
                double hours = computeHoursWorked(login, logout); // compute hours for this day

                // Accumulate hours into the correct cutoff period
                if (day <= 15) firstHalf += hours;
                else secondHalf += hours;
            }

            // Compute gross salary for each cutoff period
            double grossFirst = computeGross(firstHalf, rate);
            double grossSecond = computeGross(secondHalf, rate);
            double monthlyGross = grossFirst + grossSecond; // total gross for the month

            // Compute government deductions using the monthly gross
            double sss = computeSSS(monthlyGross);
            double pagibig = computePagibig(monthlyGross);
            double philhealth = computePhilhealth(monthlyGross);
            double totalContribution = sss + philhealth + pagibig; // sum of all contributions
            double tax = withholdingTax(monthlyGross, totalContribution);
            double totalDeductions = sss + pagibig + philhealth + tax; // total amount to be deducted

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

            // Display first cutoff — no deductions applied
            System.out.println("\nFirst Cutoff");
            System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
            System.out.println("Total Hours Worked : " + firstHalf);
            System.out.println("Gross Salary: " + grossFirst);
            System.out.println("Net Salary: " + grossFirst); // net equals gross on first cutoff

            // Display second cutoff — government deductions are applied here
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
        * Reads all employee records from the employee file, then loads all
        * attendance records once into memory before processing. For each employee,
        * computes the bi-monthly payroll (first and second cutoff) for each month
        * from June to December 2024.
        * Government deductions (SSS, PhilHealth, Pag-IBIG, and withholding tax)
        * are applied on the second cutoff only.
        * This method takes no parameters as it automatically processes all employees.
        */


    public static void allEmployee() {

        String empFile = "FINAL MO-IT101-Group24/src/details.csv";
        String attFile = "FINAL MO-IT101-Group24/src/attendance.csv";
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm"); // format for parsing time values in the CSV
        List<String[]> employees = new ArrayList<>(); // list to store all employee records loaded from the employee file

        // Read and store all employee records from the employee file into a list
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

        // Load all attendance records into memory once before the loop to avoid repeatedly opening the file for each employee and each month.
        List<String[]> attendanceRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(attFile))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    attendanceRecords.add(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
        }

        // Loop through each employee record and process their payroll
        for (String[] employeeData : employees) {
            String employeeNo = employeeData[0];
            String lastName = employeeData[1];
            String firstName = employeeData[2];
            String birthday = employeeData[3];
            double rate = Double.parseDouble(employeeData[18].trim()); // hourly rate is at column 18

            // Display the employee's basic information
            System.out.println("\n==================================== ");
            System.out.println("        All Employee's Payroll         ");
            System.out.println("==================================== ");
            System.out.println("Employee # : " + employeeNo);
            System.out.println("Employee Name : " + lastName + ", " + firstName);
            System.out.println("Birthday : " + birthday);
            System.out.println("===================================\n");

            // Process payroll for each month from June to December
            for (int month = 6; month <= 12; month++) {
                double firstHalf = 0; // total hours worked from day 1 to 15
                double secondHalf = 0; // total hours worked from day 16 to end of month
                int daysInMonth = YearMonth.of(2024, month).lengthOfMonth(); // get total days in the month

                // Filter attendance records for this employee and current month
                for (String[] data : attendanceRecords) {
                    if (!data[0].equals(employeeNo)) continue; // skip records not belonging to this employee

                    String[] dateParts = data[3].split("/");
                    int recordMonth = Integer.parseInt(dateParts[0]);
                    int day = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);

                    if (year != 2024 || recordMonth != month) continue; // skip records outside current month

                    LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                    LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);
                    double hours = computeHoursWorked(login, logout); // compute hours for this day

                    // Accumulate hours into the correct cutoff period
                    if (day <= 15) firstHalf += hours;
                    else secondHalf += hours;
                }


                // Compute gross salary for each cutoff period
                double grossFirst = computeGross(firstHalf, rate);
                double grossSecond = computeGross(secondHalf, rate);
                double monthlyGross = grossFirst + grossSecond; // total gross for the month

                // Compute government deductions using the monthly gross
                double sss = computeSSS(monthlyGross);
                double pagibig = computePagibig(monthlyGross);
                double philhealth = computePhilhealth(monthlyGross);
                double totalContribution = sss + philhealth + pagibig; // sum of all contributions
                double tax = withholdingTax(monthlyGross, totalContribution);
                double totalDeductions = sss + pagibig + philhealth + tax; // total amount to be deducted

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


                // Display first cutoff — no deductions applied
                System.out.println("\nFirst Cutoff");
                System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
                System.out.println("Total Hours Worked : " + firstHalf);
                System.out.println("Gross Salary: " + grossFirst);
                System.out.println("Net Salary: " + grossFirst); // net equals gross on first cutoff

                // Display second cutoff — government deductions are applied here
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
