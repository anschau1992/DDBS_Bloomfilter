package server;

import shared.Employee;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by Andy on 09.11.16.
 */
public interface BloomFilterService extends Remote {
    String createNewBloomFilter(int slotSize, int numberOfHashfunctions) throws RemoteException;
    ArrayList<Employee> receiveBitset(BitSet bitset) throws RemoteException;
    //TODO: add sentJoinedData back somehow

}
