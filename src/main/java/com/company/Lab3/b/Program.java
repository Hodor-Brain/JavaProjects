package com.company.Lab3.b;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.Thread.sleep;

class Barbershop {
    public static int CUSTOMERS_NUMBER = 10;
    public static int incr = 0;

    public static volatile boolean occupied;
    public static boolean endOfTheDay;
    public static ArrayBlockingQueue<Client> clients = new ArrayBlockingQueue<>(CUSTOMERS_NUMBER);

    public Barbershop() {
        this.occupied = false;
        this.endOfTheDay = false;
    }

    public void clientHasCome(Client client) throws InterruptedException {
        System.out.println(client.getName() + " has come.");
        synchronized (this) {
            clients.put(client);
            incr++;
            notifyAll();
        }
    }

    public static boolean isOccupied() {
        return occupied;
    }

    public static void setOccupied() {
        occupied = true;
    }

    public static boolean isEndOfTheDay() {
        return endOfTheDay;
    }

    public static ArrayBlockingQueue<Client> getClients() {
        return clients;
    }

    public static int getCustomersNumber() {
        return CUSTOMERS_NUMBER;
    }

    public void barberLogic() throws InterruptedException {
        if (clients.isEmpty()) {
            synchronized (this) {
                System.out.println("Barber is sleeping.");
                wait();
            }
        } else {
            trim(getClients().take());
        }
    }

    public void clientLogic() throws InterruptedException {
        while(isOccupied()){
            System.out.println(Thread.currentThread().getName() + " is sleeping.");
            wait();
        }
    }

    public void trim(Client client) throws InterruptedException {
        Random random = new Random();
        System.out.println("Trim of " + client.getName() + " started.");
        sleep(random.nextInt(2000));
        System.out.println("Trim of " + client.getName() + " finished.");
        if (incr == CUSTOMERS_NUMBER && clients.isEmpty()) {
            endOfTheDay = true;
        }
        synchronized (this) {
            notifyAll();
        }
    }
}

class Barber implements Runnable {
    Barbershop barbershop;

    public Barber(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        while (!barbershop.isEndOfTheDay()) {
            try {
                barbershop.barberLogic();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("End of the day.");
    }
}

class Client implements Runnable {
    Barbershop barbershop;
    String name;
    public static int incr = 0;

    public Client(Barbershop barbershop) {
        this.barbershop = barbershop;
        this.name = "Client #" + incr++;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        while (!barbershop.isEndOfTheDay()) {
            try {
                barbershop.clientLogic();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Program {
    public static Barbershop barbershop = new Barbershop();
    public static Thread barber;

    public static void init() throws InterruptedException {
        Random random = new Random();
        barber = new Thread(new Barber(barbershop));
        barber.start();
        for (int i = 0; i < barbershop.getCustomersNumber(); ++i) {
            Client newClient = new Client(barbershop);
            Thread newThread = new Thread(newClient);
            newThread.start();
            barbershop.clientHasCome(newClient);
            Thread.currentThread().sleep(random.nextInt(2000));
        }
        barber.join();
    }

    public static void main(String[] args) throws InterruptedException {
        init();
    }
}
