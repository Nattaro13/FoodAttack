package com.foodattack.foodattack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenu;


public class RecipeList extends Activity {
    protected List<ParseObject> recipeObjects = null;
    //private SwipeMenuListView mStockList_ListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        //set up swipe menu
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

        //set the view for the list
        final SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.recipe_list);

        // set creator
        listView.setMenuCreator(swipeMenuCreator);

        //get the recipe titles for that familyID
        //construct query for the "Family" database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
        //get the owner's data from the database
        query.whereEqualTo("Owner", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if ((e == null) && (!objects.isEmpty())) {
                    //get the owner's familyID.
                    ParseObject userData = objects.get(0);
                    final String currFamilyID = userData.getString("familyID");

                    //construct query for the "Recipe" database to get all entries with
                    //the same familyID
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Recipe");
                    query2.whereEqualTo("familyID", currFamilyID);
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> objects, ParseException e) {
                            if ((e == null) && (!objects.isEmpty())) {
                                String[] recipesList = new String[objects.size()];
                                for (int i = 0; i < objects.size(); i++) {
                                    recipesList[i] = objects.get(i).getString("recipeTitle");
                                }
                                viewList(listView, recipesList, objects);
                            }
                        }
                    });
                }
            }
        });

        //listener swipe options click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                //StockListItem recipeItem = mStockList.get(position);
                ParseObject recipeItem = recipeObjects.get(position);
                Log.d("recipe name", recipeItem.getString("recipeTitle"));
                switch (index) {
                    case 0:
                        //edit function!
                        break;
                    case 1:
                        //delete function!
                        //update list!
                        break;
                }

                return false; //close menu
            }
        });
    }


    public void viewList(ListView listView, String[] recipesList, List<ParseObject> objects) {
        recipeObjects = objects; //pointer to the objects list. global add.
        final ListView listView2 = listView;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.recipe_list_row, R.id.recipe_title, recipesList);
        listView2.setAdapter(adapter);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView2.getItemAtPosition(position);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_list, menu);
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

        } else if (id == R.id.action_new_recipe) {
            //Switch interface to the "add recipe activity"
            Intent intent = new Intent(this, AddRecipe.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    Executed when a recipe title is clicked.
     */
    public void viewRecipe(View view) {
        String recipeID = null;
        TextView recipeTitle = (TextView) view.findViewById(R.id.recipe_title);
        Log.d("Recipe clicked",recipeTitle.getText().toString());
        String recipeTitleString = recipeTitle.getText().toString();
        //find the recipe's ID
        for(int i=0; i<recipeObjects.size(); i++) {
            if(recipeObjects.get(i).getString("recipeTitle").compareTo(recipeTitleString) == 0) {
                recipeID = recipeObjects.get(i).getObjectId();
            }
        }
        //Switch interface to "view recipe activity"
        Intent intent = new Intent(this, DisplayRecipe.class);
        intent.putExtra("RECIPE_ID",recipeID);
        intent.putExtra("RECIPE_NAME",recipeTitleString);
        startActivity(intent);

    }
}
