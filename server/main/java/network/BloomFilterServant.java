package network;

import BloomFilter.BloomFilter;
import BloomFilter.hashFunctions.SecondModuleHashFunction;
import BloomFilter.hashFunctions.SimpleModuloHashFunction;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
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
     * Receives a new Bloomfilter from client side and copies it for using.
     * In order to communicate proberly. Server side and Client side need to interact with the same Bloomfilter
     * @param slotSize is the number of slots within the Bitset of the Bloomfilter
     * @param numberOfHashFunctions generate the numbers of hashfunctions used for the bloomfilter
     * @return confirmation message
     */
    public String createNewBloomFilter(int slotSize, int numberOfHashFunctions) {
        this.bloomFilter = new BloomFilter(slotSize, numberOfHashFunctions, new SimpleModuloHashFunction(), new SecondModuleHashFunction());
        return "New Bloomfilter is set";
    }

    public ArrayList<Employee> receiveBitset(BitSet bitset) {
        try {
            this.bloomFilter.readInBloomFilter(bitset);
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

            //ID's containing in Bloomfilter & DB (incl. possible false positives)
            idsInBLoomFilter = checkIDsinBloomfilter(employeeIds);
            ArrayList<Employee> returningEmployees =  getWholeEmployeeData(idsInBLoomFilter);

            return returningEmployees;

        } catch (Exception e) {
            System.err.println("Join has been failed: " + e);
            return null;
        }
    }

    /**
     * Checks all ID given as Parameter, if they are also in the Bloomfilter
     * IMPORTANT: The returning ID's can include false-positives
     * @param employeeIds
     * @return all the ID also found in the bloom filter
     */
    private ArrayList<String> checkIDsinBloomfilter(ArrayList<String> employeeIds) {
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
