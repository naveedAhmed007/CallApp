package com.itoasis.callingapp.modal;

public class MessageModal {
    private String name, message, time;


    public MessageModal(String name, String message, String time) {
        this.name = name;
        this.message = message;
        this.time = time;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for time
    public String getTime() {
        return time;
    }

    // Setter for time
    public void setTime(String time) {
        this.time = time;
    }
}
