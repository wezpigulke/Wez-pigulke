package com.wezpigulke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MedicineListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Medicine> results;

    MedicineListAdapter(Context mContext, List<Medicine> results) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.medicine_listview, null);

        TextView medName = v.findViewById(R.id.medName);
        TextView medQuantity = v.findViewById(R.id.medQuantity);

        medName.setText(results.get(position).getName());
        medQuantity.setText("Ilość tabletek: " + results.get(position).getQuantity());

        return v;
    }
}
