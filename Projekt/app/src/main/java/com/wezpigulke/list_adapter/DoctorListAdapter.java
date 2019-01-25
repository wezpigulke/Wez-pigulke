package com.wezpigulke.list_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wezpigulke.classes.Doctor;
import com.wezpigulke.R;

import java.util.List;

public class DoctorListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Doctor> results;

    public DoctorListAdapter(Context mContext, List<Doctor> results) {
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

        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.doctor_listview, null);

        TextView doctorName = v.findViewById(R.id.medicineReminder);
        TextView specializationDoctor = v.findViewById(R.id.dateVisit);
        TextView address = v.findViewById(R.id.addressVisit);
        ImageView doctorImage = v.findViewById(R.id.imageView4);

        doctorName.setText(results.get(position).getName());
        specializationDoctor.setText(results.get(position).getSpecialization());
        address.setText(results.get(position).getAddress());

        if(results.get(position).getPicture()==1) doctorImage.setImageResource(R.drawable.doctor);
        else doctorImage.setImageResource(R.drawable.doctorr);

        v.setTag(results.get(position).getId());

        return v;
    }
}
