package com.example.happinesspiggybank;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateActivity extends AppCompatActivity {
    int year, month, day, hour, minute;
    Button btnDate, btnTime;
    private String Tag = "clickTest";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        btnDate = (Button) findViewById(R.id.date);
        btnTime = (Button) findViewById((R.id.time));

        Calendar cal = new GregorianCalendar();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        UpdateNow();
    }

    public void mOnClick(View v) {
        Log.i(Tag,"onClick: " + v.getId());
        switch(v.getId()) {
            case R.id.date:
                new DatePickerDialog(CreateActivity.this, dateSetListener, year, month, day).show();
                break;

            case R.id.time:
                new TimePickerDialog(CreateActivity.this, timeSetListener, hour, minute, false).show();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int y, int m, int d) {
                    year = y;
                    month = m;
                    day = d;
                    UpdateNow();
                }
            };

    TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int h, int min) {
                    hour = h;
                    minute = min;

                    UpdateNow();
                }
            };

    void UpdateNow() {
        btnDate.setText(String.format("%d. %d. %d", year, month+1, day));
        btnTime.setText(String.format("%d : %d", hour, minute));
    }
}
