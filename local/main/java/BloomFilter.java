/**
 * Created by Andy on 31.10.16.
 */

import hashFunctions.HashFunction;

import java.util.BitSet;
import java.util.Random;

public class BloomFilter {
    private BitSet bitSet;
    final int capacity;
    final HashFunction[] hashFunctions;

    public BloomFilter(int capacity, HashFunction[] hashFunctions) {
        bitSet = new BitSet(capacity);
        this.hashFunctions = hashFunctions;
        this.capacity = capacity;
    }

    public void add(int value) {

       int  hashLocation = hashFunctions[0].hash(value, capacity);

        while (bitSet.get(hashLocation)) {
            hashLocation += (hashFunctions[1].hash(value, capacity));
            hashLocation = hashLocation % capacity;
        }
        bitSet.set(hashLocation);
    }

    public void add2(int value) {
        int hashLocation = 0;
        int functionsNumb = hashFunctions.length;

        for(int i = 0; i < functionsNumb; i++) {
            hashLocation = (hashLocation + hashFunctions[i].hash(value, capacity)) % capacity;

            //if location in bitSet is free
            if(!bitSet.get(hashLocation)) {
                break;
            }
        }

        // step over with last hashfunction, until empty slot is found
        // if empty slot is already found over, while condition will just be skipped
        while (bitSet.get(hashLocation)) {
            hashLocation = (hashLocation + hashFunctions[functionsNumb-1].hash(value, capacity)) % capacity;
        }

        bitSet.set(hashLocation);
    }

    public void displayHashTable() {

        System.out.println("Hash Table: ");
        for (int i = 0; i < capacity; i++) {
            System.out.println(i + ": " + bitSet.get(i));
        }
    }

    private int getPositionIbBitSet(int value) {
        if(value <= 0) {
            return capacity -1;
        } else {
            return value--;
        }
    }

}
