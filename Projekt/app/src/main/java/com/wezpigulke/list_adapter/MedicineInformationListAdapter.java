package com.wezpigulke.list_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wezpigulke.R;
import com.wezpigulke.classes.MedicineInformation;

import java.util.List;

public class MedicineInformationListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MedicineInformation> results;

    public MedicineInformationListAdapter(Context mContext, List<MedicineInformation> results) {
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

        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.medicine_information_listview, null);

        TextView medName = v.findViewById(R.id.medNameInformation);
        TextView medTypeDose = v.findViewById(R.id.medNameInformation2);
        TextView medPack = v.findViewById(R.id.medNameInformation4);
        TextView medPrice = v.findViewById(R.id.medNameInformation5);

        medName.setText(results.get(position).getName());
        if(results.get(position).getDose().length()>0) medTypeDose.setText(results.get(position).getType() + " | " + results.get(position).getDose());
        else medTypeDose.setText(results.get(position).getType());
        medPack.setText(results.get(position).getPack());
        medPrice.setText(results.get(position).getPrice() + "zł");

        return v;
    }
}
