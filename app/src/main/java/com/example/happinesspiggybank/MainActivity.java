package com.example.happinesspiggybank;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button shopBtn, creationBtn, bankBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        shopBtn = (Button) findViewById(R.id.shop);
        creationBtn = (Button) findViewById(R.id.creat_happiness);
        bankBtn = (Button) findViewById(R.id.piggy_bank);

        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shopIntent = new Intent(getApplicationContext(), ShopActivity.class);
                startActivity(shopIntent);
            }
        });

        creationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(getApplicationContext(), CreateActivity.class);
                startActivity(createIntent);
            }
        });

        bankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bankIntent = new Intent(getApplicationContext(), BankActivity.class);
                startActivity(bankIntent);
            }
        });
    }
}
