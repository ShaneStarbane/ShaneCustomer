package com.example.hendryshanedeguia.freshcartcustomer;

import android.app.ProgressDialog;

/**
 * Created by HendryShanedeGuia on 28/08/2017.
 */

public class ProductInformation {

    public String prodName;
    public String prodPrice;
    public String prodDes;
    public String imageURL;
    public String prodID;
    public String prodCategory;


    public ProductInformation(){

    }

    public ProductInformation(String prodName, String prodPrice, String prodDes, String imageURL,String prodID,String prodCategory) {
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.prodDes = prodDes;
        this.imageURL = imageURL;
        this.prodID =  prodID;
        this.prodCategory = prodCategory;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public String getProdDes() {
        return prodDes;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getProdID() {
        return prodID;
    }

    public String getProdCategory() {
        return prodCategory;
    }
}
