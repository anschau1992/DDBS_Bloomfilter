package shared.hashFunctions;

/**
 * Created by Andy on 31.10.16.
 */

/**
 * Interface for different hash functions
 */
public  interface HashFunction {
    public int hash (int value, int slotSize);
}
