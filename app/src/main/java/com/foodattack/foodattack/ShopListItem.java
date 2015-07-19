package com.foodattack.foodattack;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * ShopListItem is a ParseObject that encapsulates items in the shopping list
 **/

@ParseClassName("ShopListItem")
public class ShopListItem extends ParseObject {

    /**
     * Constructor
     **/
    public ShopListItem(){
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
    //TODO change qty to double when shoplistparse ready
    public String getItemQty() {
        return getString("itemQty");
    }

    public void setItemQty(String itemQty) {
        put("itemQty", itemQty);
    }

    /**
     * getters and setters for itemFamilyID
     **/
    public String getItemFamilyID(){
        return getString("itemFamilyID");
    }
    public void setItemFamilyID(String itemFamilyID){
        put("itemFamilyID", itemFamilyID);
    }
}
