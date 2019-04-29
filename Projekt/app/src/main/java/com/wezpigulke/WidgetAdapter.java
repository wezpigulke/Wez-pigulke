package com.wezpigulke;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.wezpigulke.classes.Today;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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

        String dzisiejszaData = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String dzisiejszyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat tdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        if(cursor.getCount()!=0) {
            while(cursor.moveToNext()) {

                Date firstDate = null;
                Date secondDate = null;

                Date firstTime = null;
                Date secondTime = null;

                try {
                    firstDate = sdf.parse(dzisiejszaData);
                    firstTime = tdf.parse(dzisiejszyCzas);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    secondDate = sdf.parse(cursor.getString(4));
                    secondTime = tdf.parse(cursor.getString(3));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long diff = Objects.requireNonNull(secondDate).getTime() - Objects.requireNonNull(firstDate).getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                long diffInMillis = Objects.requireNonNull(secondTime).getTime() - Objects.requireNonNull(firstTime).getTime();

                if (diffDays == 0 && diffInMillis >= 0) {
                    todayArrayList.add(new Today(cursor.getInt(0), cursor.getString(1) + " (Dawka: " + cursor.getString(2) + ")", "Godzina: " + cursor.getString(3), cursor.getString(5)));
                }

            }
        } else {
            todayArrayList.add(new Today(-1, "Brak przypomnień w dniu dzisiejszym", "", ""));
        }
        cursor.close();
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
