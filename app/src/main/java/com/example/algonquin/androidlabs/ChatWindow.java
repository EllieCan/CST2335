package com.example.algonquin.androidlabs;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.algonquin.androidlabs.ChatDatabaseHelper.TABLE_NAME;
import static com.example.algonquin.androidlabs.StartActivity.ACTIVITY_NAME;

public class ChatWindow extends FragmentActivity {

    protected ListView listView;
    protected EditText textView;
    protected Button send_button;
    private boolean isFrameLayoutExist = false;
    private Cursor cursor;
    private ChatAdapter messageAdapter;
    private MessageFragment fragment;
    private FragmentTransaction ft;


    private ArrayList<String> list = new ArrayList<>();

    ChatDatabaseHelper helper = new ChatDatabaseHelper(this);
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        isFrameLayoutExist = false;
        if(findViewById(R.id.fragment_container_two) != null){
            isFrameLayoutExist = true;
        }else {

        }
        
        database = helper.getWritableDatabase();

        String query = "select * from " + TABLE_NAME;
        cursor = database.rawQuery(query, new String[]{});
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " +
                    cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            list.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor’s column count = " + cursor.getColumnCount() );
        for (int i = 0; i<cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME, "Cursor’s column name " + i + " = " + cursor.getColumnName(i) );
        }

        listView = findViewById(R.id.listview);
        textView = findViewById(R.id.text_message);
        send_button = findViewById(R.id.send_button);

        //in this case, “this” is the ChatWindow, which is-A Context object
        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = textView.getText().toString();
                list.add(inputText);
                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, inputText);
                database.insert(com.example.algonquin.androidlabs.ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID, values);
                messageAdapter.notifyDataSetChanged();
                textView.setText("");
                String query = "select * from " + TABLE_NAME;
                cursor = database.rawQuery(query, new String[]{});
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {

                Context context = v.getContext();
                String message = messageAdapter.getItem(position);
                long id = messageAdapter.getItemId(position);
                Bundle arguments = new Bundle();
                arguments.putString(ChatDatabaseHelper.KEY_MESSAGE, message);
                arguments.putString(ChatDatabaseHelper.KEY_ID, id+"");

                if(isFrameLayoutExist){
                    fragment = new MessageFragment(ChatWindow.this);
                    fragment.setArguments(arguments);
                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container_two, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }else{
                    Intent intent = new Intent(context, MessageDetails.class);
                    intent.putExtra(ChatDatabaseHelper.KEY_MESSAGE, message);
                    intent.putExtra(ChatDatabaseHelper.KEY_ID, id+"");
                    startActivityForResult(intent, 1);
                }
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

        public long getItemId (int position){
            cursor.moveToPosition(position);
            String id = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
            long key_id = Long.parseLong(id);
            return key_id;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String id = data.getStringExtra(ChatDatabaseHelper.KEY_ID);
            String sql = "delete from " + ChatDatabaseHelper.TABLE_NAME + " where " + ChatDatabaseHelper.KEY_ID + " = " + id;
            database.execSQL(sql);

            String query = "select * from " + TABLE_NAME;
            cursor = database.rawQuery(query, new String[]{});
            cursor.moveToFirst();

            list = new ArrayList<>();
            while(!cursor.isAfterLast()) {
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " +
                        cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                list.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                cursor.moveToNext();
            }

            messageAdapter.notifyDataSetChanged();


        }
    }
    protected void deleteMessage(String keyId) {
            String sql = "delete from " + ChatDatabaseHelper.TABLE_NAME + " where " + ChatDatabaseHelper.KEY_ID + " = " + keyId;
            database.execSQL(sql);
            ft = getFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();

            String query = "select * from " + TABLE_NAME;
            cursor = database.rawQuery(query, new String[]{});
            cursor.moveToFirst();

            list = new ArrayList<>();
            while(!cursor.isAfterLast()) {
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " +
                        cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                list.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                cursor.moveToNext();
            }

            messageAdapter.notifyDataSetChanged();
        }

}
