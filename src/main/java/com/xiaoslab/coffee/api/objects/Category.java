package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaoslab.coffee.api.utility.Constants;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Created by Iqbal on 01/29/17.
 */

@Entity
@Table(name = "category")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private long category_id;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String description;


    @Column(nullable = false)
    private long shop_id;

    @Transient
    private Shop shop;

    @Transient
    private List<Item> items;

    @Column(nullable = false)
    private Constants.StatusCodes status;

    public Category(){

    }

    public Category(String name, String description, long shop_id) {
        this.name = name;
        this.description = description;
        this.shop_id = shop_id;
        this.status = Constants.StatusCodes.ACTIVE;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
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

        return category_id == category.category_id &&
                shop_id == category.shop_id &&
                Objects.equals(name, category.name) &&
                Objects.equals(description, category.description) &&
                status == category.status;


    }

    @Override
    public int hashCode() {
        int result = (int) (category_id ^ (category_id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (int) (shop_id ^ (shop_id >>> 32));
        result = 31 * result + shop.hashCode();
        result = 31 * result + items.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category_id=" + category_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", shop_id=" + shop_id +
                ", shop=" + shop +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}
