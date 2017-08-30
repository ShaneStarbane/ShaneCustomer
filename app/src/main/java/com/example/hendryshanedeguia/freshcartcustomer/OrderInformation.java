package com.example.hendryshanedeguia.freshcartcustomer;

/**
 * Created by HendryShanedeGuia on 29/08/2017.
 */

public class OrderInformation {
    public String orderID;
    public String orderBill;
    public String custContact;
    public String custAddress;
    public String custImageUrl;
    public String custUsername;
    public OrderInformation() {
    }
    public OrderInformation(String orderID, String orderBill, String custContact, String custAddress, String custImageUrl, String custUsername) {
        this.orderID = orderID;
        this.orderBill = orderBill;
        this.custContact = custContact;
        this.custAddress = custAddress;
        this.custImageUrl = custImageUrl;
        this.custUsername = custUsername;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getOrderBill() {
        return orderBill;
    }

    public String getCustContact() {
        return custContact;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public String getCustImageUrl() {
        return custImageUrl;
    }

    public String getCustUsername() {
        return custUsername;
    }
}

