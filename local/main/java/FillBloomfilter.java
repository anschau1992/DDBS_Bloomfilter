import hashFunctions.HashFunction;
import hashFunctions.ModuloHashFunction;
import hashFunctions.SecondModuleHashFunction;

/**
 * Created by Andy on 31.10.16.
 */
public class FillBloomfilter {

    public static void main(String args[]) {

        int capacity = 4;
        ModuloHashFunction moduloHashFunction = new ModuloHashFunction();
        SecondModuleHashFunction secondModuleHashFunction = new SecondModuleHashFunction();
        HashFunction [] hashFunctions = new HashFunction[2];

        hashFunctions[0] = moduloHashFunction;
        hashFunctions[1] = secondModuleHashFunction;

        BloomFilter bloomFilter = new BloomFilter(capacity, hashFunctions);

        bloomFilter.add2(2);
        bloomFilter.add2(6);
        bloomFilter.add2(18);

        bloomFilter.displayHashTable();

        BloomFilter bloom2 = new BloomFilter(capacity, hashFunctions);

        bloom2.add(2);
        bloom2.add(6);
        bloom2.add(18);
        bloom2.displayHashTable();
    }
}
