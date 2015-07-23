package com.foodattack.foodattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
            changeScreenSettings();
            return true;

        } else if (id == R.id.parse_log_out) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser == null) {
                Log.d("LogOut", "Successfully logged out");
                changeScreenLogout();
            }
            return true;
        } else if (id == R.id.about_app) {
            aboutApp();

            return true;
        } else if (id == R.id.app_tutorial) {
            viewTutorial();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /*
    Bring user to the Tutorial
     */
    public void viewTutorial() {
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
    }


    /*
    Displays information about the app when the "About" button is clicked
    on the action bar
     */
    public void aboutApp()  {
        String msg = "This app was created by Xue Hui and Yi Yan for NUS Orbital 2015.";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About this app");
        builder.setMessage(msg);
        final AlertDialog alertDialog = builder.create();

        //Re-Enable this when we do a custom style for alert dialog
        //final AlertDialog alertDialog = builder.create();
        //LayoutInflater mInflater = alertDialog.getLayoutInflater();
        //View dialogLayout = mInflater.inflate(R.layout.dialog_login_error, null);

        //builder.setView(dialogLayout);

        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                alertDialog.cancel();
            }
        });
        builder.create().show();

    }


    /*
    Bring user to the Settings page
     */
    public void changeScreenSettings() {
        Intent intent = new Intent(this, MainSettingsActivity.class);
        startActivity(intent);
    }


    /*
   Upon successful log out, bring the user to the login page.
    */
    public void changeScreenLogout() {
        //Switch interface to the main screen
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    /*
    Executed when back button on the phone pad is pressed.
    Brings user to home screen of their android phone
    ie. EXIT APP.
     */
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

        Intent intent = new Intent(this,StockListActivityParse.class);
        startActivity(intent);
    }


    //called when the user clicks the "Shopping List" button in the main menu
    public void getShopList(View view) {
        Intent intent = new Intent(this,ShopListActivityParse.class);
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
