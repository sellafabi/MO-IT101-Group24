package motorph.payroll;

import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PayrollCalculator {

    /*========================================================================================
        SSS Computation (Method #7) [rosella]
    =========================================================================================*/

        /**
         * Computes the SSS contribution based on the employee's monthly gross salary.
         *
         * Algorithm:
         * 1. Iterates through the SSS table containing salary brackets.
         * 2. Checks if the monthly gross falls within a bracket range.
         * 3. Returns the corresponding employee share once a match is found.
         * 4. If no exact match is found, returns the last valid bracket value.
         *
         * @param monthlyGross combined gross salary for the month
         * @return employee's SSS contribution
         */
    public static double computeSSS(double monthlyGross, List<String[]> sssTable) {

        // Stores last valid employee share as fallback
        double lastEmployeeShare = 0;
        for (String[] sssRow : sssTable) {

            // Columns: [0] rangeFrom, [1] rangeTo (or "Over"), [3] employee share
            double rangeFrom     = Double.parseDouble(sssRow[0].trim());
            String rangeToText   = sssRow[1].trim();
            double employeeShare = Double.parseDouble(sssRow[3].trim());

            lastEmployeeShare    = employeeShare;

            // Handle "Over" bracket (no upper limit)
            if (rangeToText.equalsIgnoreCase("Over")) {
                if (monthlyGross >= rangeFrom) return employeeShare;
            } else {
                double rangeTo = Double.parseDouble(rangeToText);
                if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                    return employeeShare;
                }
            }
        }
        return lastEmployeeShare; 
    }


    /*========================================================================================
        Pag-IBIG Computation (Method #8) [rosella]
    =========================================================================================*/

        /**
         * Computes the Pag-IBIG contribution based on the employee's monthly gross salary.
         *
         * Algorithm:
         * 1. Iterates through the Pag-IBIG table containing salary ranges and rates.
         * 2. Identifies the matching salary bracket.
         * 3. Computes contribution using the corresponding rate.
         * 4. Applies a maximum cap of PHP 100.00.
         *
         * @param monthlyGross combined gross salary for the month
         * @return employee's Pag-IBIG contribution (capped at PHP 100.00)
         */
    public static double computePagibig(double monthlyGross, List<String[]> pagibigTable) {

        double contribution = 0;

        for (String[] pagibigRow : pagibigTable) {

            if (pagibigRow.length < 2) continue;

            // Cleans the data by removing quotes and spaces
            String salaryRange = pagibigRow[0].trim().replace("\"", "");
            String rateText    = pagibigRow[1].trim();

            // Skip invalid or incomplete rows
            if (salaryRange.isEmpty() || rateText.isEmpty() || !rateText.endsWith("%")) continue;

            double rate = Double.parseDouble(rateText.replace("%", "").trim()) / 100.0;

            // Handle "Over" range (no upper limit)
            if (salaryRange.toLowerCase().startsWith("over")) {

                String floorText = salaryRange.substring("over".length()).trim().replace(",", "");
                double floor     = Double.parseDouble(floorText);

                if (monthlyGross > floor) {
                    contribution = monthlyGross * rate;
                    break;
                }
            // Handle ranged values (e.g., "At least X to Y")
            } else if (salaryRange.toLowerCase().startsWith("at least")) {

                String rangeOnly = salaryRange.substring("at least".length()).trim();
                String[] parts   = rangeOnly.split("(?i)\\s+to\\s+");

                double rangeFrom = Double.parseDouble(parts[0].trim().replace(",", ""));
                double rangeTo   = Double.parseDouble(parts[1].trim().replace(",", ""));

                if (monthlyGross >= rangeFrom && monthlyGross <= rangeTo) {
                    contribution = monthlyGross * rate;
                    break;
                }
            }
        }
        // Apply maximum contribution cap (PHP 100.00)
        return Math.min(contribution, 100);
    }


    /*========================================================================================
        PhilHealth Computation (Method #9) [ann]
    =========================================================================================*/

        /**
         * Computes the PhilHealth contribution based on the employee's monthly gross salary.
         *
         * Algorithm:
         * 1. Determines the applicable salary bracket.
         * 2. Applies the corresponding contribution rule.
         * 3. Returns the employee's share (50% of total premium).
         *
         * @param monthlyGross combined gross salary for the month
         * @return employee's PhilHealth contribution
         */
    public static double computePhilhealth (double monthlyGross) {

        double philhealthDeduction = 0.0;

        if (monthlyGross <= 10000) {
            philhealthDeduction = 300 / 2;

        } else if (monthlyGross > 10000 && monthlyGross < 60000) {
            philhealthDeduction =  monthlyGross*(0.03) / 2;

        } else if (monthlyGross >= 60000) {
            philhealthDeduction = 1800 / 2;
        }
        return philhealthDeduction; 
    }


    /* =======================================================================================
        Tax Computation (Method #10) [ann]
    ==========================================================================================*/

        /**
        * Computes the monthly withholding tax of an employee using the BIR tax table.
        *
        * Algorithm:
        * The BIR (Bureau of Internal Revenue) withholding tax is computed after deducting all of the 
        * mandated government contributions (SSS + PhilHealth + Pag-IBIG) from the monthly gross salary.
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
        * @return                  the computed withholding tax amount
        */
    public static double withholdingTax (double totalGross, double totalContribution) {

        double tax = 0.00;

        // Contributions are subtracted first; tax is applied only to the remaining taxable amount
        double taxableMonthlySalary = totalGross - totalContribution; 
        
        // Match taxable salary against BIR brackets
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
        return tax;
    }  


    /*========================================================================================
        Hours Worked Computation (Method #11) [ann]
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
        final int       LUNCH_BREAK    = 60;                               // unpaid break in minutes

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
        // Convert minutes to hours
        return minutesWorked / 60.0;      
    }


    /*========================================================================================
        Gross Computation (Method #12) [ann]
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
         * @return      gross salary for the cutoff period
         */
    static double computeGross(double hours, double rate) {
        return hours * rate;
    }

        public static double[] computeMonthlyPayroll( String employeeNo, int month, double rate,
                                                    List<String[]> attendanceRecords,
                                                    DateTimeFormatter timeFormat,
                                                    List<String[]> sssTable,
                                                    List<String[]> pagibigTable) {

        double firstHalf  = 0; // hours worked days 1–15
        double secondHalf = 0; // hours worked days 16–end

        // lengthOfMonth() gives the correct last day (e.g., 30 for June, 31 for July)
        int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();

        for (String[] attData : attendanceRecords) {

            // Skip malformed rows with missing columns
            if (attData.length < 6) {
                System.out.println("Skipping invalid attendance row: " + java.util.Arrays.toString(attData));
                continue;
            }

            if (!attData[0].equals(employeeNo)) continue; // Column 0 contains the employee number

            // Column 3 is the date in MM/DD/YYYY format — split to get month, day, and year
            String[] dateParts   = attData[3].split("/");
            int recordMonth, day, year;

            try {
                recordMonth = Integer.parseInt(dateParts[0]);
                day         = Integer.parseInt(dateParts[1]);
                year        = Integer.parseInt(dateParts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid date format: " + java.util.Arrays.toString(attData));
                continue;
            }

            if (year != 2024 || recordMonth != month) continue;

            LocalTime login, logout;

            try {
                login  = LocalTime.parse(attData[4].trim(), timeFormat);
                logout = LocalTime.parse(attData[5].trim(), timeFormat);
            } catch (Exception e) {
                System.out.println("Invalid time format: " + java.util.Arrays.toString(attData));
                continue;
            }

            double hours = computeHoursWorked(login, logout);

            if (day <= 15) firstHalf  += hours; // days 1–15:   first cutoff
            else           secondHalf += hours; // days 16–end of the month: second cutoff
        }

        // Compute gross salary for the first and second cut-off.
        double grossFirst  = computeGross(firstHalf,  rate);
        double grossSecond = computeGross(secondHalf, rate);

        // Deductions are based on the combined monthly gross, not per-cutoff gross
        double monthlyGross = grossFirst + grossSecond;
        double sss          = computeSSS(monthlyGross, sssTable);
        double pagibig      = computePagibig(monthlyGross, pagibigTable);
        double philhealth   = computePhilhealth(monthlyGross);

        // BIR rule: contributions must be deducted from gross before tax is computed
        double totalContribution = sss + philhealth + pagibig;
        double tax               = withholdingTax(monthlyGross, totalContribution);
        double totalDeductions   = sss + pagibig + philhealth + tax;

        // Deductions are applied on the second cutoff payout only, per process flow.
        double netSalary = grossSecond - totalDeductions;

        return new double[] {
            firstHalf, secondHalf,
            grossFirst, grossSecond,
            sss, philhealth, pagibig, tax,
            totalDeductions, netSalary,
            daysInMonth
        };
    }  


    
}
