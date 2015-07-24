package com.foodattack.foodattack;

/**
 * Created by Xue Hui on 16/7/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MySimpleArrayAdapter extends ArrayAdapter {
    private final Activity activity;
    private final ArrayList<PersonInfo> values;


    protected static class ViewHolder{
        protected Switch isEating;
        protected TextView memberName;
        protected TextView mealID;
    }

    public MySimpleArrayAdapter(Activity activity, ArrayList<PersonInfo> values) {
        super(activity, -1, values);
        this.activity = activity;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder view;

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.roll_call_list_view, null);

            // Hold the view objects in an object, that way the don't need to be "re-finded"
            view = new ViewHolder();
            view.isEating = (Switch) rowView.findViewById(R.id.roll_call_check_box);
            view.memberName = (TextView) rowView.findViewById(R.id.roll_call_member_name);
            view.mealID= (TextView) rowView.findViewById(R.id.roll_call_meal_type);
            rowView.setTag(view);
        } else {
            view = (ViewHolder) rowView.getTag();
        }

        /** Set data to your Views. */
        PersonInfo person = values.get(position);
        if(person.getEating()) {
            view.isEating.setChecked(true);
            //Log.d("Person eat","true" );
        } else {
            view.isEating.setChecked(false);
            //Log.d("Person Eat","false");
        }
        view.memberName.setText(person.getName());
        view.memberName.setTextColor(Color.parseColor("#000000"));
        view.mealID.setText(person.getMealID());
        //so that people don't see my cheap trick - the meal identifier
        view.mealID.setTextColor(android.R.color.transparent);

        return rowView;
    }
}