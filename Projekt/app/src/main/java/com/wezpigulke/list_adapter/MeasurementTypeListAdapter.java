package com.wezpigulke.list_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wezpigulke.classes.MeasurementType;
import com.wezpigulke.R;

import java.util.List;

public class MeasurementTypeListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MeasurementType> results;

    public MeasurementTypeListAdapter(Context mContext, List<MeasurementType> results) {
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
        @SuppressLint("ViewHolder") View v = View.inflate(mContext,
                R.layout.measurement_type_listview, null);
        TextView measurementName = v.findViewById(R.id.measurementTypeName);
        measurementName.setText(results.get(position).getProfile());
        v.setTag(results.get(position).getId());
        return v;
    }
}





