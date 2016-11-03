/**
 * Created by Andy on 02.11.16.
 */
import BloomFilter.BloomFilter;
import hashFunctions.HashFunction;
import hashFunctions.SecondModuleHashFunction;
import hashFunctions.SimpleModuloHashFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;


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

    /**
     * Tests the adding of a number in the bloomfilter
     */
    @Test
    public void testInsert5() {
        final int testNumber = 5;
        bloomFilter.emptyBloomFilter();
        boolean isInBloomfilter = bloomFilter.check(5);
        assertFalse("Number 5 is not in Bloomfilter at the beginning", isInBloomfilter);

        bloomFilter.add(5);
        isInBloomfilter = bloomFilter.check(5);
        assertTrue("Number 5 is in BloomFilter.BloomFilter after it was added", isInBloomfilter);
    }

    /**
     * Tests if the bloomfilter sets the expected position in hashTable to true
     */
    @Test
    public void testCorrectPositionsInBloom() {
        bloomFilter.emptyBloomFilter();
        int expectedPosition1 = 6; // x % 20 + i * 3 - (x % 3), with x=5, i = 1
        int expectedPosition2 = 7; // x % 20 + i * 3 - (x % 3), with x=5, i = 2
        int expectedPosition3 = 8; // x % 20 + i * 3 - (x % 3), with x=5, i = 3

        List<Integer> expectedPos = Arrays.asList(expectedPosition1, expectedPosition2, expectedPosition3);

        bloomFilter.add(5);

        for(int i = 0 ; i < slotCapacity; i++) {
            if(expectedPos.contains(i)) {
                assertTrue("Position "+ i + " in Bloomfilter is true", bloomFilter.checkSlot(i));
            }else {
                assertFalse("Position "+ i + " in Bloomfilter is false", bloomFilter.checkSlot(i));
            }
        }
    }


}
