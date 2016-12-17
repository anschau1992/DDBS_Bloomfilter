package client;

import java.util.ArrayList;

/**
 * Created by Andy on 17.12.16.
 */
public class JoinResult {
    private int joins;
    private int falsePositives;

    private ArrayList<CompareThing> semiJoinSizes;
    private ArrayList<CompareThing> bloomFilterJoinSizes;

    private int bloomFilterSize;
    private int hashes;

    public void setJoins(int joins) {
        this.joins = joins;
    }

    public void setFalsePositives(int falsePositives) {
        this.falsePositives = falsePositives;
    }

    public void setSemiJoinSizes(ArrayList<CompareThing> semiJoinSizes) {
        this.semiJoinSizes = semiJoinSizes;
    }

    public void setBloomFilterJoinSizes(ArrayList<CompareThing> bloomFilterJoinSizes) {
        this.bloomFilterJoinSizes = bloomFilterJoinSizes;
    }

    public void setBloomFilterSize(int bloomFilterSize) {
        this.bloomFilterSize = bloomFilterSize;
    }

    public void setHashes(int hashes) {
        this.hashes = hashes;
    }

    public int getJoins() {
        return joins;
    }

    public int getFalsePositives() {
        return falsePositives;
    }

    public ArrayList<CompareThing> getSemiJoinSizes() {
        return semiJoinSizes;
    }

    public ArrayList<CompareThing> getBloomFilterJoinSizes() {
        return bloomFilterJoinSizes;
    }

    public int getBloomFilterSize() {
        return bloomFilterSize;
    }

    public int getHashes() {
        return hashes;
    }
}
