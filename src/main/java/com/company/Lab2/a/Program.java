package com.company.Lab2.a;

import java.time.Duration;
import java.time.Instant;

import static com.company.Lab2.a.Program.start;

class SharedValue {
    private static int lastOccupiedCellRow = -1;
    private static int WinnieThePoohRow = -1, WinnieThePoohColumn = -1;
    private static boolean winnieFound = false;

    public synchronized void increment(){
        lastOccupiedCellRow++;
    }

    public synchronized void setWinniePosition(int row, int column){
        WinnieThePoohRow = row;
        WinnieThePoohColumn = column;
        winnieFound = true;

        printWinniePosition();
    }

    public static boolean isWinnieFound() {
        return winnieFound;
    }

    public static int getLastOccupiedCellRow() {
        return lastOccupiedCellRow;
    }

    public void printWinniePosition(){
        System.out.println("\nWinnie has been destroyed at position: (" +
                WinnieThePoohRow +
                ", " +
                WinnieThePoohColumn +
                ")\n" +
                "By thread " +
                Thread.currentThread().getName());
        System.out.printf("Time elapsed: %,d nanos", Duration.between(start, Instant.now()).toNanos());
    }
}


class BeeHive implements Runnable {
    private final SharedValue forestInfo;
    private final Forest currentForest;
    private int myRow = -1;

    public BeeHive(SharedValue forestInfo, Forest forest) {
        this.forestInfo = forestInfo;
        currentForest = forest;
    }

    private boolean setRow() {
        forestInfo.increment();
        if (SharedValue.getLastOccupiedCellRow() < Forest.getRows())
            myRow = SharedValue.getLastOccupiedCellRow();
        else {
            //System.out.println(Thread.currentThread().getName() + " end ");
            return false;
        }

        //System.out.println(Thread.currentThread().getName() + " is on row number " + myRow);
        return true;
    }

    private boolean searchInRow(boolean[] cellsRow) {
        for (int i = 0; i < cellsRow.length; ++i) {
            if (cellsRow[i]) {
                forestInfo.setWinniePosition(myRow, i);
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        if (SharedValue.isWinnieFound()) {
            return;
        }
        //System.out.println(Thread.currentThread().getName() + " start ");

        while (!SharedValue.isWinnieFound()) {
            if (!setRow())
                break;

            if (searchInRow(currentForest.getRow(myRow)))
                break;

            if (!SharedValue.isWinnieFound()) {
                for (int i = 0; i < 1e6; ++i) ;
            }
        }

        //System.out.println(Thread.currentThread().getName() + " end ");
    }
}

class Forest {
    private static int rows, columns;
    private static boolean[][] cells;

    public Forest(int rows, int columns) {
        Forest.rows = rows;
        Forest.columns = columns;

        cells = new boolean[rows][columns];

    }

    public static void printForest(){
        for (var cellsRow:cells) {
            for (var cell:cellsRow) {
                System.out.print((cell?"1":"0") + " ");
            }
            System.out.println();
        }
    }

    public static void randomizePositionWinnieThePooh(){
        int randomRow = (int)(Math.random()*(rows-1));
        int randomColumn = (int)(Math.random()*(columns-1));

        cells[randomRow][randomColumn] = true;
    }

    public boolean[] getRow(int i){
        return cells[i];
    }

    public static int getRows() {
        return rows;
    }
}


public class Program {
    static int NUMBER_OF_THREADS = 20;
    public static Instant start;

    public static void main(String[] args) {
        Forest forest = new Forest(500, 500);
        SharedValue forestInfo = new SharedValue();

        Forest.randomizePositionWinnieThePooh();
        startThreads(forestInfo, forest);
    }

    private static void startThreads(SharedValue forestInfo, Forest forest) {
        Thread[] threads = new Thread[NUMBER_OF_THREADS];

        start = Instant.now();

        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            threads[i] = new Thread(new BeeHive(forestInfo, forest));
            threads[i].setName("BeeHive " + i);
            threads[i].start();
        }
    }
}