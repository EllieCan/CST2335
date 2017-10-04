package com.example.algonquin.androidlabs;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    protected ListView listView;
    protected EditText textView;
    protected Button send_button;

    final ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listView = findViewById(R.id.listview);
        textView = findViewById(R.id.text_message);
        send_button = findViewById(R.id.send_button);

        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = textView.getText().toString();
                list.add(inputText);

                messageAdapter.notifyDataSetChanged();
                textView.setText("");
            }
        });
    }


    private class ChatAdapter extends ArrayAdapter<String> {

        ChatAdapter(Context context){
            super(context, 0);
        }

         public int getCount(){
             return list.size();
         }

         public String getItem(int position){
             return list.get(position);
         }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result;

            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            else
                result = inflater.inflate(R.layout.sys_call_incoming, null);

            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }

    }

}
