package com.simonwong.cuhkvirtualcampus;

import android.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class FrontActivity extends AppCompatActivity {

    ImageButton RouteButton, DeptButton, CallHelpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CALL_PHONE}, 1);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                // For Info day Only
                //boolean isSeemInfoDayFirstStart = getPrefs.getBoolean("SeemInfoDayFirstStart", true);
                boolean isFirstStart = getPrefs.getBoolean("FrontfirstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(FrontActivity.this, FrontIntroActivity.class);
                    final Intent j = new Intent(FrontActivity.this, FrontIntroActivity.class);

                    //i.putExtra("LaunchFrom", "front");
                    j.putExtra("LaunchFrom", "SeemInfoDay");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(j);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("FrontfirstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        RouteButton = (ImageButton)findViewById(R.id.RouteButton);
        DeptButton = (ImageButton)findViewById(R.id.DepartmentButton);
        CallHelpButton = (ImageButton)findViewById(R.id.CallHelpButton);

        RouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        DeptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DepartmentActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        CallHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CallForHelpActivity.class);

                /*Disabled, will be enable later.
                Toast toast = Toast.makeText(getApplicationContext(), "Coming soon!", Toast.LENGTH_SHORT);
                toast.show();*/

                startActivityForResult(intent, 0);
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
