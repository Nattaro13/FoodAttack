package com.foodattack.foodattack;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class StockListActivityParse extends ListActivity {

    private List<StockListItem> mStockList;
    private StockListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_stock_list);

        //set up adapter
        mStockList = new ArrayList<StockListItem>();
        mAdapter = new StockListAdapter(this, mStockList);
        setListAdapter(mAdapter);

        updateStockList();
    }

    /**
     * updateStockList
     * Description: fetch item data from parse
     */
    private void updateStockList(){
        ParseQuery<StockListItem> query = ParseQuery.getQuery(StockListItem.class);

        setProgressBarIndeterminateVisibility(true);

        query.findInBackground(new FindCallback<StockListItem>() {
            @Override
            public void done(List<StockListItem> stockList, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    //following michael's tutorial
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
    protected void onListItemClick(ListView l, View v, int position, long id) {

        StockListItem item = mStockList.get(position);
        Intent intent = new Intent(this, EditStockItemActivity.class);
        intent.putExtra("itemName", item.getItemName());
        intent.putExtra("itemBrand", item.getItemBrand());
        intent.putExtra("itemQty", item.getItemQty());
        intent.putExtra("itemRestock", item.getItemRestock());
        intent.putExtra("itemID", item.getObjectId());
        startActivity(intent);

    }


}
