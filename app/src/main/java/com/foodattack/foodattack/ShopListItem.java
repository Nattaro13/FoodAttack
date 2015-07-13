package com.foodattack.foodattack;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.UUID;

/**
 * ShopListItem is a ParseObject that encapsulates items in the shopping list
 **/

@ParseClassName("ShopListItem")
public class ShopListItem extends ParseObject {

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
    // TODO change qty to double when shoplistparse ready
    public String getItemQty() {
        return getString("itemQty");
    }

    public void setItemQty(String itemQty) {
        put("itemQty", itemQty);
    }

    //getters and setters for author
    // TODO not sure if needed; remove if not required
    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser currentUser) {
        put("author", currentUser);
    }

    //TODO what is uuid ?_?
    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put("uuid", uuid.toString());
    }

    public String getUuidString() {
        return getString("uuid");
    }

    public static ParseQuery<ShopListItem> getQuery() {
        return ParseQuery.getQuery(ShopListItem.class);
    }

}
