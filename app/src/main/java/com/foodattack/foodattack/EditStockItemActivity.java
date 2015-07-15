package com.foodattack.foodattack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


public class EditStockItemActivity extends Activity {

    private StockListItem mStockItem;
    private EditText mItemNameEditText;
    private EditText mItemBrandEditText;
    private EditText mItemQtyEditText;
    private EditText mItemRestockEditText;
    private String mItemName;
    private String mItemBrand;
    private String mItemQty;
    private String mItemRestock;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stock_item);

        Intent intent = this.getIntent();

        mItemNameEditText = (EditText) findViewById(R.id.add_stock_itemName);
        mItemBrandEditText = (EditText) findViewById(R.id.add_stock_itemBrand);
        mItemQtyEditText = (EditText) findViewById(R.id.add_stock_itemQty);
        mItemRestockEditText = (EditText) findViewById(R.id.add_stock_itemRestock);

        //this part need to be slightly different from sitepoint code
        if(intent.getExtras() != null){

            mItemNameEditText.setText(intent.getStringExtra("itemName"));
            mItemBrandEditText.setText(intent.getStringExtra("itemBrand"));
            mItemQtyEditText.setText(intent.getStringExtra("itemQty"));
            mItemRestockEditText.setText(intent.getStringExtra("itemRestock"));
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

        //if the user enters at least item name and qty, save
        if((!mItemName.isEmpty()) && (!mItemQty.isEmpty())){

            //check if item is being added or edited
            //if item is being added, save it
            if (mStockItem == null){
                //create a new stockListItem
                StockListItem stockItem = new StockListItem();
                stockItem.setItemName(mItemName);
                stockItem.setItemBrand(mItemBrand);
                stockItem.setItemQty(mItemQty);
                stockItem.setItemRestock(mItemRestock);
                stockItem.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
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
                });
            }
            //if item is being edited, update it
            else{
                ParseQuery<StockListItem> query = ParseQuery.getQuery(StockListItem.class);
            }


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
