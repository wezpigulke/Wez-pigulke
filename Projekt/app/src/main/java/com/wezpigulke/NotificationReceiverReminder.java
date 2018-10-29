package com.wezpigulke;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class NotificationReceiverReminder extends BroadcastReceiver {

    DatabaseHelper myDb;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        myDb = new DatabaseHelper(context);

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

        myDb.insert_HISTORIA(uzytkownik, godzina, data, nazwaLeku, jakaDawka, "BRAK", "BRAK");

        Cursor cm = myDb.getMAXid_HISTORIA();
        cm.moveToFirst();
        Integer id_h = Integer.parseInt(cm.getString(0));

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context, RepeatingActivityReminder.class);

        repeating_intent.putExtra("coPokazac", 0);
        repeating_intent.putExtra("id_h", id_h);
        repeating_intent.putExtra("id", id_n);
        repeating_intent.putExtra("idd", id_p);
        repeating_intent.putExtra("godzina", godzina);
        repeating_intent.putExtra("data", data);
        repeating_intent.putExtra("uzytkownik", uzytkownik);
        repeating_intent.putExtra("nazwaLeku", nazwaLeku);
        repeating_intent.putExtra("jakaDawka", jakaDawka);
        repeating_intent.putExtra("iloscDni", iloscDni);

        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id_n, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound;
        alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm1);

        if (wybranyDzwiek == 2)
            alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm2);
        else if (wybranyDzwiek == 3)
            alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm3);
        else if (wybranyDzwiek == 4)
            alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm4);
        else if (wybranyDzwiek == 5)
            alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm5);
        else if (wybranyDzwiek == 6)
            alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm6);
        else if (wybranyDzwiek == 7)
            alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm7);
        else if (wybranyDzwiek == 8)
            alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm8);
        else if (wybranyDzwiek == 9)
            alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm9);

        Intent yes = new Intent(context, ButtonIntent.class);
        yes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        yes.putExtra("coZrobic", 0);
        yes.putExtra("id_h", id_h);
        yes.putExtra("id_h", id_h);
        yes.putExtra("id", id_n);
        PendingIntent yesIntent = PendingIntent.getBroadcast(context, id_n*10, yes, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent no = new Intent(context, ButtonIntent.class);
        no.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        no.putExtra("coZrobic", 1);
        no.putExtra("id_h", id_h);
        no.putExtra("id", id_n);
        no.putExtra("nazwaLeku", nazwaLeku);
        no.putExtra("jakaDawka", jakaDawka);
        PendingIntent noIntent = PendingIntent.getBroadcast(context, id_n*20, no, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Weź pigułke")
                .setContentText(powiadomienie)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .addAction(R.drawable.yes, "Wziąłem", yesIntent)
                .addAction(R.drawable.no, "Zapomniałem", noIntent)
                .setVibrate(new long[]{1000, 1000});

        builder.getNotification().flags |= NotificationCompat.FLAG_AUTO_CANCEL;

        notificationManager.notify(id_n, builder.build());

        String dzisiaj = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Cursor cd = myDb.getDays_PRZYPOMNIENIE(id_p);
        cd.moveToFirst();

        Cursor cz = myDb.getCount_NOTYFIKACJA(id_p, dzisiaj);
        cz.moveToFirst();

        Cursor cl = myDb.getDataName_LEK(nazwaLeku);
        cl.moveToFirst();

        double iloscLeku = Double.valueOf(cl.getString(2)) - Double.valueOf(jakaDawka.substring(7, jakaDawka.length()));

        if (iloscLeku < 0) iloscLeku = 0;

        myDb.update_LEK(Integer.parseInt(cl.getString(0)), String.valueOf(iloscLeku));
        Integer id = Integer.parseInt(cl.getString(0));

        Cursor cp = myDb.getIDfromMedicine_PRZYPOMNIENIE(nazwaLeku);
        Double ileNotyfikacji, typPrzypomnienia, pozostalaIloscDni;
        String jakaDawkaS;
        Double sumujTypy = 0.0;
        Double jakaDawkaTabletki = 0.0;

        while (cp.moveToNext()) {

            Cursor cs = myDb.getCountType_NOTYFIKACJA(cp.getInt(0));
            cs.moveToFirst();
            ileNotyfikacji = Double.parseDouble(cs.getString(0));

            Cursor css = myDb.getType_PRZYPOMNIENIE(cp.getInt(0));
            css.moveToFirst();
            typPrzypomnienia = Double.parseDouble(css.getString(0));

            Cursor csss = myDb.getDose_PRZYPOMNIENIE(cp.getInt(0));
            csss.moveToFirst();
            jakaDawkaS = csss.getString(0);
            jakaDawkaTabletki = Double.valueOf(jakaDawkaS.substring(7, jakaDawkaS.length()));

            Cursor cssss = myDb.getDays_PRZYPOMNIENIE(cp.getInt(0));
            cssss.moveToFirst();
            pozostalaIloscDni = Double.parseDouble(cssss.getString(0));

            if (typPrzypomnienia > 1) {

                if (pozostalaIloscDni <= 7)
                    sumujTypy += ((1 / typPrzypomnienia) * jakaDawkaTabletki) * pozostalaIloscDni;
                else sumujTypy += ((1 / typPrzypomnienia) * jakaDawkaTabletki) * 7;

            } else {

                if (pozostalaIloscDni <= 7)
                    sumujTypy += (typPrzypomnienia * ileNotyfikacji * jakaDawkaTabletki) * pozostalaIloscDni;
                else sumujTypy += (typPrzypomnienia * ileNotyfikacji * jakaDawkaTabletki) * 7;
            }

        }

        sumujTypy -= jakaDawkaTabletki;

        if (iloscDni <= 0) {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent myIntent = new Intent(context, NotificationReceiverReminder.class);
            PendingIntent pendinIntent = PendingIntent.getBroadcast(
                    context, id_n, myIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            assert alarmManager != null;
            alarmManager.cancel(pendinIntent);

            if (Integer.parseInt(cz.getString(0)) == 1) myDb.remove_PRZYPOMNIENIE(id_p);
            myDb.remove_NOTYFIKACJA(id_n);

        } else {

            if (Integer.parseInt(cz.getString(0)) == 1)
                myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);
            myDb.remove_NOTYFIKACJA(id_n);

        }

        if (sumujTypy > iloscLeku) {

            sumujTypy -= Double.valueOf(jakaDawka.substring(7, jakaDawka.length()));

            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            repeating_intent = new Intent(context, RepeatingActivityReminder.class);

            repeating_intent.putExtra("coPokazac", 1);
            repeating_intent.putExtra("id", id);
            repeating_intent.putExtra("nazwa", cl.getString(1));
            repeating_intent.putExtra("ilosc", String.valueOf(iloscLeku));
            repeating_intent.putExtra("sumujTypy", String.valueOf(sumujTypy));

            repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            pendingIntent = PendingIntent.getActivity(context, id_n * 30, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm1);

            builder = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Weź pigułke")
                    .setContentText("UWAGA | Pozostało tylko " + String.valueOf(iloscLeku) + " tabletek leku " + cl.getString(1))
                    .setSound(alarmSound)
                    .setVibrate(new long[]{1000, 1000})
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);
            notificationManager.notify(id_n * 2, builder.build());

        }

    }

}
