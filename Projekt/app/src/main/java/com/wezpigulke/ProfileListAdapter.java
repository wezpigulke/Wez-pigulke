package com.wezpigulke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProfileListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Profile> results;

    ProfileListAdapter(Context mContext, List<Profile> results) {
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

        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.profile_listview, null);

        TextView profileName = v.findViewById(R.id.profileReminder);
        ImageView profileImage = v.findViewById(R.id.imageView5);

        profileName.setText(results.get(position).getProfile());

        if(results.get(position).getPicture()==1) profileImage.setImageResource(R.drawable.profilee);
        else profileImage.setImageResource(R.drawable.profileee);

        v.setTag(results.get(position).getId());

        return v;
    }
}
