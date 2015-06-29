package com.foodattack.foodattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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
                null,null,null,null,null);

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
            View dialogLayout = mInflater.inflate(R.layout.shop_list_dialog, null);

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
            Log.d("ShopList","Search for an item in the shopping list");
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
        TextView itemNameTextView = (TextView) v.findViewById(R.id.ShopList_ItemName_View);
        //convert item name to string
        String itemName = itemNameTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                ShopListContract.TABLE,
                ShopListContract.Columns.ITEM_NAME,
                itemName);


        helper = new ShopListDBHelper(ShopList.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }
}
