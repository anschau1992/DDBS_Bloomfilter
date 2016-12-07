package server;

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
        try {
            int portNumber = Integer.parseInt(args[0]);
            Registry registry = LocateRegistry.createRegistry(portNumber);
            registry.rebind("bloom", new BloomFilterServant());
            System.out.println("Server Application is set up on port: " + portNumber);

        }catch (Exception e) {
            System.out.println("First argument has to be an integer for local port");
            System.exit(-1);
        }
    }
}
