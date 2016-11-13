package client; /**
 * Created by Andy on 09.11.16.
 */


import shared.BloomFilter;
import shared.hashFunctions.SecondModuleHashFunction;
import shared.hashFunctions.SimpleModuloHashFunction;
import server.BloomFilterService;
import shared.Employee;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class Client {

    private Client() {
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        BloomFilterService service;
        int slotCapacity = 0;
        int numberOfBloomfunctions = 0;
        //check arguments
        if(args.length == 2) {
            slotCapacity = checkSlotCapacity(args[0]);
            numberOfBloomfunctions = checkBloomfunctionsNumberCapacity(args[1]);
        } else {
            System.out.println("The lenght of the arguments must be 2: (1) slotCapacity, (2) number of Bloomfunctions");
            System.exit(0);
        }

        //Setup shared.BloomFilter with given arguments
        BloomFilter bloomFilter = new BloomFilter(slotCapacity, numberOfBloomfunctions,
                new SimpleModuloHashFunction(), new SecondModuleHashFunction());

        //Create connection to remote Server; send Bloomfilter
        //TODO: change to remote binding for ec2
        service = (BloomFilterService) Naming.lookup("rmi://localhost:3000/bloom");
        System.out.println("Remote Server: " + service.createNewBloomFilter(slotCapacity, numberOfBloomfunctions));

        //TODO fill in Bitset in Bloomfilter with inputs of client DB
        //Test with some test-inputs

        bloomFilter.add(("10004").hashCode());
        bloomFilter.add(("10005").hashCode());
        bloomFilter.add(("20012").hashCode());



        //TODO send bitset to remote server
        //test send
        ArrayList<Employee> employees = service.receiveBitset(bloomFilter.getBitSet());
        System.out.println(employees);



        //TODO receive join and handle it --> print out result?

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
