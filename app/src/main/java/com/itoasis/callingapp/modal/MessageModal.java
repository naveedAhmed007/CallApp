package com.itoasis.callingapp.modal;

public class MessageModal {
    private String roomName;

    public MessageModal() {
        // Default constructor required for Firestore
    }

    public MessageModal(String roomName) {
        this.roomName = roomName;
    }


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
