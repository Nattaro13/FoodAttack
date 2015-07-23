package com.foodattack.foodattack;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class Tutorial extends AppIntro {

    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("Monitor food supplies",
                "Use our 'Available Food' feature to record,delete, or update the food you currently have in your fridge/ cupboard",
                R.drawable.apple,
                Color.parseColor("#3F51B5") ));

        addSlide(AppIntroFragment.newInstance("Track your shopping list",
                "Items removed from your available food will automatically go into the Shopping List. You can also add new items to the shopping list." +
                        "Click on 'Done Shopping' once you have gone to the supermarket!",
                R.drawable.apple,
                Color.parseColor("#3F51B5") ));

        addSlide(AppIntroFragment.newInstance("Record recipes",
                "Store recipes in this app and share it with your entire family!",
                R.drawable.apple,
                Color.parseColor("#3F51B5") ));

        addSlide(AppIntroFragment.newInstance("Make informed cooking choices",
                "Keep track of who will be coming back for dinner using our 'Roll Call' feature!",
                R.drawable.apple,
                Color.parseColor("#3F51B5") ));

        addSlide(AppIntroFragment.newInstance("End of Tutorial",
                "You are ready to use this app. Click on 'Done', and you're set to go!",
                R.drawable.apple,
                Color.parseColor("#3F51B5") ));

        // OPTIONAL METHODS
        // Override bar/separator color
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button
        showSkipButton(false);
        showDoneButton(true);

    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
