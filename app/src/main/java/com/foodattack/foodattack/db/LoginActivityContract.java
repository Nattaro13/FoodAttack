package com.foodattack.foodattack.db;

import android.provider.BaseColumns;

/**
 * Created by Xue Hui on 3/7/2015.
 */
public class LoginActivityContract {

    public static final String DB_NAME = "com.foodattack.foodattack.db.LoginActivity";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "LoginActivity";

    public class Columns {
        public static final String USER_ID = "user_id";
        public static final String USER_PASSWORD = "user_password";
        public static final String _ID = BaseColumns._ID;
    }
}
