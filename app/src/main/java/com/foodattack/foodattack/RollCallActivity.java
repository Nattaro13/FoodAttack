package com.foodattack.foodattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class RollCallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //construct query for the "Family" database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
        //get the owner's data from the database
        query.whereEqualTo("Owner", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if ((e == null) && (!objects.isEmpty())) {
                    //get the owner's family ID.
                    ParseObject userData = objects.get(0);
                    final String currFamilyID = userData.getString("familyID");

                    //construct query for the "Family" database to get all entries with
                    //the same familyID
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Family");
                    query2.whereEqualTo("familyID", currFamilyID);
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            if ((e == null) && (!objects.isEmpty())) {
                                ScrollView myView = setLayout(objects);
                                setContentView(myView);
                            }
                        }
                    });
                }
            }
        });
    }


    /*
    Returns the dynamic layout for the family's roll call register
     */
    public ScrollView setLayout(List<ParseObject> objects) {
        //initialise fields for linear layout
        TextView bf,lun,din;
        CheckBox temp;
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setPadding(30,30,30,30);
        //initialise fields to allow scrolling (if many family members)
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(android.R.color.transparent);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        bf = new TextView(this);
        bf.setText("Breakfast");
        bf.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        layout.addView(bf);

        for(int i=0; i<objects.size(); i++) {
            //get a family member's name and add it to the check box
            String memberName = objects.get(i).getString("Owner");
            temp = new CheckBox(this);
            temp.setText(memberName);
            temp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            layout.addView(temp);

        }

        lun = new TextView(this);
        lun.setText("Lunch");
        lun.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        layout.addView(lun);

        for(int i=0; i<objects.size(); i++) {
            //get a family member's name and add it to the check box
            String memberName = objects.get(i).getString("Owner");
            temp = new CheckBox(this);
            temp.setText(memberName);
            temp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            layout.addView(temp);

        }

        din = new TextView(this);
        din.setText("Dinner");
        din.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        layout.addView(din);

        for(int i=0; i<objects.size(); i++) {
            //get a family member's name and add it to the check box
            String memberName = objects.get(i).getString("Owner");
            temp = new CheckBox(this);
            temp.setText(memberName);
            temp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            layout.addView(temp);

        }

        //add the linear layout view to the scroll view
        scroll.addView(layout);

        return scroll;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_roll_call, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
