package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import shared.BloomFilter;
import shared.db_projection.Dept_Manager;
import shared.JoinedEmployee;
import shared.db_projection.Employee;
import shared.db_projection.Salary;
import shared.hashFunctions.UniversalHashFunction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by Andy on 09.11.16.
 */
public class BloomFilterServant extends UnicastRemoteObject implements BloomFilterService {
    private BloomFilter bloomFilter;
    DBConnector connector = DBConnector.getUniqueInstance();
    Gson gson = new GsonBuilder().create();

    //all the Employees from the last call of getBitSetEmployeesByName
    ArrayList<Employee> lastEmployeeProjection;
    //all the Employees from the last call of getBitSetEmployeesByName
    ArrayList<Salary> lastSalaryProjection;

    public BloomFilterServant() throws RemoteException {
        super();
    }

    /**
     * Receives a new BloomFilter from client side and copies it for using.
     * In order to communicate properly. Server side and client.Client side need to interact with the exact same BloomFilter
     *
     * @param slotSize              is the number of slots within the Bitset of the Bloomfilter
     * @param numberOfHashFunctions generate the numbers of hashFunctions used for the bloomFilter
     * @return confirmation message
     */
    public String createNewBloomFilter(int slotSize, int numberOfHashFunctions) {
        this.bloomFilter = new BloomFilter(slotSize, numberOfHashFunctions);
        System.out.println("New Bloomfilter is set on Server");
        return "New Bloomfilter is set on Server";
    }

    public String sendHashFunctions(int[] aRandom, int[] bRandom) {
        UniversalHashFunction[] universalHashFunctions = new UniversalHashFunction[aRandom.length];
        for (int i = 0; i < aRandom.length; i++) {
            universalHashFunctions[i] = new UniversalHashFunction(aRandom[i], bRandom[i]);
        }
        this.bloomFilter.setHashFunctions(universalHashFunctions);
        return "New Hashfunctions are set on remote";
    }

    /**
     * Recevies a loaded bitSet from client side.
     * Iterates through the database and check for matches of Employee's ID with the BloomFilter of the BitSet
     * Returns all matching Employees (including possibly false positives)
     *
     * @param bitSet used by the BloomFilter
     * @return matching employees, incl. false positives
     */
    public String sendBitset(BitSet bitSet) {
        try {
            this.bloomFilter.readInBloomFilter(bitSet);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }

        ArrayList<String> idsInBLoomFilter;
        try {
            //all ID's of DB
            ArrayList<String> employeeIds = connector.getAllEmployeeIds();

            //ID's containing in BloomFilter & DB (incl. possible false positives)
            idsInBLoomFilter = checkIDsInBloomfilter(employeeIds);
            ArrayList<Employee> returningEmployees = getWholeEmployeeData(idsInBLoomFilter);

            Gson gson = new GsonBuilder().create();
            return gson.toJson(returningEmployees);

        } catch (Exception e) {
            System.err.println("Join has been failed: " + e);
            return null;
        }
    }

    /**
     * Checks all ID given as Parameter, if they are also in the BloomFilter
     * IMPORTANT: The returning ID's can include false-positives
     *
     * @param employeeIds
     * @return all the ID matching with the BloomFilter's BitSet
     */
    private ArrayList<String> checkIDsInBloomfilter(ArrayList<String> employeeIds) {
        ArrayList<String> idsInBloomFilter = new ArrayList<String>();
        for (String employeeID : employeeIds) {
            if (bloomFilter.check(employeeID.hashCode())) {
                idsInBloomFilter.add(employeeID);
            }
        }
        return idsInBloomFilter;
    }

