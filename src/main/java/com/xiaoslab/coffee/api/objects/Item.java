package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.xiaoslab.coffee.api.utility.Constants;

import javax.persistence.*;
import java.math.BigDecimal;

import java.util.*;

/**
 * Created by islamma on 10/31/16.
 */

@Entity
@Table(name = "item")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Item {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private long item_id;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String description;


    @Column(nullable = false)
    private BigDecimal price;


    @Column(nullable = false)
    private long shop_id;

    @Transient
    private Shop shop;

    @Transient
    private List<ItemAddon> itemAddonList;

    @Column(nullable = false)
    private Constants.StatusCodes status;

    public long getItemId() {
        return item_id;
    }

    public void setItemId(long item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getShopId() {
        return shop_id;
    }

    public void setShopId(long shop_id) {
        this.shop_id = shop_id;
    }

    public Constants.StatusCodes getStatus() {
        return status;
    }

    public void setStatus(Constants.StatusCodes status) {
        this.status = status;
    }
}
