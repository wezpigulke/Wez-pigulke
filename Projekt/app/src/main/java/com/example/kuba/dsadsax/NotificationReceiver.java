package com.example.kuba.dsadsax;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String powiadomienie = intent.getStringExtra("Value");
        Integer id_n = intent.getIntExtra("id", 0);
        Integer id_p = intent.getIntExtra("idd", 0);
        String godzina = intent.getStringExtra("godzina");
        String data = intent.getStringExtra("data");
        String uzytkownik = intent.getStringExtra("uzytkownik");
        String nazwaLeku = intent.getStringExtra("nazwaLeku");
        String jakaDawka = intent.getStringExtra("jakaDawka");
        Integer iloscDni = intent.getIntExtra("iloscDni", 0);
        Integer wybranyDzwiek = intent.getIntExtra("wybranyDzwiek", 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context, RepeatingActivity.class);

        repeating_intent.putExtra("id", id_n);
        repeating_intent.putExtra("idd", id_p);
        repeating_intent.putExtra("godzina", godzina);
        repeating_intent.putExtra("data", data);
        repeating_intent.putExtra("uzytkownik", uzytkownik);
        repeating_intent.putExtra("nazwaLeku", nazwaLeku);
        repeating_intent.putExtra("jakaDawka", jakaDawka);
        repeating_intent.putExtra("iloscDni", iloscDni);

        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound;
        alarmSound = Uri.parse("android.resource://com.example.kuba.dsadsax/" + R.raw.alarm1);

        if (wybranyDzwiek==2) alarmSound = Uri.parse("android.resource://com.example.kuba.dsadsax/" + R.raw.alarm2);
        else if (wybranyDzwiek==3) alarmSound = Uri.parse("android.resource://com.example.kuba.dsadsax/" + R.raw.alarm3);
        else if (wybranyDzwiek==4) alarmSound = Uri.parse("android.resource://com.example.kuba.dsadsax/" + R.raw.alarm4);
        else if (wybranyDzwiek==5) alarmSound = Uri.parse("android.resource://com.example.kuba.dsadsax/" + R.raw.alarm5);
        else if (wybranyDzwiek==6) alarmSound = Uri.parse("android.resource://com.example.kuba.dsadsax/" + R.raw.alarm6);
        else if (wybranyDzwiek==7) alarmSound = Uri.parse("android.resource://com.example.kuba.dsadsax/" + R.raw.alarm7);
        else if (wybranyDzwiek==8) alarmSound = Uri.parse("android.resource://com.example.kuba.dsadsax/" + R.raw.alarm8);
        else if (wybranyDzwiek==9) alarmSound = Uri.parse("android.resource://com.example.kuba.dsadsax/" + R.raw.alarm9);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Weź pigułke")
                .setContentText(powiadomienie)
                .setSound(alarmSound)
                .setVibrate(new long[]{1000, 1000})
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);
        notificationManager.notify(id_n, builder.build());

        /* SPRAWDZANIE */

        Integer ilosc_dn;
        DatabaseHelper myDb;

        myDb = new DatabaseHelper(context);

        String dzisiaj = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Cursor cd = myDb.getDays_PRZYPOMNIENIE(id_p);
        cd.moveToFirst();
        ilosc_dn = Integer.parseInt(cd.getString(0));

        Cursor cz = myDb.getCount_NOTYFIKACJA(id_p, dzisiaj);
        cz.moveToFirst();

        Toast.makeText(context, "Wykonuje sprawdzanie", Toast.LENGTH_LONG).show();

        if ((ilosc_dn - 1) <= 0) {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(context, NotificationReceiver.class);
            PendingIntent pendinIntent = PendingIntent.getBroadcast(
                    context, id_n, myIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendinIntent);

            if(Integer.parseInt(cz.getString(0))==1) myDb.remove_PRZYPOMNIENIE(id_p);
            myDb.remove_NOTYFIKACJA(id_n);

            Toast.makeText(context, String.valueOf(id_n), Toast.LENGTH_LONG).show();

        } else {

            if(Integer.parseInt(cz.getString(0))==1) myDb.updateDays_PRZYPOMNIENIE(id_p, ilosc_dn - 1);
            myDb.remove_NOTYFIKACJA(id_n);

        }

    }

}
