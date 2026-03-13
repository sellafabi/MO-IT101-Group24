import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MotorPH {
  public static void main(String[] args) {

        String empInfo = "Ann's/resources/empInfo.csv";
        String empAttendance = "Ann's/resources/empAttendance.csv";
        String sssContribution = "Ann's/resources/SSS Contribution - SSS.csv";
        
        Scanner sc = new Scanner(System.in);

        String payrollUsername = "payroll_staff";
        
        String employeeUsername = "employee";

        String password = "12345";

        System.out.println("Enter Username: ");
        String username = sc.nextLine();

        System.out.println("Enter Password: ");
        String inputPassword = sc.nextLine();

        


//EMPLOYEE LOGIN PROGRAM -- EMPLOYEE LOGIN PROGRAM -- EMPLOYEE LOGIN PROGRAM -- EMPLOYEE LOGIN PROGRAM -- EMPLOYEE LOGIN PROGRAM -- EMPLOYEE LOGIN PROGRAM
                if (username.equals(employeeUsername) && inputPassword.equals(password)) {
                    System.out.println("Employee login successful.");

                    String option = "";
            
                    System.out.println("1. Enter your Employee Number");
                    System.out.println("2. Exit program");

                    option = sc.nextLine();

                        if (option.equals("1")){
                            Scanner enterEmpNum = new Scanner(System.in);
                            System.out.println("Enter Employee #: ");
                            String empNum = enterEmpNum.nextLine();
                            enterEmpNum.close();

                            String empNumber = "";
                            String empLastName = "";
                            String empFirstName = "";
                            String empBirthday = "";
                            boolean found = false;
                            

                                try (BufferedReader br = new BufferedReader (new FileReader (empInfo))){
                                    br.readLine();
                                    String line;
                                        while ((line = br.readLine()) !=null){
                                            if(line.trim().isEmpty()) continue;
                                            String[] data = line.split(",");

                                            if (data[0].equals(empNum)){
                                                empNumber = data[0];
                                                empLastName = data[1];
                                                empFirstName  = data[2];
                                                empBirthday = data[3];
                                                found = true;
                                                break;
                                            }
                                        }
                                    } catch (IOException e) {
                                        System.out.println("Employee file error.");
                                        }
                                        if (found){
                                            System.out.println("\n=========================================================");
                                            System.out.println( "Employee Information");
                                            System.out.println("\n=========================================================");
                                            System.out.println("Employee #: " + empNumber);
                                            System.out.println("Employee Name: " + empLastName + ", " + empFirstName);
                                            System.out.println("Employee Birthday: " + empBirthday);
                                            System.out.println("\n=========================================================");
                                            
                                        } else {
                                            System.out.println("Employee does not exist.");
                                        }
                                    enterEmpNum.close(); /* closing the empnum scanner (test) */
                                    } else if (option.equals("2")){
                                        System.out.println("Exiting program.");
                                        System.exit(0);
                                    }
                } else if (!username.equals(employeeUsername) && !inputPassword.equals(password)) {
                    System.out.println("Incorrect credentials.");
                }
                

//PAYROLL LOGIN PROGRAM -- PAYROLL LOGIN PROGRAM -- PAYROLL LOGIN PROGRAM -- PAYROLL LOGIN PROGRAM -- PAYROLL LOGIN PROGRAM -- PAYROLL LOGIN PROGRAM
                if (username.equals(payrollUsername) && inputPassword.equals(password)) {
                    System.out.println("Payroll staff Login successful.");

                    String option; //= "";
                    String subOption; //= "";

                    System.out.println("1.Process Payroll");
                    System.out.println("2. Exit program");

                    option = sc.nextLine();

                        if (option.equals("1")) {
                            System.out.println("1. View One Employee");
                            System.out.println("2. View All Employees");
                            System.out.println("3. Exit program");
                            subOption = sc.nextLine();
                            

                                if (subOption.equals("1")){
                                    System.out.println("Enter Employee #: ");
                                    int enterEmpNumber = sc.nextInt();
                                    sc.nextLine();
                                    viewOneEmp(enterEmpNumber, empInfo, empAttendance, sssContribution);

                                } else if (subOption.equals("2")){
                                    viewAllEmp(empInfo, empAttendance, sssContribution);

                                } else if (subOption.equals("3")){
                                    System.out.println("Exiting program.");
                                    System.exit(0);
                                }

                        } else if (option.equals("2")) {
                            System.out.println("Exiting program.");
                            System.exit(0);
                        }
                    } else if (!username.equals(payrollUsername) && !inputPassword.equals(password)) {
                    System.out.println("Incorrect credentials.");
                }
                sc.close();
            } 

            /* =====================================================
                        SSS computation (method 4)
    ===================================================== */
 
    public static double computeSSS(double monthlyGross) {

                String file = "MO-IT101-Group24.test/src/sss.csv";
                double lastEmployeeShare = 0;

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                        br.readLine(); // skip header
                        String line;

                        while ((line = br.readLine()) != null) {

                                if (line.trim().isEmpty()) continue;

                                String[] data = line.split(",");

                                double rangeFrom = Double.parseDouble(data[0].trim());
                                String rangeToText = data[1].trim();
                                double employeeShare = Double.parseDouble(data[3].trim());

                                lastEmployeeShare = employeeShare;

                                if (rangeToText.equalsIgnoreCase("Over")) {
                                        if (monthlyGross >= rangeFrom) {
                                        return employeeShare;
                                        }
                                } else {
                                        double rangeTo = Double.parseDouble(rangeToText);

                                        if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                                        return employeeShare;
                                        }
                                }
                        }
                        br.close();

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return lastEmployeeShare;
        
    }



























    

    /* =====================================================
                        Pag-ibig computation (method 5)
    ===================================================== */

    public static double computePagibig(double monthlyGross) {

                String file = "MO-IT101-Group24.test/src/pagibig.csv";
                double contribution = 0;

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                        br.readLine(); // skip header
                        String line;

                        while ((line = br.readLine()) != null) {

                        if (line.trim().isEmpty()) continue;

                        String[] data = line.split(",");

                        double rangeFrom = Double.parseDouble(data[0].trim());
                        double rangeTo = Double.parseDouble(data[1].trim());
                        double rate = Double.parseDouble(data[2].trim());

                        if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                                contribution = monthlyGross * rate;
                                break;
                        }
                        }

                } catch (Exception e) {
                                e.printStackTrace();
                }

                return Math.min(contribution, 100); 
    }
            
     
            








            //       sss DETAILS

    public static void oneEmployee (Scanner sc) {

        String empFile = "MO-IT101-Group24.test/src/details.csv";
        String attFile = "MO-IT101-Group24.test/src/attendance.csv";

    

        System.out.print("Enter Employee #: ");
        String inputEmpNo = sc.nextLine();

        String empNo = "";
        String firstName = "";
        String lastName = "";
        String birthday = "";
        boolean found = false;
        double rate = 0;

                // Read Employee Details CSV
        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {

            br.readLine(); // Skip Header
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (data[0].equals(inputEmpNo)) {
                    empNo = data[0];
                    lastName = data[1];
                    firstName = data[2];
                    birthday = data[3];
                    rate      = Double.parseDouble(data[18].trim());
                    found = true;
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading employee file.");
            
        }

        if (!found) {
            System.out.println("Employee does not exist.");
            
        }

        System.out.println("\n===================================");
        System.out.println("Employee # : " + empNo);
        System.out.println("Employee Name : " + lastName + ", " + firstName);
        System.out.println("Birthday : " + birthday);
        System.out.println("===================================");

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

        // Read Attendance Records CSV
        // Nested loop: month ---> cutoff (1-15, 16-end-of-month)
        for (int month = 6; month <= 12; month++) { // June to December 2024
            double firstHalf = 0;
            double secondHalf = 0;
            int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();

            try (BufferedReader br = new BufferedReader(new FileReader(attFile))) {

                br.readLine(); // Skip Header
                String line;

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                    if (!data[0].equals(empNo)) continue;

                    String[] dateParts = data[3].split("/");
                    int recordMonth = Integer.parseInt(dateParts[0]);
                    int day = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);

                    if (year != 2024 || recordMonth != month) continue;

                    LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                    LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);

                    double hours = computeHours(login, logout);

                    if (day <= 15) firstHalf += hours;
                    else secondHalf += hours;
                }

            } catch (Exception e) {
                System.out.println("Error reading attendance file for month " + month);
                e.printStackTrace();
                continue;
            }

            
            // First cutoff gross
            double grossFirst = computeGross(firstHalf, rate);

            // Second cutoff gross
            double grossSecond = computeGross(secondHalf, rate);

            double monthlyGross = grossFirst + grossSecond; // ← total for the month

            // Then call it:
            double sss = computeSSS(monthlyGross);

            //pagibig
            double pagibig = computePagibig(monthlyGross);

            //philhealth
            double philhealth = computePhilhealth(monthlyGross);

            //tax
            double taxableIncome = monthlyGross - sss - philhealth - pagibig;
            double tax = withholdingTax(taxableIncome);

            //total deductions
            double totalDeductions = sss +  pagibig + philhealth + tax;

            //net salary
            double netSalary = grossSecond - totalDeductions;

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

            System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
            System.out.println("Total Hours Worked : " + firstHalf);
            System.out.println("Gross Salary: " + grossFirst);
            System.out.println("Net Salary: " + grossFirst);

            System.out.println("\nCutoff Date: " + monthName + " 16 to " + daysInMonth);
            System.out.println("Total Hours Worked : " + secondHalf);
            System.out.println("Gross Salary: " + grossSecond);
            System.out.println("    SSS: " + sss);
            System.out.println("    PhilHealth: " + philhealth);
            System.out.println("    Pag-IBIG: " + pagibig);
            System.out.println("    Tax: " + tax);
            System.out.println("Total Deductions: " + totalDeductions);
            System.out.println("Net Salary: " + netSalary);
        }

        
    }

    public static void allEmployee(Scanner sc) {

        String empFile = "MO-IT101-Group24.test/src/details.csv";
        String attFile = "MO-IT101-Group24.test/src/attendance.csv";
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

        // Step 1: Load ALL employees into a list
        List<String[]> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                // line = line.replace("\"", "");
                // if (line.trim().isEmpty()) continue;
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                employees.add(data); // store each employee row
            }
        } catch (Exception e) {
            System.out.println("Error reading employee file.");
        }

        // Step 2: Loop through each employee (repeats 34 times)
        for (String[] empData : employees) {

            String empNo     = empData[0];
            String lastName  = empData[1];
            String firstName = empData[2];
            String birthday  = empData[3]; // adjust index if needed
            double rate      = Double.parseDouble(empData[18].trim());

            System.out.println("\n===================================");
            System.out.println("Employee # : " + empNo);
            System.out.println("Employee Name : " + lastName + ", " + firstName);
            System.out.println("Birthday : " + birthday);
            System.out.println("===================================");

            // Step 3: Attendance loop per employee
            for (int month = 6; month <= 12; month++) {
                double firstHalf = 0;
                double secondHalf = 0;
                int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();

                try (BufferedReader br = new BufferedReader(new FileReader(attFile))) {
                    br.readLine(); // Skip header
                    String line;

                    while ((line = br.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;
                        String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                        if (!data[0].equals(empNo)) continue; // ← match THIS employee

                        String[] dateParts = data[3].split("/");
                        int recordMonth = Integer.parseInt(dateParts[0]);
                        int day         = Integer.parseInt(dateParts[1]);
                        int year        = Integer.parseInt(dateParts[2]);

                        if (year != 2024 || recordMonth != month) continue;

                        LocalTime login  = LocalTime.parse(data[4].trim(), timeFormat);
                        LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);
                        double hours = computeHours(login, logout);

                        if (day <= 15) firstHalf  += hours;
                        else           secondHalf += hours;
                    }

                } catch (Exception e) {
                    System.out.println("Error reading attendance for month " + month);
                    continue;
                }

                String monthName = Month.of(month).toString();

                // First cutoff gross
                double grossFirst = computeGross(firstHalf, rate);

                // Second cutoff gross
                double grossSecond = computeGross(secondHalf, rate);

                double monthlyGross = grossFirst + grossSecond; // ← total for the month

                // Then call it:
                double sss = computeSSS(monthlyGross);

                //pagibig
                double pagibig = computePagibig(monthlyGross);

                //philhealth
                double philhealth = computePhilhealth(monthlyGross);

                //withholding tax
                double taxableIncome = monthlyGross - sss - philhealth - pagibig;
                double tax = withholdingTax(taxableIncome);

                // total deductions
                double totalDeductions = sss + pagibig + philhealth + tax;

                // net salary
                double netSalary = grossSecond - totalDeductions;

                System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
                System.out.println("-----------------------------------");
                System.out.println("Total Hours Worked : " + firstHalf);
                System.out.println("Gross Salary: " + grossFirst);
                System.out.println("Net Salary: " + grossFirst);

                System.out.println("\nCutoff Date: " + monthName + " 16 to " + daysInMonth);
                System.out.println("-----------------------------------");
                System.out.println("Total Hours Worked : " + secondHalf);
                System.out.println("Gross Salary: " + grossSecond);
                System.out.println("    SSS: " + sss);
                System.out.println("    PhilHealth: " + philhealth);
                System.out.println("    Pag-IBIG: " + pagibig);
                System.out.println("    Tax: " + tax);
                System.out.println("Deductions: " + totalDeductions);
                System.out.println("Net Salary: " + netSalary);
            }

            System.out.println("\n===================================");
            System.out.println("          END OF RECORD");
            System.out.println("===================================");
        }
    }
}
