package client;

import au.com.bytecode.opencsv.CSVWriter;
import shared.Constants;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Andy on 17.12.16.
 */
public class ResultWriter {

    public void writeJoinResult(JoinResult[][] results) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(Constants.csvOutput), ',');

            int mSize = results.length;
            int kSize = results[0].length;


            //header
            String[] header  = new String[mSize+1];
            header[0] = "K \\ M";
            for (int m = 1; m <= mSize; m++) {
                header[m] = Integer.toString(results[m-1][0].getBloomFilterSize());

            }
            writer.writeNext(header);

            //k and body(False-Positives)
            for (int k = 0; k < kSize; k++) {
                String[] body = new String[mSize+1];
                body[0] = Integer.toString(results[0][k].getHashes());
                for(int m = 0; m < (mSize); m++) {
                    body[m+1] = Integer.toString(results[m][k].getSetToTrue1());
                }
                writer.writeNext(body);
                //print numbers of ones in bf2
                body[0] = "";
                for(int m = 0; m < (mSize); m++) {
                    body[m+1] = Integer.toString(results[m][k].getSetToTrue2());
                }
                writer.writeNext(body);

                //print numbers of ones in cbf
                body[0] = "";
                for(int m = 0; m < (mSize); m++) {
                    body[m+1] = Integer.toString(results[m][k].getSetToTrue3());
                }
                writer.writeNext(body);

                //print number of false positives
                body[0] = "";
                for(int m = 0; m < (mSize); m++) {
                    body[m+1] = Integer.toString(results[m][k].getFalsePositives());
                }
                writer.writeNext(body);
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
