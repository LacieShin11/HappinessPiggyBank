package com.example.happinesspiggybank;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.happinesspiggybank.HappyDbManager.TABLE_Happy;

public class CreateActivity extends AppCompatActivity {
    int year, month, day, hour, minute;
    Button btnDate, btnTime, btnCan, btnSave, btnHelp;
    EditText content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        final HappyDbManager dbManager = HappyDbManager.getInstance(this);


        btnDate = (Button) findViewById(R.id.date);
        btnTime = (Button) findViewById((R.id.time));
        btnCan = (Button) findViewById(R.id.cancel);
        btnSave = (Button) findViewById(R.id.save);
        content = (EditText) findViewById(R.id.content);

        Calendar cal = new GregorianCalendar();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        UpdateNow();

        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        } );

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues addRowValue = new ContentValues();

                addRowValue.put("date", btnDate.getText().toString());
                addRowValue.put("time", btnTime.getText().toString());
                addRowValue.put("content", content.getText().toString());

                dbManager.insert(addRowValue);

                Toast.makeText(getApplicationContext(), "행복을 저장했습니다.", Toast.LENGTH_SHORT).show();
                CreateActivity.super.onBackPressed();
            }
        });
    }

    public void mOnClick(View v) {
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

    @Override
    public void onBackPressed() {
        show();
    }

    public void backPressed() {
        Toast.makeText(this, "기록하는 것을 중지했습니다.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("행복 기록을 취소하겠습니까?");
        builder.setMessage("이 행복은 저장되지 않습니다. 뒤로 가시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        backPressed();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }


}
