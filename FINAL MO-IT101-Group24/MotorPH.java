import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
}
