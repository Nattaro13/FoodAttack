package com.foodattack.foodattack;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

import com.foodattack.foodattack.db.ShopListDBHelper;
import com.foodattack.foodattack.db.ShopListContract;

/**
 * Created by Xue Hui on 29/6/2015.
 */
public class SearchShopList extends ListActivity {
    private ShopListDBHelper helper;
    private String queryUpdated;

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

        queryUpdated = query;

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
                R.layout.list_item_shoplist,
                cursor,
                new String[] { ShopListContract.Columns.ITEM_NAME,
                        ShopListContract.Columns.ITEM_QTY},
                new int[] { R.id.shoplist_itemNameView,R.id.shoplist_itemQtyView},
                0
        );
        this.setListAdapter(listAdapter);

    }


    /*
    When the remove button is pressed on the UI, this method is executed.
     */
    public void onShopListDeleteButtonClick(View view) {
        View v = (View) view.getParent();
        //get the item name
        Button itemNameButton = (Button) v.findViewById(R.id.shoplist_itemNameView);
        //convert item name to string
        String itemName = itemNameButton.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                ShopListContract.TABLE,
                ShopListContract.Columns.ITEM_NAME,
                itemName);


        helper = new ShopListDBHelper(SearchShopList.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }


    /*
    Updates the main UI when any item is added or deleted from the database
     */
    private void updateUI() {
        helper = new ShopListDBHelper(SearchShopList.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        //format of an SQlite query
        //Cursor cursor = sqLiteDatabase.query(
        //tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        String whereClause = String.format("%s = ?",ShopListContract.Columns.ITEM_NAME);
        //Cursor cursor = sqLiteDatabase.query(
        //tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        Cursor cursor = sqlDB.query(ShopListContract.TABLE,
                new String[]{ShopListContract.Columns._ID,
                        ShopListContract.Columns.ITEM_NAME,
                        ShopListContract.Columns.ITEM_QTY},
                whereClause, new String[]{queryUpdated}, null, null, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item_shoplist,
                cursor,
                new String[] { ShopListContract.Columns.ITEM_NAME,
                        ShopListContract.Columns.ITEM_QTY},
                new int[] { R.id.shoplist_itemNameView,R.id.shoplist_itemQtyView},
                0
        );
        this.setListAdapter(listAdapter);
    }


    /*
    When an item on the shooping list is tapped, this method is executed,
    so that people can change the details of the item.
     */
    public void editOnClick(View view){
        View v = (View) view.getParent();
        Button oldItemNameButton = (Button) v.findViewById(R.id.shoplist_itemNameView);

        final String oldItemName = oldItemNameButton.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit an ingredient's details");

        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_shop_list,null);
        builder.setView(dialogLayout);

        //edittext var of input fields
        final EditText rawItemName = (EditText) dialogLayout.findViewById(R.id.shoplist_item_name);
        final EditText rawItemBrand = (EditText) dialogLayout.findViewById(R.id.shoplist_item_brand);
        final EditText rawItemQty = (EditText) dialogLayout.findViewById(R.id.shoplist_quantity);

        helper = new ShopListDBHelper(SearchShopList.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(ShopListContract.TABLE,
                new String[]{ShopListContract.Columns.ITEM_NAME,
                        ShopListContract.Columns.ITEM_BRAND,
                        ShopListContract.Columns.ITEM_QTY,},
                String.format("%s = ? ", ShopListContract.Columns.ITEM_NAME),
                new String[]{oldItemName},
                null, null, null, null);

        String oldBrand = "oldBrand";
        String oldQty = "oldQty";
        if(cursor.moveToFirst()){
            int brandColIndex = cursor.getColumnIndex(ShopListContract.Columns.ITEM_BRAND);
            oldBrand = cursor.getString(brandColIndex);

            int qtyColIndex = cursor.getColumnIndex(ShopListContract.Columns.ITEM_QTY);
            oldQty = cursor.getString(qtyColIndex);
        }

        //set text in input fields to old details
        rawItemName.setText(oldItemName);
        rawItemBrand.setText(oldBrand);
        rawItemQty.setText(oldQty);

        //add button
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Contents of input fields
                String itemName = rawItemName.getText().toString();
                String itemBrand = rawItemBrand.getText().toString();
                String itemQty = rawItemQty.getText().toString();

                //update SQL statement
                String sqlUpdate = String.format("UPDATE %s SET %s = '%s', %s = '%s', %s = '%s' WHERE %s = '%s'",
                        ShopListContract.TABLE,
                        ShopListContract.Columns.ITEM_NAME, itemName,
                        ShopListContract.Columns.ITEM_BRAND, itemBrand,
                        ShopListContract.Columns.ITEM_QTY, itemQty,
                        ShopListContract.Columns.ITEM_NAME, oldItemName);

                helper = new ShopListDBHelper(SearchShopList.this);
                SQLiteDatabase sqlDB = helper.getWritableDatabase();
                sqlDB.execSQL(sqlUpdate);
                //Log.d("Edit ShopList", itemName);
                updateUI();
            }
        });

        //cancel button
        builder.setNegativeButton("Cancel",null);

        builder.create().show();

    }

}
