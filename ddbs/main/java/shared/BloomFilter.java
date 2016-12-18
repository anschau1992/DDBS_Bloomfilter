package shared; /**
 * Created by Andy on 31.10.16.
 */

import shared.hashFunctions.UniversalHashFunction;

import java.util.BitSet;

public class BloomFilter {
    private BitSet bitSet;
    final int slotCapacity;
    final int numberOfBloomFunctions;

    private UniversalHashFunction[] hashFunctions;

    /**
     * Initialize the Bloomfilter
     * @param slotCapacity - the length of the bloom filter
     * @param numberOfBloomFunctions - the number of function used to hash a value
     */
    public BloomFilter(int slotCapacity, int numberOfBloomFunctions) {
        bitSet = new BitSet(slotCapacity);
        this.slotCapacity = slotCapacity;
        this.numberOfBloomFunctions = numberOfBloomFunctions;

    }

    public void setHashFunctions(UniversalHashFunction[] hashFunctions) {
        this.hashFunctions = hashFunctions;
    }

    /**
     * Add a value into the bloom filter
     * @param value, the to adding number
     */
    public void add(int value){
        for(int i = 0; i < numberOfBloomFunctions; i++) {
            int hashLocation = hashFunctions[i].hash(value, slotCapacity);
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
            int hashLocation = hashFunctions[i].hash(value, slotCapacity);
            //if BitSetLocation is not true
            if(!bitSet.get(hashLocation)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets all slot back to false
     * --> bloom filter is empty again
     */
    public void emptyBloomFilter() {
        bitSet.clear();
    }

    /**
     * Reads in a BitSet with the same length
     * Used on Server side to check afterwards what the Bloomfilter is containing
     * @param toReadBitSet
     * @throws Exception
     */
    public void readInBloomFilter(BitSet toReadBitSet) throws Exception {
        if (this.slotCapacity != slotCapacity) {
            throw new Exception("BitSet must have slotCapacity of " + slotCapacity);
        }
        bitSet = toReadBitSet;
        System.out.println("ReadIn of BitSet was successful");
    }

    /**
     * Prints out every slot with the corresponding value
     */
    public void displayHashTable() {

        System.out.println("Hash Table: ");
        for (int i = 0; i < slotCapacity; i++) {
            System.out.println(i + ": " + bitSet.get(i));
        }
    }

    /**
     * Used for tes purpose to test correctnes of bloomfilter
     * @param slotPosition
     * @return
     */
    public boolean checkSlot(int slotPosition) {
        return bitSet.get(slotPosition);
    }


    public int getSlotCapacity() {
        return slotCapacity;
    }

    public BitSet getBitSet() {
        return bitSet;
    }
}
