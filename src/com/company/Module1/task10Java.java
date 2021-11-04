package com.company.Module1;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

class Exchange {
    private final Lock lock = new ReentrantLock();
    public static int CompaniesNumber = 5;

    public static Company companies[] = new Company[CompaniesNumber];

    public static boolean EndWork = false;

    public static int Index = 100;

    public Exchange(){
        for(int i = 0; i < CompaniesNumber; i++){
            companies[i] = new Company();
        }
    }

    public boolean getEndWork(){
        return EndWork;
    }

    public void changeIndex(){
        lock.lock();
        try {
            Random random = new Random();
            int temp = random.nextInt(3);
            if(temp == 0){
                int change = random.nextInt((int)Index / 4);
                Index = Index + change;
                if ((int)Index / 5 <= change){
                    EndWork = true;
                }
            }
            else if(temp == 1){
                int change = random.nextInt((int)Index / 4);
                Index = Index - change;
                if ((int)Index / 5 <= change){
                    EndWork = true;
                }
            }
        } finally {
            lock.unlock();
        }
    }
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
            int temp1 = random.nextInt((int)price / 5);
            int temp2 = random.nextInt((int)price / 5);
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
        this.exchange = exc;
        id = i;

        Random random = new Random();
        this.stock = random.nextInt(40);

        this.company = exc.companies[random.nextInt(exc.CompaniesNumber)];
    }

    public void trySellStock(){
        Random random = new Random();

        if (random.nextInt(5) == 0){
            int temp = random.nextInt(stock + 1);
            this.stock = stock - temp;

            company.changePrice();
            exchange.changeIndex();

            System.out.println("Broker " + this.id + " sold " + temp + " stock with total price " + temp * company.getPrice());
        }
    }

    @Override
    public void run() {
        while (!exchange.EndWork) {
            try {
                trySellStock();
                sleep(500);
            } catch (Exception e){

            }
        }
    }
}

class Program {
    public static Exchange exchange = new Exchange();
    public static void init() throws InterruptedException {
        Broker brokers[] = new Broker[10];

        for(int i = 0; i < brokers.length; i++){
            brokers[i] = new Broker(exchange, i);
            Thread thread = new Thread(brokers[i]);
            thread.start();
        }

        long startTime = System.nanoTime() / 1000000;
        while (System.nanoTime() / 1000000 - startTime < 10000 && !exchange.getEndWork()){}

        sleep(1000);
    }

    public static void main(String[] args) throws InterruptedException {
        init();
    }
}