    private ArrayList<Employee> getWholeEmployeeData(ArrayList<String> idsInBLoomFilter) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        for (String id : idsInBLoomFilter) {
            employees.add(connector.getEmployeeByID(id));
        }
        return employees;
    }


    public String sendDeptManagerClassic(String gsonDeptManagers) throws RemoteException {
        Gson gson = new GsonBuilder().create();
        ArrayList<Dept_Manager> deptManagers = gson.fromJson(gsonDeptManagers, new TypeToken<ArrayList<Dept_Manager>>() {
        }.getType());


        ArrayList<JoinedEmployee> joinedEmployees = null;
        try {
            joinedEmployees = joinDeptManagersWithEmployees(deptManagers);
        } catch (SQLException e) {
            System.out.println("Joining of Employee and Dept_manager failed: " + e);
        }

        //stringify
        return gson.toJson(joinedEmployees);
    }

    /**
     * Gets all the ID's from employees of the DB where firstname is same as the param
     * and returns them as Integer-ArrayList in GSON-Format
     *
     * @param name
     * @return
     * @throws RemoteException
     */
    public String getEmployeesIDByName(String name) throws RemoteException {
        try {
            ArrayList<Integer> employeesID = connector.getEmployeeIdsByFirstName(name);

            System.out.println("Server is returning Employee-ID's with name " + name + " as Integer-ArrayList");
            return gson.toJson(employeesID);

        } catch (SQLException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * Gets all the employees from the DB where firstname is same as the param,
     * but send them back as a Bitset
     *
     * @param name
     * @return Bitset filled with the employees matching
     * @throws RemoteException
     */
    public BitSet getBitSetEmployeesByName(String name) throws RemoteException {
        try {
            lastEmployeeProjection = connector.getEmployeesByFirstName(name);

            //fill in Bitset with emp_no
            for (Employee emp : lastEmployeeProjection) {
                bloomFilter.add(Integer.parseInt(emp.getEmp_no()));
            }
            System.out.println("Server is returning Employees with name " + name + " as BitSet");
            return bloomFilter.getBitSet();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all the ID's from Employees where the salary is higher than the param
     *
     * @param minSalary
     * @return salary-Objects
     * @throws RemoteException
     */
    public String getSalaryIdsHigherAs(int minSalary) throws RemoteException {
        try {
            ArrayList<Integer> emp_nos = connector.getSalaryIDHigherThan(minSalary);

            System.out.println("Server is returning Employee-ID's with salaries > " + minSalary + " as Integer ArrayList");
            return gson.toJson(emp_nos);

        } catch (SQLException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * Get all the salaries from the DB where amount of salary is higher than the param,
     * but sends them back as a Bitset
     *
     * @param minSalary
     * @return Bitset filled with the salaries matching
     * @throws RemoteException
     */
    public BitSet getBitSetSalaryHigherAs(int minSalary) throws RemoteException {
        try {
            lastSalaryProjection = connector.getSalariesHigherThan(minSalary);

            for (Salary sal : lastSalaryProjection) {
                bloomFilter.add(Integer.parseInt(sal.getEmp_no()));
            }

            System.out.println("Server is returning salaries > " + minSalary + "as BitSet");
            return bloomFilter.getBitSet();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Evaluates matches with the given Bitset in the projection of employees
     * and sends them back to the client
     *
     * @param bitSet
     * @return
     * @throws RemoteException
     */
    public String getEmployeesMatching(BitSet bitSet) throws RemoteException {
        ArrayList<Employee> matchingEmployees = new ArrayList<Employee>();
        try {
            this.bloomFilter.readInBloomFilter(bitSet);

            // evaluate all matches
            for (Employee emp : lastEmployeeProjection) {
                int emp_no = Integer.parseInt(emp.getEmp_no());
                if (bloomFilter.check(emp_no)) {
                    matchingEmployees.add(emp);
                }
            }
            return gson.toJson(matchingEmployees);

        } catch (Exception e) {
            System.out.println("EmployeesMatching: Bitset-Size is not matching, " + e);
            return null;
        }
    }

    public String getSalariesMatching(BitSet bitSet) throws RemoteException {
        ArrayList<Salary> matchingSalaries = new ArrayList<Salary>();
        try {
            this.bloomFilter.readInBloomFilter(bitSet);

            // evaluate all matches
            for (Salary sal : lastSalaryProjection) {
                int emp_no = Integer.parseInt(sal.getEmp_no());
                if (bloomFilter.check(emp_no)) {
                    matchingSalaries.add(sal);
                }
            }
            return gson.toJson(matchingSalaries);

        } catch (Exception e) {
            System.out.println("SalariesMatching: Bitset-Size is not matching, " + e);
            return null;
        }
    }

    public String getEmployeesMatchingId(String gsonIds) throws RemoteException {
        ArrayList<Integer> empNos = gson.fromJson(gsonIds,
                new TypeToken<ArrayList<Integer>>() {
                }.getType());

        ArrayList<Employee> employees = new ArrayList<Employee>();
        for (int empNo : empNos) {
            try {
                employees.add(connector.getEmployeeByID(Integer.toString(empNo)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(employees);
    }

    public String getSalariesMatchingId(String gsonIds) throws RemoteException {
        ArrayList<Integer> empNos = gson.fromJson(gsonIds,
                new TypeToken<ArrayList<Integer>>() {
                }.getType());

        ArrayList<Salary> salaries = new ArrayList<Salary>();
        for (int empNo : empNos) {
            for (Salary salary: lastSalaryProjection) {
                if(empNo == Integer.parseInt(salary.getEmp_no())) {
                    salaries.add(salary);
                }
            }

        }
        return gson.toJson(salaries);
    }

    private ArrayList<JoinedEmployee> joinDeptManagersWithEmployees(ArrayList<Dept_Manager> deptManagers) throws SQLException {
        connector = DBConnector.getUniqueInstance();
        ArrayList<JoinedEmployee> joinedEmployees = new ArrayList<JoinedEmployee>();
        for (Dept_Manager deptManger : deptManagers) {
            Employee employee = connector.getEmployeeByID(deptManger.getEmp_no());
            JoinedEmployee joinedEmployee = createJoinedEmployee(deptManger, employee);
            joinedEmployees.add(joinedEmployee);
        }
        return joinedEmployees;
    }

    private JoinedEmployee createJoinedEmployee(Dept_Manager deptManger, Employee employee) {
        JoinedEmployee joinedEmployee = new JoinedEmployee();

        //attributes of deptManager
        joinedEmployee.setEmp_no(deptManger.getEmp_no());
        joinedEmployee.setDept_no(deptManger.getDept_no());
        joinedEmployee.setFrom_date(deptManger.getFrom_date());
        joinedEmployee.setTo_date(deptManger.getTo_date());

        //attributes of employee
        joinedEmployee.setBirthdate(employee.getBirthdate());
        joinedEmployee.setFirst_name(employee.getFirst_name());
        joinedEmployee.setLast_name(employee.getLast_name());
        joinedEmployee.setGender(employee.getGender());
        joinedEmployee.setHire_date(employee.getHire_date());

        return joinedEmployee;
    }


}
