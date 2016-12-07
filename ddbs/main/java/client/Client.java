package client; /**
 * Created by Andy on 09.11.16.
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.DBConnector;
import shared.*;
import shared.db_projection.Dept_Manager;
import server.BloomFilterService;
import shared.db_projection.Employee;
import shared.db_projection.EmployeeAndSalaries;
import shared.db_projection.Salary;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.InputMismatchException;

public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        //check argument and exit if not correct
        checkArguments(args);

        NodeStarter nodeStarter = new NodeStarter(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        // [0] Setup localBloomfilter
        BloomFilter localBloom = nodeStarter.setupLocalBloomfilter();
        // [0] Setup service to end-nodes
        BloomFilterService service1 = nodeStarter.setRemoteService(Constants.PORT1);
        BloomFilterService service2 = nodeStarter.setRemoteService(Constants.PORT2);

        Gson gson = new GsonBuilder().create();



        // [1] Get projections from remote nodes
        BitSet bitSet1 = service1.getBitSetEmployeesByName("Tokuyasu");
        BitSet bitSet2 = service2.getBitSetSalaryHigherAs(155000);
        System.out.println("Bitset1 : size= "+ bitSet1.size() +"\t" + bitSet1.toString());
        System.out.println("Bitset2 : size= "+ bitSet2.size() +"\t" + bitSet2.toString());

        // [2] Intersection with a logical AND-Operator
        bitSet1.and(bitSet2);
        System.out.println("Intersected : size= "+ bitSet1.size() +"\t" + bitSet1.toString());

        // [3] Send Intersection to nodes

        String gsonMatchingEmpl = service1.getEmployeesMatching(bitSet1);
        String gsonMatchingSal = service2.getSalariesMatching(bitSet1);

        // [4] read in matches of nodes
        ArrayList<Employee> matchingEmpl = gson.fromJson(gsonMatchingEmpl,
                new TypeToken<ArrayList<Employee>>() {}.getType());
        ArrayList<Salary> matchingSal = gson.fromJson(gsonMatchingSal,
                new TypeToken<ArrayList<Salary>>() {}.getType());

        // [5] final join and check for False Positives
        ArrayList<EmployeeAndSalaries> combinedEmployees= new ArrayList<EmployeeAndSalaries>();
        for (Employee emp: matchingEmpl) {
            for (Salary sal : matchingSal) {
                if(emp.getEmp_no().equals(sal.getEmp_no())) {
                    EmployeeAndSalaries match = new EmployeeAndSalaries(emp, sal);
                    match.printOut();
                    combinedEmployees.add(match);
                }
            }
        }




        //TODO old code: remove at the end

        //        //Testing: print out 'Mary's from node1
//        String gsonEmployees = service1.getEmployeesByName("Mary");
//        ArrayList<Employee> employees = gson.fromJson(gsonEmployees,
//                new TypeToken<ArrayList<Employee>>() {}.getType());
//        for (Employee emp: employees) {
//            emp.printOut();
//        }
//
//        //Testing: get all Salaries from node2
//        String gsonSalaries = service2.getSalaryHigherAs(150000);
//        ArrayList<Salary> salaries = gson.fromJson(gsonSalaries,
//                new TypeToken<ArrayList<Salary>>() {}.getType());
//        for (Salary sal: salaries) {
//            sal.printOut();
//        }




        //TODO old code: remove at the end

        //bloomFilter.add(345);
        //bloomFilter.displayHashTable();

//        SizeComparer sizeComparer = new SizeComparer();
//        //Send dept managers classicly for join
//        Gson gson = new GsonBuilder().create();
//        String gsonDeptManagers = gson.toJson(deptManagers);
//        sizeComparer.increaseClassicJoinSize(RamUsageEstimator.sizeOf(gsonDeptManagers));
//
//        String gsonJoinedEmployees = service.sendDeptManagerClassic(gsonDeptManagers);
//        sizeComparer.increaseClassicJoinSize(RamUsageEstimator.sizeOf(gsonJoinedEmployees));
//
//        ArrayList<JoinedEmployee> joinedEmployees = gson.fromJson(gsonJoinedEmployees, new TypeToken<ArrayList<JoinedEmployee>>() {
//        }.getType());
//        sizeComparer.setNumberOfJoins(joinedEmployees.size());
//
//
//        //send by BloomFilter
//        for (Dept_Manager deptManager : deptManagers) {
//            bloomFilter.add(Integer.parseInt(deptManager.getEmp_no()));
//        }
//
//
//        //send Bloomfilter, print out result
//        sizeComparer.increaseBloomFilterJoinSize(RamUsageEstimator.sizeOf(bloomFilter.getBitSet()));
//        String gsonEmployees = service.sendBitset(bloomFilter.getBitSet());
//        sizeComparer.increaseBloomFilterJoinSize(RamUsageEstimator.sizeOf(gsonEmployees));
//        ArrayList<Employee> employees = gson.fromJson(gsonEmployees, new TypeToken<ArrayList<Employee>>() {
//        }.getType());
//
//        sizeComparer.calculateFalsePositives(employees.size());
//        sizeComparer.printStats();

    }

    /**
     * Gets an ArrayList of all Dept_Managers stored in DB
     *
     * @return ArrayList, null if connection to DB failed;
     */
    private static ArrayList<Dept_Manager> getDeptManagerFromLocalDB() {
        DBConnector connector = DBConnector.getUniqueInstance();
        ArrayList<Dept_Manager> dept_maanagers = null;
        try {
            dept_maanagers = connector.getAllDeptManager();
        } catch (SQLException e) {
            System.out.println("Get Dept_manager from DB failed: " + e);
        }
        return dept_maanagers;
    }


    private static void checkArguments(String[] args) {
        //check arguments
        if (args.length == 2) {
            checkSlotCapacity(args[0]);
            checkBloomfunctionsNumberCapacity(args[1]);
        } else {
            System.out.println("The lenght of the arguments must be 2: (1) slotCapacity, (2) number of Bloomfunctions");
            System.exit(0);
        }
    }

    private static void checkSlotCapacity(String slotCapacityString) {
        int slotCapacity;
        try {
            slotCapacity = Integer.parseInt(slotCapacityString);
            if (slotCapacity <= 0) {
                System.out.println("First Argument (Slotcapacity) must be >0");
                System.exit(0);
            }
        } catch (InputMismatchException e) {
            System.out.println("You did not enter a integer as first argument");
            System.exit(0);
        }
    }

    private static void checkBloomfunctionsNumberCapacity(String numberOfBloomfunctionsString) {
        int numberOfBloomfunctions;
        try {
            numberOfBloomfunctions = Integer.parseInt(numberOfBloomfunctionsString);
            if (numberOfBloomfunctions <= 0) {
                System.out.println("Second Argument (NumberOfBloomfunctions) must be >0");
                System.exit(0);
            }
        } catch (InputMismatchException e) {
            System.out.println("You did not enter a integer as second argument");
            System.exit(0);
        }
    }
}

