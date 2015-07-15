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
    private List<StockListItem> mStockList;
    private LayoutInflater mInflater;

    /**
     * Private class that holds textViews
     */
    private class ViewHolder {
        TextView itemNameView;
        TextView itemQtyView;
    }

    /**
     * Constructor
     **/
    public StockListAdapter(Context context, List<StockListItem> stockList){
        super(context, R.layout.list_item_stocklist,stockList);
        mInflater = LayoutInflater.from(context);
        mStockList = stockList;
    }

    /**
     * getView
     * @param position
     * @param convertView
     * @param parent
     * @return
     * Description: Sets view to display both item name and item qty
     */
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder = null;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_stocklist, null);
            holder.itemNameView = (TextView) convertView.findViewById(R.id.stocklist_itemNameView);
            holder.itemQtyView = (TextView) convertView.findViewById(R.id.stocklist_itemQtyView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        StockListItem stockItem = mStockList.get(position);

        holder.itemNameView.setText(stockItem.getItemName());
        holder.itemQtyView.setText(stockItem.getItemQty());
        return convertView;
    }
}
