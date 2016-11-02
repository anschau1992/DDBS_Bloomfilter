/**
 * Created by Andy on 31.10.16.
 */

import hashFunctions.HashFunction;

import java.util.BitSet;
import java.util.Random;

public class BloomFilter {
    private BitSet bitSet;
    final int slotCapacity;
    final int numberOfBloomFunctions;

    final HashFunction baseFunction1;
    final HashFunction baseFunction2;

    public BloomFilter(int slotCapacity, int numberOfBloomFunctions, HashFunction baseFunction1, HashFunction baseFunction2) {
        bitSet = new BitSet(slotCapacity);
        this.baseFunction1 = baseFunction1;
        this.baseFunction2 = baseFunction2;
        this.slotCapacity = slotCapacity;
        this.numberOfBloomFunctions = numberOfBloomFunctions;
    }

    /**
     * Add a value into the bloom filter
     * @param value, the to adding number
     */
    public void add(int value){
        for(int i = 0; i < numberOfBloomFunctions; i++) {
            int hashLocation = bloomFunction(value, i);
            bitSet.set(hashLocation);
        }
    }

    /**
     * Checks if the given value is in the bloom filter
     * @param value
     * @return false if not in bloom filter;
     *         true if it's possibly in the bloom filter (false-positive is possible)
     */
    public boolean check(int value){
        for(int i = 0; i < numberOfBloomFunctions; i++) {
            int hashLocation = bloomFunction(value, i);
            //if BitSetLocation is not true
            if(!bitSet.get(hashLocation)) {
                return false;
            }
        }
        return true;
    }

    //hash i (x,m) = hash a (x) + i * hash b (x) % m

    /**
     * Based on the base function 1 & 2 and the iteration number,
     * calculating the position within the bloomfilter.
     * @param value
     * @param functionNumber
     * @return
     */
    private int bloomFunction(int value, int functionNumber) {
        return (baseFunction1.hash(value, slotCapacity) + functionNumber * baseFunction2.hash(value, slotCapacity)) % slotCapacity;
    }


    public void displayHashTable() {

        System.out.println("Hash Table: ");
        for (int i = 0; i < slotCapacity; i++) {
            System.out.println(i + ": " + bitSet.get(i));
        }
    }


}
