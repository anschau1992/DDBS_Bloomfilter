package server;

import shared.Employee;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by Andy on 09.11.16.
 */

/**
 * Abstract Interface for the BloomFilterServant, used for RMI procedure
 */
public interface BloomFilterService extends Remote {
    String createNewBloomFilter(int slotSize, int numberOfHashFunctions) throws RemoteException;
    String sendBitset(BitSet bitset) throws RemoteException;
}
