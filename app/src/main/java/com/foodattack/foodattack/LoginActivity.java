package com.foodattack.foodattack;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.app.Application;
import android.widget.ImageView;
import android.widget.Toast;

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
    private boolean dDone = true;

    /*#################################################################
     * START OF METHODS */


    /*
    Skip the login and enter the main menu!
     */
    public void getMain(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /*
    This method is called when the "login" button is clicked on the screen.
     */
    public void onLogin(View view) {

        //get username and password from edittext fields
        EditText rawUsername = (EditText)findViewById(R.id.login_userID);
        EditText rawPassword = (EditText)findViewById(R.id.login_userPW);
        //convert to string
        String userName = rawUsername.getText().toString();
        String userPass = rawPassword.getText().toString();

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
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
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

        //get current user from the cache, if user has logged in before.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //Switch interface to the main screen
            Log.d("Login","Login from cache!");
            changeScreen();
        }

        //else load login screen
        setContentView(R.layout.activity_login);
    }


    /*
    When the activity comes into view, load the blink feed
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //load the blink feed
        ImageView imageView = (ImageView) findViewById(R.id.login_blink_feed);
        imageView.setBackgroundResource(R.drawable.blink);
        AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
        //start animation
        anim.start();
    }


    protected void onResume() {
        super.onResume();
        //get current user from the cache, if user has logged in before.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //Switch interface to the main screen
            Log.d("Login","Login from cache!");
            changeScreen();
        }

        //else load login screen
        setContentView(R.layout.activity_login);

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
     * Saves the resolution state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
