package com.example.happinesspiggybank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//ListView 어댑터 정의
public class BankListAdapter extends BaseAdapter {
    Context mContext= null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<HappyList> happySample;

    public BankListAdapter(Context context, ArrayList<HappyList> data) {
        mContext = context;
        happySample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return happySample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public HappyList getItem(int position) {
        return happySample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_view, null);

        TextView date = (TextView) view.findViewById(R.id.dateList);
        TextView time = (TextView) view.findViewById(R.id.timeList);

        date.setText(happySample.get(position).getDate());
        time.setText(happySample.get(position).getTime());

        return view;
    }
}

