package com.wezpigulke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MeasurementListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Measurement> results;

    //Constructor

    MeasurementListAdapter(Context mContext, List<Measurement> results) {
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

        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.measurement_listview, null);

        TextView measurementType = v.findViewById(R.id.typeMeasurement);
        TextView measurementResult = v.findViewById(R.id.resultMeasurement);
        TextView measurementProfile = v.findViewById(R.id.profileMeasurement);
        TextView measurementDate = v.findViewById(R.id.dateMeasurement);
        TextView measurementHour = v.findViewById(R.id.dateMeasurement2);

        measurementType.setText(results.get(position).getType());
        measurementResult.setText(results.get(position).getResult());
        measurementProfile.setText(results.get(position).getProfile());
        measurementDate.setText(results.get(position).getDate());
        measurementHour.setText(results.get(position).getHour());

        v.setTag(results.get(position).getId());

        return v;
    }
}
