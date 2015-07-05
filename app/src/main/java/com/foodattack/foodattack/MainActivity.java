package com.foodattack.foodattack;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    protected void onResume(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        } else if (id == R.id.parse_log_out) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser == null) {
                Log.d("LogOut", "Successfully logged out");
                changeScreenLogout();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
   Upon successful log out, bring the user to the login page.
    */
    public void changeScreenLogout() {
        //Switch interface to the main screen
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }



    /**
     * WHEN BUTTONS ON THE MAIN MENU ARE CLICKED: ALL THE CODE GOES BELOW.
     * ###################################################################
     */


    //called when the user clicks the "available food" button in the main menu
    public void getStock(View view) {

        /* base code for transiting from one activity to another
        //Do something in response to the button
        Intent intent = new Intent(this,DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
         */

        Intent intent = new Intent(this,StockList.class);
        startActivity(intent);
    }


    //called when the user clicks the "Shopping List" button in the main menu
    public void getShopList(View view) {
        Intent intent = new Intent(this,ShopList.class);
        startActivity(intent);
    }


    //called when the user clicks the "Roll Call" button in the main menu
    public void getRollCallActivity(View view) {
        Intent intent = new Intent(this,RollCallActivity.class);
        startActivity(intent);
    }


    //called when the user clicks on the "recipes" button in the main menu
    public void getRecipeList(View view) {
        Intent intent = new Intent(this,RecipeList.class);
        startActivity(intent);
    }

}
