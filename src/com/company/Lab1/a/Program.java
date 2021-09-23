package com.company.Lab1.a;

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

    public synchronized void moveSlider(int value) {
        if (this.slider.getValue() + value < 10){
            return;
        }
        else if (this.slider.getValue() + value > 90){
            return;
        }

        this.slider.setValue(this.slider.getValue() + value);
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
        System.out.println(Thread.currentThread().getName() + " started with value " + value);

        boolean isInterrupted = false;
        while (!isInterrupted) {

            sharedValue.moveSlider(value);

            try {
                Thread.sleep(0, 2);
            } catch (InterruptedException e) {
                isInterrupted = true;
            }
        }

        System.out.println(Thread.currentThread().getName() + " ended");
    }
}

public class Program {
    static Thread Thread1, Thread2;
    static JButton startBtn, stopBtn, Thread1IncrPrior, Thread2IncrPrior, Thread1DecrPrior, Thread2DecrPrior;
    static JSlider slider;
    static JTextField priorities;
    static JPanel panel, linePanelTop, linePanelCentral, linePanelBottom;

    public static void main(String[] args) {
        JFrame win = new JFrame();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(650, 480);

        JPanel panel = getjPanel();

        win.setContentPane(panel);
        win.setVisible(true);

        /*System.out.println(Thread1IncrPrior.getSize());
        Thread1IncrPrior.setSize(119,26);
        //Thread1DecrPrior.setSize(119,26);
        System.out.println(Thread1IncrPrior.getSize());*/
    }

    private static void printPriorities(JTextField textField) {
        textField.setText(" " + Thread1.getPriority() + "-" + Thread2.getPriority() + " ");
    }

    private static void changePriority(Thread th, boolean isIncrement) {
        if (isIncrement) {
            if (th.getPriority() < 10)
                th.setPriority(th.getPriority() + 1);
        } else {
            if (th.getPriority() > 1)
                th.setPriority(th.getPriority() - 1);
        }
        printPriorities(priorities);
    }

    private static JPanel getjPanel() {
        panel = new JPanel();
        linePanelTop = new JPanel();
        linePanelCentral = new JPanel();
        linePanelBottom = new JPanel();

        startBtn = new JButton("Start");
        stopBtn = new JButton("Stop");
        slider = new JSlider();

        stopBtn.setEnabled(false);
        slider.setEnabled(false);

        slider.setValue(50);
        /*slider.setMinimum(10);
        slider.setMaximum(90);*/

        Thread1IncrPrior = new JButton("First Prior +");
        Thread1DecrPrior = new JButton("First Prior -");
        Thread2IncrPrior = new JButton("Second Prior +");
        Thread2DecrPrior = new JButton("Second Prior -");

        priorities = new JTextField("               ");
        priorities.setEnabled(false);
        priorities.setHorizontalAlignment(SwingConstants.CENTER);

        Thread1IncrPrior.setEnabled(false);
        Thread2IncrPrior.setEnabled(false);
        Thread1DecrPrior.setEnabled(false);
        Thread2DecrPrior.setEnabled(false);

        SharedValue sliderData = new SharedValue(slider);
        startBtn.addActionListener(e -> {
            Thread1 = new Thread(new CustomThread(sliderData, -1));
            Thread2 = new Thread(new CustomThread(sliderData, 1));

            Thread1.setDaemon(true);
            Thread2.setDaemon(true);

            Thread1.setPriority(5);
            Thread2.setPriority(5);

            Thread1.start();
            Thread2.start();

            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);

            Thread1IncrPrior.setEnabled(true);
            Thread2IncrPrior.setEnabled(true);
            Thread1DecrPrior.setEnabled(true);
            Thread2DecrPrior.setEnabled(true);

            printPriorities(priorities);
        });


        stopBtn.addActionListener(e -> {
            Thread1.interrupt();
            Thread2.interrupt();

            slider.setValue(50);

            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);

            Thread1IncrPrior.setEnabled(false);
            Thread2IncrPrior.setEnabled(false);
            Thread1DecrPrior.setEnabled(false);
            Thread2DecrPrior.setEnabled(false);

            priorities.setText("      :      ");
        });

        Thread1IncrPrior.addActionListener(e -> {
            changePriority(Thread1, true);
        });
        Thread1DecrPrior.addActionListener(e -> {
            changePriority(Thread1, false);
        });

        Thread2IncrPrior.addActionListener(e -> {
            changePriority(Thread2, true);
        });
        Thread2DecrPrior.addActionListener(e -> {
            changePriority(Thread2, false);
        });

        linePanelTop.add(startBtn);
        linePanelTop.add(stopBtn);
        //linePanelTop.add(slider);

        linePanelCentral.add(Thread1IncrPrior);
        linePanelCentral.add(Thread1DecrPrior);
        linePanelCentral.add(priorities);
        linePanelCentral.add(Thread2IncrPrior);
        linePanelCentral.add(Thread2DecrPrior);

        linePanelBottom.add(slider);

        /*linePanelBottom.add(Thread1IncrPrior);
        linePanelBottom.add(Thread1DecrPrior);
        linePanelBottom.add(priorities);
        linePanelBottom.add(Thread2IncrPrior);
        linePanelBottom.add(Thread2DecrPrior);*/

        panel.add(linePanelTop);
        panel.add(linePanelCentral);
        panel.add(linePanelBottom);

        return panel;
    }
}

