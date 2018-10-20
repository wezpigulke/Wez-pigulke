package com.example.kuba.dsadsax;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TodayListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Today> results;

    //Constructor

    public TodayListAdapter(Context mContext, List<Today> results) {
        this.mContext = mContext;
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.today_listview, null);

        TextView medicineToday = (TextView)v.findViewById(R.id.specializationVisit);
        TextView dateToday = (TextView)v.findViewById(R.id.dateVisit);
        TextView profileToday = (TextView)v.findViewById(R.id.profileVisit);

        medicineToday.setText(results.get(position).getMedicine());
        dateToday.setText(results.get(position).getDate());
        profileToday.setText(results.get(position).getProfile());

        v.setTag(results.get(position).getId());

        return v;
    }
}
