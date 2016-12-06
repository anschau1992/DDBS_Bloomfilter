package client; /**
 * Created by Andy on 09.11.16.
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.DBConnector;
import shared.*;
import shared.hashFunctions.SecondModuleHashFunction;
import shared.hashFunctions.SimpleModuloHashFunction;
import server.BloomFilterService;
import com.carrotsearch.sizeof.*;
import shared.hashFunctions.UniversalHashFunction;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.concurrent.ThreadLocalRandom;

public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {

        int slotCapacity = 0;
        int numberOfBloomfunctions = 0;

        int [] aRandom;
        int [] bRandom;
        UniversalHashFunction[] hashFunctions;

        //check arguments
        if (args.length == 2) {
            slotCapacity = checkSlotCapacity(args[0]);
            numberOfBloomfunctions = checkBloomfunctionsNumberCapacity(args[1]);
        } else {
            System.out.println("The lenght of the arguments must be 2: (1) slotCapacity, (2) number of Bloomfunctions");
            System.exit(0);
        }


        hashFunctions = new UniversalHashFunction[numberOfBloomfunctions];
        aRandom = new int[numberOfBloomfunctions];
        bRandom = new int[numberOfBloomfunctions];

        for (int i=0; i< numberOfBloomfunctions; i++) {
            aRandom[i] = ThreadLocalRandom.current().nextInt(1, CONSTANTS.NEXT_PRIME);
            bRandom[i] = ThreadLocalRandom.current().nextInt(0, CONSTANTS.NEXT_PRIME);
            hashFunctions[i] = new UniversalHashFunction(aRandom[i], bRandom[i]);
        }

        BloomFilterService service;
        SizeComparer sizeComparer = new SizeComparer();



        //Create connection to remote Server
        service = (BloomFilterService) Naming.lookup("rmi://localhost:3000/bloom");

        //Get Dept_Manager of Local DB
        ArrayList<Dept_Manager> deptManagers = getDeptManagerFromLocalDB();

        //Setup BloomFilter with given arguments on Client & Server
        System.out.println("Remote Server: " + service.createNewBloomFilter(slotCapacity, numberOfBloomfunctions));
        BloomFilter bloomFilter = new BloomFilter(slotCapacity, numberOfBloomfunctions);

        //generate UniversalHashFunction with random a & b and set it locally and remotely

        bloomFilter.setHashFunctions(hashFunctions);
        System.out.println("Remote Server: " + service.sendHashFunctions(aRandom, bRandom));

        bloomFilter.add(345);
        bloomFilter.displayHashTable();

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

    private static int checkSlotCapacity(String slotCapacityString) {
        int slotCapacity;
        try {
            slotCapacity = Integer.parseInt(slotCapacityString);
            if (slotCapacity <= 0) {
                System.out.println("First Argument (Slotcapacity) must be >0");
                System.exit(0);
            }
            return slotCapacity;

        } catch (InputMismatchException e) {
            System.out.println("You did not enter a integer as first argument");
            System.exit(0);
        }
        return 0;
    }

    private static int checkBloomfunctionsNumberCapacity(String numberOfBloomfunctionsString) {
        int numberOfBloomfunctions;
        try {
            numberOfBloomfunctions = Integer.parseInt(numberOfBloomfunctionsString);
            if (numberOfBloomfunctions <= 0) {
                System.out.println("Second Argument (NumberOfBloomfunctions) must be >0");
                System.exit(0);
            }
            return numberOfBloomfunctions;

        } catch (InputMismatchException e) {
            System.out.println("You did not enter a integer as second argument");
            System.exit(0);
        }

        return 0;
    }
}

