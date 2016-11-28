package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by ipeli on 10/15/16.
 */


@Entity
@Table(name = "addon")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Addon {

    @Id
    @GeneratedValue
    @Column(unique = true, name = "addon_id")
    private long addonId;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private BigDecimal price;


    @Column(nullable = false, name = "shop_id")
    private long shopId;

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

    //------------------------- Equals, HasCode and toString

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Addon addon = (Addon) o;
        return addonId == addon.addonId &&
                shopId == addon.shopId &&
                Objects.equals(name, addon.name) &&
                Objects.equals(price, addon.price) &&
                Objects.equals(shop, addon.shop) &&
                status == addon.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(addonId, name, price, shopId, shop, status);
    }

    //------------------- Getter and Setter

    public long getAddonId() {
        return addonId;
    }

    public void setAddonId(long addon_id) {
        this.addonId = addon_id;
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

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shop_id) {
        this.shopId = shop_id;
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
