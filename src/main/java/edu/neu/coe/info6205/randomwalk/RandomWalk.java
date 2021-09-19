/*
 * Copyright (c) 2017. Phasmid Software
 */

package edu.neu.coe.info6205.randomwalk;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class RandomWalk {

    private int x = 0;
    private int y = 0;

    private final Random random = new Random();

    /**
     * Private method to move the current position, that's to say the drunkard moves
     *
     * @param dx the distance he moves in the x direction
     * @param dy the distance he moves in the y direction
     */
    private void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Perform a random walk of m steps
     *
     * @param m the number of steps the drunkard takes
     */
    private void randomWalk(int m) {
        for (int i = 0; i < m; i++) {
            randomMove();
        }
    }

    /**
     * Private method to generate a random move according to the rules of the situation.
     * That's to say, moves can be (+-1, 0) or (0, +-1).
     */
    private void randomMove() {
        boolean ns = random.nextBoolean();
        int step = random.nextBoolean() ? 1 : -1;
        move(ns ? step : 0, ns ? 0 : step);
    }

    /**
     * Method to compute the distance from the origin (the lamp-post where the drunkard starts) to his current position.
     *
     * @return the (Euclidean) distance from the origin to the current position.
     */
    public double distance() {
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }

    /**
     * Perform multiple random walk experiments, returning the mean distance.
     *
     * @param m the number of steps for each experiment
     * @param n the number of experiments to run
     * @return the mean distance
     */
    public static double randomWalkMulti(int m, int n) {
        double totalDistance = 0;
        for (int i = 0; i < n; i++) {
            RandomWalk walk = new RandomWalk();
            walk.randomWalk(m);
            totalDistance = totalDistance + walk.distance();
        }
        return totalDistance / n;
    }

    public static void main(String[] args) {
        if (args.length == 0)
            throw new RuntimeException("Syntax: RandomWalk steps [experiments]");
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("No of Steps(n),")
                .append("No of Experiments,")
                .append("Mean Distance (d),")
                .append("\n");
        int n = 25;
        if (args.length > 1) n = Integer.parseInt(args[1]);
        int m;
        for (String s : args[0].split(",")) {
            m = Integer.parseInt(s);
            double meanDistance = randomWalkMulti(m, n);
            System.out.println(m + " steps: " + meanDistance + " over " + n + " experiments");
            outputBuilder.append(m).append(",")
                    .append(n).append(",")
                    .append(String.format("%.2f",meanDistance)).append("\n");
        }
        try {
            PrintWriter writer = new PrintWriter("./src/main/java/edu/neu/coe/info6205/randomwalk/random-walk-exp-data.csv");
            writer.write(outputBuilder.toString());
            writer.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

}
