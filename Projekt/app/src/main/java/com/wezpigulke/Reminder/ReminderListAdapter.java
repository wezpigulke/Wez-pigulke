package com.wezpigulke.Reminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wezpigulke.R;

import java.util.List;

public class ReminderListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Reminder> results;

    ReminderListAdapter(Context mContext, List<Reminder> results) {
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

        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.reminder_listview, null);

        TextView medicineName = v.findViewById(R.id.medicineReminder);
        TextView dateMedicine = v.findViewById(R.id.dateVisit);
        TextView howManyDays = v.findViewById(R.id.howManyDays);
        TextView profileName = v.findViewById(R.id.profileReminder);

        medicineName.setText(results.get(position).getMedicine());
        dateMedicine.setText(results.get(position).getDate());
        howManyDays.setText(results.get(position).getHowManyDays());
        profileName.setText(results.get(position).getProfile());

        v.setTag(results.get(position).getId());

        return v;
    }
}
