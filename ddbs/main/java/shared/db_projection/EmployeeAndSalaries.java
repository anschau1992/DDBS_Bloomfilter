package shared.db_projection;

import shared.Gender;

/**
 * Created by Andy on 07.12.16.
 */
public class EmployeeAndSalaries {
    // employee
    String emp_no;
    String birthdate;
    String first_name;
    String last_name;
    Gender gender;
    String hire_date;

    //salary
    private int salary;
    private String from_date;
    private String to_date;

    public EmployeeAndSalaries(Employee employee, Salary salary) {
        this.emp_no = employee.getEmp_no();
        this.birthdate = employee.getBirthdate();
        this.first_name = employee.getFirst_name();
        this.last_name = employee.last_name;
        this.gender = employee.getGender();
        this.hire_date = employee.getHire_date();
        this.salary = salary.getSalary();
        this.from_date = salary.getFrom_date();
        this.to_date = salary.getTo_date();
    }

    public String getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(String emp_no) {
        this.emp_no = emp_no;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getHire_date() {
        return hire_date;
    }

    public void setHire_date(String hire_date) {
        this.hire_date = hire_date;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }


    public void printOut() {
        System.out.println("----------------------------");
        System.out.println("Emp No: " + emp_no + "\t" + first_name + " " + last_name+"," + gender.toString());
        System.out.println("Salary: " +salary + "\t From Date: " + from_date + "\t To Date: " + to_date);
    }
}
