import hashFunctions.HashFunction;
import hashFunctions.ModuloHashFunction;
import hashFunctions.SecondModuleHashFunction;

/**
 * Created by Andy on 31.10.16.
 */
public class FillBloomfilter {

    public static void main(String args[]) {

        int capacity = 16;
        ModuloHashFunction moduloHashFunction = new ModuloHashFunction();
        SecondModuleHashFunction secondModuleHashFunction = new SecondModuleHashFunction();
        HashFunction [] hashFunctions = new HashFunction[2];

        hashFunctions[0] = moduloHashFunction;
        hashFunctions[1] = secondModuleHashFunction;

        BloomFilter bloomFilter = new BloomFilter(capacity, hashFunctions);

        bloomFilter.add(10);
        bloomFilter.add(26);
        bloomFilter.add(42);
        bloomFilter.add(58);
        bloomFilter.add(16);
        bloomFilter.add(12);
        bloomFilter.displayHashTable();
    }
}
