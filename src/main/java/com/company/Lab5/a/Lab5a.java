package com.company.Lab5.a;

public class Lab5a {
    public static void main(String[] args){
        Recruits recruits = new Recruits(160);
        recruits.printRecruits();

        ThreadManager manager = new ThreadManager(recruits, 50);
    }
}
