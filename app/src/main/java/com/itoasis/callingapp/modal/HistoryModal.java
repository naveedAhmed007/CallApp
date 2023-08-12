package com.itoasis.callingapp.modal;

public class HistoryModal {

    private String callerName;

    private String receiverName;
    private String callDate;
    private String callTime;

    public HistoryModal(String callerName, String receiverName, String callDate, String callTime) {
        this.callerName = callerName;

        this.receiverName = receiverName;
        this.callDate = callDate;
        this.callTime = callTime;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }


    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }
}
