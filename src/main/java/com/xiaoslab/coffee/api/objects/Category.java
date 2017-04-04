package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
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
    @Column(unique = true)
    private long categoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private long shopId;

    @ElementCollection
    @CollectionTable(name="item_category", joinColumns=@JoinColumn(name="category_id"))
    @Column(name="item_id")
    private Set<Long> itemIds = new HashSet<>();

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

    public Constants.StatusCodes getStatus() {
        return status;
    }

    public void setStatus(Constants.StatusCodes status) {
        this.status = status;
    }

    public Set<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(Set<Long> itemIds) {
        this.itemIds = itemIds;
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
                Objects.equals(itemIds, category.itemIds) &&
                status == category.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, name, description, shopId, itemIds, status);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

}
