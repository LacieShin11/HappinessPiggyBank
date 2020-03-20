package com.example.happinesspiggybank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HappyDbManager {
    static final String DB_Happy = "Happiness.db";
    static final String TABLE_Happy = "Happiness";
    static final int DB_VERSION = 1;

    public static final String ID = "_id";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String CONTENT = "content";

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
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_Happy + "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "date INTEGER, time INTEGER, content TEXT);");
    }

    //내용 추가
    public long insert(ContentValues addRowValue) {
        return mydatabase.insert(TABLE_Happy, null, addRowValue);
    }

    // 내용 삭제
    public void delete(int id) {
        String sql = "DELETE FROM " + TABLE_Happy + " WHERE " + TABLE_Happy + "." + ID + "='" + id + "';";
        Log.d("delete success : ", Integer.toString(id));
        mydatabase.execSQL(sql);
    }

    // 내용 업데이트
    public long update(int update_id, Integer date, Integer time, String content) {
        Cursor c = mydatabase.rawQuery("select * from " + TABLE_Happy + " where " + TABLE_Happy + "." + ID + "= '" + update_id + "';", null);

        Log.i("", "update success");

        ContentValues updateRowValues = new ContentValues();
        updateRowValues.put("date", date);
        updateRowValues.put("time", time);
        updateRowValues.put("content", content);

        return mydatabase.update(TABLE_Happy, updateRowValues, "_id = " + update_id, null);
    }

    //내용 보기
    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderby) {
        return mydatabase.query(TABLE_Happy, columns, selection, selectionArgs, groupBy, having, orderby);
    }

    // DB 상의 행복 id 데이터를 다 가져와서 배열로 return
    // 행복 삭제 이벤트 시 listview 상의 id와 DB 상의 id가 달라지기 때문에 사용
    public int[] getID() {
        Cursor c = mydatabase.rawQuery("Select * from " + TABLE_Happy + " order by date DESC, time DESC;", null);
        int[] IDArray = new int[c.getCount()];
        int i = 0;

        while (c.moveToNext()) {
            IDArray[i] = c.getInt(c.getColumnIndex("_id"));
            i++;
        }
        return IDArray;
    }

    // DB 상의 id에 해당하는 date 값 return
    public String getDateItem(int id){
        Cursor c = mydatabase.rawQuery("Select * from " + TABLE_Happy + " where _id = " + id + ";", null);
        String dateItem = "null";

        while (c.moveToNext()){
            dateItem = c.getString(c.getColumnIndex("date"));
        }

        Log.i("dateItem : ", dateItem);

        return dateItem;
    }

    // DB 상의 id에 해당하는 time 값 return
    public String getTimeItem(int id){
        Cursor c = mydatabase.rawQuery("Select * from " + TABLE_Happy + " where _id = " + id + ";", null);
        String timeItem = "null";

        while (c.moveToNext()){
            timeItem = c.getString(c.getColumnIndex("time"));
        }

        Log.i("timeItem : ", timeItem);

        return timeItem;
    }

    // DB 상의 id에 해당하는 content 값 return
    public String getContentItem(int id){
        Cursor c = mydatabase.rawQuery("Select * from " + TABLE_Happy + " where _id = " + id + ";", null);
        String contentItem = "null";

        while (c.moveToNext()){
            contentItem = c.getString(c.getColumnIndex("content"));
        }

        Log.i("contentItem : ", contentItem);

        return contentItem;
    }

    //레코드 개수 반환
    public int getCntQuery() {
        int cnt = 0;
        Cursor cursor = mydatabase.rawQuery("select * from " + TABLE_Happy, null);
        cnt = cursor.getCount();
        return cnt;
    }
}
