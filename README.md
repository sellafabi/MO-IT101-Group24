# MO-IT101-Group24 — MotorPH Payroll System

## Team Details

| Name | Contribution |
|------|-------------|
| Rosella Fabillar | SSS computation, Pag-IBIG computation, Payroll computation & display, View One Employee, View All Employees |
| Ann Margarette Pascual | Main method & login system, PhilHealth computation, Withholding tax computation, Hours worked computation, Gross salary computation |

---

## Program Details

The MotorPH Payroll System is a Java console application that automates payroll processing for MotorPH employees.

**How it works:**
1. The program launches with a login prompt. Valid credentials are:
   - Username: `employee` or `payroll_staff`
   - Password: `12345`
   - If credentials are incorrect, the program displays an error and terminates immediately — no retry is allowed
2. **Employees** can look up their own basic personal information (employee number, name, and birthday) using their employee number
3. **Payroll Staff** can process payroll for one specific employee or for all employees at once
4. The system reads employee details from `details.csv` and attendance records from `attendance.csv`, then computes each employee's salary from **June to December 2024** on a **semi-monthly cutoff** basis:
   - **First Cutoff (Days 1–15):** No government deductions — net salary equals gross salary
   - **Second Cutoff (Days 16–end of month):** All government deductions are applied and subtracted from gross to produce the net salary
5. Government deductions — **SSS, PhilHealth, Pag-IBIG, and Withholding Tax** — are computed from the **combined monthly gross** (1st cutoff + 2nd cutoff) and deducted on the second payout only

---

## Features

### Login System
- Two roles: `employee` and `payroll_staff`
- Both username and password must be correct simultaneously
- Invalid input for menu options is handled gracefully with an error message

### Employee Role
- Enter employee number to view: Employee Number, Full Name, and Birthday
- Displays "Employee number does not exist." if no match is found

### Payroll Staff Role
- **Process One Employee:** Enter an employee number to generate a full payroll report from June–December 2024
- **Process All Employees:** Automatically generates payroll reports for every employee in the CSV file

---

## Payroll Computation Details

### Hours Worked (`computeHoursWorked`)
Three rules are applied per attendance record:
1. **Overtime Cap:** Logout times after 5:00 PM are capped at 5:00 PM — overtime is not counted
2. **Grace Period:** Employees who log in at or before 8:10 AM have their login adjusted to the standard start of 8:00 AM
3. **Lunch Break Deduction:** A mandatory 60-minute unpaid lunch break is deducted. If total time logged is 60 minutes or less, hours worked is 0

### Gross Salary (`computeGross`)
- Gross = Total Hours Worked × Hourly Rate
- Computed separately for each cutoff period
- Allowances are **not** included in gross salary

### SSS (`computeSSS`)
- Contribution is looked up from `sss.csv` using salary bracket matching
- Based on combined monthly gross (1st + 2nd cutoff)
- The last bracket uses "Over" as the upper bound (no ceiling — maximum bracket applies)

### Pag-IBIG (`computePagibig`)
- Contribution rate is looked up from `pagibig.csv`
- Raw contribution = Monthly Gross × Rate
- **Capped at a maximum of PHP 100.00 per month** regardless of salary

### PhilHealth (`computePhilhealth`)
- Fixed-rate brackets based on combined monthly gross:
  - Monthly gross ≤ PHP 10,000 → Fixed PHP 150.00
  - Monthly gross PHP 10,001–59,999 → 1.5% of monthly gross
  - Monthly gross ≥ PHP 60,000 → Fixed PHP 900.00
- Employee pays only half of the total premium

### Withholding Tax (`withholdingTax`)
- Computed from **taxable monthly salary** = Monthly Gross − (SSS + PhilHealth + Pag-IBIG)
- Uses 6 BIR tax brackets with a fixed base plus a percentage on the excess

---

## Payroll Output Format

For each month (June–December), each employee's report displays:

**First Cutoff (e.g., June 1 to June 15)**
- Cutoff Date
- Total Hours Worked
- Gross Salary
- Net Salary *(equals Gross — no deductions)*

**Second Cutoff (e.g., June 16 to June 30)**
- Cutoff Date
- Total Hours Worked
- Gross Salary
- SSS, PhilHealth, Pag-IBIG, Withholding Tax (itemized)
- Total Deductions
- Net Salary

---

## File Structure

| File | Description |
|------|-------------|
| `src/details.csv` | Employee records (employee number, name, birthday, hourly rate at column 18, etc.) |
| `src/attendance.csv` | Attendance records (employee number, date in MM/DD/YYYY, login time, logout time) |
| `src/sss.csv` | SSS contribution table (salary brackets and employee share amounts) |
| `src/pagibig.csv` | Pag-IBIG contribution table (salary ranges and contribution rates) |

---

## Methods Summary

| Method | Role | Contributor |
|--------|------|-------------|
| `main()` | Login system and menu routing | Ann |
| `computeSSS()` | SSS contribution lookup from bracket table | Rosella |
| `computePagibig()` | Pag-IBIG contribution with PHP 100 cap | Rosella |
| `computePhilhealth()` | PhilHealth contribution by salary bracket | Ann |
| `withholdingTax()` | BIR withholding tax by 6-bracket table | Ann |
| `computeHoursWorked()` | Daily hours with grace period, OT cap, lunch deduction | Ann |
| `computeGross()` | Gross salary = hours × hourly rate | Ann |
| `processPayroll()` | Full payroll computation and display (June–December) | Rosella |
| `oneEmployee()` | Loads and processes payroll for a single employee | Rosella |
| `allEmployee()` | Loads and processes payroll for all employees | Rosella |

---

## Project Plan

[View Project Plan](https://docs.google.com/spreadsheets/d/1nOglg-Bu7eLWkUNbUG-aD5LpB2aPPZ5IaRWzEvQpfC4/edit?gid=2134013708#gid=2134013708)