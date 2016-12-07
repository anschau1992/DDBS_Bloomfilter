package shared.db_projection;

/**
 * Created by Andy on 13.11.16.
 */
public class Dept_Manager {
    String emp_no;
    String dept_no;
    String from_date;
    String to_date;

    public String getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(String emp_no) {
        this.emp_no = emp_no;
    }

    public String getDept_no() {
        return dept_no;
    }

    public void setDept_no(String dept_no) {
        this.dept_no = dept_no;
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
     * Prints out dept_manager's data
     */
    public void printOut() {
        System.out.println("----------------------------");
        System.out.println("Emp No: " + emp_no + "\t" + "Dept No:" + dept_no);
        System.out.println("From Date: " +from_date + "\t To Date: " + to_date);
    }
}
