package com.example.algonquin.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {


    public static final String ACTIVITY_NAME = "StartActivity";

    protected Button myButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        myButton = findViewById(R.id.button);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,ListItemActivity.class);
                startActivityForResult(intent, 10);
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 10
        if(requestCode==10)
        {
            Log.i(ACTIVITY_NAME, "Return to StartActivity.onActivityResult");
        }

        if(resultCode==Activity.RESULT_OK)        {

            String messagePassed = data.getStringExtra("Response");

            CharSequence text = "ListItemsActivity passed: " + messagePassed;
            Toast toast = Toast.makeText(StartActivity.this, text, Toast.LENGTH_LONG);
            toast.show();
        }
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
