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

/**
 *
 * @author rosellafabillar
 */
public class motorPH {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = sc.nextLine();

        if (!username.equals("e") && !username.equals("s")) {
            System.out.println("\nIncorrect username!");
            System.out.println("Program terminated.\n");
            sc.close();
            return;
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (username.equals("e")) {
            if (password.equals("1")) {
                employeeMenu();
            } else {
                System.out.println("\nIncorrect password!");
                System.out.println("Program terminated.\n");
            }

        } else if (username.equals("s")) {
            if (password.equals("12")) {
                staffMenu();
            } else {
                System.out.println("\nIncorrect password!");
                System.out.println("Program terminated.\n");
            }
        }

        sc.close();
    }

    // Calculate Hours Worked
    static double computeHours(LocalTime login, LocalTime logout) {

        LocalTime graceTime = LocalTime.of(8, 10);
        LocalTime cutoffTime = LocalTime.of(17, 0);

        // Apply 17:00 cutoff
        if (logout.isAfter(cutoffTime)) {
            logout = cutoffTime;
        }

        long minutesWorked = Duration.between(login, logout).toMinutes();

        // Deduct lunch (if total worked is more than 1 hour)
        if (minutesWorked > 60) {
            minutesWorked -= 60;
        } else {
            minutesWorked = 0;
        }

        double hours = minutesWorked / 60.0;

        // Grace period rule
        if (!login.isAfter(graceTime)) {
            return 8.0;
        }

        // Return hours worked, capped at 8
        return Math.min(hours, 8.0);
    }

    static double computeGross(double hours, double rate) {
        return hours * rate;
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


            /* =====================================================
                        Philhealth computation (method 6)
        ===================================================== */

public static double computePhilhealth(double monthlyGross) {

    String file = "MO-IT101-Group24.test/src/philhealth.csv";

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

        br.readLine(); // skip header
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] data = line.split(",");

            double rangeFrom = Double.parseDouble(data[0].trim()); //the first column
            String rangeToText = data[1].trim(); // yover to 1800

            
            if (data.length == 4) {
                double rangeTo = Double.parseDouble(rangeToText);
                if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                    double premium = monthlyGross * 0.03; // 3%
                    return premium / 2; // employee share na 2%
                }

            } else if (rangeToText.equalsIgnoreCase("Over")) {
                // fixed na to max 1800
                if (monthlyGross >= rangeFrom) {
                    double fixedPremium = Double.parseDouble(data[2].trim());
                    return fixedPremium / 2;
                }

            } else {
                // Fixed min — 300
                double rangeTo = Double.parseDouble(rangeToText);
                if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                    double fixedPremium = Double.parseDouble(data[2].trim());
                    return fixedPremium / 2;
                }
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}


public static double withholdingTax(double taxableIncome) {

    String file = "MO-IT101-Group24.test/src/withholding.csv";

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

        br.readLine(); // skip header
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] data = line.split(",");

            double floor   = Double.parseDouble(data[0].trim());
            double ceiling = Double.parseDouble(data[1].trim());
            double baseTax = Double.parseDouble(data[2].trim());
            double rate    = Double.parseDouble(data[3].trim());

            if (taxableIncome >= floor && taxableIncome <= ceiling) {
                return baseTax + (taxableIncome - floor) * rate;
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

        

    // __________________LOGIN SYSTEM________________________
        
    public static void employeeMenu() {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n1. View Employee Details");
        System.out.println("2. Exit");
        System.out.print("Choose option (1 or 2): ");

        int choice = sc.nextInt();
        sc.nextLine();

            if (choice == 1) {
                        employeeDetails();

                } else if (choice == 2) {

                    System.out.println("\n");
                    System.out.println("\nThank You for using MotorPH Payroll System!");
                    System.out.println("\n");
            }
        sc.close();
    }

    /* =====================================================
                        Employee View Details (method 3)
    ===================================================== */

    public static void employeeDetails() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your employee number: ");
        String number = sc.nextLine();
        System.out.println();

        boolean found = false;

        String fileforemployee = "MO-IT101-Group24.test/src/details.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(fileforemployee))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String employeeNo = data[0].trim();


                if (employeeNo.equals(number)) {

                    found = true;

                    String fullName = data[1] + ", " + data[2];
                    String birthday = data[3];

                    // employee details
                    System.out.println("===================================================================");
                    System.out.printf("%40s%n", "Employee Details");
                    System.out.println("===================================================================");

                    System.out.println("Employee No.: " + employeeNo);
                    System.out.println("Employee Name: " + fullName);
                    System.out.println("Employee Birthday: " + birthday);
                    System.out.println("___________________________________________________________________");

                    break;
                }
            }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!found) {
                    System.out.println("\n");
                    System.out.println("Employee number does not exist.");
                    System.out.println("\n");
                }

        sc.close();
    }

    /* =====================================================
                Payroll Access Menu (method 3)
    ===================================================== */

    public static void staffMenu() {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n1. Process Payroll");
        System.out.println("2. Exit the program");
        System.out.print("Choose option: ");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {

            System.out.println("\n1. One employee");
            System.out.println("2. All employees");
            System.out.println("3. Exit the program");
            System.out.print("Choose option: ");

            int option = sc.nextInt();
            sc.nextLine();

            if (option == 1) {

                oneEmployee();

            } else if (option == 2) {

                allEmployee();
                         

            } else if (option == 3) {
                System.out.println("\n");
                System.out.println("Thank You for using MotorPH Payroll System!"); 
                System.out.println("\n");
            }
        }

            if (choice == 2) {
                System.out.println("\n");
                System.out.println("Thank You for using MotorPH Payroll System!");
                System.out.println("\n");
            }
        sc.close();
    }

    public static void oneEmployee () {

        String empFile = "MO-IT101-Group24.test/src/details.csv";
        String attFile = "MO-IT101-Group24.test/src/attendance.csv";

        Scanner sc = new Scanner(System.in);

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

        sc.close();
    }

    public static void allEmployee() {

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
    

