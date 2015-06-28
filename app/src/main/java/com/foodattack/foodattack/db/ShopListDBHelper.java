package com.foodattack.foodattack.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Xue Hui on 28/6/2015.
 */
public class ShopListDBHelper extends SQLiteOpenHelper {

    //Constructor
    public ShopListDBHelper(Context context) {
        super(context, ShopListContract.DB_NAME, null, ShopListContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT, %s TEXT, %s TEXT)", ShopListContract.TABLE,
                        ShopListContract.Columns.ITEM_NAME,
                        ShopListContract.Columns.ITEM_BRAND,
                        ShopListContract.Columns.ITEM_QTY);

        Log.d("TaskDBHelper","Query to form table: "+sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVer, int newVer) {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+ShopListContract.TABLE);
        onCreate(sqlDB);
    }
}
