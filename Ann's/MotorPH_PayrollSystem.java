import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MotorPH_PayrollSystem {
    
    public static void main(String[] args) {

        String empInfo = "Ann's/resources/empInfo.csv";
        String empAttendance = "Ann's/resources/empAttendance.csv";
        String sssContribution = "C:\\Users\\annma\\Downloads\\VSCodeProjects\\SSS Contribution - SSS.csv";
        
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
                            //Scanner sc = new sc(System.in);
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


//=METHODS:
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

//method for SSS contribution computation.

public static double computeSSS (double totalGross, String sssContribution) {
    double sssDeduction = 0.0;
    double lastRowContri = 0.0; //last contribution value in the file

    try (BufferedReader br = new BufferedReader(new FileReader(sssContribution))) {
        br.readLine(); // Skips the Header
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] data = line.split(",");

            double minGross = Double.parseDouble(data[0].trim().replace(",", ""));
            double maxGross = Double.parseDouble(data[1].trim().replace(",", ""));
            double contribution = Double.parseDouble(data[2].trim().replace(",", ""));

            lastRowContri = contribution; // store the contribution of the last row in case totalGross exceeds all ranges

            if (totalGross >= minGross && totalGross <= maxGross) { //hahanapin niya if match sa range ng row yung gross then imamatch niya sa value sa contribution column. 
                sssDeduction = contribution;
                return sssDeduction; // return immediately if a matching range is found
            }
        }
    } catch (IOException e) {

    }
    return lastRowContri; // return the contribution of the last row if totalGross exceeds all ranges
}

//method for pagibig contribution.
public static double computePagIbig (double totalGross) {
    double pagIbigDeduction = 0.0;

    if (totalGross == 1000 && totalGross <= 1500) {
        pagIbigDeduction = totalGross * 0.01;
    }  else {
        pagIbigDeduction = 100.00;
    }
        
    return pagIbigDeduction;
}

