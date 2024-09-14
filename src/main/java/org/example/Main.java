package org.example;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    private static boolean validatePositiveInteger(String input) {
        try {
            int number = Integer.parseInt(input);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private static int[] generateArray(int size) {
        int[] array = new int[size];
        Arrays.fill(array, 1);
        return array;
    }

    private static long segmentSum(int[] array, int start, int end) {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
        return sum;
    }
    private static long multithreadedSum(int[] array, int threads) {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        int chunkSize = array.length / threads;

        @SuppressWarnings("unchecked")
        Future<Long>[] futures = new Future[threads];
        for (int i = 0; i < threads; i++) {
            int start = i * chunkSize;
            int end = (i == threads - 1) ? array.length : (i + 1) * chunkSize;

            futures[i] = executor.submit(() -> segmentSum(array, start, end));
        }

        long sum = 0;
        try {
            for (int i = 0; i < threads; i++) {
                sum += futures[i].get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return sum;
    }
    public static void main(String[] args) {
        int n = (int)10e8;
        System.out.printf("Generating array of size %d\n", n);
        int[] array = generateArray(n);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter amount of threads:");
        String input;
        do {
            input = scanner.nextLine();
        } while (!validatePositiveInteger(input));
        int threads = Integer.parseInt(input);

        long startTime = System.currentTimeMillis();
        long sum = multithreadedSum(array, threads);
        long endTime = System.currentTimeMillis();

        System.out.printf("Sum: %d\n", sum);
        System.out.printf("Time: %d ms\n", endTime - startTime);
    }
}