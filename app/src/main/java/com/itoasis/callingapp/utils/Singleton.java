package com.itoasis.callingapp.utils;


public class Singleton {


    private static Singleton instance;
    private int counter,activityCall,answeredCall;
    private String phoneNumber; // New member variable to store phoneNumber

    private Singleton() {
        counter = 0;
        phoneNumber = ""; // Initialize phoneNumber as an empty string
        activityCall=0;
        answeredCall=0;
    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public synchronized int getCounter() {
        return counter;
    }

    public synchronized void incrementCounter() {
        counter++;
    }

    public synchronized void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public synchronized String getPhoneNumber() {
        return phoneNumber;
    }


    public synchronized int getActivityCall() {
        return activityCall;
    }
    public synchronized void incrementActivityCall() {
        activityCall++;
    }

    public synchronized void incrementAnsweredCall() {
        answeredCall++;
    }
    public synchronized int getAnsweredcall() {
        return answeredCall;
    }
    public synchronized void resetAnswerCall(){
        answeredCall=0;

    }
    public synchronized void resetActivityCall(){
        activityCall=0;

    }

}
