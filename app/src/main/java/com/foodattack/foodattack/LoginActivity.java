package com.foodattack.foodattack;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.app.Application;

import java.text.BreakIterator;
import java.util.List;

import com.foodattack.foodattack.db.LoginActivityContract;
import com.foodattack.foodattack.db.LoginActivityDBHelper;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginActivity extends Activity  {


    /* #################################################################
     * ALL THE VARS GO HERE
     */
    private final String applicationID = "4sWMVHznnjN5mmMwwHe4tFiMbv2lpvPTpl3HGzil";
    private final String clientKey = "apqmYfADARe5bX63TdOjhjqObIAY2nIwmfxCOekn";

    /*#################################################################
     * START OF METHODS */


    /*
    Skip the login and enter the main menu!
     */
    public void getMain(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    public void onLogin(View view) {
        //get username and password from edittext fields
        EditText rawUsername = (EditText)findViewById(R.id.login_userID);
        EditText rawPassword = (EditText)findViewById(R.id.login_userPW);
        //convert to string
        String userName = rawUsername.getText().toString();
        String userPass = rawPassword.getText().toString();
        //store to local database
        //...

        boolean userMatched = false;
        //Connect to Parse database.
        ParseUser.logInInBackground(userName, userPass, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    Log.d("Login", "Login Successful");
                    changeScreen();
                } else {
                    Log.d("Login", "Login failed");
                    showAlert();

                }
            }
        });
    }


    /*
    This alert is displayed when login fails.
     */
    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Login failed. Please re-enter your ID and password.");

        //Re-Enable this when we do a custom style for alert dialog
        //final AlertDialog alertDialog = builder.create();
        //LayoutInflater mInflater = alertDialog.getLayoutInflater();
        //View dialogLayout = mInflater.inflate(R.layout.dialog_login_error, null);

        //builder.setView(dialogLayout);

        builder.setNegativeButton("Ok", null);
        builder.create().show();
    }


    /*
    Upon successful login, bring the user to the main screen.
     */
    public void changeScreen() {
        //Switch interface to the main screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    /*
    When the user presses the "Create New Account" button on this login screen,
    it will bring him to the "create new user" activity.
     */
    public void onCreateAccBtn(View view) {
        //Switch interface to the create account page
        Intent intent = new Intent(this,NewUserActivity.class);
        startActivity(intent);

    }



    @Override
    protected void onStart() {
        super.onStart();

        ActionBar actionBar = getActionBar();
        actionBar.hide();
        //TO DO!
        //make this part check for any available details, then skip this screen if there's a
        //available username and password
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, applicationID, clientKey);

        //get current user from the cache, if user has logged in before.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //Switch interface to the main screen
            Log.d("Login","Login from cache!");
            changeScreen();
        }

        //else load login screen
        setContentView(R.layout.activity_login);



        /*
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        */
    }


    /**
     * Saves the resolution state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
