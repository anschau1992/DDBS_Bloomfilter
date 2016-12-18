package shared.hashFunctions;

/**
 * Created by Andy on 07.12.16.
 */

import shared.Constants;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates the random numbers for the UniversalHashFunction
 */
public class UniversalHashNumberGenerator {
    private int[] aRandom;
    private int[] bRandom;

    public UniversalHashNumberGenerator(int numberOfBloomfunctions) {
        aRandom = new int[numberOfBloomfunctions];
        bRandom = new int[numberOfBloomfunctions];

        generateNumbers(numberOfBloomfunctions);
    }

    private void generateNumbers(int numberOfBloomfunctions) {
        for (int i=0; i< numberOfBloomfunctions; i++) {
            aRandom[i] = ThreadLocalRandom.current().nextInt(1, Constants.NEXT_PRIME);
            bRandom[i] = ThreadLocalRandom.current().nextInt(0, Constants.NEXT_PRIME);
        }
    }

    public int[] getANumbers() {
        return aRandom;
    }
    public int[] getBNumbers() {
        return bRandom;
    }
    public int getANumber(int position) {
        return aRandom[position];
    }
    public int getBNumber(int position) {
        return aRandom[position];
    }
}
