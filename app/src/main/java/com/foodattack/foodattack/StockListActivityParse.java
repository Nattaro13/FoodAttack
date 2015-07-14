package com.foodattack.foodattack;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;

public class StockListActivityParse extends ListActivity{

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);


    }


    private class StockListAdapter extends ParseQueryAdapter<StockListItem> {

        public StockListAdapter(Context context, ParseQueryAdapter.QueryFactory<StockListItem> queryFactory) {
            super(context, queryFactory);
        }

        @Override
        public View getItemView(StockListItem stockItem, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.list_item_stocklist, parent, false);
                holder = new ViewHolder();
                holder.itemName = (TextView) view.findViewById(R.id.stocklist_itemNameView);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            TextView todoTitle = holder.itemName;
            todoTitle.setText(stockItem.getItemName());
            return view;
        }
    }

    private static class ViewHolder {
        TextView itemName;
    }


}
