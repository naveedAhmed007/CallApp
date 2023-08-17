package com.itoasis.callingapp.modal;

public class UserDetailsModal {

    private String Name;
    private String UserId;

    // Constructor
    public UserDetailsModal(String name, String userId) {
        this.Name = name;
        this.UserId = userId;
    }

    // Getter for Name
    public String getName() {
        return Name;
    }

    // Setter for Name
    public void setName(String name) {
        this.Name = name;
    }

    // Getter for UserId
    public String getUserId() {
        return UserId;
    }

    // Setter for UserId
    public void setUserId(String userId) {
        this.UserId = userId;
    }
}
