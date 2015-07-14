package com.foodattack.foodattack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


public class AddStockItemActivity extends Activity {

    private StockListItem mStockItem;
    private EditText mItemNameEditText;
    private EditText mItemBrandEditText;
    private EditText mItemQtyEditText;
    private EditText mItemRestockEditText;
    private String mItemName;
    private String mItemBrand;
    private String mItemQty;
    private String mItemRestock;
    private Button mAddItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock_item);

        Intent intent = this.getIntent();

        mItemNameEditText = (EditText) findViewById(R.id.add_stock_itemName);
        mItemBrandEditText = (EditText) findViewById(R.id.add_stock_itemBrand);
        mItemQtyEditText = (EditText) findViewById(R.id.add_stock_itemQty);
        mItemRestockEditText = (EditText) findViewById(R.id.add_stock_itemRestock);

        //this part need to be slightly different from sitepoint code
        //TODO HERE HERE HERE!!
        if(intent.getExtras() != null){

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
