package com.foodattack.foodattack;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Xue Hui on 4/7/2015.
 */
public class NewUserActivity extends Activity {

    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }


    /*
    When the "sign up" button is clicked on this screen,
    the details will be saved to the cloud (if successful) and bring the user to the main activity screen.
    If not, an error message will be shown.
     */
    public void onCreateNewUser(View view) {
        //get username and password from edittext fields
        EditText rawUsername = (EditText)findViewById(R.id.new_userID);
        EditText rawPassword = (EditText)findViewById(R.id.new_userPW);
        EditText rawPassword2 = (EditText)findViewById(R.id.re_key_userPW);
        //convert to string
        final String userName = rawUsername.getText().toString();
        String userPass = rawPassword.getText().toString();
        String userPass2 = rawPassword2.getText().toString();

        //both passwords match, let them sign up.
        if (userPass.compareTo(userPass2) == 0) {
            ParseUser user = new ParseUser();
            user.setUsername(userName);
            user.setPassword(userPass);

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        ParseObject newFamily = new ParseObject("Family");
                        newFamily.put("Owner",userName);
                        newFamily.put("familyID",userName);
                        newFamily.put("Breakfast",false);
                        newFamily.put("Lunch",false);
                        newFamily.put("Dinner",false);
                        newFamily.saveInBackground();
                        //switch to the main screen since sign-up successful.
                        changeScreenMain();
                    } else {
                        // Sign up didn't succeed, conflicts in user-id
                        showAlertUserID();
                    }
                }
            });

        } else {
            showAlertPassword();
        }
    }


    /*
    This alert is displayed when sign up fails.
     */
    public void showAlertUserID() {
        Toast.makeText(getApplicationContext(), "Error: UserID unavailable", Toast.LENGTH_SHORT).show();
    }


    /*
    This alert is displayed when passwords do not match.
     */
    public void showAlertPassword() {
        Toast.makeText(getApplicationContext(), "Error: Passwords do not match", Toast.LENGTH_SHORT).show();

    }


    /*
    Brings the user to the main activity screen
     */
    public void changeScreenMain() {
        //Switch interface to the main screen
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    /**
     * Saves the resolution state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



}
