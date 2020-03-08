package com.example.happinesspiggybank;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show);

        final HappyDbManager dbManager = HappyDbManager.getInstance(this); // DB Open

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

        // 이전 화면으로 나가기
        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bankActivityIntent = new Intent(getApplicationContext(), BankActivity.class);
                bankActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bankActivityIntent);
            }
        } );

        // 스크린 캡처하기
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 외부 저장소에 쓰기 권한이 있는지 검사
                if (ContextCompat.checkSelfPermission(getApplication(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ShowActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    } else { // 외부 저장소 쓰기 권한 요청
                        ActivityCompat.requestPermissions(ShowActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    takeScreenCapture();
                }
            }
        });

        // 행복 삭제하기
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManager.delete(id);
                // 리스트 갱신을 위해 BankActivity 다시 실행
                Intent bankActivityIntent = new Intent(getApplicationContext(), BankActivity.class);
                bankActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bankActivityIntent);
            }
        });

        // 행복 수정하기
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = btnDate.getText().toString();
                time = btnTime.getText().toString();
                cont = editContent.getText().toString();

                Log.i("date : ", date);
                Log.i("time : ", time);
                Log.i("cont : ", cont);

                dbManager.update(id, date, time, cont);

                editContent.clearFocus();
            }
        });

        date = dbManager.getDateItem(id);
        time = dbManager.getTimeItem(id);
        cont = dbManager.getContentItem(id);

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

    @Override
    public void onBackPressed() { // 뒤로가기 눌렀을 때
        // 리스트 갱신을 위해 BankActivity 다시 실행
        Intent bankActivityIntent = new Intent(getApplicationContext(), BankActivity.class);
        bankActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(bankActivityIntent);

        super.onBackPressed();
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
            if(imageFile != null){
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
                    Snackbar.make(findViewById(R.id.layout_show), "권한 거부(스크린 캡처 불가)", Snackbar.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}