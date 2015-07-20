package com.foodattack.foodattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RollCallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_roll_call);

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
        scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        //init name list
        ArrayList<PersonInfo> nameListB = new ArrayList<PersonInfo>();
        for (int i = 0; i < objects.size(); i++) {
            //get a family member's info and add it to the nameList
            boolean isEating = objects.get(i).getBoolean("Breakfast");
            String memberName = objects.get(i).getString("Owner");
            String mealType = "B";
            PersonInfo person = new PersonInfo(isEating,memberName,mealType);
            nameListB.add(person);
        }

        ArrayList<PersonInfo> nameListL = new ArrayList<PersonInfo>();
        for (int i = 0; i < objects.size(); i++) {
            //get a family member's info and add it to the nameList
            boolean isEating = objects.get(i).getBoolean("Lunch");
            String memberName = objects.get(i).getString("Owner");
            String mealType = "L";
            PersonInfo person = new PersonInfo(isEating,memberName,mealType);
            nameListL.add(person);
        }

        ArrayList<PersonInfo> nameListD = new ArrayList<PersonInfo>();
        for (int i = 0; i < objects.size(); i++) {
            //get a family member's info and add it to the nameList
            boolean isEating = objects.get(i).getBoolean("Dinner");
            String memberName = objects.get(i).getString("Owner");
            String mealType = "D";
            PersonInfo person = new PersonInfo(isEating,memberName,mealType);
            nameListD.add(person);
        }

        MySimpleArrayAdapter adapter1 = new MySimpleArrayAdapter(this, nameListB);

        MySimpleArrayAdapter adapter2 = new MySimpleArrayAdapter(this, nameListL);

        MySimpleArrayAdapter adapter3 = new MySimpleArrayAdapter(this, nameListD);

        final int adapterCount = adapter1.getCount();

        bf = new TextView(this);
        bf.setText("Breakfast");
        bf.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        bf.setTextColor(Color.parseColor("#D32F2F"));
        layout.addView(bf);

        for (int i=0; i<adapterCount; i++) {
            View item = adapter1.getView(i, null, null);
            layout.addView(item);
        }


        lun = new TextView(this);
        lun.setText("Lunch");
        lun.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        lun.setTextColor(Color.parseColor("#D32F2F"));
        layout.addView(lun);

        for (int i=0; i<adapterCount; i++) {
            View item = adapter2.getView(i, null, null);
            layout.addView(item);
        }

        din = new TextView(this);
        din.setText("Dinner");
        din.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        din.setTextColor(Color.parseColor("#D32F2F"));
        layout.addView(din);

        for (int i=0; i<adapterCount; i++) {
            View item = adapter3.getView(i, null, null);
            layout.addView(item);
        }

        //add the linear layout view to the scroll view
        scroll.addView(layout);

        return scroll;
    }


    /*
    When the checkbox is clicked for an item, update meal preferences for that family member
     */
    public void updateMeal(View view) {
        View v = (View) view.getParent();
        //get all your variable info
        final TextView personName = (TextView) v.findViewById(R.id.roll_call_member_name);
        final TextView mealID = (TextView) v.findViewById(R.id.roll_call_meal_type);


        //Find user on the "Family" database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
        query.whereEqualTo("Owner", personName.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if ((e == null) && (!objects.isEmpty())) {
                    //If the query was successful, update meal preferences
                    ParseObject memberData = objects.get(0);
                    //Log.d("Person Name", memberData.getObjectId());
                    updateMealPreferences(memberData.getObjectId(), mealID.getText().toString());

                }
            }
        });
    }


    /*
    Update the meal preferences for a family member
     */
    public void updateMealPreferences(String MemberID,String mealID) {
        final String personObjectID = MemberID;
        final String mealType = mealID;

        ParseQuery <ParseObject> query = ParseQuery.getQuery("Family");

        query.getInBackground(personObjectID, new GetCallback<ParseObject>() {
            public void done(ParseObject personObject, ParseException e) {
                if (e == null) {
                    //if breakfast, update breakfast.
                    if(mealType.compareTo("B") == 0) {
                        if (personObject.getBoolean("Breakfast") == true) {
                            personObject.put("Breakfast",false);
                        } else {
                            personObject.put("Breakfast", true);
                        }
                    } else if (mealType.compareTo("L") == 0) {
                        //update lunch
                        if (personObject.getBoolean("Lunch") == true) {
                            personObject.put("Lunch",false);
                        } else {
                            personObject.put("Lunch", true);
                        }
                    } else {
                        //update dinner
                        if (personObject.getBoolean("Dinner") == true) {
                            personObject.put("Dinner",false);
                        } else {
                            personObject.put("Dinner", true);
                        }
                    }

                    personObject.saveInBackground();
                }
            }
        });
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
