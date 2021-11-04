package com.company.Module1;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Exchange {
    public static int CompaniesNumber = 5;

    public static Company companies[] = new Company[CompaniesNumber];

    public static boolean EndWork = false;

    public static int Index = 100;

    public Exchange(){};
}

class Company {
    private int price;
    private final Lock lock = new ReentrantLock();

    public Company() {
        Random random = new Random();
        this.price = random.nextInt(100);
    }

    public int getPrice() {
        return price;
    }

    public void changePrice(){
        lock.lock();

        try {
            Random random = new Random();
            int temp1 = random.nextInt((int)price / 3);
            int temp2 = random.nextInt((int)price / 3);
            this.price = price - temp1 + temp2;
        } finally {
            lock.unlock();
        }
    }
}

class Broker implements Runnable {
    public Exchange exchange;
    public Company company;
    public int id;

    public int stock;

    public Broker(Exchange exc, int i){
        exchange = exc;
        id = i;

        Random random = new Random();
        this.stock = random.nextInt(40);

        this.company = exc.companies[random.nextInt(exc.CompaniesNumber)];
    }

    public void sellStock(){
        Random random = new Random();
        int temp = random.nextInt(stock + 1);
        this.stock = stock - temp;

        System.out.println("Broker " + this.id + "sold" + temp + "stock with total price " + temp * company.getPrice());
    }

    @Override
    public void run() {
        while (!exchange.EndWork) {
            try {
                sellStock();
            } catch (Exception e){

            }
        }
    }
}

class Program {

    public static void init() throws InterruptedException {

    }

    public static void main(String[] args) throws InterruptedException {
        init();
    }
}
