package com.itoasis.callingapp.modal;

public class SummaryModal {
    private String cName, minutes, imageUrl;

    // Constructor
    public SummaryModal(String cName, String minutes, String imageUrl) {
        this.cName = cName;
        this.minutes = minutes;
        this.imageUrl = imageUrl;
    }

    // Getter for cName
    public String getCName() {
        return cName;
    }

    // Setter for cName
    public void setCName(String cName) {
        this.cName = cName;
    }

    // Getter for minutes
    public String getMinutes() {
        return minutes;
    }

    // Setter for minutes
    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    // Getter for imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    // Setter for imageUrl
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
