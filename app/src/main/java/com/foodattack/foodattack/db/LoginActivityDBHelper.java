package com.foodattack.foodattack.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Xue Hui on 3/7/2015.
 */
public class LoginActivityDBHelper extends SQLiteOpenHelper {

    //Constructor
    public LoginActivityDBHelper(Context context) {
        super(context, LoginActivityContract.DB_NAME, null, LoginActivityContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT, %s TEXT)", LoginActivityContract.TABLE,
                        LoginActivityContract.Columns.USER_ID,
                        LoginActivityContract.Columns.USER_PASSWORD);

        Log.d("LoginActivityDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVer, int newVer) {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+LoginActivityContract.TABLE);
        onCreate(sqlDB);
    }
}
