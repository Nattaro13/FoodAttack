package com.foodattack.foodattack;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.foodattack.foodattack.db.StockListContract;
import com.foodattack.foodattack.db.StockListDBHelper;


public class StockList extends ListActivity {

    private StockListDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        //display the Stock List based on what is in the database
        updateUI();
    }

    //update contents and display in list
    private void updateUI() {
        helper = new StockListDBHelper(StockList.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();

        Cursor cursor = sqlDB.query(StockListContract.TABLE,
                new String[]{StockListContract.Columns._ID,
                        StockListContract.Columns.ITEM_NAME,
                        StockListContract.Columns.ITEM_BRAND,
                        StockListContract.Columns.ITEM_QTY,
                        StockListContract.Columns.ITEM_RESTOCK},
                null, null, null, null, StockListContract.Columns.ITEM_NAME+" ASC");

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.stock_list_view,
                cursor,
                new String[] { StockListContract.Columns.ITEM_NAME, StockListContract.Columns.ITEM_QTY},
                new int[] { R.id.stocklist_itemNameView, R.id.stocklist_itemQtyView },
                0
        );
        this.setListAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_list, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_stock_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add an ingredient");

                final AlertDialog alertDialog = builder.create();
                LayoutInflater inflater = alertDialog.getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.dialog_stock_list,null);
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

                        Log.d("StockList", itemName);
                        updateUI();
                    }
                });

                //cancel button
                builder.setNegativeButton("Cancel",null);

                builder.create().show();
                return true;

            case R.id.action_search:
                Log.d("ShopList", "Search for an item in the shopping list");
                onSearchRequested();
                return true;

            default:
                return false;
        }
    }

    public void onDelButtonClick(View view) {
        View v = (View) view.getParent();
        Button itemNameButton = (Button) v.findViewById(R.id.stocklist_itemNameView);

        String itemName = itemNameButton.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                StockListContract.TABLE,
                StockListContract.Columns.ITEM_NAME,
                itemName);


        helper = new StockListDBHelper(StockList.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }

    public void editOnClick(View view){
        View v = (View) view.getParent();
        Button oldItemNameButton = (Button) v.findViewById(R.id.stocklist_itemNameView);

        final String oldItemName = oldItemNameButton.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit an ingredient's details");

        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_stock_list,null);
        builder.setView(dialogLayout);

        //edittext var of input fields
        final EditText rawItemName = (EditText) dialogLayout.findViewById(R.id.stocklist_ingredient_name);
        final EditText rawItemBrand = (EditText) dialogLayout.findViewById(R.id.stocklist_brand);
        final EditText rawItemQty = (EditText) dialogLayout.findViewById(R.id.stocklist_qty);
        final EditText rawItemRestock = (EditText) dialogLayout.findViewById(R.id.stocklist_restock);

        helper = new StockListDBHelper(StockList.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(StockListContract.TABLE,
                new String[]{StockListContract.Columns._ID,
                        StockListContract.Columns.ITEM_NAME,
                        StockListContract.Columns.ITEM_BRAND,
                        StockListContract.Columns.ITEM_QTY,
                        StockListContract.Columns.ITEM_RESTOCK},
                String.format("%s = ? ", StockListContract.Columns.ITEM_NAME),
                new String[]{oldItemName},
                null, null, null, null);

        String oldBrand = "oldBrand";
        String oldQty = "oldQty";
        String oldRestock = "oldRestock";
        if(cursor.moveToFirst()){
            int brandColIndex = cursor.getColumnIndex(StockListContract.Columns.ITEM_BRAND);
            oldBrand = cursor.getString(brandColIndex);

            int qtyColIndex = cursor.getColumnIndex(StockListContract.Columns.ITEM_QTY);
            oldQty = cursor.getString(qtyColIndex);

            int restockColIndex = cursor.getColumnIndex(StockListContract.Columns.ITEM_RESTOCK);
            oldRestock = cursor.getString(restockColIndex);
        }


        //String oldBrand = cursor.getString(brandColIndex);

        //set text in input fields to old details
        rawItemName.setText(oldItemName);
        // TODO edit set text arguments for edit dialog in stocklist
        rawItemBrand.setText(oldBrand);
        rawItemQty.setText(oldQty);
        rawItemRestock.setText(oldRestock);

        //add button
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Contents of input fields
                String itemName = rawItemName.getText().toString();
                String itemBrand = rawItemBrand.getText().toString();
                String itemQty = rawItemQty.getText().toString();
                String itemRestock = rawItemRestock.getText().toString();

                //update SQL statement
                String sqlUpdate = String.format("UPDATE %s SET %s = '%s', %s = '%s', %s = '%s', %s = '%s' WHERE %s = '%s'",
                        StockListContract.TABLE,
                        StockListContract.Columns.ITEM_NAME, itemName,
                        StockListContract.Columns.ITEM_BRAND, itemBrand,
                        StockListContract.Columns.ITEM_QTY, itemQty,
                        StockListContract.Columns.ITEM_RESTOCK, itemRestock,
                        StockListContract.Columns.ITEM_NAME, oldItemName);

                helper = new StockListDBHelper(StockList.this);
                SQLiteDatabase sqlDB = helper.getWritableDatabase();
                sqlDB.execSQL(sqlUpdate);
                Log.d("Edit StockList", itemName);
                updateUI();
            }
        });

        //cancel button
        builder.setNegativeButton("Cancel",null);

        builder.create().show();

    }
}
