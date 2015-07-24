package com.foodattack.foodattack;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.SearchView;
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
 * StockListActivityParse is the activity that is started when
 * "Available Food" of the main activity is clicked
 **/

public class StockListActivityParse extends Activity {

    /**
     * Member Variables
     */
    private List<StockListItem> mStockList;
    private StockListAdapter mAdapter;
    private SwipeMenuListView mStockList_ListView;
    private String mUserFamilyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_stock_list);

        mStockList_ListView = (SwipeMenuListView) findViewById(R.id.stocklist_listview);

        //set up adapter
        mStockList = new ArrayList<StockListItem>();
        mAdapter = new StockListAdapter(this, mStockList);
        mStockList_ListView.setAdapter(mAdapter);

        //display stocklist
        updateStockList();
        setSwipeMenu();
    }

    /**
     * updateStockList
     * Description: fetch item data from parse
     */
    private void updateStockList() {
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

                    //retrieve all stock items entered by user's family
                    ParseQuery<StockListItem> stockListQuery = ParseQuery.getQuery(StockListItem.class);
                    stockListQuery.whereEqualTo("itemFamilyID", mUserFamilyID);
                    stockListQuery.orderByAscending("itemName");
                    stockListQuery.findInBackground(new FindCallback<StockListItem>() {
                        @Override
                        public void done(List<StockListItem> stockList, ParseException e) {
                            setProgressBarIndeterminateVisibility(false);
                            if (e == null) {
                                //following gadget habit's tutorial
                                mAdapter.clear();
                                mAdapter.addAll(stockList);
                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong when retrieving data", Toast.LENGTH_SHORT).show();
                                Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                            }
                        }
                    });
                }
                else{
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
        mStockList_ListView.setMenuCreator(swipeMenuCreator);

        /*set what happens when the swipe options are clicked*/
        mStockList_ListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                StockListItem stockItem = mStockList.get(position);
                switch (index) {
                    case 0:
                        onEditOptionClick(stockItem);
                        break;
                    case 1:
                        onDeleteButtonClick(stockItem);
                        break;
                }

                return false;
            }
        });
    }

    /**
     * onEditOptionClick
     * Description: code for edit in swipe menu
     * @param stockItem
     */
    private void onEditOptionClick(StockListItem stockItem){
        Intent intent = new Intent(this, EditStockItemActivity.class);
        intent.putExtra("itemName", stockItem.getItemName());
        intent.putExtra("itemBrand", stockItem.getItemBrand());
        intent.putExtra("itemQty", stockItem.getItemQty());
        intent.putExtra("itemRestock", stockItem.getItemRestock());
        intent.putExtra("itemID", stockItem.getObjectId());
        intent.putExtra("itemFamilyID", stockItem.getItemFamilyID());
        startActivity(intent);
    }

    /**
     * onDeleteButtonClick
     * Description: code for delete in swipe menu
     * @param stockItem
     */
    private void onDeleteButtonClick(StockListItem stockItem){
        stockItem.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    updateStockList();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to Delete", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "User delete error: " + e);
                }
            }
        });
    }

    //TODO solved the refresh prob for add but not edit
    // edit's refresh works sometimes only
    // --> maybe use startactivtyforresult and onactivity result
    @Override
    protected void onRestart() {
        super.onRestart();
        updateStockList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_list, menu);

        //Associate searchable configuration with SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_stock_list_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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

            case R.id.action_refresh_stocklist: {
                updateStockList();
                break;
            }

            case R.id.action_add_stock_item: {
                Intent intent = new Intent(this, EditStockItemActivity.class);
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
