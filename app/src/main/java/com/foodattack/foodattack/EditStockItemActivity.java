package com.foodattack.foodattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


public class EditStockItemActivity extends Activity {

    //private StockListItem mStockItem;
    private EditText mItemNameEditText;
    private EditText mItemBrandEditText;
    private EditText mItemQtyEditText;
    private EditText mItemRestockEditText;
    private String mItemName;
    private String mItemBrand;
    private String mItemQty;
    private String mItemRestock;
    private String mItemID;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stock_item);

        Intent intent = this.getIntent();

        //get edit texts
        mItemNameEditText = (EditText) findViewById(R.id.add_stock_itemName);
        mItemBrandEditText = (EditText) findViewById(R.id.add_stock_itemBrand);
        mItemQtyEditText = (EditText) findViewById(R.id.add_stock_itemQty);
        mItemRestockEditText = (EditText) findViewById(R.id.add_stock_itemRestock);

        if(intent.getExtras() != null){
            //set input fields to previously entered data
            mItemNameEditText.setText(intent.getStringExtra("itemName"));
            mItemBrandEditText.setText(intent.getStringExtra("itemBrand"));
            mItemQtyEditText.setText(intent.getStringExtra("itemQty"));
            mItemRestockEditText.setText(intent.getStringExtra("itemRestock"));
            //get objectID of item clicked
            mItemID = intent.getStringExtra("itemID");
        }

        mSaveButton = (Button) findViewById(R.id.edit_stock_saveItem);
        mSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveItem();
            }
        });
    }

    /**
     * saveItem
     * Description: save item to parse when user add or edit an item
     */
    private void saveItem(){
        //get text input by user
        mItemName = mItemNameEditText.getText().toString();
        mItemBrand = mItemBrandEditText.getText().toString();
        mItemQty = mItemQtyEditText.getText().toString();
        mItemRestock = mItemRestockEditText.getText().toString();

        //remove trailing whitespace
        mItemName.trim();
        mItemBrand.trim();
        mItemQty.trim();
        mItemRestock.trim();

        //if the user does not enter item name, do nothing
        //if the user enters at least item name and qty, save
        //if the user enters item name but no qty, give warning
        if((!mItemName.isEmpty()) && (!mItemQty.isEmpty())){

            //check if item is being added or edited
            //if item is being added (i.e. item has no objectID yet), save it
            if (mItemID == null){
                //create a new stockListItem
                StockListItem stockItem = new StockListItem();
                stockItem.setItemName(mItemName);
                stockItem.setItemBrand(mItemBrand);
                stockItem.setItemQty(mItemQty);
                stockItem.setItemRestock(mItemRestock);
                stockItem.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        saveToast(e);
                    }
                });
            }
            //if item is being edited, update it
            else{
                //query
                ParseQuery<StockListItem> query = ParseQuery.getQuery(StockListItem.class);
                //retrieve item by id
                query.getInBackground(mItemID, new GetCallback<StockListItem>() {
                    @Override
                    public void done(StockListItem stockItem, ParseException e) {
                        if (e == null){
                            //update data
                            stockItem.setItemName(mItemName);
                            stockItem.setItemBrand(mItemBrand);
                            stockItem.setItemQty(mItemQty);
                            stockItem.setItemRestock(mItemRestock);
                            stockItem.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    saveToast(e);
                                }
                            });
                        }
                    }
                });
            }
        }
        else if ((!mItemName.isEmpty()) && (mItemQty.isEmpty())){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditStockItemActivity.this);
            builder.setMessage(R.string.edit_error_no_qty)
                    .setTitle(R.string.edit_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * saveToast
     * Description: display toast msg depending if saved successfully or unsuccessfully
     * @param e
     */
    private void saveToast(ParseException e) {
        if (e == null){
            //show toast msg - saved successfully
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            //show toast msg - save failed
            Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
            Log.d(getClass().getSimpleName(), "User update error: " + e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_stock_item, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
