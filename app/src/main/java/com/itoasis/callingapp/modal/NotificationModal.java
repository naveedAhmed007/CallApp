package com.itoasis.callingapp.modal;

public class NotificationModal {
    private String notificationDescription;
    private String notificationTime;

    // Constructor
    public NotificationModal(String notificationDescription, String notificationTime) {
        this.notificationDescription = notificationDescription;
        this.notificationTime = notificationTime;
    }

    // Getter for notificationDescription
    public String getNotificationDescription() {
        return notificationDescription;
    }

    // Setter for notificationDescription
    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    // Getter for notificationTime
    public String getNotificationTime() {
        return notificationTime;
    }

    // Setter for notificationTime
    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }
}
