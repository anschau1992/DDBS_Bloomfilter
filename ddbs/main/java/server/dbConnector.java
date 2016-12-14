package server;

import shared.db_projection.Dept_Manager;
import shared.db_projection.Employee;
import shared.Gender;
import shared.db_projection.Salary;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Andy on 10.11.16.
 * This class handles connection and request to the MySQL database
 */
public class DBConnector {
    private volatile static DBConnector uniqueInstance;
    private Connection connection;

    /**
     * Tries to connect with the client MySQL DB
     */
    private DBConnector() {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/employees";
            String username = "ddbsUser";
            String password = "ddbs";
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to client MySQL");
            this.connection = conn;
        } catch (Exception e) {
            System.err.println("DB-Connection Error: " + e);
        }
    }

    /**
     * Singleton DBConnector
     * @return the DBConnector Instance
     */
    public static synchronized DBConnector getUniqueInstance() {
        if(uniqueInstance == null) {
            synchronized (DBConnector.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new DBConnector();
                }
            }
        }

        return uniqueInstance;
    }

    /**
     * Get all the employees from DB having the exact same name as the given String parameter
     * @param firstName of the searched employees
     * @return a Array of Employees
     * @return null if failing
     * @throws SQLException
     */
    public ArrayList<Employee> getEmployeesByFirstName(String firstName) throws SQLException {
        String sqlQuery = "SELECT * FROM employees " +
                                "WHERE first_name = '" + firstName + "'";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        ResultSet result = statement.executeQuery();

        ArrayList<Employee> employees = new ArrayList<Employee>();

        while (result.next()) {
            employees.add(createDBEmployee(result));
        }

        return employees;
    }

    /**
     * Get all the employeeIds from DB having the exact same name as the given String parameter
     * @param firstName of the searched employees
     *                   a Array of emp_no as Integer
     * @return null if failing
     * @throws SQLException
     */
    public ArrayList<Integer> getEmployeeIdsByFirstName (String firstName) throws SQLException {
        String sqlQuery = "SELECT * FROM employees " +
                "WHERE first_name = '" + firstName + "'";

        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        ResultSet result = statement.executeQuery();
        ArrayList<Integer> employeeIds = new ArrayList<Integer>();

        while (result.next()) {
            employeeIds.add(Integer.parseInt(result.getString("emp_no")));
        }
        return employeeIds;
    }


    public Employee getEmployeeByID (String id) throws SQLException {
        String sqlQuery = "SELECT * FROM employees " +
                "WHERE emp_no = '" + id + "'";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            return createDBEmployee(result);
        }
        return null;
    }

    /**
     * Get all the ID's of the employees in the DB
     * @return Array of ID's as String
     * @throws SQLException
     */
    public ArrayList <String> getAllEmployeeIds () throws SQLException {
        ArrayList<String> array = new ArrayList<String>();

        String sqlQuery = "SELECT emp_no FROM employees";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            array.add(result.getString("emp_no"));
        }

        return array;
    }

    /**
     * Copies all relevant data from a db-employee into a new created Employee-Object
     * @param result
     * @return employee new employee
     * @throws SQLException
     */
    private Employee createDBEmployee(ResultSet result) throws SQLException {
        Employee employee = new Employee();
        employee.setFirst_name(result.getString("first_name"));
        employee.setLast_name(result.getString("last_name"));
        employee.setEmp_no(result.getString("emp_no"));
        employee.setBirthdate(result.getString("birth_date"));
        employee.setGender(Gender.valueOf(result.getString("gender")));
        employee.setHire_date(result.getString("hire_date"));

        return employee;
    }

    /**
     * Gets all the Dept_Managers form the DB as Dept_Managers Objects
     * @return a ArrayList of Dept_Managers
     * @throws SQLException if request on DB not successful
     */
    public ArrayList<Dept_Manager> getAllDeptManager() throws SQLException {
        ArrayList<Dept_Manager> dept_managers = new ArrayList<Dept_Manager>();

        String sqlQuery = "SELECT * FROM dept_manager";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        ResultSet result = statement.executeQuery();
        while(result.next()) {
            dept_managers.add(createDBDeptManager(result));
        }
        return dept_managers;
    }

    private Dept_Manager createDBDeptManager(ResultSet result) throws SQLException {
        Dept_Manager dept_manager = new Dept_Manager();
        dept_manager.setEmp_no(result.getString("emp_no"));
        dept_manager.setDept_no(result.getString("dept_no"));
        dept_manager.setFrom_date(result.getString("from_date"));
        dept_manager.setTo_date(result.getString("to_date"));

        return dept_manager;
    }

    /**
     * Get all the Salaries from the DB as Salaries object,
     * where salary-amount is higher than the given salary
     * @param salary as the lower bound
     * @return all Salaries higher than the given param
     * @throws SQLException
     */
    public ArrayList<Salary> getSalariesHigherThan(int salary) throws SQLException {
        ArrayList<Salary> salaries = new ArrayList<Salary>();

        String sqlQuery = "SELECT * FROM salaries WHERE salary >'" + salary + "'";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        ResultSet result = statement.executeQuery();
        while(result.next()) {
            salaries.add(createSalary(result));
        }

        return salaries;
    }

    private Salary createSalary(ResultSet result) throws SQLException {
        Salary salary = new Salary();
        salary.setSalary(Integer.parseInt(result.getString("salary")));
        salary.setEmp_no(result.getString("emp_no"));
        salary.setFrom_date(result.getString("from_date"));
        salary.setTo_date(result.getString("to_date"));

        return salary;
    }

    public ArrayList<Integer> getSalaryIDHigherThan(int salary) throws SQLException {
        ArrayList<Integer> emp_nos = new ArrayList<Integer>();

        String sqlQuery = "SELECT * FROM salaries WHERE salary >'" + salary + "'";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        ResultSet result = statement.executeQuery();
        while(result.next()) {
            emp_nos.add(Integer.parseInt(result.getString("emp_no")));
        }

        return emp_nos;
    }

    public Salary getSalaryByID (String id) throws SQLException {
        String sqlQuery = "SELECT * FROM salaries " +
                "WHERE emp_no = '" + id + "'";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            return createSalary(result);
        }
        return null;
    }
}
