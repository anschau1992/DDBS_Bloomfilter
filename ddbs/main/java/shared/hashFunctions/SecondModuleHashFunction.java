package shared.hashFunctions;


/**
 * Created by Andy on 31.10.16.
 */

/**
 * Simple hashing function for testing purpose. Not good for BloomFilter, because of imbalanced distribution
 */
public class SecondModuleHashFunction implements HashFunction {

    public int hash(long value, int slotSize) {
        //absolute number is used to prevent the case of negative number
        //return slotSize - (value % slotSize);
        return (int) (3 - (value%3));
    }
}
