package com.foodattack.foodattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * EditShopItemActivity is the activity that is started when the user
 * clicks on the "edit" in the swipe menu or "add" in the action bar
 * in the shoplistactivitypase
 **/

public class EditShopItemActivity extends Activity {

    /**
     * Member variables
     **/
    private EditText mItemNameEditText;
    private EditText mItemBrandEditText;
    private EditText mItemQtyEditText;
    private String mItemName;
    private String mItemBrand;
    private String mItemQty;
    private String mItemID;
    private String mItemFamilyID;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_edit_shop_item);

        Intent intent = this.getIntent();

        //get edit texts
        mItemNameEditText = (EditText) findViewById(R.id.edit_shop_itemName);
        mItemBrandEditText = (EditText) findViewById(R.id.edit_shop_itemBrand);
        mItemQtyEditText = (EditText) findViewById(R.id.edit_shop_itemQty);

        //if edit is clicked, get data of item to be edited
        if(intent.getExtras() != null){
            //set input fields to previously entered data
            mItemNameEditText.setText(intent.getStringExtra("itemName"));
            mItemBrandEditText.setText(intent.getStringExtra("itemBrand"));
            mItemQtyEditText.setText(intent.getStringExtra("itemQty"));

            //get objectID of item clicked
            mItemID = intent.getStringExtra("itemID");
        }

        //always get the familyID of user
        mItemFamilyID = intent.getStringExtra("itemFamilyID");

        mSaveButton = (Button) findViewById(R.id.edit_shop_saveItem);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    /**
     * saveItem
     * Description: save item to parse when user add or edit a shop item
     */
    private void saveItem(){
        //get text input by user
        mItemName = mItemNameEditText.getText().toString();
        mItemBrand = mItemBrandEditText.getText().toString();
        mItemQty = mItemQtyEditText.getText().toString();

        //remove trailing whitespace
        mItemName.trim();
        mItemBrand.trim();
        mItemQty.trim();

        //if the user does not enter item name, do nothing
        //if the user enters at least item name and qty, save
        //if the user enters item name but no qty, give warning
        if((!mItemName.isEmpty()) && (!mItemQty.isEmpty())){

            //check if item is being added or edited
            //if item is being added (i.e. item has no objectID yet), save it
            if (mItemID == null){
                //create new shopListItem
                final ShopListItem shopItem = new ShopListItem();
                shopItem.setItemName(mItemName);
                shopItem.setItemBrand(mItemBrand);
                shopItem.setItemQty(mItemQty);
                shopItem.setItemFamilyID(mItemFamilyID);

                //save shopItem to parse
                setProgressBarIndeterminateVisibility(true);
                shopItem.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        setProgressBarIndeterminateVisibility(false);
                        saveToast(e);
                        //prevents item to be save multiple times if alrdy saved
                        if (e == null) {
                            mItemID = shopItem.getObjectId();
                        }
                    }
                });

                finish();
            }
            //if item is being edited, update it
            else{
                //create query object and retrieve item by id
                ParseQuery<ShopListItem> query = ParseQuery.getQuery(ShopListItem.class);
                query.getInBackground(mItemID, new GetCallback<ShopListItem>() {
                    @Override
                    public void done(ShopListItem shopItem, ParseException e) {
                        if (e == null){
                            //update data
                            shopItem.setItemName(mItemName);
                            shopItem.setItemBrand(mItemBrand);
                            shopItem.setItemQty(mItemQty);
                            setProgressBarIndeterminateVisibility(true);
                            shopItem.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    setProgressBarIndeterminateVisibility(false);
                                    saveToast(e);
                                }
                            });
                        }
                    }
                });

                finish();
            }
        }
        else if ((!mItemName.isEmpty()) && (mItemQty.isEmpty())){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditShopItemActivity.this);
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
        getMenuInflater().inflate(R.menu.menu_edit_shop_item, menu);
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
