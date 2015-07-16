package com.foodattack.foodattack;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class AddRecipe extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add_recipe) {
            //store the recipe onto Parse, then...
            storeRecipe();
            //Switch interface to the RecipeList
            Intent intent = new Intent(this, RecipeList.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void storeRecipe() {
        //get all your recipe info!
        final EditText recipeTitle = (EditText) findViewById(R.id.recipe_name);
        final EditText recipeIngredients = (EditText) findViewById(R.id.recipe_ingredients);
        final EditText recipeSteps = (EditText) findViewById(R.id.recipe_steps);
        Log.d("Recipe",recipeTitle.getText().toString());

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

                    //construct object to store Recipe into the Parse Database.
                    ParseObject newRecipe = new ParseObject("Recipe");
                    newRecipe.put("familyID",currFamilyID);
                    newRecipe.put("recipeTitle", recipeTitle.getText().toString());
                    newRecipe.put("recipeIngredients",recipeIngredients.getText().toString());
                    newRecipe.put("recipeSteps",recipeSteps.getText().toString());
                    //store into the database
                    newRecipe.saveInBackground();

                }
            }
        });

    }

}
