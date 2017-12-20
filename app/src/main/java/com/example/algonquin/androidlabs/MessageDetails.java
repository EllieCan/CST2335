package com.example.algonquin.androidlabs;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MessageDetails extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Intent intent = getIntent();
        Bundle arguments = new Bundle();
        arguments.putString(ChatDatabaseHelper.KEY_MESSAGE, intent.getStringExtra(ChatDatabaseHelper.KEY_MESSAGE));
        arguments.putString(ChatDatabaseHelper.KEY_ID, intent.getStringExtra(ChatDatabaseHelper.KEY_ID));
        MessageFragment frag = new MessageFragment();
        frag.setArguments(arguments);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout1, frag);
        ft.commit();

    }



}
