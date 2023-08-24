package com.itoasis.callingapp.modal;

public class UserDetailsModal {

    private String Name;
    private String UserId,credit;

    // Constructor
    public UserDetailsModal(String name, String userId,String credit) {
        this.Name = name;
        this.UserId = userId;
        this.credit = credit;
    }

    // Getter for Name
    public String getName() {
        return Name;
    }

    // Setter for Name
    public void setName(String name) {
        this.Name = name;
    }
// Getter for credtit
    public String getCredit() {
        return credit;
    }

    // Setter for credit
    public void setCredit(String credit) {
        this.credit= credit;
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
