package com.example.happinesspiggybank;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;


import androidx.appcompat.app.AppCompatActivity;

import static com.example.happinesspiggybank.HappyDbManager.TABLE_Happy;

import java.util.ArrayList;

public class BankActivity extends AppCompatActivity {
    HappyDbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        dbManager = HappyDbManager.getInstance(this);


    }
    /*
    public void getHappyData() {
        happyList = new ArrayList<>();

        String[] columns = new String[] {"_id", "date", "time", "content"};

        Cursor cursor = dbManager.query(columns, null, null, null, null, null);

        if(cursor != null) {
            while (cursor.moveToNext()) {
                happyItem currentData = new happyItem();

                currentData.setId(cursor.getInt(0));
                currentData.setDate(cursor.getString(1));
                currentData.setTime(cursor.getString(2));
                currentData.setContent(cursor.getString(3));

                happyList.add(currentData);
            }
        }
    }*/
}
/*
public class BankListAdapter extends BaseAdapter {
    Context mContext= null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<happyList>
}

 */
