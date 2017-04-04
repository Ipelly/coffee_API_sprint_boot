package com.xiaoslab.coffee.api.objects;

import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.*;
import java.util.Objects;

/**
 * Created by islamma on 10/31/16.
 */

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private long itemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private long shopId;

    @ElementCollection
    @CollectionTable(name="item_category", joinColumns=@JoinColumn(name="item_id"))
    @Column(name="category_id")
    private Set<Long> categoryIds = new HashSet<>();

    @Column(nullable = false)
    private Constants.StatusCodes status;

    public Item() {

    }

    public Item(String name, String description, BigDecimal price, long shopId, Constants.StatusCodes status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.shopId = shopId;
        this.status = status;
    }

    public Item(String name, String description, BigDecimal price, long shopId, long categoryId, Constants.StatusCodes status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.shopId = shopId;
        this.status = status;
        this.categoryIds = new HashSet<>(Arrays.asList(categoryId));
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
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
        return shopId;
    }

    public void setShopId(long shop_id) {
        this.shopId = shop_id;
    }

    public Constants.StatusCodes getStatus() {
        return status;
    }

    public void setStatus(Constants.StatusCodes status) {
        this.status = status;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
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
        return itemId == item.itemId &&
                shopId == item.shopId &&
                Objects.equals(name, item.name) &&
                Objects.equals(description, item.description) &&
                Objects.equals(price, item.price) &&
                Objects.equals(categoryIds, item.categoryIds) &&
                status == item.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, name, description, price, shopId, categoryIds, status);
    }

}
