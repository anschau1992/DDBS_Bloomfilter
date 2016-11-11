import network.BloomFilterServant;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Andy on 10.11.16.
 */
public class ApplicationServer {
    /**
     * Start the application on server side and registers for RMI Connection as "bloom"
     * @param args
     * @throws RemoteException
     */
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(3000);
        registry.rebind("bloom", new BloomFilterServant());
        System.out.println("Server Application is set up");
    }
}
