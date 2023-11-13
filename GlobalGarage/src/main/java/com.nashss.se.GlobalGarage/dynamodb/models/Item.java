package com.nashss.se.GlobalGarage.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.nashss.se.GlobalGarage.converters.LocalDateTimeConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.Objects;

@DynamoDBTable(tableName = "items")
public class Item {
    private String garageID;
    private String itemID;
    private String sellerID;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Set<String> images;
    private LocalDateTime dateListed;
    private Set<String> buyersInterested;
    private String status;

    @DynamoDBHashKey(attributeName = "garageID")
    public String getGarageID() {
        return garageID;
    }

    public void setGarageID(String garageID) {
        this.garageID = garageID;
    }

    @DynamoDBRangeKey(attributeName = "itemID")
    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    @DynamoDBAttribute(attributeName = "sellerID")
    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @DynamoDBAttribute(attributeName = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @DynamoDBAttribute(attributeName = "images")
    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "dateListed")
    public LocalDateTime getDateListed() {
        return dateListed;
    }

    public void setDateListed(LocalDateTime dateListed) {
        this.dateListed = dateListed;
    }

    @DynamoDBAttribute(attributeName = "buyersInterested")
    public Set<String> getBuyersInterested() {
        return buyersInterested;
    }

    public void setBuyersInterested(Set<String> buyersInterested) {
        this.buyersInterested = buyersInterested;
    }

    @DynamoDBAttribute(attributeName = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // equals, hashCode, and toString methods


    @Override
    public String toString() {
        return "Item{" +
                "garageID='" + garageID + '\'' +
                ", itemID='" + itemID + '\'' +
                ", sellerID='" + sellerID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", images=" + images +
                ", dateListed=" + dateListed +
                ", buyersInterested=" + buyersInterested +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(garageID, item.garageID) && Objects.equals(itemID, item.itemID) && Objects.equals(sellerID, item.sellerID) && Objects.equals(name, item.name) && Objects.equals(description, item.description) && Objects.equals(price, item.price) && Objects.equals(category, item.category) && Objects.equals(images, item.images) && Objects.equals(dateListed, item.dateListed) && Objects.equals(buyersInterested, item.buyersInterested) && Objects.equals(status, item.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(garageID, itemID, sellerID, name, description, price, category, images, dateListed, buyersInterested, status);
    }

}
