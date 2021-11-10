package edu.neu.coe.info6205.sort.par;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        int size = 500000;
        processArgs(args);

        while (size <= 2000000) {
            int thread = 2;
            while (thread <= 64) {
                ForkJoinPool forkJoinPool = new ForkJoinPool(thread);
                System.out.println("parallelism degree =  " + forkJoinPool.getParallelism() + " and  the size is: " + size);
                Random random = new Random();
                int[] numArray = new int[size];
                ArrayList<Long> timeList = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    ParSort.cutoff = 5000 * (j + 1);
                    long time;
                    long startTime = System.currentTimeMillis();
                    for (int t = 0; t < 10; t++) {
                        for (int i = 0; i < numArray.length; i++) numArray[i] = random.nextInt(10000000);
                        ParSort.sort(numArray, 0, numArray.length, forkJoinPool);
                    }
                    long endTime = System.currentTimeMillis();
                    time = (endTime - startTime);
                    timeList.add(time);


                    System.out.println("cutoff：" + (ParSort.cutoff) + "------------------ 10times Time:" + time + "ms");

                }

                int j = 0;
                StringBuilder outputBuilder = new StringBuilder();
                for (long i : timeList) {
                    outputBuilder.append((double) 5000 * (j++ + 1) + "," + (double) i + "\n");
                }

                try {
                    PrintWriter writer = new PrintWriter("./AssignmentReports/Assignment-5/data.csv");
                    writer.write(outputBuilder.toString());
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                thread *= 2;
            }

            size *= 2;
        }

    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}