package com.itoasis.callingapp.modal;

public class MessageModal {
    private String roomName;
    private String chatRoomName; // Add this field

    public MessageModal() {
        // Default constructor required for Firestore
    }

    public MessageModal(String roomName, String chatRoomName) {
        this.roomName = roomName;
        this.chatRoomName = chatRoomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }
}
