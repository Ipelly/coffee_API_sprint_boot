package com.xiaoslab.coffee.api.objects;

import java.math.BigDecimal;

/**
 * Created by ipeli on 10/1/16.
 */

public class Shop {

    private int ShopID;
    private String Name;
    private String Address1;
    private String Address2;
    private String City;
    private String State;
    private String Zip;
    private String Phone;
    private BigDecimal Longitute;
    private BigDecimal latitude;
    private int Rating;

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int shopID) {
        ShopID = shopID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public BigDecimal getLongitute() {
        return Longitute;
    }

    public void setLongitute(BigDecimal longitute) {
        Longitute = longitute;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }
}
