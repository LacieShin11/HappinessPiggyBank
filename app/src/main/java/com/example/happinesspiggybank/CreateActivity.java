package com.example.happinesspiggybank;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.happinesspiggybank.HappyDbManager.TABLE_Happy;

public class CreateActivity extends AppCompatActivity {
    int year, month, day, hour, minute;
    Button btnDate, btnTime, btnCan, btnSave, btnHelp;
    EditText editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        final HappyDbManager dbManager = HappyDbManager.getInstance(this);


        btnDate = (Button) findViewById(R.id.date);
        btnTime = (Button) findViewById((R.id.time));
        btnCan = (Button) findViewById(R.id.cancel);
        btnSave = (Button) findViewById(R.id.save);
        editContent = (EditText) findViewById(R.id.content);

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
                showCancelDialog();
            }
        } );

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues addRowValue = new ContentValues();

                addRowValue.put("date", Integer.valueOf(btnDate.getText().toString().replace(". ", "")));
                addRowValue.put("time", Integer.valueOf(btnTime.getText().toString().replace(" : ", "")));
                addRowValue.put("content", editContent.getText().toString());

                dbManager.insert(addRowValue);

                Snackbar.make(view, "행복을 저장했습니다.", Snackbar.LENGTH_SHORT).show();
                CreateActivity.super.onBackPressed();
            }
        });
    }

    //btnDate, btnTime 클릭 이벤트
    public void mOnClick(View v) {
        switch(v.getId()) {
            case R.id.date:
                new DatePickerDialog(CreateActivity.this, dateSetListener, year, month, day).show();
                break;

            case R.id.time:
                new TimePickerDialog(CreateActivity.this, timeSetListener, hour, minute, true).show();
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
                    System.out.println(h);
                    minute = min;

                    UpdateNow();
                }
            };

    //날짜와 시간 업데이트
    void UpdateNow() {
        // 날짜별 정렬을 위해 날짜 포맷을 yyyy. MM. dd로 변경
        int adjustedMonth = month + 1;
        String date = year + "-" + adjustedMonth + "-" + day;
        Date d = Date.valueOf(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MM. dd");
        btnDate.setText(dateFormat.format(d));

        // 시간별 정렬을 위해 시간 포맷을 hh : mm으로 변경
        String time = hour + ":" + minute + ":00";
        Time t = Time.valueOf(time);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH : mm");
        btnTime.setText(timeFormat.format(t));
/*      이전 코드
        btnDate.setText(String.format("%d. %d. %d", year, month+1, day));
        btnTime.setText(String.format("%d : %d", hour, minute));*/
    }

    @Override
    public void onBackPressed() {
        showCancelDialog();
    }

    public void backPressed() {
        Toast.makeText(getApplicationContext(), "기록하는 것을 중지했습니다.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    // 취소 시 다이얼로그
    void showCancelDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final View customDialogView = inflater.inflate(R.layout.dialog_custom, null);

        TextView customDialogTitle = (TextView) customDialogView.findViewById(R.id.dialog_custom_title_textview);
        TextView customDialogGuide = (TextView) customDialogView.findViewById(R.id.dialog_custom_guide_textview);
        Button customDialogOkButton = (Button) customDialogView.findViewById(R.id.dialog_custom_ok_button);
        Button customDialogCancelButton = (Button) customDialogView.findViewById(R.id.dialog_custom_cancel_button);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog customDialog = builder.create();
        customDialog.setCanceledOnTouchOutside(false);

        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setView(customDialogView);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 다이얼로그 배경 투명하게 만들기

        customDialogTitle.setText("행복 기록을 취소하겠습니까?");
        customDialogGuide.setText("이 행복은 저장되지 않습니다. 뒤로 가시겠습니까?");

        customDialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
                finish();
            }
        });

        customDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

        customDialog.show();
    }


}
