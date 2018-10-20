package com.example.kuba.dsadsax;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class VisitListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Visit> results;

    //Constructor

    public VisitListAdapter(Context mContext, List<Visit> results) {
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

        View v = View.inflate(mContext, R.layout.visit_listview, null);

        TextView profileVisit = (TextView)v.findViewById(R.id.profileVisit);
        TextView nameVisit = (TextView)v.findViewById(R.id.nameVisit);
        TextView specializationVisit = (TextView)v.findViewById(R.id.specializationVisit);
        TextView dateVisit = (TextView)v.findViewById(R.id.dateVisit);
        TextView addressVisit = (TextView)v.findViewById(R.id.addressVisit);

        profileVisit.setText(results.get(position).getProfile());
        nameVisit.setText(results.get(position).getName());
        specializationVisit.setText(results.get(position).getSpecialization());
        dateVisit.setText(results.get(position).getDate());
        addressVisit.setText(results.get(position).getAddress());

        v.setTag(results.get(position).getId());

        return v;
    }
}
