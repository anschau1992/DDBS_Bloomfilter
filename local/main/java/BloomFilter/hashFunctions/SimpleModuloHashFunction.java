package BloomFilter.hashFunctions;

/**
 * Created by Andy on 31.10.16.
 */
public class SimpleModuloHashFunction implements HashFunction {

    public int hash(int value, int slotSize) {
        return value % slotSize;
    }
}
