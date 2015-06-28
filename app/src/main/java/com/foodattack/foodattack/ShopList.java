package com.foodattack.foodattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


public class ShopList extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        } else if (id == R.id.action_add_item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add an item to the shopping list");
            //builder.setMessage("Key in an item name");

            final AlertDialog alertDialog = builder.create();
            LayoutInflater mInflater = alertDialog.getLayoutInflater();
            View dialogLayout = mInflater.inflate(R.layout.shop_list_dialog,null);

            builder.setView(dialogLayout);


            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d("ShopList","Add an item to the shopping list");
                }
            });


            builder.setNegativeButton("Cancel",null);

            builder.create().show();
            return true;

        } else if (id == R.id.action_search) {
            Log.d("ShopList","Search for an item in the shopping list");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
