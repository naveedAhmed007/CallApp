package com.itoasis.callingapp.utils;

import java.util.HashSet;
import java.util.Set;

public class Singleton {

    private static Singleton instance;

    private String userEmail,clientEmailForTime; // User's email

    private int counter,activityCall,answeredCall,counterreceivedcalls;

    private String phoneNumber2,userName,phoneNumber1; // New member variable to store phoneNumber
    private String callScreenFrom,callerNames,documentId; // New member variable to store phoneNumber

    private String phoneNumber; // New member variable to store phoneNumber
    private String phoneNumberr,credits; // New member variable to store phoneNumber



    private boolean listener; // New member variable to store phoneNumber
 // New member variable to store the user's name
    private char firstChar;
    boolean isCallActive;
    private byte fsNumberslength;

    private Singleton() {
        counter = 0;
        phoneNumber2 = ""; // Initialize phoneNumber as an empty string
        phoneNumber1="";


        phoneNumber = ""; // Initialize phoneNumber as an empty string
        //phoneNumberr="";
        //credits="";

        activityCall=0;
        answeredCall=0;
        callScreenFrom="";
        counterreceivedcalls=0;
        listener=false;
       userName = ""; // Initialize userName as an empty string



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

    public synchronized void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public synchronized String getCallScreenFrom() {
        return callScreenFrom;
    }

    public synchronized void setCallScreenFrom(String callScreenFrom) {
        this.callScreenFrom = callScreenFrom;
    }

    public synchronized String getPhoneNumber2() {
        return phoneNumber2;
    }
    public synchronized void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }
    public synchronized String getPhoneNumber1() {
        return phoneNumber1;
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

    public synchronized void resetAnswerCall() {
        answeredCall = 0;
    }

    public synchronized void resetActivityCall() {
        activityCall = 0;
    }

    public synchronized void setUserName(String userName) {
        this.userName = userName;
    }
    public  synchronized  void setPhoneNumberr(String phoneNumberr){this.phoneNumberr=phoneNumberr;}
    public synchronized  String getPhoneNumberr(){return phoneNumberr;}


    public  synchronized  void setCredits(String credits){this.credits=credits;}
    public synchronized  String getCredits(){return credits;}

    public synchronized String getUserName() {
        return userName;
    }

    public synchronized void setChar(char firstChar) {
        this.firstChar = firstChar;
    }

    public synchronized char getChar() {
        return firstChar;
    }
    public synchronized void resetCallScreenFrom(){
     callScreenFrom="";

    }
    public synchronized void resetCountCalls(){
        counterreceivedcalls=0;

    }
    // Getter and setter for user email
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public synchronized int getCounterCalls() {
        return counterreceivedcalls;
    }

    public synchronized void incrementCounterCalls() {
        counterreceivedcalls++;
    }
    public synchronized void changeListener() {
        listener=true;
    }
    public synchronized boolean getListener() {
        return listener;
    }
    public synchronized void resetListener(){
        listener=false;

    }
    public synchronized void setCallerName(String callerName) {
        this.callerNames=callerName;
    }
    public synchronized String getCallerName() {
        return callerNames;
    }

    public synchronized void setFireStoreNumbersLength(byte fsNumberslength) {
        this.fsNumberslength=fsNumberslength;
    }
    public synchronized byte getFireStoreNumbersLength() {
        return fsNumberslength;
    }


    public synchronized void setCallActive(boolean isCallActive) {
        this.isCallActive=isCallActive;
    }
    public synchronized boolean getCallActive() {
        return isCallActive;
    }


    public synchronized void setDocumentId(String id) {
        this.documentId=id;
    }
    public synchronized String getDocumentId() {
        return documentId;
    }

    public synchronized void setClientEmailForTime(String clientEmail) {
        this.clientEmailForTime=clientEmail;
    }
    public synchronized String getClientEmailForTime() {
        return clientEmailForTime;
    }

}
