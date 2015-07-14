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

import com.foodattack.foodattack.db.StockListContract;
import com.foodattack.foodattack.db.StockListDBHelper;

/**
 * Created by Xue Hui on 1/7/2015.
 */
public class SearchStockList extends ListActivity {

    private StockListDBHelper helper;
    private String queryUpdated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        Log.d("Entered Searchable", "Searchable");
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
        helper = new StockListDBHelper(SearchStockList.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();

        queryUpdated = query;

        String whereClause = String.format("%s = ?", StockListContract.Columns.ITEM_NAME);
        //Cursor cursor = sqLiteDatabase.query(
        //tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        Cursor cursor = sqlDB.query(StockListContract.TABLE,
                new String[]{StockListContract.Columns._ID,
                        StockListContract.Columns.ITEM_NAME,
                        StockListContract.Columns.ITEM_QTY},
                whereClause, new String[]{query}, null, null, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item_stocklist,
                cursor,
                new String[] { StockListContract.Columns.ITEM_NAME,
                        StockListContract.Columns.ITEM_QTY},
                new int[] { R.id.stocklist_itemNameView,R.id.stocklist_itemQtyView},
                0
        );
        this.setListAdapter(listAdapter);

    }


    /*
    When the remove button is pressed on the UI, this method is executed.
     */
    public void onDelButtonClick(View view) {
        View v = (View) view.getParent();
        Button itemNameButton = (Button) v.findViewById(R.id.stocklist_itemNameView);

        String itemName = itemNameButton.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                StockListContract.TABLE,
                StockListContract.Columns.ITEM_NAME,
                itemName);


        helper = new StockListDBHelper(SearchStockList.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }



    /*
    Updates the main UI when any item is added or deleted from the database
     */
    private void updateUI() {
        helper = new StockListDBHelper(SearchStockList.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        //format of an SQlite query
        //Cursor cursor = sqLiteDatabase.query(
        //tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        String whereClause = String.format("%s = ?",StockListContract.Columns.ITEM_NAME);
        //Cursor cursor = sqLiteDatabase.query(
        //tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        Cursor cursor = sqlDB.query(StockListContract.TABLE,
                new String[]{StockListContract.Columns._ID,
                        StockListContract.Columns.ITEM_NAME,
                        StockListContract.Columns.ITEM_QTY},
                whereClause, new String[]{queryUpdated}, null, null, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.stock_list_view,
                cursor,
                new String[] { StockListContract.Columns.ITEM_NAME,
                        StockListContract.Columns.ITEM_QTY},
                new int[] { R.id.stocklist_itemNameView,R.id.stocklist_itemQtyView},
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

        helper = new StockListDBHelper(SearchStockList.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(StockListContract.TABLE,
                new String[]{StockListContract.Columns.ITEM_NAME,
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


        //set text in input fields to old details
        rawItemName.setText(oldItemName);
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

                helper = new StockListDBHelper(SearchStockList.this);
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
