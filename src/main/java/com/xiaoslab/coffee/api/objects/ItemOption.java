package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by ipeli on 10/15/16.
 */


@Entity
@Table(name = "item_option")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemOption {

    @Id
    @GeneratedValue
    @Column(name ="item_option_id", unique = true)
    private long itemOptionId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    //@Transient
    @Column(name = "item_id", nullable = false)
    private long itemId;

    @Transient
    //@ManyToOne
    //@JoinColumn(name = "item_id")
    private Item item;

    // Default constructor
    public ItemOption(){

    }
    // Optional Constructor
    public ItemOption(String name, BigDecimal price, Constants.StatusCodes status) {
        this.name = name;
        this.price = price;
        this.status = status;
    }

    @Column(nullable = false)
    private Constants.StatusCodes status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemOption itemOption = (ItemOption) o;
        return itemOptionId == itemOption.itemOptionId &&
                Objects.equals(name, itemOption.name) &&
                Objects.equals(price, itemOption.price) &&
                Objects.equals(itemId, itemOption.itemId) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemOptionId, name, price, itemId, status);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

    public long getItemOptionId() {
        return itemOptionId;
    }

    public void setItemOptionId(long itemOptionId) {
        this.itemOptionId = itemOptionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price == null ? null : price.setScale(Constants.ITEM_PRICE_SCALE, BigDecimal.ROUND_HALF_DOWN);
    }


    public void setPrice(BigDecimal price) {
        this.price = price == null ? null : price.setScale(Constants.ITEM_PRICE_SCALE, BigDecimal.ROUND_HALF_DOWN);
    }

    //@ManyToOne
    //@JoinColumn(name = "item_id")
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Constants.StatusCodes getStatus() {
        return status;
    }

    public void setStatus(Constants.StatusCodes status) {
        this.status = status;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
}
