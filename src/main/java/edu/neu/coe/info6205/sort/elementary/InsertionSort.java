/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.Config;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class InsertionSort<X extends Comparable<X>> extends SortWithHelper<X> {

    /**
     * Constructor for any sub-classes to use.
     *
     * @param description the description.
     * @param N           the number of elements expected.
     * @param config      the configuration.
     */
    protected InsertionSort(String description, int N, Config config) {
        super(description, N, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public InsertionSort(int N, Config config) {
        this(DESCRIPTION, N, config);
    }

    public InsertionSort(Config config) {
        this(new BaseHelper<>(DESCRIPTION, config));
    }

    /**
     * Constructor for InsertionSort
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public InsertionSort(Helper<X> helper) {
        super(helper);
    }

    public InsertionSort() {
        this(BaseHelper.getHelper(InsertionSort.class));
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();
        // if instrument = true; use helper methods else use own insertion sort swaps
        if (helper.instrumented()){
            for (int i = from; i < to-1; i++) {
                for (int j = i+1; j > from && helper.swapStableConditional(xs,j); j--);
            }
        }else {
            for (int i = from; i < to-1; i++) {
                for (int j = i+1; j > from && (xs[j].compareTo(xs[j-1]) < 0); j--) {
                    X x = xs[j-1];
                    xs[j-1] = xs[j];
                    xs[j] = x;
                }
            }
        }
    }

    public static final String DESCRIPTION = "Insertion sort";

    public static <T extends Comparable<T>> void sort(T[] ts) {
        new InsertionSort<T>().mutatingSort(ts);
    }


    public static void main(String[] args) {
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("SL.NO,")
                .append("Array Length(n),")
                .append("Random Array,")
                .append("Partially Ordered Array,")
                .append("Ordered Array,")
                .append("Reverse Ordered Array")
                .append("\n");
        Benchmark_Timer<Integer[]> benchmark_timer = new Benchmark_Timer<>(
                "Insertion Sort Benchmark",
                numsArray -> new InsertionSort<Integer>().sort(numsArray, 0, numsArray.length));
        int limit = 12800;
        for (int i = 1; i < 6; i++) {
            limit/=2;
            outputBuilder.append(i).append(",").append(limit).append(",");
            final int bound = limit*10;
            Integer[] numArray = IntStream.generate(() -> new Random().nextInt(bound))
                    .limit(limit)
                    .boxed()
                    .toArray(Integer[]::new);
            double randomSortTime = benchmark_timer.runFromSupplier(numArray::clone,10);
            outputBuilder.append(randomSortTime).append(",");
            Arrays.sort(numArray,0,numArray.length/2);
            double partiallyOrderedTime = benchmark_timer.runFromSupplier(()->numArray,10);
            outputBuilder.append(partiallyOrderedTime).append(",");
            Arrays.sort(numArray);
            double orderedSortTime = benchmark_timer.runFromSupplier(()->numArray,10);
            outputBuilder.append(orderedSortTime).append(",");
            double reversedSortTime = benchmark_timer.runFromSupplier(()->reverseArray(numArray),10);
            outputBuilder.append(reversedSortTime).append("\n");
        }
        try {
            PrintWriter writer = new PrintWriter("./src/main/resources/assignment-reports/Assignment-2(Benchmark)/insertion-sort-benchmark.csv");
            writer.write(outputBuilder.toString());
            writer.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }


    public static Integer[] reverseArray(Integer[] numArray){
        Integer[] newArray = new Integer[numArray.length];
        for (int i = 0; i < numArray.length; i++) {
            newArray[i] = numArray[numArray.length-i-1];
        }
        return newArray;
    }
}
