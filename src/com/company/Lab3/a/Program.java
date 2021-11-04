package com.company.Lab3.a;

class Bee implements Runnable{
    HoneyPot honeyPot;

    public Bee(HoneyPot honeyPot) {
        this.honeyPot = honeyPot;
    }

    @Override
    public void run() {
        while(true){
            try {
                if(honeyPot.getTimesDrink() == 10){
                    break;
                }
                honeyPot.fillPot();
                Thread.currentThread().sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class HoneyPot {
    public static boolean full;
    public static int volume = 0;
    public static int capacity;
    public static int timesDrink = 0;

    public static boolean isFull() {
        return full;
    }

    public HoneyPot(int capacity) {
        this.capacity = capacity;
    }

    public static int getTimesDrink() {
        return timesDrink;
    }

    public void fillPot() throws InterruptedException {
        synchronized (this) {
            if(full){
                wait();
            }
            if(timesDrink == 10){
                return;
            }
            System.out.println(Thread.currentThread().getName() + " has brought honey.");
            volume++;
            if(volume == capacity){
                full = true;
            }
            if(full){
                notifyAll();
            }
        }
    }

    public void emptyPot() throws InterruptedException {
        synchronized (this){
            if(!full){
                wait();
            }
            volume = 0;
            timesDrink++;
            Thread.currentThread().sleep(500);
            System.out.println(Thread.currentThread().getName() + " has drunk all the honey.");
            full = false;
            notifyAll();
        }
    }
}

class Winnie implements Runnable{
    HoneyPot honeyPot;

    public Winnie(HoneyPot honeyPot) {
        this.honeyPot = honeyPot;
    }

    @Override
    public void run() {
        while(true) {
            try {
                honeyPot.emptyPot();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(honeyPot.getTimesDrink() == 10){
                break;
            }
        }
    }
}

public class Program {
    public static HoneyPot honeyPot = new HoneyPot(10);
    public static int numberOfBees = 8;

    public static void main(String[] args) throws InterruptedException {
        Thread winnie = new Thread(new Winnie(honeyPot));
        winnie.setName("Winnie the Pooh");
        winnie.start();

        for(int i=0;i<numberOfBees;++i){
            Thread bee = new Thread(new Bee(honeyPot));
            bee.setName("Bee #" + i);
            bee.start();
        }

        winnie.join();
    }
}
