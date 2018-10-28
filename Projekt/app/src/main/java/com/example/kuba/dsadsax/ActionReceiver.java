package com.example.kuba.dsadsax;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActionReceiver extends BroadcastReceiver {

    DatabaseHelper myDb;

    @Override
    public void onReceive(Context context, Intent intent) {

        myDb = new DatabaseHelper(context);

        Integer coZrobic = intent.getIntExtra("coZrobic",-1);
        String godzina = intent.getStringExtra("godzina");
        String data = intent.getStringExtra("data");
        String uzytkownik = intent.getStringExtra("uzytkownik");
        String nazwaLeku = intent.getStringExtra("nazwaLeku");
        String jakaDawka = intent.getStringExtra("jakaDawka");
        Integer iloscDni = intent.getIntExtra("iloscDni", 0);
        Integer id = intent.getIntExtra("id", 0);
        Integer id_h = intent.getIntExtra("id_h", 0);
        String obecnyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        if(coZrobic==0){

            Cursor cn = myDb.get_STATYSTYKI_NIEWZIETE(0);
            cn.moveToFirst();
            myDb.update_STATYSTYKI_NIEWZIETE(0, Integer.parseInt(cn.getString(0)) + 1);

            Cursor cl = myDb.getDataName_LEK(nazwaLeku);
            cl.moveToFirst();
            double iloscLeku = Double.valueOf(cl.getString(2)) + Double.valueOf(jakaDawka.substring(7, jakaDawka.length()));
            myDb.update_LEK(Integer.parseInt(cl.getString(0)), String.valueOf(iloscLeku));
            myDb.update_HISTORIA(id_h, obecnyCzas, "NIEWZIETE");

            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);

        }
        else if(coZrobic==1){

            Cursor cw = myDb.get_STATYSTYKI_WZIETE(0);
            cw.moveToFirst();
            myDb.update_STATYSTYKI_WZIETE(0, Integer.parseInt(cw.getString(0)) + 1);
            myDb.update_HISTORIA(id_h, obecnyCzas, "WZIETE");

            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);

        }
        else if(coZrobic==2){

            Intent i = new Intent(context.getApplicationContext(), RepeatingActivityReminder.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("coPokazac", 0);
            i.putExtra("id_h", id_h);
            i.putExtra("godzina", godzina);
            i.putExtra("data", data);
            i.putExtra("uzytkownik", uzytkownik);
            i.putExtra("nazwaLeku", nazwaLeku);
            i.putExtra("jakaDawka", jakaDawka);
            i.putExtra("iloscDni", iloscDni);
            context.startActivity(i);

        }
        else if(coZrobic==3){

            Intent i = new Intent(context.getApplicationContext(), RepeatingActivityReminder.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("coPokazac", 1);
            i.putExtra("id_h", id_h);
            i.putExtra("godzina", godzina);
            i.putExtra("data", data);
            i.putExtra("uzytkownik", uzytkownik);
            i.putExtra("nazwaLeku", nazwaLeku);
            i.putExtra("jakaDawka", jakaDawka);
            i.putExtra("iloscDni", iloscDni);
            context.startActivity(i);

        }
    }
}