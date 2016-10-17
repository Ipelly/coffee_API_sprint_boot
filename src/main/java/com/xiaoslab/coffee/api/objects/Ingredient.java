package com.xiaoslab.coffee.api.objects;

import java.math.BigDecimal;

/**
 * Created by ipeli on 10/15/16.
 */
public class Ingredient {

    private int ingredientID;
    private String name;
    private BigDecimal price;

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
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
}
