package client;
/**
 * Created by Andy on 09.11.16.
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.glass.ui.Size;
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

        int bloomfilterSize = Integer.parseInt(args[0]);
        int numberOfHashes = Integer.parseInt(args[1]);

        NodeStarter nodeStarter = new NodeStarter(bloomfilterSize, numberOfHashes);

        // [0] Setup localBloomfilter
        BloomFilter localBloom = nodeStarter.setupLocalBloomfilter();
        // [0] Setup service to end-nodes
        BloomFilterService service1 = nodeStarter.setRemoteService(Constants.PORT1);
        BloomFilterService service2 = nodeStarter.setRemoteService(Constants.PORT2);

        Gson gson = new GsonBuilder().create();
        SizeComparer sizeComparer = new SizeComparer(bloomfilterSize,numberOfHashes);



        // [1] Get projections from remote nodes
        BitSet bitSet1 = service1.getBitSetEmployeesByName(Constants.searchName);
        BitSet bitSet2 = service2.getBitSetSalaryHigherAs(Constants.minSalary);

        // [1.1] Get Projections Semi-Join
        String gsonEmpNo1 = service1.getEmployeesIDByName(Constants.searchName);
        String gsonEmpNo2 = service2.getSalaryIdsHigherAs(Constants.minSalary);
        ArrayList<Integer> empNo1 = gson.fromJson(gsonEmpNo1,
                new TypeToken<ArrayList<Integer>>() {}.getType());
        ArrayList<Integer> empNo2 = gson.fromJson(gsonEmpNo2,
                new TypeToken<ArrayList<Integer>>() {}.getType());

        System.out.println("Employee # " + empNo1.size());
        System.out.println("Salaries-Empl# " + empNo2.size());

        // [Size]
        sizeComparer.increaseBloomFilterJoinSize(bitSet1, "[1] Bitset 1");
        sizeComparer.increaseBloomFilterJoinSize(bitSet2, "[1] Bitset 2");
        //System.out.println("Bitset1 : size= "+ bitSet1.size() +"\t" + bitSet1.toString());
        //System.out.println("Bitset2 : size= "+ bitSet2.size() +"\t" + bitSet2.toString());

        sizeComparer.increaseSemiJoinSize(gsonEmpNo1, "[1] Emp No's 1");
        sizeComparer.increaseSemiJoinSize(gsonEmpNo2, "[1] Emp No's 2");



        // [2.1] Intersection with a logical AND-Operator
        System.out.println("BitSet 1: size= "+ bitSet1.size() +" True's: " + bitSet1.cardinality() +"\t" + bitSet1.toString());
        System.out.println("BitSet 2: size= "+ bitSet2.size() +" True's: " + bitSet2.cardinality() +"\t" + bitSet2.toString());
        bitSet1.and(bitSet2);
        System.out.println("Intersected : size= "+ bitSet1.size() +" True's: " + bitSet1.cardinality() +"\t" + bitSet1.toString());
        // [2.2] Intersection Semi-Join
        ArrayList<Integer> intersetion = integerIntersection(empNo1, empNo2);


        // [3.1] Send Intersection to nodes
        String gsonEmplBloom = service1.getEmployeesMatching(bitSet1);
        String gsonSalBloom = service2.getSalariesMatching(bitSet1);

        // [3.2] Send Intersection to nodes - Semi-Join
        String gsonEmplSemi = service1.getEmployeesMatchingId(gson.toJson(intersetion));
        String gsonSalSemi = service2.getSalariesMatchingId(gson.toJson(intersetion));



        // [Size]
        sizeComparer.increaseBloomFilterJoinSize(bitSet1, "[3] Intersected Bitset 1");
        sizeComparer.increaseBloomFilterJoinSize(bitSet1, "[3] Intersected Bitset 2");

        sizeComparer.increaseSemiJoinSize(intersetion, "[3] Intersected Integer-List 1");
        sizeComparer.increaseSemiJoinSize(intersetion, "[3] Intersected Integer-List 2");


        // [4] read in matches of nodes
        ArrayList<Employee> matchingEmpl = gson.fromJson(gsonEmplBloom,
                new TypeToken<ArrayList<Employee>>() {}.getType());
        ArrayList<Salary> matchingSal = gson.fromJson(gsonSalBloom,
                new TypeToken<ArrayList<Salary>>() {}.getType());

        // [Size]
        sizeComparer.increaseBloomFilterJoinSize(gsonEmplBloom, "[4] Matching Empl");
        sizeComparer.increaseBloomFilterJoinSize(gsonSalBloom, "[4] Matching Sal");

        sizeComparer.increaseSemiJoinSize(gsonEmplSemi, "[4] Matching Empl");
        sizeComparer.increaseSemiJoinSize(gsonSalSemi, "[4] Matching Sal");

        // [5] final join and check for False Positives
        ArrayList<EmployeeAndSalaries> combinedEmployees= new ArrayList<EmployeeAndSalaries>();
        for (Employee emp: matchingEmpl) {
            for (Salary sal : matchingSal) {
                if(emp.getEmp_no().equals(sal.getEmp_no())) {
                    EmployeeAndSalaries match = new EmployeeAndSalaries(emp, sal);
                    //match.printOut();
                    combinedEmployees.add(match);
                }
            }
        }
        System.out.println("False positiv only employee # " + sizeComparer.getFalsePositives());

        //falsePositives
        sizeComparer.increaseFalsePositives(matchingSal.size() - combinedEmployees.size());
        sizeComparer.increaseFalsePositives(matchingEmpl.size() - combinedEmployees.size());


        // [size]
        sizeComparer.setNumberOfJoins(combinedEmployees.size());
        //sizeComparer.calculateFalsePositives(matchingEmpl.size() + matchingSal.size());
        sizeComparer.printStats();
    }

    /**
     * Logical AND-Intersection between two Integer-Lists
     * @param empNo1
     * @param empNo2
     * @return
     */
    private static ArrayList<Integer> integerIntersection(ArrayList<Integer> empNo1, ArrayList<Integer> empNo2) {
        ArrayList<Integer> intersection = new ArrayList<Integer>();
        for (int numb1: empNo1) {
            for (int numb2:empNo2) {
                if(numb1 == numb2) {
                    intersection.add(numb1);
                    break;
                }
            }
        }
        return intersection;
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

