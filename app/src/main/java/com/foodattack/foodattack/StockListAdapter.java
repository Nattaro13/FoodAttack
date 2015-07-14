package com.foodattack.foodattack;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StockListAdapter extends ArrayAdapter<StockListItem> {

    /**
     * Member Variables
     **/
    private Context mContext;
    private List<StockListItem> mStockList;

    /**
     * Constructor
     **/
    public StockListAdapter(Context context, List<StockListItem> stockList){
        super(context, R.layout.list_item_stocklist, stockList);
        mContext = context;
        mStockList = stockList;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.list_item_stocklist, null);
        }

        StockListItem stockItem = mStockList.get(position);
        TextView itemNameView = (TextView) convertView.findViewById(R.id.stocklist_itemNameView);
        itemNameView.setText(stockItem.getItemName());

        return convertView;
    }
}
