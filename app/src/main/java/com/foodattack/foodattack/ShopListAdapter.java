package com.foodattack.foodattack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * ShopListAdapter set view that displays stock item's itemName and itemQty
 **/

public class ShopListAdapter extends ArrayAdapter<ShopListItem> {

    /**
     * Member Variables
     **/
    private List<ShopListItem> mShopList;
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
    public ShopListAdapter(Context context, List<ShopListItem> shopList){
        super(context, R.layout.list_item_shoplist, shopList);
        mInflater = LayoutInflater.from(context);
        mShopList = shopList;
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
            convertView = mInflater.inflate(R.layout.list_item_shoplist, null);
            holder.itemNameView = (TextView) convertView.findViewById(R.id.shoplist_itemNameView);
            holder.itemQtyView = (TextView) convertView.findViewById(R.id.shoplist_itemQtyView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        ShopListItem shopItem = mShopList.get(position);

        holder.itemNameView.setText(shopItem.getItemName());
        holder.itemQtyView.setText(shopItem.getItemQty());
        return convertView;
    }
}
