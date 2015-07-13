package com.foodattack.foodattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class MainSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_settings, menu);
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


    //############################################################################################
    //THIS PART IS ALL FOR ADD FAMILY MEMBER
    //############################################################################################
    /*
    Called when the "add family member" button is clicked
     */
    public void addFamily(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a user to your family");
        builder.setMessage("Please key in your family member's UserID.");
        final AlertDialog alertDialog = builder.create();

        LayoutInflater mInflater = alertDialog.getLayoutInflater();
        View dialogLayout = mInflater.inflate(R.layout.dialog_settings_add_family, null);
        builder.setView(dialogLayout);

        //initialise the EditText data from the xml file.
        final EditText rawFamilyName = (EditText) dialogLayout.findViewById(R.id.family_member_name);


        builder.setPositiveButton("Add family member", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                //convert to string
                String familyName = rawFamilyName.getText().toString();

                //check if that member exists on the parse database
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
                query.whereEqualTo("Owner", familyName);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        if ((e == null) && (!objects.isEmpty())) {
                            //If the query was successful, set the familyID of the new member to
                            //that of the adder/head of the family.
                            ParseObject newMember = objects.get(0);
                            Log.d("New Member Name", newMember.getObjectId());
                            getFamilyID(newMember.getObjectId());

                        } else {
                            alertDialog.cancel();
                            showAlertNoSuchUser();
                        }
                    }
                });
            }
        });

        builder.create().show();

    }


    public void getFamilyID(String newMemberID) {
        final String newMemberObjectID = newMemberID;

        ParseQuery <ParseObject> query = ParseQuery.getQuery("Family");
        query.whereEqualTo("Owner", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("Current User",ParseUser.getCurrentUser().getUsername());
                    ParseObject userData = objects.get(0);
                    final String familyID = userData.getString("familyID");
                    Log.d("Curr User Family ID", familyID);

                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Family");

                    query2.getInBackground(newMemberObjectID, new GetCallback<ParseObject>() {
                        public void done(ParseObject newMember, ParseException e) {
                            if (e == null) {
                                newMember.put("familyID", familyID);
                                Log.d("New Family ID updated", familyID);
                                newMember.saveInBackground();
                            } else {
                                Log.d("Unable to update",e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }


    /*
    This alert is displayed when sign up fails.
     */
    public void showAlertNoSuchUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("No such user exists.");
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

    //###########################################################################################


    /*
    Called when the "remove family member" button is clicked
     */
    public void removeFamily(View view) {

    }


    /*
    Called when the "view family status" button is clicked
     */
    public void viewFamily(View view) {

    }
}
