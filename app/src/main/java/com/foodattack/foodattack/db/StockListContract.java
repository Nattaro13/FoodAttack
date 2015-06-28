package com.foodattack.foodattack.db;

import android.provider.BaseColumns;

public class StockListContract {
    public static final String DB_NAME = "com.foodattack.foodattack.db.StockList";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "ingredients";

    public class Columns {
        public static final String ITEM_NAME = "ingredient";
        public static final String ITEM_QTY = "quantity";
        public static final String ITEM_BRAND = "brand";
        public static final String ITEM_RESTOCK = "restock";

        public static final String _ID = BaseColumns._ID;
    }
}
