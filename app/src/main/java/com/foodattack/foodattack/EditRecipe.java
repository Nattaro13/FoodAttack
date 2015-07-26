package com.foodattack.foodattack;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class EditRecipe extends Activity {
    protected String recipeID = null;
    protected String recipeName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        //pull recipe name from previous activity, so that we can query parse for the
        //recipe to display
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            recipeID = extras.getString("RECIPE_ID");
            recipeName = extras.getString("RECIPE_NAME");
        }

        //construct a parse Query to obtain the recipe details
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");

        query.getInBackground(recipeID, new GetCallback<ParseObject>() {
            public void done(ParseObject recipeDetails, ParseException e) {
                if (e == null) {
                    TextView a,b,c;
                    a = (TextView) findViewById(R.id.edit_recipe_name);
                    a.setText(recipeName);
                    b = (TextView) findViewById(R.id.edit_recipe_ingredients);
                    b.setText(recipeDetails.getString("recipeIngredients"));
                    c = (TextView) findViewById(R.id.edit_recipe_steps);
                    c.setText(recipeDetails.getString("recipeSteps"));

                } else {
                    Log.d("Unable to get recipe", e.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_recipe, menu);
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


    /*
    Stores your recipe edits onto Parse once you are done! :D
     */
    public void storeRecipe() {
        //get all your recipe info!
        final EditText rawRecipeTitle = (EditText) findViewById(R.id.edit_recipe_name);
        final EditText rawRecipeIngredients = (EditText) findViewById(R.id.edit_recipe_ingredients);
        final EditText rawRecipeSteps = (EditText) findViewById(R.id.edit_recipe_steps);

        final String recipeTitle = rawRecipeTitle.getText().toString().trim();
        final String recipeIngredients = rawRecipeIngredients.getText().toString().trim();
        final String recipeSteps = rawRecipeSteps.getText().toString().trim();

        if ((!recipeTitle.isEmpty())
                || (!recipeIngredients.isEmpty())
                || (!recipeSteps.isEmpty())) {

            //construct query for the "Family" database
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
            //get the recipe object from the database
            query.getInBackground(recipeID, new GetCallback<ParseObject>() {
                public void done(ParseObject recipeDetails, ParseException e) {
                    if (e == null) {
                        recipeDetails.put("recipeTitle", recipeTitle);
                        recipeDetails.put("recipeIngredients", recipeIngredients);
                        recipeDetails.put("recipeSteps", recipeSteps);
                        //store into the database
                        recipeDetails.saveInBackground();
                        Toast.makeText(getApplicationContext(), "Recipe updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_SHORT).show();
        }
    }


    /*
    Executed when back button on the phone pad is pressed.
    Brings user to main activity
     */
    public void onBackPressed() {
        Intent intent = new Intent(this,RecipeList.class);
        startActivity(intent);
    }
}
