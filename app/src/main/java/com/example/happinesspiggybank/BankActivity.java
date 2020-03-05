package com.example.happinesspiggybank;


import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BankActivity extends AppCompatActivity {
    HappyDbManager dbManager;
    ArrayList<HappyList> happyDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        dbManager = HappyDbManager.getInstance(this);

        this.getHappyData();

        ListView listView = (ListView)findViewById(R.id.happyList);
        final BankListAdapter myAdapter = new BankListAdapter(this, happyDataList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), myAdapter.getItem(position).getDate(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getHappyData() {
        happyDataList = new ArrayList<>();

        String[] columns = new String[] {"_id", "date", "time", "content"};

        Cursor cursor = dbManager.query(columns, null, null, null, null, null);

        if(cursor != null) {
            while (cursor.moveToNext()) {
                HappyList currentData = new HappyList();

                currentData.setId(cursor.getInt(0));
                currentData.setDate(cursor.getString(1));
                currentData.setTime(cursor.getString(2));
                currentData.setContent(cursor.getString(3));

                happyDataList.add(currentData);
            }
        }
    }

}




