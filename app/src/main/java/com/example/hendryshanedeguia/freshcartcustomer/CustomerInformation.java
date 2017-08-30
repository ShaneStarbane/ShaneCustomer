package com.example.hendryshanedeguia.freshcartcustomer;

/**
 * Created by HendryShanedeGuia on 27/08/2017.
 */

public class CustomerInformation {
    public String custEmail;
    public String custContact;
    public String custAddress;
    public String custImageUrl;
    public String custUsername;


    public CustomerInformation(String cust_email, String cust_contact, String cust_address){

    }

    public CustomerInformation(String custEmail, String custContact, String custAddress, String custImageUrl, String custUsername) {
        this.custEmail = custEmail;
        this.custContact = custContact;
        this.custAddress = custAddress;
        this.custImageUrl = custImageUrl;
        this.custUsername = custUsername;
    }

    public String getCustEmail() {
        return custEmail;
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
