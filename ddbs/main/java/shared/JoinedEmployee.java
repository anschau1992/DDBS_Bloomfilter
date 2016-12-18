package shared;

/**
 * Created by Andy on 13.11.16.
 */

/**
 * This class represents a join of employee and dept_manager data's
 */
public class JoinedEmployee {
    //key attribute
    String emp_no;

    //employee's attributes
    String birthdate;
    String first_name;
    String last_name;
    Gender gender;
    String hire_date;

    //dept_manager's attributes
    String dept_no;
    String from_date;
    String to_date;

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

    public void printOut() {
        System.out.println("----------------------------");
        System.out.println("Emp No: " + emp_no + "\t" + first_name + " " + last_name+"," + gender.toString());
        System.out.println("Birth Date: " +birthdate + "\t Hire Date: " + hire_date);
        System.out.println("Dept No:" + dept_no + "\t From Date: " +from_date + "\t To Date: " + to_date);
    }
}
