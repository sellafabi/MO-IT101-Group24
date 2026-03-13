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

    /* ============================
        Login System (MAIN METHOD) [ann]
    ===============================*/
    public static void main(String[] args) {

        String empInfo = "FINAL MO-IT101-Group24/src/details.csv";
            
        Scanner sc = new Scanner(System.in);

        String payrollUsername = "payroll_staff";   
        String employeeUsername = "employee";
        String password = "12345";

        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String inputPassword = sc.nextLine();

        


        //EMPLOYEE LOGIN PROGRAM -- EMPLOYEE LOGIN PROGRAM -- EMPLOYEE LOGIN PROGRAM -- EMPLOYEE LOGIN PROGRAM -- EMPLOYEE LOGIN PROGRAM -- EMPLOYEE LOGIN PROGRAM
        if (username.equals(employeeUsername) && inputPassword.equals(password)) {
            System.out.println("Employee login successful.\n");

            String option = "";
                
            System.out.println("1. Enter your Employee Number");
            System.out.println("2. Exit program");
            System.out.print("Choose Option: ");
            option = sc.nextLine();

            if (option.equals("1")){
                Scanner enterEmpNum = new Scanner(System.in);
                System.out.print("Enter Employee #: ");
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

                } if (found){
                    System.out.println("\n=========================================================");
                    System.out.println( "                   Employee Information");
                    System.out.println("\n=========================================================");
                    System.out.println("Employee #: " + empNumber);
                    System.out.println("Employee Name: " + empLastName + ", " + empFirstName);
                    System.out.println("Employee Birthday: " + empBirthday);
                    System.out.println("\n=========================================================");
                                                
                    } else {
                        System.out.println("\nEmployee does not exist.\n");
                    }
                       
                } else if (option.equals("2")){
                    System.out.println("\nExiting program.\n");
                    System.exit(0);
                }
                        
                } else if (!username.equals(employeeUsername) && !inputPassword.equals(password)) {
                    System.out.println("\nIncorrect credentials.\n");
            }
                    

        //PAYROLL LOGIN PROGRAM -- PAYROLL LOGIN PROGRAM -- PAYROLL LOGIN PROGRAM -- PAYROLL LOGIN PROGRAM -- PAYROLL LOGIN PROGRAM -- PAYROLL LOGIN PROGRAM
        if (username.equals(payrollUsername) && inputPassword.equals(password)) {

            System.out.println("Payroll staff Login successful.");
            String option; 
            String subOption;

            System.out.println("1.Process Payroll");
            System.out.println("2. Exit program");
            System.out.print("Choose Option: ");
            option = sc.nextLine();

            if (option.equals("1")) {

                System.out.println("1. View One Employee");
                System.out.println("2. View All Employees");
                System.out.println("3. Exit program");
                System.out.print("Choose Option: ");
                subOption = sc.nextLine();
                                

                if (subOption.equals("1")){

                    oneEmployee(sc);;

                    } else if (subOption.equals("2")){

                        allEmployee(sc);;

                    } else if (subOption.equals("3")){

                        System.out.println("\nExiting program.\n");
                        System.exit(0);

                    }

            } else if (option.equals("2")) {

                    System.out.println("\nExiting program.\n");
                    System.exit(0);

            }

        } else if (!username.equals(payrollUsername) && !inputPassword.equals(password)) {

            System.out.println("Incorrect credentials."); /*if the user enters the wrong username and password*/

        }

        sc.close();
    } 



    /* =====================================================
        SSS Computation (Method # 1) [rosella]
    ===================================================== */
 
    public static double computeSSS(double monthlyGross) {

        String file = "FINAL MO-IT101-Group24/src/sss.csv";
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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lastEmployeeShare;
        
    }

    

    /* =====================================================
        Pag-ibig Computation (Method # 2) [rosella]
    ===================================================== */

    public static double computePagibig(double monthlyGross) {

        String file = "FINAL MO-IT101-Group24/src/pagibig.csv";
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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Math.min(contribution, 100); 
    }



    /* =====================================================
        PhilHealth Computation (Method # 3) [ann]
    ===================================================== */

    public static double computePhilhealth (double totalGross) {
        double PHdeduction = 0.0;

        if (totalGross <= 10000) {
            PHdeduction = 300/2;
            } else if (totalGross > 10000 && totalGross < 60000){
                PHdeduction =  totalGross*(0.03)/2;
            } else if (totalGross >= 60000) {
                PHdeduction = 1800/2;
            }

        return PHdeduction;
    }



    /* ==================================================
       Tax Computation (Method # 4) [ann]
    ===================================================== */

    public static double withholdingTax (double totalGross, double totalContribution) {
        double tax = 0.00;
        double taxableSalary = totalGross - totalContribution; /* retrieves the taxable salary by deducting all of the govt contributions to the gross*/
        
        if (totalGross <= 20832) {
            tax = 0.00;
        } else if (totalGross >= 20833 && totalGross < 33333) {
            tax = (taxableSalary-20833)*0.2;

        } else if (totalGross >= 33333 && totalGross < 66667) {
            tax = 2500+(taxableSalary-33333)*0.25;

        } else if (totalGross >= 66667 && totalGross < 166667) {
            tax = 10833+(taxableSalary-66667)*0.30;

        } else if (totalGross >= 166667 && totalGross < 666667) {
            tax = 40833.33+(taxableSalary-166667)*0.32;

        } else if (totalGross >= 666667) {
            tax = 200833.33+(taxableSalary-666667)*0.35;
        }
        return tax;
    }  
    
    

    /* =====================================================
        Hours Worked Computation (Method # 5) [ann]
    ===================================================== */

    public static double computeHoursWorked(LocalTime logIn, LocalTime logOut) {
        
        LocalTime gracePeriod = LocalTime.of(8, 10); // 8:00 AM - 8:10 AM
        LocalTime cutoffTime = LocalTime.of(17, 0); // 5:00 PM

        if (logOut.isAfter(cutoffTime)) {
            logOut = cutoffTime; // limits logout time at 5:00 PM
        }

            long  minutesWorked = Duration.between(logIn, logOut).toMinutes(); //calculates the duration between log in and log out time.
            int lunchBreak = 60; // 1 hour lunch break

        if (minutesWorked > lunchBreak) {

            minutesWorked -= lunchBreak; // deducts 1 hour for lunch break if total minutes worked is more than 1 hour.

            } else {

                minutesWorked = 0; // if total minutes worked is less than or equal to 1 hour, then no hours are counted.

            }

            double hoursWorked = minutesWorked;

            if (!logIn.isAfter(gracePeriod)) {

                return 8.0; // if log in time is before or at 8:10 AM, counts as 8 hours worked.

            }

            return hoursWorked / 60.0; // converts minutes worked to hours.
            
    }


    /* =====================================================
        Gross Computation (Method # 6) [ann]
    ===================================================== */

    static double computeGross(double hours, double rate) {

        return hours * rate;

    }

              
        
    /* =====================================================
       For One Employee (Method # 7) [rosella]
    ===================================================== */

    public static void oneEmployee (Scanner sc) {

        String empFile = "FINAL MO-IT101-Group24/src/details.csv";
        String attFile = "FINAL MO-IT101-Group24/src/attendance.csv";
        System.out.print("Enter Employee #: ");
        String inputEmpNo = sc.nextLine();
        String empNo = "";
        String firstName = "";
        String lastName = "";
        String birthday = "";
        boolean found = false;
        double rate = 0;

    

        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {

            br.readLine();
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

        } catch (IOException e) {
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



        // Loop through each employee's computations from June to December
        for (int month = 6; month <= 12; month++) { 
            double firstHalf = 0;
            double secondHalf = 0;
            int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();

            try (BufferedReader br = new BufferedReader(new FileReader(attFile))) {

                br.readLine(); 
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

                    double hours = computeHoursWorked(login, logout);

                    if (day <= 15) firstHalf += hours;
                    else secondHalf += hours;
                }

            } catch (IOException e) {
                System.out.println("Error reading attendance file for month " + month);
                e.printStackTrace();
                continue;
            }



            // Gross Computations
            double grossFirst = computeGross(firstHalf, rate); 
            double grossSecond = computeGross(secondHalf, rate);
            double monthlyGross = grossFirst + grossSecond; 

            // Deductions Computations
            double sss = computeSSS(monthlyGross);
            double pagibig = computePagibig(monthlyGross);
            double philhealth = computePhilhealth(monthlyGross);
            double totalContribution = sss + philhealth + pagibig;
            double tax = withholdingTax(monthlyGross, totalContribution); 
            double totalDeductions = sss + pagibig + philhealth + tax;

            // Net Salary for the second cutoff with deductions
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

        System.out.println("\n===================================");
        System.out.println("          END OF RECORD");
        System.out.println("=====================================");
    }



    /* =====================================================
       For All Employee (Method # 8) [rosella]
    ===================================================== */

    public static void allEmployee(Scanner sc) {

        String empFile = "FINAL MO-IT101-Group24/src/details.csv";
        String attFile = "FINAL MO-IT101-Group24/src/attendance.csv";
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
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
        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }



        // Loop through each employee's  result (34 times employees)
        for (String[] empData : employees) {

            String empNo     = empData[0];
            String lastName  = empData[1];
            String firstName = empData[2];
            String birthday  = empData[3]; 
            double rate      = Double.parseDouble(empData[18].trim());

            System.out.println("\n===================================");
            System.out.println("Employee # : " + empNo);
            System.out.println("Employee Name : " + lastName + ", " + firstName);
            System.out.println("Birthday : " + birthday);
            System.out.println("===================================");

            // Loop each employee's computations from June to December
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

                        if (!data[0].equals(empNo)) continue; 

                        String[] dateParts = data[3].split("/");
                        int recordMonth = Integer.parseInt(dateParts[0]);
                        int day         = Integer.parseInt(dateParts[1]);
                        int year        = Integer.parseInt(dateParts[2]);

                        if (year != 2024 || recordMonth != month) continue;

                        LocalTime login  = LocalTime.parse(data[4].trim(), timeFormat);
                        LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);
                        double hours = computeHoursWorked(login, logout);

                        if (day <= 15) firstHalf  += hours;
                        else           secondHalf += hours;
                    }

                } catch (IOException e) {
                    System.out.println("Error reading attendance for month " + month);
                    continue;
                }



                String monthName = Month.of(month).toString();

                
                // Gross Computations
                double grossFirst = computeGross(firstHalf, rate); 
                double grossSecond = computeGross(secondHalf, rate);
                double monthlyGross = grossFirst + grossSecond; 

                // Deductions Computations
                double sss = computeSSS(monthlyGross);
                double pagibig = computePagibig(monthlyGross);
                double philhealth = computePhilhealth(monthlyGross);
                double totalContribution = sss + philhealth + pagibig;
                double tax = withholdingTax(monthlyGross, totalContribution); 
                double totalDeductions = sss + pagibig + philhealth + tax;

                // Net Salary for the second cutoff with deductions
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
            System.out.println("=====================================");
        }
    }
}
