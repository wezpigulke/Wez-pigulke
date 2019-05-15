package com.wezpigulke.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.wezpigulke.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ButtonIntent extends BroadcastReceiver {

    private Cursor cursor;
    private Intent intent;
    private Context context;
    private DatabaseHelper myDb;
    private String obecnyCzas;
    private int id_h;
    private int rand_val;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        this.intent = intent;
        myDb = new DatabaseHelper(context);

        initializeVariables();

        int coZrobic = intent.getIntExtra("coZrobic", -1);

        switch (coZrobic) {

            case 0:
                setYesButton();
                break;

            case 1:
                setNoButton();
                break;

        }

        if (cursor != null) cursor.close();

    }

    private void initializeVariables() {

        id_h = intent.getIntExtra("id_h", 0);
        rand_val = intent.getIntExtra("rand_val", 0);
        obecnyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

    }

    private void setYesButton() {

        updateStatistics(true);
        sendBroadcast();

    }

    private void setNoButton() {

        updateStatistics(false);
        sendBroadcast();

    }

    private void updateStatistics(boolean type) {

        if (type) {

            cursor = myDb.getWziete_STATYSTYKI(0);
            cursor.moveToFirst();
            myDb.update_STATYSTYKI_WZIETE(0, Integer.parseInt(cursor.getString(0)) + 1);
            myDb.update_HISTORIA(id_h, obecnyCzas, "WZIETE");

        } else {

            cursor = myDb.getNiewziete_STATYSTYKI(0);
            cursor.moveToFirst();
            myDb.update_STATYSTYKI_NIEWZIETE(0, Integer.parseInt(cursor.getString(0)) + 1);

            String nazwaLeku = intent.getStringExtra("nazwaLeku");
            Double jakaDawka = intent.getDoubleExtra("jakaDawka", 0);

            cursor = myDb.getDataName_LEK(nazwaLeku);
            cursor.moveToFirst();
            double iloscLeku = cursor.getDouble(2) + jakaDawka;
            myDb.update_LEK(cursor.getInt(0), iloscLeku);
            myDb.update_HISTORIA(id_h, obecnyCzas, "NIEWZIETE");

        }

    }

    private void sendBroadcast() {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.cancel(rand_val);
        Intent intz = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(intz);

    }

}