package com.example.kuba.dsadsax;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class NotesListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Notes> results;

    //Constructor

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

        View v = View.inflate(mContext, R.layout.notes_listview, null);

        TextView profileNotes = (TextView)v.findViewById(R.id.profileNotes);
        TextView dateNotes = (TextView)v.findViewById(R.id.dateNotes);
        TextView nameNotes = (TextView)v.findViewById(R.id.nameNotes2);
        TextView textNotes = (TextView) v.findViewById(R.id.textNotes);

        profileNotes.setText(results.get(position).getProfile());
        dateNotes.setText(results.get(position).getDate());
        nameNotes.setText(results.get(position).getTitle());
        textNotes.setText(results.get(position).getText());

        v.setTag(results.get(position).getId());

        return v;
    }
}
