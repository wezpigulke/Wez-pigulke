package com.wezpigulke.list_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wezpigulke.classes.Notes;
import com.wezpigulke.R;

import java.util.List;

public class NotesListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Notes> results;

    public NotesListAdapter(Context mContext, List<Notes> results) {
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

        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.notes_listview, null);

        TextView profileNotes = v.findViewById(R.id.profileNotes);
        TextView dateNotes = v.findViewById(R.id.dateNotes);
        TextView nameNotes = v.findViewById(R.id.nameNotes2);

        profileNotes.setText(results.get(position).getProfile());
        dateNotes.setText(results.get(position).getDate());
        nameNotes.setText(results.get(position).getTitle());

        v.setTag(results.get(position).getId());

        return v;
    }
}
