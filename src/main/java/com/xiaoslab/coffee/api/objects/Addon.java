package com.xiaoslab.coffee.api.objects;

import java.math.BigDecimal;

/**
 * Created by ipeli on 10/15/16.
 */
public class Addon {
    private int addonID;
    private String name;
    private BigDecimal price;
    private Shop shop;

    public int getAddonID() {
        return addonID;
    }

    public void setAddonID(int addonID) {
        this.addonID = addonID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
