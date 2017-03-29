package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaoslab.coffee.api.utility.Constants;

import javax.persistence.*;
import java.util.*;
import java.util.Objects;

/**
 * Created by Iqbal on 01/29/17.
 */

@Entity
@Table(name = "category")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category{

    @Id
    @GeneratedValue
    @Column(unique = true, name="category_id")
    private long categoryId;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name="shop_id")
    private long shopId;

    //@Transient
    @ManyToMany(mappedBy = "categories")
    private List<Item> items;

    @Column(nullable = false)
    private Constants.StatusCodes status;

    public Category(){

    }

    public Category(String name, String description, long shopId) {
        this.name = name;
        this.description = description;
        this.shopId = shopId;
        this.status = Constants.StatusCodes.ACTIVE;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }
//
//    public Shop getShop() {
//        return shop;
//    }
//
//    public void setShop(Shop shop) {
//        this.shop = shop;
//    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Constants.StatusCodes getStatus() {
        return status;
    }

    public void setStatus(Constants.StatusCodes status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;

        return categoryId == category.categoryId &&
                shopId == category.shopId &&
                Objects.equals(name, category.name) &&
                Objects.equals(description, category.description) &&
                status == category.status;


    }

    @Override
    public int hashCode() {
        int result = (int) (categoryId ^ (categoryId >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (int) (shopId ^ (shopId >>> 32));
        result = 31 * result + items.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category_id=" + categoryId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", shop_id=" + shopId +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}
