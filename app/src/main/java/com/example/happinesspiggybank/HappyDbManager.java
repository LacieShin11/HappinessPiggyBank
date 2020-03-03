package com.example.happinesspiggybank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HappyDbManager {
    static final String DB_Happy = "Happiness.db";
    static final String TABLE_Happy = "Happiness";
    static final int DB_VERSION = 1;

    Context myContext = null;

    private static HappyDbManager myDBManager = null;
    private SQLiteDatabase mydatabase = null;

    public static HappyDbManager getInstance(Context context) {
        if(myDBManager == null) {
            myDBManager = new HappyDbManager(context);
        }
        return  myDBManager;
    }

    private HappyDbManager(Context context) {
        myContext = context;

        //DB 열기
        mydatabase = context.openOrCreateDatabase(DB_Happy, context.MODE_PRIVATE, null);

        //Table 생성
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_Happy + "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "date TEXT, time TEXT, content TEXT);");
    }

    //내용 추가
    public long insert(ContentValues addRowValue) {
        return mydatabase.insert(TABLE_Happy, null, addRowValue);
    }

    //내용 보기
    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderby) {
        return mydatabase.query(TABLE_Happy, columns, selection, selectionArgs, groupBy, having, orderby);
    }
}
