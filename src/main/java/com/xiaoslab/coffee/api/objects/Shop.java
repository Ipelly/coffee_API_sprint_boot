package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by ipeli on 10/1/16.
 */


@Entity
@Table(name = "shop")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shop implements Serializable {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private long shopId;

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

    @Column(precision = Constants.LAT_LONG_PRECISION, scale = Constants.LAT_LONG_SCALE)
    private BigDecimal longitude;

    @Column(precision = Constants.LAT_LONG_PRECISION, scale = Constants.LAT_LONG_SCALE)
    private BigDecimal latitude;

    @Column
    private int rating;

    @Column(nullable = false)
    private Constants.StatusCodes status;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopId, name, address1, address2, city, state, zip, phone, longitude, latitude, rating, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shop shop = (Shop) o;
        return shopId == shop.shopId &&
                rating == shop.rating &&
                status == shop.status &&
                Objects.equals(name, shop.name) &&
                Objects.equals(address1, shop.address1) &&
                Objects.equals(address2, shop.address2) &&
                Objects.equals(city, shop.city) &&
                Objects.equals(state, shop.state) &&
                Objects.equals(zip, shop.zip) &&
                Objects.equals(phone, shop.phone) &&
                Objects.equals(longitude, shop.longitude) &&
                Objects.equals(latitude, shop.latitude);
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
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

    public BigDecimal getLongitude() {
        return longitude.setScale(Constants.LAT_LONG_SCALE, BigDecimal.ROUND_HALF_DOWN);
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude.setScale(Constants.LAT_LONG_SCALE, BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getLatitude() {
        return latitude.setScale(Constants.LAT_LONG_SCALE, BigDecimal.ROUND_HALF_DOWN);
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude.setScale(Constants.LAT_LONG_SCALE, BigDecimal.ROUND_HALF_DOWN);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Constants.StatusCodes getStatus() {
        return status;
    }

    public void setStatus(Constants.StatusCodes status) {
        this.status = status;
    }
}
