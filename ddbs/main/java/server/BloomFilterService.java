package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.BitSet;

/**
 * Created by Andy on 09.11.16.
 */

/**
 * Abstract Interface for the BloomFilterServant, used for RMI procedure
 */
public interface BloomFilterService extends Remote {
    String createNewBloomFilter(int slotSize, int numberOfHashFunctions) throws RemoteException;
    String sendHashFunctions(int[] aRandom, int[] bRandom) throws RemoteException;
    String sendBitset(BitSet bitset) throws RemoteException;
    String sendDeptManagerClassic(String  gsonDeptManagers) throws RemoteException;

    String getEmployeesIDByName(String name) throws RemoteException;
    BitSet getBitSetEmployeesByName(String name) throws RemoteException;
    String getSalaryIdsHigherAs(int minSalary) throws RemoteException;
    BitSet getBitSetSalaryHigherAs(int minSalary) throws RemoteException;

    String getEmployeesMatching(BitSet bitSet) throws RemoteException;
    String getSalariesMatching(BitSet bitSet) throws RemoteException;

    String getEmployeesMatchingId(String gsonIds) throws RemoteException;
    String getSalariesMatchingId(String gsonIds) throws RemoteException;
}
