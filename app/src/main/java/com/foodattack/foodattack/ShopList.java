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

import com.foodattack.foodattack.db.ShopListDBHelper;
import com.foodattack.foodattack.db.ShopListContract;

public class ShopList extends ListActivity {
    private ShopListDBHelper helper;

    /*
    Like a constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        //update the Shopping List based on what is in the database
        updateUI();

    }


    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_shop_list);

        //update the Shopping List based on what is in the database
        updateUI();
    }


    /*
    Updates the main UI when any item is added or deleted from the database
     */
    private void updateUI() {
        helper = new ShopListDBHelper(ShopList.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        //format of an SQlite query
        //Cursor cursor = sqLiteDatabase.query(
        //tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        Cursor cursor = sqlDB.query(ShopListContract.TABLE,
                new String[]{ShopListContract.Columns._ID,
                        ShopListContract.Columns.ITEM_NAME,
                        ShopListContract.Columns.ITEM_QTY},
                null,null,null,null, ShopListContract.Columns.ITEM_NAME+" ASC");

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop_list, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        } else if (id == R.id.action_add_item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add an item to the shopping list");
            //builder.setMessage("Key in an item name");

            final AlertDialog alertDialog = builder.create();
            LayoutInflater mInflater = alertDialog.getLayoutInflater();
            View dialogLayout = mInflater.inflate(R.layout.dialog_shop_list, null);

            builder.setView(dialogLayout);

            //initialise the EditText data from the xml file.
            final EditText rawItemName = (EditText) dialogLayout.findViewById(R.id.shoplist_item_name);
            final EditText rawItemBrand = (EditText) dialogLayout.findViewById(R.id.shoplist_item_brand);
            final EditText rawItemQuantity = (EditText) dialogLayout.findViewById(R.id.shoplist_quantity);

            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //convert to string for the SQLite database
                    String itemName = rawItemName.getText().toString();
                    String itemBrand = rawItemBrand.getText().toString();
                    String itemQty = rawItemQuantity.getText().toString();
                    Log.d("ShopList", "Get item properties to store into database");

                    //initialise all the stuff you need for the database
                    ShopListDBHelper helper = new ShopListDBHelper(ShopList.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.clear();

                    //insert the data into the database
                    values.put(ShopListContract.Columns.ITEM_NAME, itemName);
                    values.put(ShopListContract.Columns.ITEM_BRAND, itemBrand);
                    values.put(ShopListContract.Columns.ITEM_QTY, itemQty);

                    db.insertWithOnConflict(ShopListContract.TABLE, null, values,
                            SQLiteDatabase.CONFLICT_IGNORE);
                    updateUI();
                }
            });



            builder.setNegativeButton("Cancel",null);

            builder.create().show();
            return true;

        } else if (id == R.id.action_search) {
            //Log.d("ShopList","Search for an item in the shopping list");
            onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
    When the remove button is pressed on the UI, this method is executed.
     */
    public void onShopListDeleteButtonClick(View view) {
        View v = (View) view.getParent();
        //get the item name
        Button itemNameButton = (Button) v.findViewById(R.id.ShopList_ItemName_View);
        //convert item name to string
        String itemName = itemNameButton.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                ShopListContract.TABLE,
                ShopListContract.Columns.ITEM_NAME,
                itemName);


        helper = new ShopListDBHelper(ShopList.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }


    /*
    When an item on the shooping list is tapped, this method is executed,
    so that people can change the details of the item.
     */
    public void editOnClick(View view){
        View v = (View) view.getParent();
        Button oldItemNameButton = (Button) v.findViewById(R.id.ShopList_ItemName_View);

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

        helper = new ShopListDBHelper(ShopList.this);
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
        // TODO edit set text arguments for edit dialog in shoplist
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

                helper = new ShopListDBHelper(ShopList.this);
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
