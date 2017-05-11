package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by ipeli on 10/15/16.
 */

@Entity @IdClass(ItemOption.ItemOptionId.class)
@Table(name = "item_option")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemOption {

    // composite key for item-option
    static class ItemOptionId implements Serializable {
        long itemId;
        String name;
    }

    @Id
    @Column(nullable = false)
    @NotNull
    private long itemId;

    @Id
    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private BigDecimal price;

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
        return Objects.equals(name, itemOption.name) &&
                Objects.equals(price, itemOption.price) &&
                Objects.equals(itemId, itemOption.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, itemId, status);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
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
