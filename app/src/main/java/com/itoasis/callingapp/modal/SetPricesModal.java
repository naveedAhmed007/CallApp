package com.itoasis.callingapp.modal;

public class SetPricesModal {

    private String countryName;
    private String hourlyRate;
    private String decHourlyRate;

    // Constructor
    public SetPricesModal(String countryName, String hourlyRate, String decHourlyRate) {
        this.countryName = countryName;
        this.hourlyRate = hourlyRate;
        this.decHourlyRate = decHourlyRate;
    }

    // Getter for countryName
    public String getCountryName() {
        return countryName;
    }

    // Setter for countryName
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    // Getter for hourlyRate
    public String getHourlyRate() {
        return hourlyRate;
    }

    // Setter for hourlyRate
    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    // Getter for decHourlyRate
    public String getDecHourlyRate() {
        return decHourlyRate;
    }

    // Setter for decHourlyRate
    public void setDecHourlyRate(String decHourlyRate) {
        this.decHourlyRate = decHourlyRate;
    }
}
