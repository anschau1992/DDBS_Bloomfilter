/**
 * Created by Andy on 02.11.16.
 */
import BloomFilter.BloomFilter;
import com.carrotsearch.sizeof.RamUsageEstimator;
import hashFunctions.HashFunction;
import hashFunctions.SecondModuleHashFunction;
import hashFunctions.SimpleModuloHashFunction;
import org.junit.Before;
import org.junit.Test;
import com.carrotsearch.sizeof.RamUsageEstimator.*;

import java.awt.image.SampleModel;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;


public class TestBloomFilter {
    final int slotCapacity = 20;
    final int numberOfBloomFunctions = 3;
    HashFunction baseFunction1;
    HashFunction baseFunction2;

    BloomFilter bloomFilter;
    @Before
    public void setup() {
        baseFunction1 = new SimpleModuloHashFunction();
        baseFunction2 = new SecondModuleHashFunction();

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

    /**
     * Tests if the function readInBloomFilter of Class BloomFilter is functioning properly
     */
    @Test
    public void testReadInBloomFilter() {
        //fill local bloom-filter
        bloomFilter.emptyBloomFilter();
        bloomFilter.add(5);
        bloomFilter.add(37);

        //copy local to remote bloomFilter; add number, which should be overwritten afterwards
        BloomFilter remoteBloomFilter = new BloomFilter(slotCapacity, numberOfBloomFunctions, baseFunction1, baseFunction2);
        remoteBloomFilter.add(14);
        assertTrue("Position 15 is true, before overwrite RemoteBloomFilter", remoteBloomFilter.checkSlot(15));
        assertTrue("Position 15 is true, before overwrite RemoteBloomFilter", remoteBloomFilter.checkSlot(16));
        assertTrue("Position 15 is true, before overwrite RemoteBloomFilter", remoteBloomFilter.checkSlot(17));

        try {
            remoteBloomFilter.readInBloomFilter(bloomFilter.getBitSet());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //test remote bloomFilter
        assertTrue("Position 1 in Bloomfilter is true", remoteBloomFilter.checkSlot(1));
        assertTrue("Position 3 in Bloomfilter is true", remoteBloomFilter.checkSlot(3));
        assertTrue("Position 6 in Bloomfilter is true", remoteBloomFilter.checkSlot(6));
        assertTrue("Position 7 in Bloomfilter is true", remoteBloomFilter.checkSlot(7));
        assertTrue("Position 8 in Bloomfilter is true", remoteBloomFilter.checkSlot(8));
        assertTrue("Position 19 in Bloomfilter is true", remoteBloomFilter.checkSlot(19));

        //test overwriting of old slots set to true
        assertFalse("Position 15 in Bloomfilter is false", remoteBloomFilter.checkSlot(15));
        assertFalse("Position 16 in Bloomfilter is false", remoteBloomFilter.checkSlot(16));
        assertFalse("Position 17 in Bloomfilter is false", remoteBloomFilter.checkSlot(17));

        BitSet testBitSet = new BitSet(127);

        long size = RamUsageEstimator.sizeOf(testBitSet);


        System.out.println("Size: " + size);
    }


}
