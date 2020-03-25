package com.example.happinesspiggybank;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ShowActivity extends AppCompatActivity {
    RelativeLayout captureLayout;
    Button btnDate, btnTime, btnCan, btnCapture, btnDelete, btnModify;
    EditText editContent;
    int year, month, day, hour, minute, id;
    String date, time, cont;
    private HappyDbManager dbManager;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    boolean contentChanged, dateChanged, timeChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show);

        dbManager = HappyDbManager.getInstance(this); // DB Open

        btnCapture = (Button) findViewById(R.id.capture);
        btnDelete = (Button) findViewById(R.id.delete);
        btnModify = (Button) findViewById(R.id.modify);
        btnCan = (Button) findViewById(R.id.cancel);
        btnDate = (Button) findViewById(R.id.date);
        btnTime = (Button) findViewById(R.id.time);
        editContent = (EditText) findViewById(R.id.content);
        id = getIntent().getExtras().getInt("id");
        Log.i("intent id is ", String.valueOf(id));
        captureLayout = (RelativeLayout) findViewById(R.id.layout_show);
        captureLayout.setBackgroundColor(Color.WHITE); // 배경색이 없으므로 캡처 화면에서의 배경색 지정

        editContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editContent.setSingleLine(false);

        contentChanged = false;
        dateChanged = false;
        timeChanged = false;

        // 입력 시 키보드, 커서 보임
        editContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                editContent.setCursorVisible(true);
                contentChanged = true;

                return false;
            }
        });

        //입력 완료 후 다시 안 보임
        editContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    editContent.setCursorVisible(false);
                }
            }
        });

        // 이전 화면으로 나가기
        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contentChanged||dateChanged||timeChanged) {
                    showCancelEditDialog();
                } else {
                    Intent bankActivityIntent = new Intent(getApplicationContext(), BankActivity.class);
                    bankActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(bankActivityIntent);
                }
            }
        });

        // 스크린 캡처하기
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 외부 저장소에 쓰기 권한이 있는지 검사
                if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없는 경우 권한 선택할 수 있는 다이얼로그 생성
                    ActivityCompat.requestPermissions(ShowActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    // 권한이 있는 경우
                    takeScreenCapture();
                }
            }
        });

        // 행복 삭제하기
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog();
            }
        });

        // 행복 수정하기
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = btnDate.getText().toString().replace(". ", "");
                time = btnTime.getText().toString().replace(" : ", "");
                cont = editContent.getText().toString();

                Log.i("date : ", date);
                Log.i("time : ", time);
                Log.i("cont : ", cont);

                dbManager.update(id, Integer.valueOf(date), Integer.valueOf(time), cont);

                editContent.clearFocus();
                Snackbar.make(findViewById(R.id.layout_show), "수정 완료", Snackbar.LENGTH_SHORT).show();

                contentChanged = false;
                dateChanged = false;
                timeChanged = false;
            }
        });

        date = dbManager.getDateItem(id);
        time = dbManager.getTimeItem(id);
        cont = dbManager.getContentItem(id);

        String convertedDate = date.substring(0, 4) + ". " + date.substring(4, 6) + ". " + date.substring(6, date.length());
        String convertedTime;
        if (time.length() > 3)
            convertedTime = time.substring(0, 2) + " : " + time.substring(time.length() - 2, time.length());
        else
            convertedTime = "0" + time.substring(0, 1) + " : " + time.substring(time.length() - 2, time.length());


        btnDate.setText(convertedDate);
        btnTime.setText(convertedTime);
        editContent.setText(cont);

        //저장된 날짜와 시간 저장
        year = Integer.parseInt(date.substring(0, 4));
        month = Integer.parseInt(date.substring(4, 6));
        day = Integer.parseInt(date.substring(6, date.length()));

        if (time.length() > 3)
            hour = Integer.parseInt(time.substring(0, 2));
        else
            hour = Integer.parseInt(time.substring(0, 1));
        minute = Integer.parseInt(time.substring(time.length() - 2, time.length()));

    }

    @Override
    public void onBackPressed() { // 뒤로가기 눌렀을 때
        if(contentChanged||dateChanged||timeChanged) {
            showCancelEditDialog();
        } else {
            // 리스트 갱신을 위해 BankActivity 다시 실행
            Intent bankActivityIntent = new Intent(getApplicationContext(), BankActivity.class);
            bankActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(bankActivityIntent);

            super.onBackPressed();
        }
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
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
        // 날짜별 정렬을 위해 날짜 포맷을 yyyy. MM. dd로 변경
        int adjustedMonth = month + 1;
        String date = year + "-" + adjustedMonth + "-" + day;
        java.sql.Date d = java.sql.Date.valueOf(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MM. dd");
        btnDate.setText(dateFormat.format(d));
        dateChanged = true;
    }

    void UpdateTime() {
        String time = hour + ":" + minute + ":00";
        Time t = Time.valueOf(time);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH : mm");
        btnTime.setText(timeFormat.format(t));
        timeChanged = true;
    }

    // 삭제 시 다이얼로그
    void showDeleteDialog() {
        LayoutInflater deleteDialogInflater = getLayoutInflater();
        final View customDeleteDialogView = deleteDialogInflater.inflate(R.layout.dialog_custom, null);

        TextView customDeleteDialogTitle = (TextView) customDeleteDialogView.findViewById(R.id.dialog_custom_title_textview);
        TextView customDeleteDialogGuide = (TextView) customDeleteDialogView.findViewById(R.id.dialog_custom_guide_textview);
        Button customDeleteDialogOkButton = (Button) customDeleteDialogView.findViewById(R.id.dialog_custom_ok_button);
        Button customDeleteDialogCancelButton = (Button) customDeleteDialogView.findViewById(R.id.dialog_custom_cancel_button);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog customDeleteDialog = builder.create();
        customDeleteDialog.setCanceledOnTouchOutside(false);

        customDeleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDeleteDialog.setView(customDeleteDialogView);
        customDeleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 다이얼로그 배경 투명하게 만들기

        customDeleteDialogTitle.setText("행복을 삭제하시겠습니까?");
        customDeleteDialogGuide.setText("삭제한 행복은 복구할 수 없습니다. 삭제하시겠습니까?");

        customDeleteDialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManager.delete(id);
                // 리스트 갱신을 위해 BankActivity 다시 실행
                Intent bankActivityIntent = new Intent(getApplicationContext(), BankActivity.class);
                bankActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bankActivityIntent);
                customDeleteDialog.dismiss();
                Snackbar.make(findViewById(R.id.layout_show), "삭제 완료", Snackbar.LENGTH_SHORT).show();
            }
        });

        customDeleteDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDeleteDialog.dismiss();
            }
        });

        customDeleteDialog.show();
    }

    // 수정 내용 저장하지 않고 화면 벗어날 시 다이얼로그
    void showCancelEditDialog() {
        LayoutInflater cancelEditInflater = getLayoutInflater();
        final View customCancelEditDialogView = cancelEditInflater.inflate(R.layout.dialog_custom, null);

        TextView customCancelEditDialogTitle = (TextView) customCancelEditDialogView.findViewById(R.id.dialog_custom_title_textview);
        TextView customCancelEditDialogGuide = (TextView) customCancelEditDialogView.findViewById(R.id.dialog_custom_guide_textview);
        Button customCancelEditDialogOkButton = (Button) customCancelEditDialogView.findViewById(R.id.dialog_custom_ok_button);
        Button customCancelEditDialogCancelButton = (Button) customCancelEditDialogView.findViewById(R.id.dialog_custom_cancel_button);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog customCancelEditDialog = builder.create();
        customCancelEditDialog.setCanceledOnTouchOutside(false);

        customCancelEditDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customCancelEditDialog.setView(customCancelEditDialogView);
        customCancelEditDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 다이얼로그 배경 투명하게 만들기

        customCancelEditDialogTitle.setText("뒤로 나가시겠습니까?");
        customCancelEditDialogGuide.setText("수정된 내용이 저장되지 않았습니다.\n뒤로 나가시겠습니까?");

        customCancelEditDialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customCancelEditDialog.dismiss();
                finish();
            }
        });

        customCancelEditDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customCancelEditDialog.dismiss();
            }
        });

        customCancelEditDialog.show();
    }

    // 스크린 캡처
    private void takeScreenCapture() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyyMMdd_hh:mm:ss", now);

        try {
            // 저장 경로 설정
            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/HappinessPiggyBank");
            String path = directory.toString() + "/" + now + ".jpg";

            captureLayout.buildDrawingCache();
            Bitmap captureView = Bitmap.createBitmap(captureLayout.getDrawingCache());

            // 폴더 생성
            File rootFolder = new File(directory.toString());
            if (!rootFolder.isDirectory()) {
                rootFolder.mkdirs();
            }

            // 이미지 파일 생성
            File imageFile = new File(path);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            captureView.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            // 갤러리에 생성한 스크린 캡처 추가
            if (imageFile != null) {
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
            }

            outputStream.flush();
            outputStream.close();
            captureLayout.destroyDrawingCache(); // 이전에 캡처되었던 화면이 캡처되는 것을 방지하기 위함

            Snackbar.make(captureLayout, "캡처 완료", Snackbar.LENGTH_SHORT).show();
        } catch (Throwable e) {
            e.printStackTrace();
            Snackbar.make(captureLayout, "캡처 저장 중 오류가 발생하였습니다.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(findViewById(R.id.layout_show), "권한 승인", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(R.id.layout_show), "권한 거부(스크린 캡처 불가) / 다시 묻지 않음을 선택한 경우, 설정→앱 정보→해당 앱에서 권한을 허용해 주세요.", Snackbar.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}