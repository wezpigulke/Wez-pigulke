package com.wezpigulke;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ButtonIntent extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        DatabaseHelper myDb = new DatabaseHelper(context);
        Integer coZrobic = intent.getIntExtra("coZrobic", -1);

        if (coZrobic == 0) {

            Integer id_h = intent.getIntExtra("id_h", 0);
            Integer id = intent.getIntExtra("id", 0);
            String obecnyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            Cursor cw = myDb.get_STATYSTYKI_WZIETE(0);
            cw.moveToFirst();
            myDb.update_STATYSTYKI_WZIETE(0, Integer.parseInt(cw.getString(0)) + 1);

            myDb.update_HISTORIA(id_h, obecnyCzas, "WZIETE");

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(id);
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
        } else if (coZrobic == 1) {

            Integer id_h = intent.getIntExtra("id_h", 0);
            Integer id = intent.getIntExtra("id", 0);
            String obecnyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            String nazwaLeku = intent.getStringExtra("nazwaLeku");
            String jakaDawka = intent.getStringExtra("jakaDawka");

            Cursor cn = myDb.get_STATYSTYKI_NIEWZIETE(0);
            cn.moveToFirst();
            myDb.update_STATYSTYKI_NIEWZIETE(0, Integer.parseInt(cn.getString(0)) + 1);

            Cursor cl = myDb.getDataName_LEK(nazwaLeku);
            cl.moveToFirst();
            double iloscLeku = Double.valueOf(cl.getString(2)) + Double.valueOf(jakaDawka.substring(7, jakaDawka.length()));
            myDb.update_LEK(Integer.parseInt(cl.getString(0)), String.valueOf(iloscLeku));

            myDb.update_HISTORIA(id_h, obecnyCzas, "NIEWZIETE");

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(id);
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);

        }

    }

}