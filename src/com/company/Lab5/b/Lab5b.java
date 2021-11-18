package com.company.Lab5.b;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public class Lab5b {
    private static ArrayList<Thread> threads = new ArrayList<>();
    final private static CyclicBarrier gate = new CyclicBarrier(5);

    public static void main(String[] args) {
        final CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("\n3 threads have equal A and B symbols number");

            for (Thread thread : threads) {
                thread.interrupt();
            }

            System.out.println();
        });
        initializeThreads(barrier);
    }

    public static void initializeThreads(CyclicBarrier barrier) {
        IntStream.range(0, 4).forEach(i -> {
            threads.add(new ThreadsManager(gate, barrier));
            threads.get(i).start();
        });

        try {
            gate.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
