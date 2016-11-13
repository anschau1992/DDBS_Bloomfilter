package shared.hashFunctions;

/**
 * Created by Andy on 31.10.16.
 */
public  interface HashFunction {
    public int hash (int value, int slotSize);
}
