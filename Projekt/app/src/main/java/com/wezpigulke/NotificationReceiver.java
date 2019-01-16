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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {

    DatabaseHelper myDb;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        myDb = new DatabaseHelper(context);

        Integer coPokazac = intent.getIntExtra("coPokazac", 0);

        if (coPokazac == 0) {

            String dzisiejszaData = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

            Date firstDate = null;
            Date secondDate = null;

            String data = "";
            Integer iloscDni = 0;

            String powiadomienie = intent.getStringExtra("Value");
            Integer id_n = intent.getIntExtra("id", 0);
            Integer id_p = intent.getIntExtra("idd", 0);
            String godzina = intent.getStringExtra("godzina");

            Cursor col = myDb.getdataID_NOTYFIKACJA(id_n);
            if (col.getCount() != 0) {
                col.moveToNext();
                data = col.getString(4);
                iloscDni = col.getInt(9);
            }

            String uzytkownik = intent.getStringExtra("uzytkownik");
            String nazwaLeku = intent.getStringExtra("nazwaLeku");
            Double jakaDawka = intent.getDoubleExtra("jakaDawka", 0);

            Integer wybranyDzwiek = intent.getIntExtra("wybranyDzwiek", 0);
            Integer rand_val = intent.getIntExtra("rand_val", 0);

            Cursor cs = myDb.getdataID_NOTYFIKACJA(id_n);
            cs.moveToNext();

            try {
                firstDate = sdf.parse(dzisiejszaData);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                secondDate = sdf.parse(godzina + " " + data);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diff = 0;
            long diffCopy = 0;

            if (secondDate != null && firstDate != null) {
                diff = secondDate.getTime() - firstDate.getTime();
                diffCopy = diff;
            }

            if (iloscDni <= 0) {

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                Intent myIntent = new Intent(context, NotificationReceiver.class);
                PendingIntent pendinIntent = PendingIntent.getBroadcast(
                        context, rand_val, myIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                assert alarmManager != null;
                alarmManager.cancel(pendinIntent);

                Toast.makeText(context, "Anulacja:" + String.valueOf(rand_val), Toast.LENGTH_LONG).show();

                myDb.remove_PRZYPOMNIENIE(id_p);
                myDb.removeIDprz_NOTYFIKACJA(id_p);

                return;

            } else {

                String dataPrzyszla = data;

                Cursor ct = myDb.getdataID_NOTYFIKACJA(id_n);

                if (ct.getCount() != 0) {

                    while (diff <= 0) {

                        if (iloscDni == 0) {

                            Cursor crand = myDb.getRand_NOTYFIKACJA(id_n);
                            if (crand.getCount() != 0) {

                                crand.moveToFirst();

                                AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(context).getSystemService(Context.ALARM_SERVICE);
                                Intent myIntent = new Intent(context, NotificationReceiver.class);
                                PendingIntent removeIntent = PendingIntent.getBroadcast(
                                        context, crand.getInt(0), myIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                assert alarmManager != null;
                                alarmManager.cancel(removeIntent);

                                Toast.makeText(context, "Anulacja: " + String.valueOf(crand.getInt(0)), Toast.LENGTH_LONG).show();

                            }

                            Cursor cw = myDb.getCountType_NOTYFIKACJA(id_p);
                            cw.moveToNext();

                            myDb.remove_NOTYFIKACJA(id_n);

                            if (cw.getInt(0) == 1) myDb.remove_PRZYPOMNIENIE(id_p);

                            return;

                        } else {

                            Calendar cz = Calendar.getInstance();
                            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                            Cursor ccc = myDb.getdataID_NOTYFIKACJA(id_n);
                            ccc.moveToNext();

                            try {
                                cz.setTime(dt.parse(ccc.getString(4)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            cz.add(Calendar.DATE, ccc.getInt(8));
                            dataPrzyszla = dt.format(cz.getTime());

                            myDb.updateDate_NOTYFIKACJA(id_n, dataPrzyszla);

                            Cursor cl = myDb.getDataName_LEK(nazwaLeku);
                            cl.moveToFirst();

                            double iloscLeku = cl.getDouble(2) - jakaDawka;

                            if (iloscLeku < 0) iloscLeku = 0;
                            myDb.update_LEK(cl.getInt(0), iloscLeku);

                        }

                        diff += 86400000;
                        iloscDni--;

                    }

                    Cursor cds = myDb.getCountType_NOTYFIKACJA(id_p);
                    cds.moveToFirst();

                    if (cds.getInt(0) == 0) {
                        myDb.remove_PRZYPOMNIENIE(id_p);
                    } else if (cds.getInt(0) == 1) {
                        myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);
                    } else {
                        Cursor cee = myDb.getCount_NOTYFIKACJA(id_p, dataPrzyszla);
                        cee.moveToNext();
                        Cursor ceee = myDb.getCountType_NOTYFIKACJA(id_p);
                        ceee.moveToNext();
                        if (cee.getInt(0) == ceee.getInt(0)) {
                            myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);
                        }
                    }

                }

            }

            if (diffCopy >= 0) {

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
                repeating_intent.putExtra("rand_val", rand_val);

                repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, rand_val, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri alarmSound;
                alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm1);

                if (wybranyDzwiek == 1)
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm2);
                else if (wybranyDzwiek == 2)
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm3);
                else if (wybranyDzwiek == 3)
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm4);
                else if (wybranyDzwiek == 4)
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm5);
                else if (wybranyDzwiek == 5)
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm6);
                else if (wybranyDzwiek == 6)
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm7);
                else if (wybranyDzwiek == 7)
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm8);
                else if (wybranyDzwiek == 8)
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm9);

                Intent yes = new Intent(context, ButtonIntent.class);
                yes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                yes.putExtra("coZrobic", 0);
                yes.putExtra("id_h", id_h);
                yes.putExtra("id", id_n);
                yes.putExtra("rand_val", rand_val);
                PendingIntent yesIntent = PendingIntent.getBroadcast(context, rand_val - 1, yes, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent no = new Intent(context, ButtonIntent.class);
                no.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                no.putExtra("coZrobic", 1);
                no.putExtra("id_h", id_h);
                no.putExtra("id", id_n);
                no.putExtra("nazwaLeku", nazwaLeku);
                no.putExtra("jakaDawka", jakaDawka);
                no.putExtra("rand_val", rand_val);
                PendingIntent noIntent = PendingIntent.getBroadcast(context, rand_val - 2, no, PendingIntent.FLAG_UPDATE_CURRENT);

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

                notificationManager.notify(rand_val, builder.build());

                Cursor cd = myDb.getDays_PRZYPOMNIENIE(id_p);
                cd.moveToFirst();

                Cursor cl = myDb.getDataName_LEK(nazwaLeku);
                cl.moveToFirst();

                double iloscLeku = cl.getDouble(2) - jakaDawka;

                if (iloscLeku < 0) iloscLeku = 0;

                myDb.update_LEK(cl.getInt(0), iloscLeku);
                Integer id = Integer.parseInt(cl.getString(0));

                Cursor cp = myDb.getIDfromMedicine_PRZYPOMNIENIE(nazwaLeku);
                Double ileNotyfikacji, typPrzypomnienia, pozostalaIloscDni;
                Double sumujTypy = 0.0;
                Double jakaDawkaTabletki = 0.0;

                while (cp.moveToNext()) {

                    Cursor csa = myDb.getCountType_NOTYFIKACJA(cp.getInt(0));
                    csa.moveToFirst();
                    ileNotyfikacji = Double.parseDouble(csa.getString(0));

                    Cursor css = myDb.getType_PRZYPOMNIENIE(cp.getInt(0));
                    css.moveToFirst();
                    typPrzypomnienia = Double.parseDouble(css.getString(0));

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
                        else
                            sumujTypy += (typPrzypomnienia * ileNotyfikacji * jakaDawkaTabletki) * 7;
                    }

                }

                sumujTypy -= jakaDawkaTabletki;

                if (sumujTypy > iloscLeku) {

                    sumujTypy -= jakaDawka;

                    notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    repeating_intent = new Intent(context, RepeatingActivityReminder.class);

                    repeating_intent.putExtra("coPokazac", 1);
                    repeating_intent.putExtra("id", id);
                    repeating_intent.putExtra("nazwa", cl.getString(1));
                    repeating_intent.putExtra("sumujTypy", String.valueOf(sumujTypy));
                    repeating_intent.putExtra("rand_val", rand_val - 3);

                    repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    pendingIntent = PendingIntent.getActivity(context, rand_val - 3, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

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
                    notificationManager.notify(rand_val - 3, builder.build());

                }

            }

        } else {

            String powiadomienie = intent.getStringExtra("Value");

            Integer id_v = intent.getIntExtra("id", 0);
            Integer war = intent.getIntExtra("war", 0);
            String godzina = intent.getStringExtra("godzina");
            String data = intent.getStringExtra("data");
            String imie_nazwisko = intent.getStringExtra("imie_nazwisko");
            String specjalizacja = intent.getStringExtra("specjalizacja");
            String uzytkownik = intent.getStringExtra("uzytkownik");
            Integer wybranyDzwiek = intent.getIntExtra("wybranyDzwiek", 0);
            Integer rand_val = intent.getIntExtra("rand_val", 0);

            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent repeating_intent = new Intent(context, RepeatingActivityVisit.class);

            repeating_intent.putExtra("id", id_v);
            repeating_intent.putExtra("war", war);
            repeating_intent.putExtra("godzina", godzina);
            repeating_intent.putExtra("data", data);
            repeating_intent.putExtra("imie_nazwisko", imie_nazwisko);
            repeating_intent.putExtra("specjalizacja", specjalizacja);
            repeating_intent.putExtra("uzytkownik", uzytkownik);
            repeating_intent.putExtra("rand_val", rand_val);

            repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, rand_val, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri alarmSound;
            alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm1);

            switch (wybranyDzwiek) {
                case 1:
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm2);
                    break;
                case 2:
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm2);
                    break;
                case 3:
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm3);
                    break;
                case 4:
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm4);
                    break;
                case 5:
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm5);
                    break;
                case 6:
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm6);
                    break;
                case 7:
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm7);
                    break;
                case 8:
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm8);
                    break;
                case 9:
                    alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm9);
                    break;
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Weź pigułke")
                    .setContentText(powiadomienie)
                    .setSound(alarmSound)
                    .setVibrate(new long[]{1000, 1000})
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);
            notificationManager.notify(rand_val, builder.build());

        }

    }

}


