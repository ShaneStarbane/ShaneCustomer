package com.example.hendryshanedeguia.freshcartcustomer;

/**
 * Created by HendryShanedeGuia on 29/08/2017.
 */

public class OrderInformation {
    public String orderID;
    public String orderGross;
    public String custContact;
    public String custAddress;
    public String custImageUrl;
    public String custUsername;
    public String orderStatus;
    public String cashOnHand;
    public String noteForDriver;
    public String promo;
    public String discount;
    public String orderVAT;
    public String deliveredBy;



    public OrderInformation() {
    }
    public OrderInformation(String orderID, String orderGross, String custContact, String custAddress, String custImageUrl, String custUsername,String orderStatus,String cashOnHand, String noteForDriver, String promo, String discount,String orderVAT, String deliveredBy) {
        this.orderID = orderID;
        this.orderGross = orderGross;
        this.custContact = custContact;
        this.custAddress = custAddress;
        this.custImageUrl = custImageUrl;
        this.custUsername = custUsername;
        this.orderStatus = orderStatus;
        this.cashOnHand = cashOnHand;
        this.noteForDriver = noteForDriver;
        this.promo = promo;
        this.discount = discount;
        this.orderVAT = orderVAT;
        this.deliveredBy = deliveredBy;
    }

    public String getCashOnHand() {
        return cashOnHand;
    }

    public String getNoteForDriver() {
        return noteForDriver;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getOrderBill() {
        return orderGross;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setOrderBill(String orderBill) {
        this.orderGross = orderBill;
    }

    public void setCustContact(String custContact) {
        this.custContact = custContact;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public void setCustImageUrl(String custImageUrl) {
        this.custImageUrl = custImageUrl;
    }

    public void setCustUsername(String custUsername) {
        this.custUsername = custUsername;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setCashOnHand(String cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    public void setNoteForDriver(String noteForDriver) {
        this.noteForDriver = noteForDriver;
    }
}

