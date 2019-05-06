package com.wezpigulke.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {

    private Uri alarmSound;
    private Cursor cursor;
    private Cursor cursorTemp;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("========ALARM==========", "Alarm odpalony dla: " + intent.getIntExtra("id", 0));

        DatabaseHelper myDb = new DatabaseHelper(context);

        int coPokazac = intent.getIntExtra("coPokazac", 0);

        NotificationManager notificationManager;
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

            cursor = myDb.getdataID_NOTYFIKACJA(id_n);

            if (cursor.getCount() != 0) {
                cursor.moveToNext();
                data = cursor.getString(4);
                iloscDni = cursor.getInt(9);
            }

            String uzytkownik = intent.getStringExtra("uzytkownik");
            String nazwaLeku = intent.getStringExtra("nazwaLeku");
            Double jakaDawka = intent.getDoubleExtra("jakaDawka", 0);

            int wybranyDzwiek = intent.getIntExtra("wybranyDzwiek", 0);
            int czyWibracja = intent.getIntExtra("czyWibracja", 0);
            Integer rand_val = intent.getIntExtra("rand_val", 0);

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
                Log.d("NotificationReceiver", "Usunięcie przypomnienia o id: " + id_p);
                myDb.removeIdPrz_NOTYFIKACJA(id_p);

            } else {

                String dataPrzyszla = data;

                cursor = myDb.getdataID_NOTYFIKACJA(id_n);

                if (cursor.getCount() != 0 && diff+delay < 0) {

                        while (diff < 0) {

                            diff += 24*60*60*100;
                            iloscDni--;

                            if (iloscDni <= 0) {

                                cursor = myDb.getCountType_NOTYFIKACJA(id_p);
                                cursor.moveToNext();

                                myDb.remove_NOTYFIKACJA(id_n);

                                Log.d("NotificationReceiver", "Usunięcie notyfikacji o id:" + id_n);

                                if (cursor.getInt(0) == 1) {
                                    myDb.remove_PRZYPOMNIENIE(id_p);
                                    Log.d("NotificationReceiver", "Usunięcie przypomnienia o id: " + id_p);
                                }

                                return;

                            } else {

                                Calendar cz = Calendar.getInstance();
                                SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                                cursor = myDb.getdataID_NOTYFIKACJA(id_n);
                                cursor.moveToNext();

                                try {
                                    cz.setTime(dt.parse(cursor.getString(4)));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                cz.add(Calendar.DATE, cursor.getInt(8));
                                dataPrzyszla = dt.format(cz.getTime());

                                myDb.updateDate_NOTYFIKACJA(id_n, dataPrzyszla);

                                cursor = myDb.getDataName_LEK(nazwaLeku);
                                cursor.moveToFirst();

                                double iloscLeku = cursor.getDouble(2) - jakaDawka;

                                if (iloscLeku < 0) iloscLeku = 0;
                                myDb.update_LEK(cursor.getInt(0), iloscLeku);

                                cursor = myDb.getType_PRZYPOMNIENIE(id_p);
                                cursor.moveToFirst();
                                int typPrz = cursor.getInt(0);

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

                                Log.d("========ALARM==========", "Dodanie: " + " | " + rand_val + "\n" + sdfz.format(cal.getTime()));

                            }

                        }

                        cursor = myDb.getCountType_NOTYFIKACJA(id_p);
                        cursor.moveToFirst();

                        if (cursor.getInt(0) == 0) {
                            myDb.remove_PRZYPOMNIENIE(id_p);
                            Log.d("NotificationReceiver", "Usunięcie przypomnienia o id: " + id_p);
                        } else if (cursor.getInt(0) == 1) {
                            myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);
                        } else {
                            cursor = myDb.getCount_NOTYFIKACJA(id_p, dataPrzyszla);
                            cursor.moveToNext();
                            cursorTemp = myDb.getCountType_NOTYFIKACJA(id_p);
                            cursorTemp.moveToNext();
                            if (cursor.getInt(0) == cursorTemp.getInt(0)) {
                                myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);
                            }
                    }

                } else {

                    myDb.insert_HISTORIA(uzytkownik, godzina, data, nazwaLeku, jakaDawka, "BRAK", "BRAK");

                    cursor = myDb.getMAXid_HISTORIA();
                    cursor.moveToFirst();
                    Integer id_h = Integer.parseInt(cursor.getString(0));

                    cursor = myDb.getDays_PRZYPOMNIENIE(id_p);
                    cursor.moveToFirst();
                    iloscDni = cursor.getInt(0);
                    iloscDni--;

                    cursor = myDb.getCount_NOTYFIKACJA(id_p, data);
                    cursor.moveToFirst();

                    if (iloscDni < 0) {

                        if (Integer.parseInt(cursor.getString(0)) == 1) {

                            myDb.remove_PRZYPOMNIENIE(id_p);
                            Log.d("NotificationReceiver", "Usunięcie przypomnienia o id: " + id_p);
                            myDb.removeIdPrz_NOTYFIKACJA(id_p);

                        } else {
                            myDb.remove_NOTYFIKACJA(id_n);
                            Log.d("NotificationReceiver", "Usunięcie notyfikacji o id:" + id_n);
                        }

                    } else {

                        if (Integer.parseInt(cursor.getString(0)) == 1)  {

                            myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);

                        } else {

                            cursor = myDb.getType_PRZYPOMNIENIE(id_p);

                            if(cursor.getCount() != 0) {
                                cursor.moveToNext();

                                int typ_p = cursor.getInt(0);

                                Calendar calx = Calendar.getInstance();
                                SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

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

                        setAlarmSound(wybranyDzwiek, context);

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

                        initChannels(context, wybranyDzwiek);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
                        builder.setContentIntent(pendingIntent);
                        builder.setSmallIcon(R.mipmap.ic_launcher_round);
                        builder.setContentTitle("Weź pigułke");
                        builder.setContentText(powiadomienie);
                        builder.setAutoCancel(true);
                        builder.addAction(0, "Wziąłem", yesIntent);
                        builder.addAction(0, "Zapomniałem", noIntent);

                        if(wybranyDzwiek!=0) builder.setSound(alarmSound);
                        if(czyWibracja==1) builder.setVibrate(new long[]{1000, 1000});

                        Notification notification = builder.build();
                        notification.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

                        notificationManager.notify(rand_val, notification);

                        Log.d("NotificationReceiver", "Ilosc dni: " + iloscDni);

                        if(iloscDni >= 1) {

                            cursor = myDb.getType_PRZYPOMNIENIE(id_p);
                            cursor.moveToFirst();
                            int typPrz = cursor.getInt(0);

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

                            Log.d("========ALARM==========", "Dodanie: " + " | " + rand_val + "\n" + sdfz.format(cal.getTime()));

                        } else {

                            myDb.remove_NOTYFIKACJA(id_n);
                            Log.d("NotificationReceiver", "Usunięcie notyfikacji: " + id_n);

                            cursor = myDb.getCountType_NOTYFIKACJA(id_p);
                            if(cursor != null) {
                                cursor.moveToFirst();
                                if(cursor.getInt(0)==0) {
                                    myDb.remove_PRZYPOMNIENIE(id_p);
                                    Log.d("NotificationReceiver", "Usunięcie przypomnienia o id: " + id_p);
                                }
                            }

                        }

                        cursor = myDb.getDataName_LEK(nazwaLeku);
                        cursor.moveToFirst();

                        double iloscLeku = cursor.getDouble(2) - jakaDawka;

                        if (iloscLeku < 0) iloscLeku = 0;

                        myDb.update_LEK(cursor.getInt(0), iloscLeku);
                        Integer id = Integer.parseInt(cursor.getString(0));

                        cursor = myDb.getIDfromMedicine_PRZYPOMNIENIE(nazwaLeku);
                        double ileNotyfikacji, typPrzypomnienia, pozostalaIloscDni;
                        double sumujTypy = 0.0;

                        while (cursor.moveToNext()) {

                            cursor = myDb.getCountType_NOTYFIKACJA(cursor.getInt(0));
                            cursor.moveToFirst();
                            ileNotyfikacji = cursor.getDouble(0);

                            cursor = myDb.getType_PRZYPOMNIENIE(cursor.getInt(0));
                            cursor.moveToFirst();
                            typPrzypomnienia = cursor.getInt(0);

                            cursor = myDb.getDays_PRZYPOMNIENIE(cursor.getInt(0));
                            cursor.moveToFirst();
                            pozostalaIloscDni = cursor.getDouble(0);

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
                            repeating_intent.putExtra("nazwa", cursor.getString(1));
                            repeating_intent.putExtra("sumujTypy", String.valueOf(sumujTypy));
                            repeating_intent.putExtra("rand_val", rand_val - 3);

                            repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            pendingIntent = PendingIntent.getActivity(context, rand_val - 3, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm1);

                            initChannels(context, wybranyDzwiek);

                            builder = new NotificationCompat.Builder(context, "default")
                                    .setContentIntent(pendingIntent)
                                    .setSmallIcon(R.mipmap.ic_launcher_round)
                                    .setContentTitle("Weź pigułke")
                                    .setContentText("UWAGA | Pozostało tylko " + iloscLeku + " tabletek leku " + cursor.getString(1))
                                    .setAutoCancel(true)
                                    .setOnlyAlertOnce(true);

                            if(wybranyDzwiek!=0) builder.setSound(alarmSound);
                            if(czyWibracja==1) builder.setVibrate(new long[]{1000, 1000});

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
            int czyWibracja = intent.getIntExtra("czyWibracja", 0);
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

                cursor = myDb.getRand_WIZYTY(id_v);

                if (cursor.getCount() != 0) {
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

                setAlarmSound(wybranyDzwiek, context);

                initChannels(context, wybranyDzwiek);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Weź pigułke")
                        .setContentText(powiadomienie)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true);

                if(wybranyDzwiek!=0) builder.setSound(alarmSound);
                if(czyWibracja==1) builder.setVibrate(new long[]{1000, 1000});

                Notification notification = builder.build();
                notification.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

                notificationManager.notify(rand_val, notification);

            }

        }

        cursor.close();
        cursorTemp.close();

    }

    private void setAlarmSound(int wybranyDzwiek, Context context) {
        alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm1);

        switch (wybranyDzwiek) {
            case 1:
                alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm1);
                break;
            case 2:
                alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm2);
                break;
            case 3:
                alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm3);
                break;
            case 4:
                alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm4);
                break;
            case 5:
                alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm5);
                break;
            case 6:
                alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm6);
                break;
            case 7:
                alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm7);
                break;
            case 8:
                alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm8);
                break;
            case 9:
                alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm9);
                break;
        }
    }

    public void initChannels(Context context, int wybranyDzwiek) {

        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel("default",
                "Weź pigułkę",
                NotificationManager.IMPORTANCE_DEFAULT);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();

        if(wybranyDzwiek!=0) channel.setSound(alarmSound, audioAttributes);
        channel.setDescription("Powiadomienie");
        notificationManager.createNotificationChannel(channel);

    }

}


