package com.example.hendryshanedeguia.freshcartcustomer;

/**
 * Created by HendryShanedeGuia on 29/08/2017.
 */

public class IndividualOrderInfo {
    public String orderQuantity;
    public String prodName;
    public String prodPrice;
    public String orderItemID;
    public String prodImageURL;
    public String subTotal;



    public IndividualOrderInfo() {

    }


    public IndividualOrderInfo(String orderQuantity, String prodName, String prodPrice, String orderItemID, String prodImageURL, String subTotal) {
        this.orderQuantity = orderQuantity;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.orderItemID = orderItemID;
        this.prodImageURL = prodImageURL;
        this.subTotal = subTotal;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public String getOrderItemID() {
        return orderItemID;
    }

    public String getProdImageURL() {
        return prodImageURL;
    }

    public String getSubTotal() {
        return subTotal;
    }
}
