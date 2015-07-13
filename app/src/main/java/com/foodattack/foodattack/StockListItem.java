package com.foodattack.foodattack;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.UUID;

/**
 * ShopListItem is a ParseObject that encapsulates items in the stocklist
 **/

@ParseClassName("StockListItem")
public class StockListItem extends ParseObject {

    //getters and setters for itemName
    public String getItemName() {
        return getString("itemName");
    }

    public void setItemName(String itemName) {
        put("itemName", itemName);
    }

    //getters and setters for itemBrand
    public String getItemBrand() {
        return getString("itemBrand");
    }

    public void setItemBrand(String itemBrand) {
        put("itemBrand", itemBrand);
    }

    //getters and setters for itemQty
    // TODO change qty to double when stocklistparse ready
    public String getItemQty() {
        return getString("itemQty");
    }

    public void setItemQty(String itemQty) {
        put("itemQty", itemQty);
    }

    //getters and setters for itemRestock
    // TODO change restock to int when stocklistparse ready
    public String getItemRestock() {
        return getString("itemRestock");
    }

    public void setItemRestock(String itemRestock) {
        put("itemRestock", itemRestock);
    }

    //TODO what is uuid ?_?
    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put("uuid", uuid.toString());
    }

    public String getUuidString() {
        return getString("uuid");
    }

    public static ParseQuery<StockListItem> getQuery() {
        return ParseQuery.getQuery(StockListItem.class);
    }
}
