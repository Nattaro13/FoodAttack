package com.foodattack.foodattack;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
 * StockSearchResultsActivity is the activity that is started when
 * StockListActivityParse is searched
 * It displays the search results
 **/

public class StockSearchResultsActivity  extends Activity {

    /**
     * Member Variables
     */
    private List<StockListItem> mStockList;
    private StockListAdapter mAdapter;
    private SwipeMenuListView mStockList_ListView;
    private String mUserFamilyID;
    private String mQueryStr;

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

                    /*retrieve all stock items entered by user's family that contains mQueryStr*/
                    //query for item names that contains mQueryStr
                    ParseQuery<StockListItem> itemNameQuery = ParseQuery.getQuery(StockListItem.class);
                    itemNameQuery.whereEqualTo("itemFamilyID", mUserFamilyID);
                    itemNameQuery.whereMatches("itemName", mQueryStr, "i");
                    //query for item brands that contains mQueryStr
                    ParseQuery<StockListItem> itemBrandQuery = ParseQuery.getQuery(StockListItem.class);
                    itemBrandQuery.whereEqualTo("itemFamilyID", mUserFamilyID);
                    itemBrandQuery.whereMatches("itemBrand", mQueryStr, "i");
                    //add query for item name and brand into list
                    List<ParseQuery<StockListItem>> queries = new ArrayList<ParseQuery<StockListItem>>();
                    queries.add(itemNameQuery);
                    queries.add(itemBrandQuery);
                    //find items that fulfills criteria in queries
                    ParseQuery<StockListItem> stockListQuery = ParseQuery.or(queries);
                    stockListQuery.orderByAscending("itemName");
                    stockListQuery.findInBackground(new FindCallback<StockListItem>() {
                        @Override
                        public void done(List<StockListItem> stockList, ParseException e) {
                            setProgressBarIndeterminateVisibility(false);
                            if (e == null) {
                                //following gadget habit's tutorial
                                mAdapter.clear();
                                mAdapter.addAll(stockList);

                                //set view for empty list
                                mStockList_ListView.setEmptyView(findViewById(R.id.search_stock_empty));

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


    //TODO fix edit bug
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
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to Delete", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "User delete error: " + e);
                }
            }
        });
    }

}
