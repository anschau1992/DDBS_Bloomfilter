package client;

import java.util.ArrayList;

/**
 * Created by Andy on 13.11.16.
 */
public class SizeComparer {

    private ArrayList<Long> classicJoinSizes;
    private ArrayList<Long> bloomFilterJoinSizes;

    private int numberOfJoins;
    private int falsePositives;

    public SizeComparer() {
    classicJoinSizes = new ArrayList<Long>();
    bloomFilterJoinSizes = new ArrayList<Long>();
    numberOfJoins = 0;
    falsePositives = 0;
    }

    public void increaseClassicJoinSize(long number) {
        classicJoinSizes.add(number);
    }

    public void increaseBloomFilterJoinSize(long number) {
        bloomFilterJoinSizes.add(number);
    }

    public void setNumberOfJoins(int numberOfJoins) {
        this.numberOfJoins = numberOfJoins;
    }

    public void calculateFalsePositives(int numberOfReturns) {
        this.falsePositives = numberOfReturns - numberOfJoins;
    }

    public void printStats() {
        System.out.println("=========================================================================================================");
        System.out.println("Classic Join: (" +classicJoinSizes.get(0) + ") (" + classicJoinSizes.get(1) + ") = " + (classicJoinSizes.get(0) + classicJoinSizes.get(1)));
        System.out.println("BloomFilter Join: (" +bloomFilterJoinSizes.get(0) + ") (" + bloomFilterJoinSizes.get(1) + ") = " + (bloomFilterJoinSizes.get(0) + bloomFilterJoinSizes.get(1)));
        System.out.println("Number of Joins: " + numberOfJoins + "\t Number of False Positives: " + falsePositives);
        System.out.println("=========================================================================================================");
    }
}
