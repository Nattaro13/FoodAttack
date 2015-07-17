package com.foodattack.foodattack;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * ShopListItem is a ParseObject that encapsulates items in the stocklist
 **/

@ParseClassName("StockListItem")
public class StockListItem extends ParseObject {

    /**
     * constructor
     **/
    public StockListItem(){
    }

    /**
     * getters and setters for itemName
     **/
    public String getItemName() {
        return getString("itemName");
    }

    public void setItemName(String itemName) {
        put("itemName", itemName);
    }

    /**
     * getters and setters for itemBrand
     **/
    public String getItemBrand() {
        return getString("itemBrand");
    }

    public void setItemBrand(String itemBrand) {
        put("itemBrand", itemBrand);
    }

    /**
     * getters and setters for itemQty
     **/
    //TODO change qty to double when stocklistparse ready
    public String getItemQty() {
        return getString("itemQty");
    }

    public void setItemQty(String itemQty) {
        put("itemQty", itemQty);
    }

    /**
     * getters and setters for itemRestock
     **/
    //TODO change restock to int when stocklistparse ready
    public String getItemRestock() {
        return getString("itemRestock");
    }

    public void setItemRestock(String itemRestock) {
        put("itemRestock", itemRestock);
    }

    /**
     * getters and setters for itemFamily
     **/
    public String getItemFamilyID(){
        return getString("itemfamilyID");
    }
    public void setItemFamilyID(String itemFamilyID){
        put("itemFamilyID", itemFamilyID);
    }

}
