package com.foodattack.foodattack;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.app.Application;

import java.text.BreakIterator;

import com.foodattack.foodattack.db.LoginActivityContract;
import com.foodattack.foodattack.db.LoginActivityDBHelper;

public class LoginActivity extends Activity  {


    /* #################################################################
     * ALL THE VARS GO HERE
     */
    private String applicationID = "4sWMVHznnjN5mmMwwHe4tFiMbv2lpvPTpl3HGzil";
    private String clientKey = "apqmYfADARe5bX63TdOjhjqObIAY2nIwmfxCOekn";

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
        //store to localdatabase
        //...

        //Connect to Parse database.

        //Switch interface to the main screen
        Intent intent = new Intent(this,MainActivity.class);
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
        setContentView(R.layout.activity_login);

    }


    /**
     * Saves the resolution state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
