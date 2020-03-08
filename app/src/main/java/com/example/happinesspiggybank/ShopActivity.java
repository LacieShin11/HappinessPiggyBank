package com.example.happinesspiggybank;

import android.os.Bundle;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {
    TabHost shoppingCategoryTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shoppingCategoryTabHost = (TabHost) findViewById(R.id.tabhost_shopping_category);
        shoppingCategoryTabHost.setup();

        // 염색 탭
        TabHost.TabSpec dyeingTab = shoppingCategoryTabHost.newTabSpec("dyeing"); // 'dyeing' 태그를 가진 TabSpec 객체 생성
        dyeingTab.setContent(R.id.dyeing_tab); // 탭이 선택됐을 때 표시 될 content 뷰에 대한 resource id 지정
        dyeingTab.setIndicator("염색"); // 탭에 표시 될 문구 지정
        shoppingCategoryTabHost.addTab(dyeingTab); // TabHost에 탭 추가

        // 의류 탭
        TabHost.TabSpec clothesTab = shoppingCategoryTabHost.newTabSpec("clothes"); // 'dyeing' 태그를 가진 TabSpec 객체 생성
        clothesTab.setContent(R.id.clothes_tab); // 탭이 선택됐을 때 표시 될 content 뷰에 대한 resource id 지정
        clothesTab.setIndicator("의류"); // 탭에 표시 될 문구 지정
        shoppingCategoryTabHost.addTab(clothesTab); // TabHost에 탭 추가

        // 악세사리 탭
        TabHost.TabSpec accessoriesTab = shoppingCategoryTabHost.newTabSpec("accessories"); // 'dyeing' 태그를 가진 TabSpec 객체 생성
        accessoriesTab.setContent(R.id.accessories_tab); // 탭이 선택됐을 때 표시 될 content 뷰에 대한 resource id 지정
        accessoriesTab.setIndicator("악세사리"); // 탭에 표시 될 문구 지정
        shoppingCategoryTabHost.addTab(accessoriesTab); // TabHost에 탭 추가
    }
}
