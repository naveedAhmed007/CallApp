
package com.itoasis.callingapp.modal;

import android.net.Uri;

public class PaymentModal {
    private String uri;
    private String name, date, price, iteration;

    public PaymentModal(String uri, String name, String date, String price, String iteration) {
        this.uri = uri;
        this.name = name;
        this.date = date;
        this.price = price;
        this.iteration = iteration;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIteration() {
        return iteration;
    }

    public void setIteration(String iteration) {
        this.iteration = iteration;
    }
}

