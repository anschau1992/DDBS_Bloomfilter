package shared.hashFunctions;

/**
 * Created by Andy on 31.10.16.
 */

/**
 * Simple hashing function for testing purpose. Not good for BloomFilter, because of imbalanced distribution
 */
public class SimpleModuloHashFunction implements HashFunction {

    public int hash(long value, int slotSize) {
        return (int) value % slotSize;
    }
}
