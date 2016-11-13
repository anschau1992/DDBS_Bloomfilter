package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.BloomFilter;
import shared.hashFunctions.SecondModuleHashFunction;
import shared.hashFunctions.SimpleModuloHashFunction;
import shared.Employee;

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
    DBConnector connector;


    public BloomFilterServant() throws RemoteException{
        super();
    }

    /**
     * Receives a new BloomFilter from client side and copies it for using.
     * In order to communicate properly. Server side and client.Client side need to interact with the exact same BloomFilter
     * @param slotSize is the number of slots within the Bitset of the Bloomfilter
     * @param numberOfHashFunctions generate the numbers of hashFunctions used for the bloomFilter
     * @return confirmation message
     */
    public String createNewBloomFilter(int slotSize, int numberOfHashFunctions) {
        this.bloomFilter = new BloomFilter(slotSize, numberOfHashFunctions, new SimpleModuloHashFunction(), new SecondModuleHashFunction());
        return "New Bloomfilter is set";
    }

    /**
     * Recevies a loaded bitSet from client side.
     * Iterates through the database and check for matches of Employee's ID with the BloomFilter of the BitSet
     * Returns all matching Employees (including possibly false positives)
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

        //Connect to the database and get the corresponding row with the bloom-filter
        connector = DBConnector.getUniqueInstance();
        ArrayList <String> idsInBLoomFilter;
        try {
            //all ID's of DB
            ArrayList<String> employeeIds = connector.getAllEmployeeIds();

            //ID's containing in BloomFilter & DB (incl. possible false positives)
            idsInBLoomFilter = checkIDsInBloomfilter(employeeIds);
            ArrayList<Employee> returningEmployees =  getWholeEmployeeData(idsInBLoomFilter);

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
     * @param employeeIds
     * @return all the ID matching with the BloomFilter's BitSet
     */
    private ArrayList<String> checkIDsInBloomfilter(ArrayList<String> employeeIds) {
        ArrayList<String> idsInBloomFilter = new ArrayList<String>();
        for (String employeeID: employeeIds) {
            if(bloomFilter.check(employeeID.hashCode())) {
                idsInBloomFilter.add(employeeID);
            }
        }
        return idsInBloomFilter;
    }

    private ArrayList<Employee> getWholeEmployeeData(ArrayList<String> idsInBLoomFilter) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        for (String id: idsInBLoomFilter) {
            employees.add(connector.getEmployeeByID(id));
        }
        return employees;
    }

}
