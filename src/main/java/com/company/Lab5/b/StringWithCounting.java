package com.company.Lab5.b;

public class StringWithCounting {
    private String string;
    private int numberA = 0;
    private int numberB = 0;

    StringWithCounting(String string) {
        this.string = string;

        setNumbers();
    }

    public boolean areABEqual() {
        return numberA == numberB;
    }

    public void setCharAtPos(char ch, int index) {
        char[] charArray = string.toCharArray();

        if (charArray[index] == 'A')
            numberA--;
        else if (charArray[index] == 'B')
            numberB--;

        if (ch == 'A')
            numberA++;
        else if (ch == 'B')
            numberB++;

        charArray[index] = ch;
        string = String.valueOf(charArray);
    }

    public char getCharAtPos(int index) {
        return string.toCharArray()[index];
    }

    private void setNumbers() {
        numberA = numberB = 0;

        for (char ch : string.toCharArray()) {
            if (ch == 'A') numberA++;
            else if (ch == 'B') numberB++;
        }
    }

    public int getNumberA() {
        return numberA;
    }

    public int getNumberB() {
        return numberB;
    }

    public String getString() {
        return string;
    }
}