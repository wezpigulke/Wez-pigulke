package com.wezpigulke.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wezpigulke.R;
import com.wezpigulke.classes.Doctor;

import java.util.List;

public class DoctorSpinnerAdapter extends ArrayAdapter<Doctor> {

    private List<Doctor> doctorList;

    public DoctorSpinnerAdapter(Context context, List<Doctor> doctorList) {
        super(context, 0, doctorList);
        this.doctorList = doctorList;
    }

    @Override
    public int getCount() {
        return (doctorList.size() - 1);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.doctor_spinner, parent, false
            );
        }

        TextView name = convertView.findViewById(R.id.nameOfDoctor);
        TextView address = convertView.findViewById(R.id.addressDoctor);
        TextView specialization = convertView.findViewById(R.id.specializationDoctor);

        Doctor currentItem = getItem(position);

        if (currentItem != null) {
            name.setText(currentItem.getName());
            address.setText(currentItem.getAddress());
            specialization.setText(currentItem.getSpecialization());
        }

        return convertView;
    }
}