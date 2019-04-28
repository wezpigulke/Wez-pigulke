package com.wezpigulke;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.wezpigulke.classes.Today;

import java.util.ArrayList;

public class WidgetAdapter implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private ArrayList<Today> todayArrayList;

    WidgetAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        Log.d("WidgetAdapter", "onCreate()");
    }

    @Override
    public void onDataSetChanged() {

        Log.d("WidgetAdapter", "onDataSetChanged()");

        DatabaseHelper myDb = new DatabaseHelper(context);
        Cursor cursor = myDb.getAllData_NOTYFIKACJA();
        todayArrayList = new ArrayList<>();

        if(cursor!=null) {
            while(cursor.moveToNext()) {
                todayArrayList.add(new Today(cursor.getInt(0), cursor.getString(1) + " (Dawka: " + cursor.getString(2) + ")", "Godzina: " + cursor.getString(3), cursor.getString(4)));
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return todayArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Log.d("WidgetAdapter", "getViewAt()");

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item);
        remoteViews.setTextViewText(R.id.profileReminderWidget, todayArrayList.get(position).getProfile());
        remoteViews.setTextViewText(R.id.medicineReminderWidget, todayArrayList.get(position).getMedicine());
        remoteViews.setTextViewText(R.id.dateVisitWidget, todayArrayList.get(position).getDate());

        Intent intent = new Intent();
        remoteViews.setOnClickFillInIntent(R.id.list_item, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
