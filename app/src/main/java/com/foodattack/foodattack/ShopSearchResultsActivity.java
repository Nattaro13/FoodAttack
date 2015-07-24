package com.foodattack.foodattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
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
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * ShopSearchResultsActivity is the activity that is started when
 * ShopListActivityParse is searched
 * It displays the search results
 **/

public class ShopSearchResultsActivity extends Activity {

    /**
     * Member Variables
     */
    private List<ShopListItem> mShopList;
    private ShopListAdapter mAdapter;
    private SwipeMenuListView mShopList_ListView;
    private String mUserFamilyID;
    private String mQueryStr;

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

        setSwipeMenu();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    /**
     * handleIntent
     * @param intent
     * Description: if action is search, show results for query
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQueryStr = intent.getStringExtra(SearchManager.QUERY).trim();
            showResults();
        }
    }

    /**
     * showResults
     * Description: search for item with name or brand that
     * matches the query and display results
     **/
    private void showResults(){
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

                    /*retrieve all shop items entered by user's family that contains mQueryStr*/
                    //query for item names that contains mQueryStr
                    ParseQuery<ShopListItem> itemNameQuery = ParseQuery.getQuery(ShopListItem.class);
                    itemNameQuery.whereEqualTo("itemFamilyID", mUserFamilyID);
                    itemNameQuery.whereMatches("itemName", mQueryStr, "i");
                    //query for item brands that contains mQueryStr
                    ParseQuery<ShopListItem> itemBrandQuery = ParseQuery.getQuery(ShopListItem.class);
                    itemBrandQuery.whereEqualTo("itemFamilyID", mUserFamilyID);
                    itemBrandQuery.whereMatches("itemBrand", mQueryStr, "i");
                    //add query for item name and brand into list
                    List<ParseQuery<ShopListItem>> queries = new ArrayList<ParseQuery<ShopListItem>>();
                    queries.add(itemNameQuery);
                    queries.add(itemBrandQuery);
                    //find items that fulfills criteria in queries
                    ParseQuery<ShopListItem> shopListQuery = ParseQuery.or(queries);
                    shopListQuery.orderByAscending("itemName");
                    shopListQuery.findInBackground(new FindCallback<ShopListItem>() {
                        @Override
                        public void done(List<ShopListItem> shopList, ParseException e) {
                            setProgressBarIndeterminateVisibility(false);
                            if (e == null) {
                                //following gadget habit's tutorial
                                mAdapter.clear();
                                mAdapter.addAll(shopList);

                                //set view for empty list
                                setViewForEmptyList(shopList);
                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong during the search", Toast.LENGTH_SHORT).show();
                                Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong during the search", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });
    }

    /**
     * setViewForEmptyList
     * @param shopList
     * Description: make no results msg visible if there are no results
     */
    private void setViewForEmptyList(List<ShopListItem> shopList) {
        if(shopList.isEmpty()){
            TextView emptyView = (TextView) findViewById(R.id.search_shop_empty);
            emptyView.setVisibility(View.VISIBLE);
        }
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
                doneOption.setBackground(new ColorDrawable(Color.rgb(0xFF, 0x99, 0x3B)));
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

    //TODO fix edit bug
    /**
     * onEditOptionClick
     * Description: code for edit in swipe menu
     * @param shopItem
     */
    private void onEditOptionClick(ShopListItem shopItem){
        Intent intent = new Intent(this, EditShopItemActivity.class);
        intent.putExtra("itemName", shopItem.getItemName());
        intent.putExtra("itemBrand", shopItem.getItemBrand());
        intent.putExtra("itemQty", shopItem.getItemQty());
        intent.putExtra("itemID", shopItem.getObjectId());
        intent.putExtra("itemFamilyID", shopItem.getItemFamilyID());
        startActivity(intent);
    }

    /**
     * onDoneOptionClick
     * Description: code for done in swipe menu; when the done option is clicked
     * a dialog that prompts the user to add a restock duration pop up
     * when the user click "save", the shop item will be added to the stocklist and
     * deleted from the shoplist
     * @param item
     */
    private void onDoneOptionClick(ShopListItem item){
        //get item details
        final ShopListItem shopItem = item;
        final String itemName = shopItem.getItemName();
        final String itemBrand = shopItem.getItemBrand();
        final String itemQty = shopItem.getItemQty();
        final String itemFamilyID = shopItem.getItemFamilyID();

        /* set what the alert dialog will look like */
        //set title and msg of dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a restock duration for " + itemName);

        //set layout of dialog
        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_done_swipe_option, null);
        builder.setView(dialogLayout);

        //save button
        builder.setPositiveButton(R.string.action_save_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //get restock date
                EditText itemRestockEditText = (EditText) dialogLayout.findViewById(R.id.dialog_done_swipe_restock);
                String itemRestock = itemRestockEditText.getText().toString();

                //create a new stockListItem
                StockListItem stockItem = new StockListItem();
                stockItem.setItemName(itemName);
                stockItem.setItemBrand(itemBrand);
                stockItem.setItemQty(itemQty);
                stockItem.setItemRestock(itemRestock);
                stockItem.setItemFamilyID(itemFamilyID);

                //save stockItem to parse
                stockItem.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("added to", "StockList!!");
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to Save to Stock List", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //delete the shopItem
                shopItem.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "Updated Both Stock and Shop List", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to Delete from Shop List", Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "User delete error: " + e);
                        }
                    }
                });
                finish();
            }
        });

        //cancel button
        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }

    /**
     * onDeleteButtonClick
     * Description: code for delete in swipe menu
     * @param shopItem
     */
    private void onDeleteButtonClick(ShopListItem shopItem){
        shopItem.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to Delete", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "User delete error: " + e);
                }
            }
        });
        finish();
    }

}
