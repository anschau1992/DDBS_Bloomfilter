package client;

import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.ArrayList;

/**
 * Created by Andy on 13.11.16.
 */
public class SizeComparer {

    private  int bloomFilterSize;
    private  int hashNumbers;

    private ArrayList<CompareThing> semiJoinSizes;
    private ArrayList<CompareThing> bloomFilterJoinSizes;

    private int setToTrue1;
    private int setToTrue2;
    private int setToTrue3;


    private int numberOfJoins;
    private int falsePositives;

    private long semiJointTotalSize;
    private long bloomFilterJointTotalSize;

    public SizeComparer(int bloomFilterSize, int hashNumbers) {
        this.bloomFilterSize = bloomFilterSize;
        this.hashNumbers = hashNumbers;
    semiJoinSizes = new ArrayList<CompareThing>();
    bloomFilterJoinSizes = new ArrayList<CompareThing>();
    numberOfJoins = 0;
    falsePositives = 0;
    }

    public void increaseSemiJoinSize(Object object, String reason) {
        CompareThing comparer = new CompareThing(reason, RamUsageEstimator.sizeOf(object));
        semiJoinSizes.add(comparer);
    }

    public void increaseBloomFilterJoinSize(Object object, String reason) {
        CompareThing comparer = new CompareThing(reason, RamUsageEstimator.sizeOf(object));
        bloomFilterJoinSizes.add(comparer);
    }

    public void setNumberOfJoins(int numberOfJoins) {
        this.numberOfJoins = numberOfJoins;
    }

    public void calculateFalsePositives(int numberOfReturns) {
        this.falsePositives = numberOfReturns - numberOfJoins*2;
    }

    public void increaseFalsePositives(int amount) {
        falsePositives += amount;
    }


    public void printStats() {
        calculateTotalSizes();
        String semiJoins = "(";
        for (CompareThing comparer: semiJoinSizes) {
            semiJoins += comparer.getReason() + ": " + comparer.getSize() + ", ";
        }
        semiJoins += ")";

        String blommFilterjoins = "(";
        for (CompareThing comparer: bloomFilterJoinSizes) {
            blommFilterjoins += comparer.getReason() + ": " + comparer.getSize() + ", ";
        }
        blommFilterjoins += ")";

        System.out.println();
        System.out.println();
        System.out.println("=========================================================================================================");
        System.out.println("#Hashes {k}: " + hashNumbers + "\t Bloomfiltersize {m}: " + bloomFilterSize);
        System.out.println("Semi Join - Total size:  "+ semiJointTotalSize + " "+ semiJoins);
        System.out.println("BloomFilter Join - Total size:  "+ bloomFilterJointTotalSize + " "+ blommFilterjoins);
        System.out.println("Number of Joins: " + numberOfJoins + "\t Number of False Positives: " + falsePositives);
        System.out.println("=========================================================================================================");
        System.out.println("=========================================================================================================");
    }

    private void calculateTotalSizes() {
        semiJointTotalSize = 0;
        for (CompareThing comparer : semiJoinSizes) {
            semiJointTotalSize += comparer.getSize();
        }

        bloomFilterJointTotalSize = 0;
        for (CompareThing comparer : bloomFilterJoinSizes) {
            bloomFilterJointTotalSize += comparer.getSize();
        }

    }

    public int getFalsePositives() {
        return falsePositives;
    }

    public int getBloomFilterSize() {
        return bloomFilterSize;
    }

    public int getHashNumbers() {
        return hashNumbers;
    }

    public ArrayList<CompareThing> getSemiJoinSizes() {
        return semiJoinSizes;
    }

    public ArrayList<CompareThing> getBloomFilterJoinSizes() {
        return bloomFilterJoinSizes;
    }

    public int getNumberOfJoins() {
        return numberOfJoins;
    }

    public long getSemiJointTotalSize() {
        return semiJointTotalSize;
    }

    public long getBloomFilterJointTotalSize() {
        return bloomFilterJointTotalSize;
    }

    public int getSetToTrue1() {
        return setToTrue1;
    }

    public void setSetToTrue1(int setToTrue1) {
        this.setToTrue1 = setToTrue1;
    }

    public int getSetToTrue2() {
        return setToTrue2;
    }

    public void setSetToTrue2(int setToTrue2) {
        this.setToTrue2 = setToTrue2;
    }

    public int getSetToTrue3() {
        return setToTrue3;
    }

    public void setSetToTrue3(int setToTrue3) {
        this.setToTrue3 = setToTrue3;
    }
}
