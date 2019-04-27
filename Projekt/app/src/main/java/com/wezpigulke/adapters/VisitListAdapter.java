package com.wezpigulke.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wezpigulke.classes.Visit;
import com.wezpigulke.R;

import java.util.List;

public class VisitListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Visit> results;

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

        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.visit_listview, null);

        TextView profileVisit = v.findViewById(R.id.profileReminder);
        TextView nameVisit = v.findViewById(R.id.nameVisit);
        TextView specializationVisit = v.findViewById(R.id.medicineReminder);
        TextView dateVisit = v.findViewById(R.id.dateVisit);

        profileVisit.setText(results.get(position).getProfile());
        nameVisit.setText(results.get(position).getName());
        specializationVisit.setText(results.get(position).getSpecialization());
        dateVisit.setText(results.get(position).getDate());

        v.setTag(results.get(position).getId());

        return v;
    }
}
