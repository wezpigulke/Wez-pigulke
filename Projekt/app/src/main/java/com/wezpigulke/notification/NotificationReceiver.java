package com.wezpigulke.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class NotificationReceiver extends WakefulBroadcastReceiver {

    DatabaseHelper myDb;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "ODPALONY ALARM", Toast.LENGTH_LONG).show();
        Log.d("========ALARM==========", "Alarm odpalony");

        myDb = new DatabaseHelper(context);

        int coPokazac = intent.getIntExtra("coPokazac", 0);

        if (coPokazac == 0) {

            String dzisiejszaData = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

            Date firstDate = null;
            Date secondDate = null;

            String data = "";
            Integer iloscDni = 0;

            String powiadomienie = intent.getStringExtra("tresc");
            Integer id_n = intent.getIntExtra("id", 0);
            Integer id_p = intent.getIntExtra("idd", 0);
            String godzina = intent.getStringExtra("godzina");

            int delay = 60*60*100;

            Cursor col = myDb.getdataID_NOTYFIKACJA(id_n);

            if (col.getCount() != 0) {
                col.moveToNext();
                data = col.getString(4);
                iloscDni = col.getInt(9);
            }

            String uzytkownik = intent.getStringExtra("uzytkownik");
            String nazwaLeku = intent.getStringExtra("nazwaLeku");
            Double jakaDawka = intent.getDoubleExtra("jakaDawka", 0);

            int wybranyDzwiek = intent.getIntExtra("wybranyDzwiek", 0);
            boolean czyWibracja = intent.getBooleanExtra("czyWibracja", false);
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

            if (secondDate != null && firstDate != null) {
                diff = secondDate.getTime() - firstDate.getTime();
            }

            if (iloscDni <= 0) {

                myDb.remove_PRZYPOMNIENIE(id_p);
                myDb.removeIdPrz_NOTYFIKACJA(id_p);

            } else {

                String dataPrzyszla = data;

                Cursor ct = myDb.getdataID_NOTYFIKACJA(id_n);

                if (ct.getCount() != 0 && diff+delay < 0) {

                        while (diff < 0) {

                            diff += 24*60*60*100;
                            iloscDni--;

                            if (iloscDni <= 0) {

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

                } else {

                    myDb.insert_HISTORIA(uzytkownik, godzina, data, nazwaLeku, jakaDawka, "BRAK", "BRAK");

                    Cursor cm = myDb.getMAXid_HISTORIA();
                    cm.moveToFirst();
                    Integer id_h = Integer.parseInt(cm.getString(0));

                    Cursor cssss = myDb.getDays_PRZYPOMNIENIE(id_p);
                    cssss.moveToFirst();
                    iloscDni = cssss.getInt(0);
                    iloscDni--;

                    Cursor cz = myDb.getCount_NOTYFIKACJA(id_p, data);
                    cz.moveToFirst();

                    if (iloscDni < 0) {

                        if (Integer.parseInt(cz.getString(0)) == 1) {

                            myDb.remove_PRZYPOMNIENIE(id_p);
                            myDb.removeIdPrz_NOTYFIKACJA(id_p);

                        } else myDb.remove_NOTYFIKACJA(id_n);

                    } else {

                        if (Integer.parseInt(cz.getString(0)) == 1)  {

                            myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);

                        } else {

                            Cursor coxl = myDb.getType_PRZYPOMNIENIE(id_p);

                            if(coxl.getCount() != 0) {
                                coxl.moveToNext();

                                int typ_p = coxl.getInt(0);

                                Calendar calx = Calendar.getInstance();
                                SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                                Cursor ccc = myDb.getdataID_NOTYFIKACJA(id_n);
                                ccc.moveToNext();

                                try {
                                    calx.setTime(dt.parse(data));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                calx.add(Calendar.DATE, typ_p);
                                dataPrzyszla = dt.format(calx.getTime());

                                myDb.updateDate_NOTYFIKACJA(id_n, dataPrzyszla);

                            }

                        }

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

                        Uri alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm1);

                        switch (wybranyDzwiek) {
                            case 1:
                                alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm1);
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

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(R.drawable.logo)
                                .setContentTitle("Weź pigułke")
                                .setContentText(powiadomienie)
                                .setAutoCancel(true)
                                .addAction(R.drawable.yes, "Wziąłem", yesIntent)
                                .addAction(R.drawable.no, "Zapomniałem", noIntent);

                        if(wybranyDzwiek!=0) builder.setSound(alarmSound);
                        if(czyWibracja) builder.setVibrate(new long[]{1000, 1000});

                        Notification notification = builder.build();
                        notification.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

                        notificationManager.notify(rand_val, notification);

                        if(iloscDni >= 1) {

                            Cursor csszz = myDb.getType_PRZYPOMNIENIE(id_p);
                            csszz.moveToFirst();
                            int typPrz = csszz.getInt(0);

                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfz = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfzx = new SimpleDateFormat("dd/MM/yyyy");

                            String przetwarzanaData = godzina + " " + data;

                            Date date = null;
                            try {
                                date = sdfz.parse(przetwarzanaData);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            cal.add(Calendar.DATE, typPrz);

                            Date dateTemp = cal.getTime();

                            myDb.updateDate_NOTYFIKACJA(id_n, sdfzx.format(dateTemp));

                            Intent intx = new Intent(context, NotificationReceiver.class);
                            intx.putExtra("coPokazac", 0);
                            intx.putExtra("czyPowtarzanyAlarm", true);
                            intx.putExtra("tresc", uzytkownik + " |  " + godzina + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")");
                            intx.putExtra("id", id_n);
                            intx.putExtra("idd", id_p);
                            intx.putExtra("godzina", godzina);
                            intx.putExtra("data", data);
                            intx.putExtra("uzytkownik", uzytkownik);
                            intx.putExtra("nazwaLeku", nazwaLeku);
                            intx.putExtra("jakaDawka", jakaDawka);
                            intx.putExtra("iloscDni", iloscDni - 1);
                            intx.putExtra("wybranyDzwiek", wybranyDzwiek);
                            intx.putExtra("czyWibracja", czyWibracja);
                            intx.putExtra("rand_val", rand_val);

                            PendingIntent newIntent = PendingIntent.getBroadcast(context, rand_val, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                            assert alarmManager != null;

                            if(Build.VERSION.SDK_INT < 23){
                                if(Build.VERSION.SDK_INT >= 19) alarmManager.setExact(AlarmManager.RTC, cal.getTimeInMillis(), newIntent);
                                else alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), newIntent);
                            } else alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, cal.getTimeInMillis(), newIntent);

                            //Toast.makeText(context, "Dodanie: " + " | " + String.valueOf(rand_val) + "\n" + sdfz.format(cal.getTime()), Toast.LENGTH_LONG).show();
                            Log.d("========ALARM==========", "Dodanie: " + " | " + String.valueOf(rand_val) + "\n" + sdfz.format(cal.getTime()));

                        } else {

                            myDb.remove_PRZYPOMNIENIE(id_p);
                            myDb.removeIdPrz_NOTYFIKACJA(id_p);

                            //Toast.makeText(context, "Usunięcie notyfikacji: " + id_n, Toast.LENGTH_SHORT).show();
                            Log.d("========ALARM==========", "Usunięcie notyfikacji: " + id_n);

                        }

                        Cursor cd = myDb.getDays_PRZYPOMNIENIE(id_p);
                        cd.moveToFirst();

                        Cursor cl = myDb.getDataName_LEK(nazwaLeku);
                        cl.moveToFirst();

                        double iloscLeku = cl.getDouble(2) - jakaDawka;

                        if (iloscLeku < 0) iloscLeku = 0;

                        myDb.update_LEK(cl.getInt(0), iloscLeku);
                        Integer id = Integer.parseInt(cl.getString(0));

                        Cursor cp = myDb.getIDfromMedicine_PRZYPOMNIENIE(nazwaLeku);
                        double ileNotyfikacji, typPrzypomnienia, pozostalaIloscDni;
                        Double sumujTypy = 0.0;

                        while (cp.moveToNext()) {

                            Cursor csa = myDb.getCountType_NOTYFIKACJA(cp.getInt(0));
                            csa.moveToFirst();
                            ileNotyfikacji = csa.getDouble(0);

                            Cursor css = myDb.getType_PRZYPOMNIENIE(cp.getInt(0));
                            css.moveToFirst();
                            typPrzypomnienia = css.getInt(0);

                            Cursor cssz = myDb.getDays_PRZYPOMNIENIE(cp.getInt(0));
                            cssz.moveToFirst();
                            pozostalaIloscDni = cssz.getDouble(0);

                            if (typPrzypomnienia > 1) {

                                if (pozostalaIloscDni <= 7)
                                    sumujTypy += ((1 / typPrzypomnienia) * jakaDawka) * pozostalaIloscDni;
                                else sumujTypy += ((1 / typPrzypomnienia) * jakaDawka) * 7;

                            } else {

                                if (pozostalaIloscDni <= 7)
                                    sumujTypy += (typPrzypomnienia * ileNotyfikacji * jakaDawka) * pozostalaIloscDni;
                                else
                                    sumujTypy += (typPrzypomnienia * ileNotyfikacji * jakaDawka) * 7;
                            }

                        }

                        if (sumujTypy > iloscLeku) {

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

                            builder = new NotificationCompat.Builder(context, "default")
                                    .setContentIntent(pendingIntent)
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("Weź pigułke")
                                    .setContentText("UWAGA | Pozostało tylko " + String.valueOf(iloscLeku) + " tabletek leku " + cl.getString(1))
                                    .setAutoCancel(true)
                                    .setOnlyAlertOnce(true);

                            if(wybranyDzwiek!=0) builder.setSound(alarmSound);
                            if(czyWibracja) builder.setVibrate(new long[]{1000, 1000});

                            Notification notification2 = builder.build();
                            notification2.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

                            notificationManager.notify(rand_val-3, notification2);

                        }

                    }

                }

            }

        } else {

            String dzisiejszaData = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

            Date firstDate = null;
            Date secondDate = null;

            String powiadomienie = intent.getStringExtra("tresc");

            Integer id_v = intent.getIntExtra("id", 0);
            Integer war = intent.getIntExtra("war", 0);
            String godzina = intent.getStringExtra("godzina");
            String data = intent.getStringExtra("data");
            String imie_nazwisko = intent.getStringExtra("imie_nazwisko");
            String specjalizacja = intent.getStringExtra("specjalizacja");
            String uzytkownik = intent.getStringExtra("uzytkownik");
            int wybranyDzwiek = intent.getIntExtra("wybranyDzwiek", 0);
            boolean czyWibracja = intent.getBooleanExtra("czyWibracja", false);
            int rand_val = intent.getIntExtra("rand_val", 0);

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

            if (secondDate != null && firstDate != null) {
                diff = secondDate.getTime() - firstDate.getTime();
            }

            int delay = 10*60*100;

            if (diff+delay < 0) {

                Cursor crand = myDb.getRand_WIZYTY(id_v);

                if (crand.getCount() != 0) {

                    myDb.remove_WIZYTY(id_v);

                }

            } else {

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

                Uri alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm1);

                switch (wybranyDzwiek) {
                    case 1:
                        alarmSound = Uri.parse("android.resource://com.wezpigulke/" + R.raw.alarm1);
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

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Weź pigułke")
                        .setContentText(powiadomienie)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true);

                if(wybranyDzwiek!=0) builder.setSound(alarmSound);
                if(czyWibracja) builder.setVibrate(new long[]{1000, 1000});

                Notification notification = builder.build();
                notification.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

                notificationManager.notify(rand_val, notification);

                myDb.remove_WIZYTY(id_v);

            }

        }

    }
}


