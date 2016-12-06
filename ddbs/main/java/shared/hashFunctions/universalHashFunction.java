package shared.hashFunctions;

import shared.CONSTANTS;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Andy on 05.12.16.
 */
public class UniversalHashFunction implements HashFunction {
    private int primeNumber;
    private int a;
    private int b;

    /**
     * Initializes random number a & b dependent on the given primeNumber
     * primeNUmber has to be higher than the max input into the hash-function
     * @param primeNumber
     */
    public  UniversalHashFunction (int primeNumber) {
        this.primeNumber = primeNumber;
        //usally second param is max+1, although we look for prime-1 ==> primeNumber
        a = ThreadLocalRandom.current().nextInt(1, primeNumber);
        b = ThreadLocalRandom.current().nextInt(0, primeNumber);

        //TODO: remove
        System.out.println("a: "+ a +"\tb: "+ b);
    }

    public UniversalHashFunction (int a, int b) {
        this.a = a;
        this.b = b;
        this.primeNumber = CONSTANTS.NEXT_PRIME;
    }

    public int hash(long value, int slotSize) {
        //hab(k) = ((ak+b) mod p) mod m
        return (int) (((a*value + b) % primeNumber) % slotSize);
    }
}
