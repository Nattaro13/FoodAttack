package com.foodattack.foodattack.db;

import android.provider.BaseColumns;

public class StockListContract {

    public class TaskContract {
        public static final String DB_NAME = "com.foodattack.foodattack.db.StockList";
        public static final int DB_VERSION = 1;
        public static final String TABLE = "ingredients";

        public class Columns {
            public static final String ITEM = "ingredient";
            public static final String _ID = BaseColumns._ID;
        }

}
