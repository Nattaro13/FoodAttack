package com.foodattack.foodattack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * ShopListActivityParse is the activity that is started when
 * "Shopping List" of the main activity is clicked
 **/

public class ShopListActivityParse extends Activity {

    /**
     * Member Variables
     **/
    private List<ShopListItem> mShopList;
    private ShopListAdapter mAdapter;
    private SwipeMenuListView mShopList_ListView;
    private String mUserFamilyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_shop_list);

        mShopList_ListView = (SwipeMenuListView) findViewById(R.id.shoplist_listview);

        //set up adapter
        mShopList = new ArrayList<ShopListItem>();
        mAdapter = new ShopListAdapter(this, mShopList);
        mShopList_ListView.setAdapter(mAdapter);

        // display shoplist
        updateShopList();
        setSwipeMenu();
    }

    /**
     * updateShopList
     * Description: fetch shop item data from parse
     */
    private void updateShopList(){
        //retrieve current user's familyID from "Family" class
        setProgressBarIndeterminateVisibility(true);
        ParseQuery<ParseObject> familyQuery = ParseQuery.getQuery("Family");
        familyQuery.whereEqualTo("Owner", ParseUser.getCurrentUser().getUsername());
        familyQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> userList, ParseException e) {
                if (e == null) {
                    ParseObject userData = userList.get(0);
                    mUserFamilyID = userData.getString("familyID");

                    //retrieve all shop items entered by user's family
                    ParseQuery<ShopListItem> shopListQuery = ParseQuery.getQuery(ShopListItem.class);
                    shopListQuery.whereEqualTo("itemFamilyID", mUserFamilyID);
                    shopListQuery.orderByAscending("itemName");
                    shopListQuery.findInBackground(new FindCallback<ShopListItem>() {
                        @Override
                        public void done(List<ShopListItem> shopList, ParseException e) {
                            setProgressBarIndeterminateVisibility(false);
                            if (e == null) {
                                //following gadget habit's tutorial
                                mAdapter.clear();
                                mAdapter.addAll(shopList);
                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong when retrieving data", Toast.LENGTH_SHORT).show();
                                Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong when retrieving data", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });
    }

    /**
     * setSwipeMenu
     * Description: Controls what the swipe options will look like and
     * what happens when they are clicked
     */
    private void setSwipeMenu() {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

            /*set the look of the swipe options*/
            @Override
            public void create(SwipeMenu menu) {
                //Edit menu option
                SwipeMenuItem editOption = new SwipeMenuItem(getApplicationContext());
                editOption.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                editOption.setWidth(200);
                editOption.setTitle("Edit");
                editOption.setTitleSize(18);
                editOption.setTitleColor(Color.WHITE);
                menu.addMenuItem(editOption);

                //Done menu option
                SwipeMenuItem doneOption = new SwipeMenuItem(getApplicationContext());
                doneOption.setBackground(new ColorDrawable(Color.rgb(0xFF, 0xFF, 0x00)));
                doneOption.setWidth(200);
                doneOption.setTitle("Done");
                doneOption.setTitleSize(18);
                doneOption.setTitleColor(Color.WHITE);
                menu.addMenuItem(doneOption);

                //Delete menu option
                SwipeMenuItem deleteOption = new SwipeMenuItem(getApplicationContext());
                deleteOption.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteOption.setWidth(200);
                deleteOption.setTitle("Delete");
                deleteOption.setTitleSize(18);
                deleteOption.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteOption);
            }
        };
        //set menu creator
        mShopList_ListView.setMenuCreator(swipeMenuCreator);

        /*set what happens when the swipe options are clicked*/
        mShopList_ListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                ShopListItem shopItem = mShopList.get(position);
                switch (index) {
                    case 0:
                        onEditOptionClick(shopItem);
                        break;
                    case 1:
                        onDoneOptionClick(shopItem);
                        break;
                    case 2:
                        onDeleteButtonClick(shopItem);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * onEditOptionClick
     * Description: code for edit in swipe menu
     * @param item
     */
    private void onEditOptionClick(ShopListItem item){
        Intent intent = new Intent(this, EditShopItemActivity.class);
        intent.putExtra("itemName", item.getItemName());
        intent.putExtra("itemBrand", item.getItemBrand());
        intent.putExtra("itemQty", item.getItemQty());
        intent.putExtra("itemID", item.getObjectId());
        intent.putExtra("itemFamilyID", item.getItemFamilyID());
        startActivity(intent);
    }

    /**
     * onDoneOptionClick
     * Description: code for done in swipe menu
     * @param item
     */
    //TODO add code for done option in swipe menu of shop list
    private void onDoneOptionClick(ShopListItem item){
        Log.d("done", "is clicked");
    }

    /**
     * onDeleteButtonClick
     * Description: code for delete in swipe menu
     * @param item
     */
    private void onDeleteButtonClick(ShopListItem item){
        String itemID = item.getObjectId();
        // TODO HERE HERE HERE
        ParseObject itemParseObject = ParseObject.createWithoutData(ShopListItem.class, itemID);
        itemParseObject.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    updateShopList();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to Delete", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "User delete error: " + e);
                }
            }
        });
    }

    //TODO solved the refresh prob for add but not edit in shoplistactivityparse
    // edit's refresh works sometimes only
    // --> maybe use startactivtyforresult and onactivity result
    @Override
    protected void onRestart() {
        super.onRestart();
        updateShopList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_list, menu);

        return true;
    }

    /**
     * opOptionsItemSelected
     * @param item
     * @return
     * Description: controls what happens when action bar buttons are clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_refresh_shoplist: {
                updateShopList();
                break;
            }

            case R.id.action_add_shop_item: {
                Log.d("add clicked", "!!");
                Intent intent = new Intent(this, EditShopItemActivity.class);
                intent.putExtra("itemFamilyID", mUserFamilyID);
                startActivity(intent);
                break;
            }

            case R.id.action_settings: {
                // Do something when user selects Settings from Action Bar overlay
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
