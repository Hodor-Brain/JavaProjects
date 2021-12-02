package com.company.Lab2.b;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Manager {
    private static int NUMBER_OF_GOODS = 1000;
    private static int totalPrice = 0;
    private static Instant start;

    private static BlockingQueue<MilitaryGood> wheelBarrow = new ArrayBlockingQueue<MilitaryGood>(NUMBER_OF_GOODS);
    private static BlockingQueue<MilitaryGood> automobile = new ArrayBlockingQueue<MilitaryGood>(NUMBER_OF_GOODS);
    private static BlockingQueue<MilitaryGood> warehouse = new ArrayBlockingQueue<MilitaryGood>(NUMBER_OF_GOODS);

    public static void manageGoods() throws InterruptedException {
        generateGoods();

        Thread Ivanov = new Thread(() -> {
            try {
                producer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread Petrov = new Thread(() -> {
            try {
                consumerProducer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread Nechyporchuk = new Thread(() -> {
            try {
                consumer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        start = Instant.now();

        Ivanov.start();
        Petrov.start();
        Nechyporchuk.start();

        Ivanov.join();
        Petrov.join();
        Nechyporchuk.join();

        System.out.printf("\nTime elapsed: %,d nanos\n", Duration.between(start, Instant.now()).toNanos());
    }

    public static void generateGoods() throws InterruptedException {
        for (int i = 0; i < NUMBER_OF_GOODS; ++i) {
            warehouse.put(new MilitaryGood());
        }
    }

    private static void producer() throws InterruptedException {
        while (!warehouse.isEmpty()) {
            wheelBarrow.put(warehouse.take());
            Thread.currentThread().sleep(1);
        }

    }

    private static void consumerProducer() throws InterruptedException {
        while (true) {
            automobile.put(wheelBarrow.take());
            Thread.currentThread().sleep(1);

            if (warehouse.isEmpty() && wheelBarrow.isEmpty()) {
                break;
            }
        }
    }

    private static void consumer() throws InterruptedException {
        while (true) {
            totalPrice += automobile.take().getPrice();
            //System.out.println("Total price is " + totalPrice);
            Thread.currentThread().sleep(1);

            if (warehouse.isEmpty() && automobile.isEmpty() && wheelBarrow.isEmpty()) {
                break;
            }
        }
        System.out.println("Total price is " + totalPrice);
    }
}


class MilitaryGood {
    private int price;

    public MilitaryGood() {
        this.price = (int) (Math.random() * 100);
    }

    public int getPrice() {
        return price;
    }

    public void printGood(){
        System.out.println("Price: " + price);
    }
}


public class Program {
    private static Manager manager = new Manager();

    public static void main(String[] args) throws InterruptedException {
        manager.manageGoods();
    }
}