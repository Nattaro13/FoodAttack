package com.foodattack.foodattack;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.foodattack.foodattack.db.ShopListDBHelper;
import com.foodattack.foodattack.db.ShopListContract;

/**
 * Created by Xue Hui on 29/6/2015.
 */
public class SearchShopList extends ListActivity {
    private ShopListDBHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        Log.d("Entered Searchable","Searchable");
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            showResults(query);
        }
    }

    private void showResults(String query) {
        // Query your data set and show results
        helper = new ShopListDBHelper(SearchShopList.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();

        String whereClause = String.format("%s = ?",ShopListContract.Columns.ITEM_NAME);
        //Cursor cursor = sqLiteDatabase.query(
        //tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        Cursor cursor = sqlDB.query(ShopListContract.TABLE,
                new String[]{ShopListContract.Columns._ID,
                        ShopListContract.Columns.ITEM_NAME,
                        ShopListContract.Columns.ITEM_QTY},
                whereClause, new String[]{query}, null, null, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.shop_list_view,
                cursor,
                new String[] { ShopListContract.Columns.ITEM_NAME,
                        ShopListContract.Columns.ITEM_QTY},
                new int[] { R.id.ShopList_ItemName_View,R.id.ShopList_ItemQty_View},
                0
        );
        this.setListAdapter(listAdapter);
    }

}
