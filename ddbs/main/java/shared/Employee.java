package shared;

/**
 * Created by Andy on 10.11.16.
 */

/**
 * A class representing exactly the employees-table of the DB: https://github.com/datacharmer/test_db
 */
public class Employee {
    String emp_no;
    String birthdate;
    String first_name;
    String last_name;
    Gender gender;
    String hire_date;

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

    /**
     * Prints out employee's data
     */
    public void printOut() {
        System.out.println("----------------------------");
        System.out.println("Emp No: " + emp_no + "\t" + first_name + " " + last_name+"," + gender.toString());
        System.out.println("Birth Date: " +birthdate + "\t Hire Date: " + hire_date);
    }
}
