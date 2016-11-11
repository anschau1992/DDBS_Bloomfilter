package BloomFilter.hashFunctions;


/**
 * Created by Andy on 31.10.16.
 *
 * This function is good in use of a double-hash as the second function
 * --> there is a condition for the second hash, that it does not returns 0 ==> I do not understand why exactly
 */
public class SecondModuleHashFunction implements HashFunction {

    public int hash(int value, int slotSize) {
        //absolute number is used to prevent the case of negative number
        //return slotSize - (value % slotSize);
        return 3 - (value%3);
    }
}
