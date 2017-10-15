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
    public  String prodCategory;
    public  String prodID;



    public IndividualOrderInfo() {

    }


    public IndividualOrderInfo(String orderQuantity, String prodName, String prodPrice, String orderItemID, String prodImageURL, String subTotal,String prodCategry,String prodID) {
        this.orderQuantity = orderQuantity;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.orderItemID = orderItemID;
        this.prodImageURL = prodImageURL;
        this.subTotal = subTotal;
        this.prodCategory = prodCategry;
        this.prodID = prodID;
    }

    public String getProdCategory() {
        return prodCategory;
    }

    public String getProdID() {

        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
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

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public void setOrderItemID(String orderItemID) {
        this.orderItemID = orderItemID;
    }

    public void setProdImageURL(String prodImageURL) {
        this.prodImageURL = prodImageURL;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public void setProdCategory(String prodCategory) {
        this.prodCategory = prodCategory;
    }
}
