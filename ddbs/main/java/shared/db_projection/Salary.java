package shared.db_projection;

/**
 * Created by Andy on 07.12.16.
 */

/**
 * Projection of the salaries tables of the DB employees
 */
public class Salary {
    private String emp_no;
    private int salary;
    private String from_date;
    private String to_date;

    public String getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(String emp_no) {
        this.emp_no = emp_no;
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

    /**
     * Prints out employee's data
     */
    public void printOut() {
        System.out.println("----------------------------");
        System.out.println("Emp No: " + emp_no + "\t Salary " + salary+",");
        System.out.println("From Date: " +from_date + "\t To Date: " + to_date);
    }
}
