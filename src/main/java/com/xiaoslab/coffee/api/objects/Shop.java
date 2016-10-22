package com.xiaoslab.coffee.api.objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
/**
 * Created by ipeli on 10/1/16.
 */


@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private long shopID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address1;

    @Column
    private String address2;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private String phone;

    @Column
    private BigDecimal longitute;

    @Column
    private BigDecimal latitude;

    @Column
    private int rating;

    @Column(columnDefinition="TINYINT(1) default true")
    private boolean status;

    @Override
    public String toString() {
//        return "Shop{" +
//                "shopID=" + shopID +
//                ", name='" + name + '\'' +
//                ", address1='" + address1 + '\'' +
//                ", address2='" + address2 + '\'' +
//                ", city='" + city + '\'' +
//                ", state='" + state + '\'' +
//                ", zip='" + zip + '\'' +
//                ", phone='" + phone + '\'' +
//                ", longitute=" + longitute +
//                ", latitude=" + latitude +
//                ", rating=" + rating +
//                '}';

        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(shopID)
                .append(name)
                .append(address1)
                .append(address2)
                .append(phone)
                .append(city)
                .append(state)
                .append(zip)
                .append(rating)
                .append(longitute)
                .append(latitude).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Shop shop = (Shop) obj;

        return new EqualsBuilder()
                .append(shopID, shop.shopID)
                .append(name, shop.name)
                .append(address1, shop.address1)
                .append(address2, shop.address2)
                .append(phone, shop.phone)
                .append(city, shop.city)
                .append(state, shop.state)
                .append(zip, shop.zip)
                .append(rating, shop.zip)
                .append(longitute, shop.zip)
                .append(latitude, shop.zip)
                .isEquals();
    }

    public long getShopID() {
        return shopID;
    }

    public void setShopID(long shopID) {
        this.shopID = shopID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getLongitute() {
        return longitute;
    }

    public void setLongitute(BigDecimal longitute) {
        this.longitute = longitute;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
