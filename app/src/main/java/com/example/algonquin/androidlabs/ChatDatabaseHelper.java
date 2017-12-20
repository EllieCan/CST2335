package com.example.algonquin.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Algonquin on 2017-10-09.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {


    //All declared to static
    //Database name
    static final String DATABASE_NAME = "ChatMessage.db";

    //Database version
    static final int VERSION_NUM = 5;

    //Table name
    static final String TABLE_NAME = "Chat";

    //Table Columns name
    static final String KEY_ID = "id";

    static final String KEY_MESSAGE = "Message";

    // Database creation sql statement
    private static final String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT" + ")";


    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }


    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(CREATE_MESSAGE_TABLE);
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
