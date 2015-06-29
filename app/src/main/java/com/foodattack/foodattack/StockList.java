package com.foodattack.foodattack;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

import com.foodattack.foodattack.db.StockListContract;
import com.foodattack.foodattack.db.StockListDBHelper;


public class StockList extends ActionBarActivity {

    private StockListDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_stock_list);

        SQLiteDatabase sqlDB = new StockListDBHelper(this).getWritableDatabase();
        Cursor cursor = sqlDB.query(StockListContract.TABLE,
                new String[]{StockListContract.Columns.ITEM_NAME},
                null,null,null,null,null);

        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            Log.d("MainActivity cursor",
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    StockListContract.Columns.ITEM_NAME)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_stock_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add an ingredient");
                builder.setMessage("Ingredient Details");

                final AlertDialog alertDialog = builder.create();
                LayoutInflater inflater = alertDialog.getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.popup_stock_list,null);
                builder.setView(dialogLayout);

                //edittext var of input fields
                final EditText rawItemName = (EditText) dialogLayout.findViewById(R.id.stocklist_ingredient_name);
                final EditText rawItemBrand = (EditText) dialogLayout.findViewById(R.id.stocklist_brand);
                final EditText rawItemQty = (EditText) dialogLayout.findViewById(R.id.stocklist_qty);
                final EditText rawItemRestock = (EditText) dialogLayout.findViewById(R.id.stocklist_restock);

                //add button
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Contents of input fields
                        String itemName = rawItemName.getText().toString();
                        String itemBrand = rawItemBrand.getText().toString();
                        String itemQty = rawItemQty.getText().toString();
                        String itemRestock = rawItemRestock.getText().toString();

                        helper = new StockListDBHelper(StockList.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(StockListContract.Columns.ITEM_NAME, itemName);
                        values.put(StockListContract.Columns.ITEM_QTY, itemQty);
                        values.put(StockListContract.Columns.ITEM_BRAND, itemBrand);
                        values.put(StockListContract.Columns.ITEM_RESTOCK, itemRestock);

                        db.insertWithOnConflict(StockListContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        Log.d("StockList",itemName);
                    }
                });

                //cancel button
                builder.setNegativeButton("Cancel",null);

                builder.create().show();
                return true;

            default:
                return false;
        }
    }

/*    private void updateUI() {
        helper = new StockListDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(StockListContract.TABLE,
                new String[]{StockListContract.Columns._ID, StockListContract.Columns.TASK},
                null,null,null,null,null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.stock_list_view,
                cursor,
                new String[] { StockListContract.Columns.ITEM_NAME},
                new int[] { R.id.taskTextView},
                0
        );
        this.setListAdapter(listAdapter);
    } */
}
