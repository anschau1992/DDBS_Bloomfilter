package client;

/**
 * Created by Andy on 14.12.16.
 */

/**
 * Clas sis used in the SizeComparer for matching each increas with a text as reason
 */
public class CompareThing {
    private String reason;
    private long size;

    public CompareThing(String reason, long size) {
        this.reason = reason;
        this.size = size;
    }

    public String getReason() {
        return reason;
    }

    public long getSize() {
        return size;
    }
}
