package com.wezpigulke.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.wezpigulke.Database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ButtonIntent extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Database myDb = new Database(context);
        int coZrobic = intent.getIntExtra("coZrobic", -1);

        switch(coZrobic) {

            case 0:
                Integer id_h = intent.getIntExtra("id_h", 0);
                int rand_val = intent.getIntExtra("rand_val", 0);
                String obecnyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                Cursor cw = myDb.getWziete_STATYSTYKI(0);
                cw.moveToFirst();
                myDb.update_STATYSTYKI_WZIETE(0, Integer.parseInt(cw.getString(0)) + 1);

                myDb.update_HISTORIA(id_h, obecnyCzas, "WZIETE");

                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                assert manager != null;
                manager.cancel(rand_val);
                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(it);

            case 1:
                id_h = intent.getIntExtra("id_h", 0);
                obecnyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                String nazwaLeku = intent.getStringExtra("nazwaLeku");
                Double jakaDawka = intent.getDoubleExtra("jakaDawka", 0);
                rand_val = intent.getIntExtra("rand_val", 0);

                Cursor cn = myDb.getNiewziete_STATYSTYKI(0);
                cn.moveToFirst();
                myDb.update_STATYSTYKI_NIEWZIETE(0, Integer.parseInt(cn.getString(0)) + 1);

                Cursor cl = myDb.getDataName_LEK(nazwaLeku);
                cl.moveToFirst();
                double iloscLeku = cl.getDouble(2) + jakaDawka;
                myDb.update_LEK(Integer.parseInt(cl.getString(0)), iloscLeku);

                myDb.update_HISTORIA(id_h, obecnyCzas, "NIEWZIETE");

                manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                assert manager != null;
                manager.cancel(rand_val);
                it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(it);

        }

    }

}