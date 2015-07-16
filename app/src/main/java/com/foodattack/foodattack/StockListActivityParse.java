package com.foodattack.foodattack;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenu;

public class StockListActivityParse extends Activity {

    private List<StockListItem> mStockList;
    private StockListAdapter mAdapter;
    private SwipeMenuListView mStockList_ListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_stock_list);

        mStockList_ListView = (SwipeMenuListView) findViewById(R.id.stocklist_listview);

        //set up adapter
        mStockList = new ArrayList<StockListItem>();
        mAdapter = new StockListAdapter(this, mStockList);
        //setListAdapter(mAdapter);
        mStockList_ListView.setAdapter(mAdapter);

        //display stocklist
        updateStockList();

        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
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

        //listener swipe options click event
        mStockList_ListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                StockListItem stockItem = mStockList.get(position);
                switch (index){
                    case 0:
                        onEditOptionClick(stockItem);
                        break;
                    case 1:
                        onDeleteButtonClick(stockItem);
                        updateStockList();
                }

                return false;
            }
        });

    }

    /**
     * updateStockList
     * Description: fetch item data from parse
     */
    private void updateStockList(){
        ParseQuery<StockListItem> query = ParseQuery.getQuery(StockListItem.class);

        //TODO - currently shows items by users only, not the family - need to change later
        query.whereEqualTo("itemFamily", ParseUser.getCurrentUser());

        setProgressBarIndeterminateVisibility(true);

        query.findInBackground(new FindCallback<StockListItem>() {
            @Override
            public void done(List<StockListItem> stockList, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    //following gadget habit's tutorial
                    mAdapter.clear();
                    mAdapter.addAll(stockList);
                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_refresh: {
                updateStockList();
                break;
            }

            case R.id.action_add_stock_item: {
                Intent intent = new Intent(this, EditStockItemActivity.class);
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

    //supposed to enable a user to select a list item and see its details in the Add/Edit view
/*    protected void onListItemClick(ListView l, View v, int position, long id) {

        StockListItem item = mStockList.get(position);
        Intent intent = new Intent(this, EditStockItemActivity.class);
        intent.putExtra("itemName", item.getItemName());
        intent.putExtra("itemBrand", item.getItemBrand());
        intent.putExtra("itemQty", item.getItemQty());
        intent.putExtra("itemRestock", item.getItemRestock());
        intent.putExtra("itemID", item.getObjectId());
        startActivity(intent);

    }*/

    private void onDeleteButtonClick(StockListItem item){
        String itemID = item.getObjectId();
        ParseObject itemParseObject = ParseObject.createWithoutData(StockListItem.class, itemID);
        itemParseObject.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Failed to Delete", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "User delete error: " + e);
                }
            }
        });
    }

    private void onEditOptionClick(StockListItem item){
        Intent intent = new Intent(this, EditStockItemActivity.class);
        intent.putExtra("itemName", item.getItemName());
        intent.putExtra("itemBrand", item.getItemBrand());
        intent.putExtra("itemQty", item.getItemQty());
        intent.putExtra("itemRestock", item.getItemRestock());
        intent.putExtra("itemID", item.getObjectId());
        startActivity(intent);
    }


}
