package com.company.Lab1.b;

import javax.swing.*;

class SharedValue {
    private JSlider slider;

    public SharedValue(JSlider slider) {
        this.slider = slider;
    }

    public synchronized JSlider getValue() {
        return slider;
    }

    public synchronized void setValue(JSlider slider) {
        this.slider = slider;
    }

    public synchronized void modify(int value) {
        this.slider.setValue(value);
    }

    @Override
    public String toString() {
        return "SharedValue{" +
                "value=" + slider.getValue() +
                '}';
    }
}

class CustomThread implements Runnable {
    private final SharedValue sharedValue;
    private final int value;

    public CustomThread(SharedValue sharedValue, int value) {
        this.sharedValue = sharedValue;
        this.value = value;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start " + value);

        boolean isInterrupted = false;

        while (!isInterrupted) {

            sharedValue.modify(value);

            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException e) {
                isInterrupted = true;
            }
        }

        System.out.println(Thread.currentThread().getName() + " end ");
    }
}

public class Program {
    static int semaphore = 0;

    static Thread Thread1, Thread2;
    static JButton start1ThBtn, stop1ThBtn, start2ThBtn, stop2ThBtn;
    static JSlider slider;
    static JTextField text;
    static JPanel panel, TopPanel, BottomPanel, CentralPanel;

    public static void main(String[] args) {
        JFrame win = new JFrame();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(500, 400);

        JPanel panel = getjPanel();

        win.setContentPane(panel);
        win.setVisible(true);
    }

    private static void changePriority(Thread th, boolean isIncrPriority) {
        if (isIncrPriority) {
            if (th.getPriority() < 10)
                th.setPriority(th.getPriority() + 1);
        } else {
            if (th.getPriority() > 1)
                th.setPriority(th.getPriority() - 1);
        }
    }

    private static void startThread(SharedValue data, int threadNum) {
        if(semaphore == 1){
            text.setText("Another thread is running!");
            return;
        }

        switch(threadNum){
            case 1:{
                Thread1 = new Thread(new CustomThread(data, 10));

                Thread1.setPriority(1);

                Thread1.start();

                start1ThBtn.setEnabled(false);
                stop1ThBtn.setEnabled(true);
                stop2ThBtn.setEnabled(false);

                break;
            }
            case 2:{
                Thread2 = new Thread(new CustomThread(data, 90));

                Thread2.setPriority(10);

                Thread2.start();

                start2ThBtn.setEnabled(false);
                stop2ThBtn.setEnabled(true);
                stop1ThBtn.setEnabled(false);

                break;
            }
        }

        semaphore = 1;
    }

    private static void stopThread(int threadNum){
        switch(threadNum){
            case 1:{
                Thread1.interrupt();

                start1ThBtn.setEnabled(true);
                stop1ThBtn.setEnabled(false);

                break;
            }
            case 2:{
                Thread2.interrupt();

                start2ThBtn.setEnabled(true);
                stop2ThBtn.setEnabled(false);

                break;
            }
        }

        text.setText(" ");
        semaphore = 0;
    }

    private static JPanel getjPanel() {
        panel = new JPanel();

        TopPanel = new JPanel();
        CentralPanel = new JPanel();
        BottomPanel = new JPanel();

        start1ThBtn = new JButton("Start Thread 1");
        stop1ThBtn = new JButton("Stop Thread 1");
        stop1ThBtn.setEnabled(false);

        start2ThBtn = new JButton("Start Thread 2");
        stop2ThBtn = new JButton("Stop Thread 2");
        stop2ThBtn.setEnabled(false);

        slider = new JSlider();
        slider.setEnabled(false);
        slider.setValue(50);

        text = new JTextField("                                                ");
        text.setEnabled(false);

        SharedValue sliderData = new SharedValue(slider);
        start1ThBtn.addActionListener(e -> {
            startThread(sliderData, 1);
        });
        start2ThBtn.addActionListener(e -> {
            startThread(sliderData, 2);
        });

        stop1ThBtn.addActionListener(e -> {
            stopThread(1);
        });
        stop2ThBtn.addActionListener(e -> {
            stopThread(2);
        });


        TopPanel.add(start1ThBtn);
        TopPanel.add(stop1ThBtn);
        TopPanel.add(start2ThBtn);
        TopPanel.add(stop2ThBtn);

        CentralPanel.add(slider);
        BottomPanel.add(text);

        panel.add(TopPanel);
        panel.add(CentralPanel);
        panel.add(BottomPanel);

        return panel;
    }
}

