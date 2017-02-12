package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    @Column(nullable = false)
    private long category_id;

    @Transient
    private Shop shop;

    @Transient
    private List<ItemAddon> itemAddonList;

    @Column(nullable = false)
    private Constants.StatusCodes status;

    public Item() {
    }

    public Item(String name, String description, BigDecimal price, long shop_id, long category_id, Constants.StatusCodes status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.shop_id = shop_id;
        this.category_id = category_id;
        this.status = status;
    }

    public long getitem_id() {
        return item_id;
    }

    public void setitem_id(long item_id) {
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
        return price == null ? null : price.setScale(Constants.ITEM_PRICE_SCALE, BigDecimal.ROUND_HALF_DOWN);
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? null : price.setScale(Constants.ITEM_PRICE_SCALE, BigDecimal.ROUND_HALF_DOWN);;
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


    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return item_id == item.item_id &&
                shop_id == item.shop_id &&
                category_id == item.category_id &&
                Objects.equals(name, item.name) &&
                Objects.equals(description, item.description) &&
                Objects.equals(price, item.price) &&
                status == item.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item_id, name, description, price, shop_id, category_id, status);
    }
}
