package shared.hashFunctions;

import shared.Constants;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Andy on 05.12.16.
 */
public class UniversalHashFunction implements HashFunction {
    private int primeNumber;
    private int a;
    private int b;

    public UniversalHashFunction (int a, int b) {
        this.a = a;
        this.b = b;
        this.primeNumber = Constants.NEXT_PRIME;
    }

    public int hash(long value, int slotSize) {
        //hab(k) = ((ak+b) mod p) mod m
        return (int) (((a*value + b) % primeNumber) % slotSize);
    }
}
