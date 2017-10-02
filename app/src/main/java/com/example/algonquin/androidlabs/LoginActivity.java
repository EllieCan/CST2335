package com.example.algonquin.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.TextView;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    protected Button mButton;
    protected SharedPreferences prefs;
    private static final String MySharedPrefs = "MyPrefs";

    protected TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        mButton = findViewById(R.id.button2);
        prefs = this.getSharedPreferences(MySharedPrefs, Context.MODE_PRIVATE);
        email = findViewById(R.id.email);
        email.setText(prefs.getString("DefaultEmail", "email@domain.com"));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail  = email.getText().toString();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("DefaultEmail", inputEmail);
                editor.commit();

                //switch to the startActivity
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);

            }
        });

    }

    public void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");

    }



    public void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }


    public void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }



    public  void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }


    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