//method for PhilHealth contribution.
public static double computePH (double totalGross) {
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

// method for withholding tax computation.
public static double computeTax (double totalGross, double totalContribution) {
    double tax = 0.00;
    double taxableSalary = totalGross - totalContribution;

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

//method for view one employee
public static void viewOneEmp(int enterEmpNumber, String empInfo, String empAttendance, String sssContribution) {

    String empNumber = "";
    String empLastName = "";
    String empFirstName = "";
    String empBirthday = "";
    boolean found = false;
    double hourlyRate = 0.0;

        try (BufferedReader br = new BufferedReader (new FileReader (empInfo))) {
            br.readLine();
            String line;
                            
            while ((line = br.readLine()) !=null){
            if(line.trim().isEmpty()) continue;
            String[] data = line.split(",");

                if (data[0].trim().equals(String.valueOf(enterEmpNumber))){
                    empNumber = data[0];
                    empLastName = data[1];
                    empFirstName  = data[2];
                    empBirthday = data[3];

                    String rawRate = data[data.length - 1].trim();
                        try {
                            hourlyRate = Double.parseDouble(rawRate);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid hourly rate for employee: " + empNumber);
                            hourlyRate = 0.0;
                        } // minemake sure na yung hourly rate is na sa last column ng  csv file. if may error, 0 ang lalabas na hourly rate.

                        found = true;
                        break;
                    }
                    
                } if (!found){
                        System.out.println("Employee not found.");
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                }

                if (found) { //salary computation is added from here.
                    System.out.println("\n=========================================================");

                    System.out.println("Process Payroll");

                    System.out.println("\n=========================================================");
                    System.out.println("Employee #: " + empNumber);
                    System.out.println("Employee Name: " + empLastName + ", " + empFirstName);
                    System.out.println("Employee Birthday: " + empBirthday);
                    System.out.println("\n=========================================================");
                                
                    // computation of hours worked starts here.
                    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

                    for (int month = 6; month <= 12; month++) {

                    double firstCut = 0;
                    double secondCut = 0;
                    int daysAmonth = java.time.YearMonth.of(2024, month).lengthOfMonth();
                    //for looping of the attendance in june to december.

                    try (BufferedReader br = new BufferedReader (new FileReader (empAttendance))){
                        br.readLine();
                        String line;
                        String data[];

                        while ((line = br.readLine()) != null){
                        if(line.trim().isEmpty()) continue;
                        data = line.split(",");
                        if (!data[0].trim().equals(String.valueOf(enterEmpNumber))) continue; //skip if not the employee number we are looking for
                                                            
                        //this line parse date and time - converts the string of date and time into a formatted one.
                        String[] dateFormat = data[3].split("/");
                        int recordMonth = Integer.parseInt(dateFormat[0]);
                        int recordDay = Integer.parseInt(dateFormat[1]);
                        int recordYear = Integer.parseInt(dateFormat[2]);

                        if (recordYear != 2024 || recordMonth != month) continue; //skip if not the correct year or month

                        LocalTime logIn = LocalTime.parse(data[4].trim(), timeFormat); //formats time in from the csv file, and cuts extra spaces if there are any.
                        LocalTime logOut = LocalTime.parse(data[5].trim(), timeFormat);

                        double hoursWorked = computeHoursWorked(logIn, logOut);

                        if (recordDay <= 15) {
                            firstCut += hoursWorked; //adds hours worked to the first cut until the 15th of the month.
                        } else {
                            secondCut += hoursWorked; //adds hours worked to the second cut from the 16th to the end of the month.
                        }

                    }

                } catch (IOException e) {
                    System.out.println("Employee attendance file error.");
                }
                // computation of hours ends here.

                            //gross pay calculation.
                            double firstCutGross = firstCut * hourlyRate;
                            double secondCutGross = secondCut * hourlyRate;

                            // sss contribution calculation.
                            double totalGross = firstCutGross + secondCutGross;
                            double sssDeduction = computeSSS(totalGross, sssContribution);

                            // pagibig contribution calculation.
                            double pagIbigDeduction = computePagIbig(totalGross);

                            // philhealth contribution calculation.
                            double PHdeduction = computePH (totalGross);
                                                                    
                            // total contribution calculation
                            double totalContribution = sssDeduction+pagIbigDeduction+PHdeduction;
                                                                    
                            // withholding tax calculation
                            double tax = computeTax (totalGross, totalContribution);
                                                                    
                            // total deduction computation
                            double totalDeductions = sssDeduction+pagIbigDeduction+PHdeduction+tax;
                                                                    
                            // net salary
                            double firstCutNet = firstCutGross;
                            double secondCutNet = secondCutGross-totalDeductions;

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

                            System.out.println("Total Gross Salary for " + monthName + ": " + totalGross);

                            System.out.println("\n=========================================================");

                            System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
                            System.out.println("Total Hours Worked : "+ firstCut);
                            System.out.println("Gross Salary: "+ firstCutGross);
                            System.out.println("Net Salary: " + firstCutNet);
                                                            
                            System.out.println("\n=========================================================");

                            System.out.println("\nCutoff Date: " + monthName + " 16 to " + daysAmonth);
                            System.out.println("Total Hours Worked : " + secondCut);
                            System.out.println("Gross Salary: "+ secondCutGross);
                            System.out.println("SSS: "+ sssDeduction);
                            System.out.println("PagIbig: "+ pagIbigDeduction);
                            System.out.println("PhilHealth: "+ PHdeduction);
                            System.out.println("Tax: "+ tax);
                            System.out.println("Total Deductions: " + totalDeductions);
                            System.out.println("Net Salary: " + secondCutNet);

                            System.out.println("\n=========================================================");
                        }// closing bracket ng attendance for loop
                    } // salary computation bracket
}

//method for view all employees
public static void viewAllEmp(String empInfo, String empAttendance, String sssContribution) {

    String empNumber = "";
    String empLastName = "";
    String empFirstName = "";
    String empBirthday = "";
    double hourlyRate = 0.0;

        try (BufferedReader brInfo = new BufferedReader (new FileReader (empInfo))) {
            brInfo.readLine();
            String InfoLine;
            
            while ((InfoLine = brInfo.readLine()) !=null){
                if(InfoLine.trim().isEmpty()) continue;
                String[] data = InfoLine.split(",");

                    empNumber = data[0];
                    empLastName = data[1];
                    empFirstName  = data[2];
                    empBirthday = data[3];

                    String rawRate = data[data.length - 1].trim();
                        try {
                            hourlyRate = Double.parseDouble(rawRate);
                        } catch (NumberFormatException e) {
                            hourlyRate = 0.0;
                        } // minemake sure na yung hourly rate is na sa last column ng  csv file. if may error, 0 ang lalabas na hourly rate.
                

                    System.out.println("\n=========================================================");

                    System.out.println("   " + "Process Payroll" + "   ");

                    System.out.println("\n=========================================================");
                    System.out.println("Employee #: " + empNumber);
                    System.out.println("Employee Name: " + empLastName + ", " + empFirstName);
                    System.out.println("Employee Birthday: " + empBirthday);
                    System.out.println("\n=========================================================");
                                
                    // computation of hours worked starts here.
                    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

                    for (int month = 6; month <= 12; month++) {

                    double firstCut = 0;
                    double secondCut = 0;
                    int daysAmonth = java.time.YearMonth.of(2024, month).lengthOfMonth();
                    //for looping of the attendance in june to december.

                    try (BufferedReader brAtt = new BufferedReader (new FileReader (empAttendance))){
                        brAtt.readLine();
                        String AttLine;
                        String Att[];

                        while ((AttLine = brAtt.readLine()) != null){
                            if(AttLine.trim().isEmpty()) continue;
                            Att = AttLine.split(",");
                            if (!Att[0].trim().equals(empNumber)) continue;
                        
                        //this line parse date and time - converts the string of date and time into a formatted one.
                        String[] dateFormat = Att[3].split("/");
                        int recordMonth = Integer.parseInt(dateFormat[0]);
                        int recordDay = Integer.parseInt(dateFormat[1]);
                        int recordYear = Integer.parseInt(dateFormat[2]);

                        if (recordYear != 2024 || recordMonth != month) continue; //skip if not the correct year or month

                        LocalTime logIn = LocalTime.parse(Att[4].trim(), timeFormat); //formats time in from the csv file, and cuts extra spaces if there are any.
                        LocalTime logOut = LocalTime.parse(Att[5].trim(), timeFormat);

                        double hoursWorked = computeHoursWorked(logIn, logOut);

                        if (recordDay <= 15) {
                            firstCut += hoursWorked; //adds hours worked to the first cut until the 15th of the month.
                        } else {
                            secondCut += hoursWorked; //adds hours worked to the second cut from the 16th to the end of the month.
                        }

                    }
                             //gross pay calculation.
                            double firstCutGross = firstCut * hourlyRate;
                            double secondCutGross = secondCut * hourlyRate;

                            // sss contribution calculation.
                            double totalGross = firstCutGross + secondCutGross;
                            double sssDeduction = computeSSS(totalGross, sssContribution);

                            // pagibig contribution calculation.
                            double pagIbigDeduction = computePagIbig(totalGross);

                            // philhealth contribution calculation.
                            double PHdeduction = computePH (totalGross);
                                                        
                            // total contribution calculation
                            double totalContribution = sssDeduction+pagIbigDeduction+PHdeduction;
                                                        
                            // withholding tax calculation
                            double tax = computeTax (totalGross, totalContribution);
                                                        
                            // total deduction computation
                            double totalDeductions = sssDeduction+pagIbigDeduction+PHdeduction+tax;
                                                        
                            // net salary
                            double firstCutNet = firstCutGross;
                            double secondCutNet = secondCutGross-totalDeductions;

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

                            System.out.println("Total Gross Salary for " + monthName + ": " + totalGross);

                            System.out.println("\n=========================================================");

                            System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
                            System.out.println("Total Hours Worked : "+ firstCut);
                            System.out.println("Gross Salary: "+ firstCutGross);
                            System.out.println("Net Salary: " + firstCutNet);
                                                            
                            System.out.println("\n=========================================================");

                            System.out.println("\nCutoff Date: " + monthName + " 16 to " + daysAmonth);
                            System.out.println("Total Hours Worked : " + secondCut);
                            System.out.println("Gross Salary: "+ secondCutGross);
                            System.out.println("SSS: "+ sssDeduction);
                            System.out.println("PagIbig: "+ pagIbigDeduction);
                            System.out.println("PhilHealth: "+ PHdeduction);
                            System.out.println("Tax: "+ tax);
                            System.out.println("Total Deductions: " + totalDeductions);
                            System.out.println("Net Salary: " + secondCutNet);

                            System.out.println("\n=========================================================");
                        }
                    }// closing bracket ng attendance for loop
                }
            } catch (IOException e) {
            
            System.out.println("Employee file error.");
        }
    }
}
