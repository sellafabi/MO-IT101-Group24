package motorph.payroll;
import motorph.report.ReportPrinter;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PayrollService {

    static final String EMP_FILE = "src/details.csv";
    static final String ATT_FILE = "src/attendance.csv";

    /*========================================================================================
        Payroll Computation and Display (Method #13) [rosella]
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
                                      DateTimeFormatter timeFormat,
                                      List<String[]> sssTable,
                                      List<String[]> pagibigTable) {

        System.out.println("\n======================================");
        System.out.println("           Employee Payroll          ");
        System.out.println("======================================");
        System.out.println("Employee # : "    + employeeNo);
        System.out.println("Employee Name : " + lastName + ", " + firstName);
        System.out.println("Birthday : "      + birthday);
        System.out.println("======================================\n");

        // includes months from: June (6) to December (12) per process flow requirement
        for (int month = 6; month <= 12; month++) {

            double[] result = PayrollCalculator.computeMonthlyPayroll(
                employeeNo, month, rate, attendanceRecords, timeFormat, sssTable, pagibigTable
            );
            
            ReportPrinter.printMonthlyPayroll(month, result);
        }

        System.out.println("\n=====================================");
        System.out.println("          END OF RECORD");
        System.out.println("=====================================");
    }

    /*========================================================================================
        For One Employee (Method #14) [rosella]
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
    public static void oneEmployee(String employeeNo, List<String[]> sssTable, List<String[]> pagibigTable) {

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
            String employeeLine;

            while ((employeeLine = br.readLine()) != null) {
                if (!employeeLine.trim().isEmpty())
                    // Same regex split — attendance fields may also contain commas inside quotes
                    attendanceRecords.add(employeeLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            }

        } catch (IOException e) {
            System.out.println("Error reading attendance file.");
        }

        processPayroll(employeeNo, lastName, firstName, birthday, rate, attendanceRecords, timeFormat, sssTable, pagibigTable);
    }


    /*========================================================================================
        For All Employees (Method #15) [rosella]
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
    public static void allEmployee(List<String[]> sssTable, List<String[]> pagibigTable) {

        // H:mm handles both single-digit (e.g., 8:05) and double-digit hours (e.g., 17:00)
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

        List<String[]> employees = new ArrayList<>();

        // Step 1: Read all employee records from details.csv
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {
            br.readLine();
            String employeeLine;

            while ((employeeLine = br.readLine()) != null) {
                // Regex split handles commas inside quoted fields (e.g., addresses)
                String[] empData = employeeLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                employees.add(empData);
            }

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }

        // Step 2: Load attendance records once — reused for every employee in the loop below
        List<String[]> attendanceRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ATT_FILE))) {
            br.readLine();
            String attendanceLine;

            while ((attendanceLine = br.readLine()) != null) {
                if (!attendanceLine.trim().isEmpty())
                    attendanceRecords.add(attendanceLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
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
            processPayroll(employeeNo, lastName, firstName, birthday, rate, attendanceRecords, timeFormat, sssTable, pagibigTable);
        }
    }
    
}
