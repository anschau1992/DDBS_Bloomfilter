package client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;

/**
 * Created by Andy on 17.12.16.
 */
public class TestRun {

    /**
     * arg0 : startM
     * arg1 : endM
     * arg2 : raiseM
     * arg3 : startK
     * arg4 : endK
     * arg5 : raiseK
     * @param args
     */
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        int startM = Integer.parseInt(args[0]);
        int endM = Integer.parseInt(args[1]);
        int raiseM = Integer.parseInt(args[2]);
        int startK = Integer.parseInt(args[3]);
        int endK = Integer.parseInt(args[4]);
        int raiseK = Integer.parseInt(args[5]);

        int mIteration = calculateIteration(startM, endM, raiseM) + 1;
        int kIteration = calculateIteration(startK, endK, raiseK);
        JoinResult [][] results = new JoinResult[mIteration][kIteration];

        Client client = new Client();
        for(int m = 0; m < mIteration ; m++) {
            int kPosition = startK;
            for(int k = 0; k < kIteration; k++) {
                results[m][k] = client.join(startM, kPosition);
                System.out.println("JoinResult calculated with k= " + kPosition + ", m = " + startM);
                kPosition += raiseK;
            }
            startM += raiseM;
        }
        ResultWriter writer = new ResultWriter();
        writer.writeJoinResult(results);
        System.out.println("Writing done");
    }

    private static int calculateIteration(int startNumb, int endumb, int raiseAmount) {
        return (int) Math.floor((endumb - startNumb)/raiseAmount);
    }


}
