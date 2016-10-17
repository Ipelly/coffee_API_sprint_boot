package com.xiaoslab.coffee.api.objects;

/**
 * Created by ipeli on 10/15/16.
 */
public class ItemIngredient {

    private Item item;
    private Ingredient ingredient;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
