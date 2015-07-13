package com.foodattack.foodattack;

import com.parse.ParseClassName;
import com.parse.ParseQuery;

/**
 * ShopListItem is a ParseObject that encapsulates items in the stocklist
 * It extends ShopListItem as it has similar attributes as ShopListItem
 * It only has one additional attribute which is restock
 **/

@ParseClassName("StockListItem")
public class StockListItem extends ShopListItem{

    //getters and setters for itemQty
    // TODO change restock to int when stocklistparse ready
    public String getItemRestock() {
        return getString("itemRestock");
    }

    public void setItemQty(String itemRestock) {
        put("itemRestock", itemRestock);
    }

    public static ParseQuery<StockListItem> getQuery() {
        return ParseQuery.getQuery(StockListItem.class);
    }
}
