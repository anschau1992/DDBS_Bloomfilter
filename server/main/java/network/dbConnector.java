package network;

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
     * Tries to connect with the local MySQL DB
     */
    private DBConnector() {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/employees";
            String username = "ddbsUser";
            String password = "ddbs";
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to local MySQL");
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


    public ArrayList<Employee> getEmployeesByFirstName(String firstName) throws SQLException {
        String sqlQuery = "SELECT * FROM employees " +
                                "WHERE first_name = '" + firstName + "'";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        ResultSet result = statement.executeQuery();

        ArrayList<Employee> array = new ArrayList<Employee>();

        while (result.next()) {
            array.add(createDBEmployee(result));
        }

        return array;
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

    public ArrayList <String> getAllEmployeeIds () throws SQLException {
        String sqlQuery = "SELECT emp_no FROM employees";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        ArrayList<String> array = new ArrayList<String>();

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
}
