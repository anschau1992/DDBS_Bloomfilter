/**
 * Created by Andy on 02.11.16.
 */
import hashFunctions.HashFunction;
import hashFunctions.SecondModuleHashFunction;
import hashFunctions.SimpleModuloHashFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class TestBloomFilter {
    final int slotCapacity = 20;
    final int numberOfBloomFunctions = 3;

    BloomFilter bloomFilter;
    @Before
    public void setup() {
        HashFunction baseFunction1 = new SimpleModuloHashFunction();
        HashFunction baseFunction2 = new SecondModuleHashFunction();

        bloomFilter = new BloomFilter(slotCapacity, numberOfBloomFunctions, baseFunction1, baseFunction2);
    }

    @Test
    public void testInsert5() {
        final int testNumber = 5;
        boolean isInBloomfilter = bloomFilter.check(5);
        assertFalse("Number 5 is not in Bloomfilter at the beginning", isInBloomfilter);

        bloomFilter.add(5);
        isInBloomfilter = bloomFilter.check(5);
        assertTrue("Number 5 is in BloomFilter after it was added", isInBloomfilter);
    }
}
