package com.foodattack.foodattack;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;


public class DisplayRecipe extends Activity {
    protected String recipeID = null;
    protected String recipeName = null;
    public String recipeIng = "aa"; //ingredients
    public String recipeSteps = "aa";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);

        //pull recipe name from previous activity, so that we can query parse for the
        //recipe to display
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            recipeID = extras.getString("RECIPE_ID");
            recipeName = extras.getString("RECIPE_NAME");
        }
        Log.d("Recipe ID", recipeID);

        //construct a parse Query to obtain the recipe details
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");

        query.getInBackground(recipeID, new GetCallback<ParseObject>() {
            public void done(ParseObject recipeDetails, ParseException e) {
                if (e == null) {
                    TextView a,b,c;
                    a = (TextView) findViewById(R.id.recipe_name);
                    a.setText(recipeName);
                    b = (TextView) findViewById(R.id.recipe_ingredient);
                    b.setText(recipeDetails.getString("recipeIngredients"));
                    c = (TextView) findViewById(R.id.recipe_step);
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
        getMenuInflater().inflate(R.menu.menu_display_recipe, menu);
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
