package com.foodattack.foodattack.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StockListDBHelper extends SQLiteOpenHelper {

    public StockListDBHelper(Context context) {
        super(context, StockListContract.DB_NAME, null, StockListContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT, %s TEXT, %s TEXT, %s TEXT )", StockListContract.TABLE,
                                StockListContract.Columns.ITEM_NAME,StockListContract.Columns.ITEM_BRAND,
                                StockListContract.Columns.ITEM_QTY,StockListContract.Columns.ITEM_RESTOCK);

        Log.d("TaskDBHelper","Query to form table: "+sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + StockListContract.TABLE);
        onCreate(sqlDB);
    }

}
