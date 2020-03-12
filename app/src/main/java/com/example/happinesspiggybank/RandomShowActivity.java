package com.example.happinesspiggybank;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class RandomShowActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;
    static ArrayList<HappyList> happyDataList;
    Button cancleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_show);

        happyDataList = (ArrayList<HappyList>) getIntent().getSerializableExtra("happyDataList");

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        //adapterViewPager.notifyDataSetChanged();
        vpPager.setAdapter(adapterViewPager);


        cancleButton = (Button) findViewById(R.id.button_cancel);

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );

    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public int getCount() {
            return happyDataList.size();
        }

        @Override
        public Fragment getItem(int position) {
            HappyList curList = (HappyList) happyDataList.get(position);

            return FragmentRandomShow.newInstatnce(curList.getDate(), curList.getTime(), curList.getCont());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
/*
    public void destroyFragment() {
        finish();
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        adapterViewPager.notifyDataSetChanged();
    }

}
