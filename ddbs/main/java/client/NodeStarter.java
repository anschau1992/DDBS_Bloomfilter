package client;

import server.BloomFilterService;
import shared.BloomFilter;
import shared.hashFunctions.UniversalHashFunction;
import shared.hashFunctions.UniversalHashNumberGenerator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Andy on 07.12.16.
 */
public class NodeStarter {
    private int slotCapacity;
    private int numberOfBloomfunctions;

    private UniversalHashNumberGenerator generator;

    public NodeStarter(int slotCapacity, int numberOfBloomfunctions) {
        this.slotCapacity = slotCapacity;
        this.numberOfBloomfunctions = numberOfBloomfunctions;

         generator= new UniversalHashNumberGenerator(numberOfBloomfunctions);
    }

    /**
     * Set up the local bloomfilter with the generated numbers for the Hashfunctions
     * @return the local bloomfilter
     */
    public BloomFilter setupLocalBloomfilter() {
        UniversalHashFunction[] hashfunctions = new UniversalHashFunction[numberOfBloomfunctions];
        for(int i = 0; i < numberOfBloomfunctions; i++) {
            hashfunctions[i] = new UniversalHashFunction(generator.getANumber(i), generator.getBNumber(i));
        }
        BloomFilter bloomfilter = new BloomFilter(slotCapacity, numberOfBloomfunctions);
        bloomfilter.setHashFunctions(hashfunctions);
        return bloomfilter;
    }

    /**
     * Set up a service to the given port with the same Hashfunction in the Bloomfilter as the local bloomfilter
     * @param port number of the port
     * @return the service
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException
     */
    public BloomFilterService setRemoteService(int port) throws RemoteException, NotBoundException, MalformedURLException {
        BloomFilterService service = (BloomFilterService) Naming.lookup("rmi://localhost:"+port+"/bloom");

        System.out.println("Node "+port+": " + service.createNewBloomFilter(slotCapacity, numberOfBloomfunctions));
        System.out.println("Node "+port+": " + service.sendHashFunctions(generator.getANumbers(), generator.getBNumbers()));

        return service;
    }
}
