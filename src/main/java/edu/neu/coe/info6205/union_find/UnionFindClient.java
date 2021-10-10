package edu.neu.coe.info6205.union_find;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author SaiAkhil
 */
public class UnionFindClient {


    /**
     * count and return average number of pairs generated for connected all the edges
     *
     * @param n -= no of sites or nodes
     * @return -= Total no of pairs generated for joining the vertices
     */
    public static int count(int n) {
        int totalCount = 0;
        for (int i = 0; i < 10; i++) {
            UF_HWQUPC uf = new UF_HWQUPC(n);
            int noOfPairs = 0;
            while (uf.components() != 1) {
                ++noOfPairs;
                int n1 = ThreadLocalRandom.current().nextInt(n);
                int n2 = ThreadLocalRandom.current().nextInt(n);
                if (!uf.isConnected(n1, n2)) uf.union(n1, n2);
            }
            totalCount += noOfPairs;
        }
        return totalCount / 10;
    }

    public static void main(String[] args) {

        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("SL.NO,")
                .append("No of Sites(n),")
                .append("No of Pairs(m)")
                .append("\n");

        int n = 17;

        for (int i = 1; i <= 30; i++) {
            int currentSites = n * i;
            int count = count(currentSites);
            System.out.printf("%1$d pairs generated for connecting %2$d sites\n", count, currentSites);
            outputBuilder.append(i).append(",")
                    .append(currentSites).append(",")
                    .append(count).append("\n");
        }

        try {
            String dir = "./AssignmentReports/Assignment-3(WQUPC)";
            File directory = new File(dir);
            if (!directory.exists()) directory.mkdirs();
            PrintWriter writer = new PrintWriter(String.format("%s/data.csv", dir));
            writer.write(outputBuilder.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
