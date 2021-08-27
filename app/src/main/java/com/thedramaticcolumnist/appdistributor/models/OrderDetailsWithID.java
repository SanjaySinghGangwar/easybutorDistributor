package com.thedramaticcolumnist.appdistributor.models;

public class OrderDetailsWithID {
    String id;
    OrderDetailsModel details;

    public OrderDetailsWithID(String id, OrderDetailsModel details) {
        this.id = id;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderDetailsModel getDetails() {
        return details;
    }

    public void setDetails(OrderDetailsModel details) {
        this.details = details;
    }
}
