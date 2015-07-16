package com.foodattack.foodattack;

import android.app.Activity;
import android.content.Intent;
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

import java.util.List;


public class RecipeList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        //set the view for the list
        final ListView listView = (ListView) findViewById(R.id.recipe_list);

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
                                viewList(listView,recipesList);
                            }
                        }
                    });
                }
            }
        });
    }


    public void viewList(ListView listView, String[] recipesList) {
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
        TextView recipeTitle = (TextView) view.findViewById(R.id.recipe_title);
        Log.d("Recipe clicked",recipeTitle.getText().toString());
        //Switch interface to "view recipe activity"
        Intent intent = new Intent(this, DisplayRecipe.class);
        intent.putExtra("RECIPE_NAME",recipeTitle.getText().toString());
        startActivity(intent);

    }
}
