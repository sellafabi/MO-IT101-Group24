package motorph.report;
public class ReportPrinter {

    /*========================================================================================
        Print Employee Information (Method #3) [ann]
    =========================================================================================*/

        /**
         * Displays basic employee details on the console.
         *
         * @param employeeNo employee identification number
         * @param lastName employee's last name
         * @param firstName employee's first name
         * @param birthday employee's date of birth
         */
    public static void printEmployeeInfo(String employeeNo, String lastName, String firstName, String birthday) {

        System.out.println("\n======================================");
        System.out.println( "        Employee Information");
        System.out.println("======================================");

        System.out.println("\nEmployee #: " + employeeNo);
        System.out.println("Employee Name: " + lastName + ", " + firstName);
        System.out.println("Employee Birthday: " + birthday);

        System.out.println("======================================\n");
    }

    public static void printMonthlyPayroll(int month, double[] data) {

        // Convert numeric month (6-12) to its name; otherwise, returns to the default label
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

        double firstHalf        = data[0];
        double secondHalf       = data[1];
        double grossFirst       = data[2];
        double grossSecond      = data[3];
        double sss              = data[4];
        double philhealth       = data[5];
        double pagibig          = data[6];
        double tax              = data[7];
        double totalDeductions  = data[8];
        double netSalary        = data[9];
        int daysInMonth         = (int) data[10];

        // First cutoff — no deductions; net equals gross
        System.out.println("\nFirst Cutoff");
        System.out.println("\nCutoff Date: "       + monthName + " 1 to 15");
        System.out.println("Total Hours Worked : " + firstHalf);
        System.out.println("Gross Salary: "        + grossFirst);
        System.out.println("Net Salary: "          + grossFirst);

        // Second cutoff — all four government deductions are applied here
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
        System.out.println("-------------------------------------\n");
    }
    
}
