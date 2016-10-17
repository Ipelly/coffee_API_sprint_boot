package com.xiaoslab.coffee.api.objects;

import java.math.BigDecimal;

/**
 * Created by ipeli on 10/15/16.
 */
public class ItemOption {

    private int itemOptionID;
    private String itemOption;
    private BigDecimal price;
    private Item item;

    public int getItemOptionID() {
        return itemOptionID;
    }

    public void setItemOptionID(int itemOptionID) {
        this.itemOptionID = itemOptionID;
    }

    public String getItemOption() {
        return itemOption;
    }

    public void setItemOption(String itemOption) {
        this.itemOption = itemOption;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
