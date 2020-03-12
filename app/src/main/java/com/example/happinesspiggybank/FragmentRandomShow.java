package com.example.happinesspiggybank;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FragmentRandomShow extends Fragment {
    TextView dateTextview, timeTextview, contentTextview;
    String dates, times, conts;
    static ArrayList<HappyList> happyDataList;
    //Button cancleButton;

    public static FragmentRandomShow newInstatnce(String date, String time, String cont) {
        FragmentRandomShow fragment = new FragmentRandomShow();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        bundle.putString("time", time);
        bundle.putString("content", cont);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_show, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            //bundle = getArguments();

            dates = bundle.getString("date");
            times = bundle.getString("time");
            conts = bundle.getString("content");

        } else Log.i("random: bundle is ", "null");

        dateTextview = (TextView) view.findViewById(R.id.textview_date);
        timeTextview = (TextView) view.findViewById(R.id.textview_time);
        contentTextview = (TextView) view.findViewById(R.id.textview_content);

        dateTextview.setText(dates);
        timeTextview.setText(times);
        contentTextview.setText(conts);

        /*
        cancleButton = (Button) view.findViewById(R.id.button_cancel);

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RandomShowActivity)getActivity()).destroyFragment();
            }
        } );*/

        return view;
    }


}
