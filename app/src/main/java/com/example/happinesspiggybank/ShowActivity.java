package com.example.happinesspiggybank;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {
    Button btnDate, btnTime, btnCan;
    EditText editContent;
    int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show);

        btnCan = (Button) findViewById(R.id.cancel);
        btnDate = (Button) findViewById(R.id.date);
        btnTime = (Button) findViewById(R.id.time);
        editContent = (EditText) findViewById(R.id.content);

        //커서 안 보임
        editContent.setInputType(EditorInfo.TYPE_NULL);

        //입력 시 키보드 사용가능
        editContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText)view).setInputType(EditorInfo.TYPE_CLASS_TEXT);
            }
        });

        //입력 완료 후 다시 안 보임
        editContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                textView.setInputType(EditorInfo.TYPE_NULL);
                return true;
            }
        });

        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );

        Intent intent = getIntent();

        String date = intent.getExtras().getString("date");
        String time = intent.getExtras().getString("time");
        String cont = intent.getExtras().getString("content");

        btnDate.setText(date);
        btnTime.setText(time);
        editContent.setText(cont);

        //저장된 날짜와 시간 저장
        String[] arrDate = date.split(". ");
        year = Integer.parseInt(arrDate[0]);
        month = Integer.parseInt(arrDate[1]);
        day = Integer.parseInt(arrDate[2]);

        String[] arrTime = time.split(" : ");
        hour = Integer.parseInt(arrTime[0]);
        minute = Integer.parseInt(arrTime[1]);

    }

    public void mOnClick(View v) {
        switch(v.getId()) {
            case R.id.date:
                new DatePickerDialog(ShowActivity.this, dateSetListener, year, month, day).show();
                break;

            case R.id.time:
                new TimePickerDialog(ShowActivity.this, timeSetListener, hour, minute, false).show();
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
                    UpdateDate();
                }

            };

    TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int h, int min) {
                    hour = h;
                    minute = min;

                    UpdateTime();
                }
            };

    void UpdateDate() {
        btnDate.setText(String.format("%d. %d. %d", year, month+1, day));
    }
    void UpdateTime() {
        btnTime.setText(String.format("%d : %d", hour, minute));
    }
}
