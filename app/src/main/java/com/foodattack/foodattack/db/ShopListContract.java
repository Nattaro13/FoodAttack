package com.foodattack.foodattack.db;


import android.provider.BaseColumns;


/**
 * Created by Xue Hui on 28/6/2015.
 */
public class ShopListContract {

    public static final String DB_NAME = "com.foodattack.foodattack.db.ShopList";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "ShopList";

    public class Columns {
        public static final String ITEM_NAME = "item";
        public static final String ITEM_BRAND = "brand";
        public static final String ITEM_QTY = "quantity";
        public static final String _ID = BaseColumns._ID;
    }

}
