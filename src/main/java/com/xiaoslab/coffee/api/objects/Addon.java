package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaoslab.coffee.api.utility.Constants;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by ipeli on 10/15/16.
 */


@Entity
@Table(name = "addon")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Addon {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private long addon_id;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private BigDecimal price;


    @Column(nullable = false)
    private long shop_id;

    @Transient
    private Shop shop;


    @Column(nullable = false)
    private Constants.StatusCodes status;

    //--------------------------Constructor


    public Addon() {
    }

    public Addon(String name, BigDecimal price, Constants.StatusCodes status) {
        this.name = name;
        this.price = price;
        this.status = status;
    }


    //------------------- Getter and Setter

    public long getAddon_id() {
        return addon_id;
    }

    public void setAddon_id(long addon_id) {
        this.addon_id = addon_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price == null ? null : price.setScale(Constants.ITEM_PRICE_SCALE, BigDecimal.ROUND_HALF_DOWN);
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? null : price.setScale(Constants.ITEM_PRICE_SCALE, BigDecimal.ROUND_HALF_DOWN);
    }

    public long getShop_id() {
        return shop_id;
    }

    public void setShop_id(long shop_id) {
        this.shop_id = shop_id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Constants.StatusCodes getStatus() {
        return status;
    }

    public void setStatus(Constants.StatusCodes status) {
        this.status = status;
    }
}
