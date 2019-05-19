package com.wezpigulke.notification;

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
    private String dzisiejszaData;
    private SimpleDateFormat hourAndDateSDF;
    private SimpleDateFormat onlyDateSDF;
    private long diff;
    private String godzina;
    private String data;

    private String powiadomienie;
    private Intent intent;
    private int id_n;
    private int id_p;
    private int id_l;
    private String uzytkownik;
    private String nazwaLeku;
    private Double jakaDawka;
    private int wybranyDzwiek;
    private int czyWibracja;
    private Integer rand_val;
    private Intent intx;
    private Calendar cal;
    private Integer typ;
    private int ileDniDodac;
    private DatabaseHelper myDb;
    private int id_h;
    private Context context;
    private int iloscDni;
    private NotificationManager notificationManager;
    private Intent repeating_intent;
    private PendingIntent pendingIntent;
    private NotificationCompat.Builder builder;
    private Integer id_v;
    private Integer war;
    private String imie_nazwisko;
    private String specjalizacja;
    private String dataPrzyszla;
    private Date date;

    private final int oneDay = 24 * 60 * 60 * 1000;

    private void countDiff() {

        Date firstDate = null;
        Date secondDate = null;

        try {
            firstDate = hourAndDateSDF.parse(dzisiejszaData);
            secondDate = hourAndDateSDF.parse(godzina + " " + data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (secondDate != null && firstDate != null) {
            diff = secondDate.getTime() - firstDate.getTime();
        }

    }

    private void intentGetExtra(int typ) {

        switch (typ) {
            case 0:
                powiadomienie = intent.getStringExtra("tresc");
                id_n = intent.getIntExtra("id", 0);
                id_p = intent.getIntExtra("idd", 0);
                godzina = intent.getStringExtra("godzina");
                uzytkownik = intent.getStringExtra("uzytkownik");
                nazwaLeku = intent.getStringExtra("nazwaLeku");
                jakaDawka = intent.getDoubleExtra("jakaDawka", 0);
                wybranyDzwiek = intent.getIntExtra("wybranyDzwiek", 0);
                czyWibracja = intent.getIntExtra("czyWibracja", 0);
                rand_val = intent.getIntExtra("rand_val", 0);
                break;
            case 1:
                powiadomienie = intent.getStringExtra("tresc");
                id_v = intent.getIntExtra("id", 0);
                id_l = intent.getIntExtra("id_l", 0);
                war = intent.getIntExtra("war", 0);
                godzina = intent.getStringExtra("godzina");
                data = intent.getStringExtra("data");
                imie_nazwisko = intent.getStringExtra("imie_nazwisko");
                specjalizacja = intent.getStringExtra("specjalizacja");
                uzytkownik = intent.getStringExtra("uzytkownik");
                wybranyDzwiek = intent.getIntExtra("wybranyDzwiek", 0);
                czyWibracja = intent.getIntExtra("czyWibracja", 0);
                rand_val = intent.getIntExtra("rand_val", 0);
                break;
        }

    }

    private void setAlarm(Context context) {

        PendingIntent newIntent = PendingIntent.getBroadcast(context, rand_val, intx, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        assert alarmManager != null;

        if (Build.VERSION.SDK_INT < 23) {
            if (Build.VERSION.SDK_INT >= 19)
                alarmManager.setExact(AlarmManager.RTC, cal.getTimeInMillis(), newIntent);
            else alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), newIntent);
        } else
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, cal.getTimeInMillis(), newIntent);
    }

    private void removeNotificationNorReminder() {


        cursor = myDb.getCountType_NOTYFIKACJA(id_p);
        cursor.moveToNext();

        if (cursor.getInt(0) == 1) {
            myDb.remove_PRZYPOMNIENIE(id_p);
            Log.d("NotificationReceiver", "Usunięcie przypomnienia o id: " + id_p);
        }

        myDb.remove_NOTYFIKACJA(id_n);
        Log.d("NotificationReceiver", "Usunięcie notyfikacji o id:" + id_n);

    }

    private void updateDaysOrRemoveReminder() {

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

    }

    private void removeNotificationAndReminder() {

        myDb.remove_PRZYPOMNIENIE(id_p);
        Log.d("NotificationReceiver", "Usunięcie przypomnienia o id: " + id_p);
        myDb.removeIdPrz_NOTYFIKACJA(id_p);

    }

    private void intentPutExtra(int typ) {
        switch (typ) {
            case 0:
                intx.putExtra("coPokazac", 0);
                intx.putExtra("tresc", uzytkownik + "  |  " + godzina + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")");
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
                break;
            case 1:
                repeating_intent.putExtra("id", id_v);
                repeating_intent.putExtra("id_l", id_l);
                repeating_intent.putExtra("war", war);
                repeating_intent.putExtra("godzina", godzina);
                repeating_intent.putExtra("data", data);
                repeating_intent.putExtra("imie_nazwisko", imie_nazwisko);
                repeating_intent.putExtra("specjalizacja", specjalizacja);
                repeating_intent.putExtra("uzytkownik", uzytkownik);
                repeating_intent.putExtra("rand_val", rand_val);
                break;
            case 2:
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
                break;
        }
    }

    private void setSimpleDateFormats() {
        dzisiejszaData = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
        hourAndDateSDF = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        onlyDateSDF = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    private void updateDateAndSetNewAlarm() {

        Calendar cz = Calendar.getInstance();
        cursor = myDb.getdataID_NOTYFIKACJA(id_n);

        if (cursor != null) {
            cursor.moveToNext();
            try {
                cz.setTime(onlyDateSDF.parse(cursor.getString(4)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        cz.add(Calendar.DATE, ileDniDodac);
        dataPrzyszla = onlyDateSDF.format(cz.getTime());

        myDb.updateDate_NOTYFIKACJA(id_n, dataPrzyszla);

        cursor = myDb.getDataName_LEK(nazwaLeku);
        cursor.moveToFirst();

        double iloscLeku = cursor.getDouble(2) - jakaDawka * ileDniDodac;

        if (iloscLeku < 0) iloscLeku = 0;
        myDb.update_LEK(cursor.getInt(0), iloscLeku);
        String przetwarzanaData = godzina + " " + data;

        try {
            date = hourAndDateSDF.parse(przetwarzanaData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, ileDniDodac);

        Date dateTemp = cal.getTime();

        myDb.updateDate_NOTYFIKACJA(id_n, onlyDateSDF.format(dateTemp));

        intx = new Intent(context, NotificationReceiver.class);
        intentPutExtra(0);
        setAlarm(context);

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("========ALARM==========", "Alarm odpalony dla: " + intent.getIntExtra("id", 0));
        this.context = context;
        this.intent = intent;
        date = null;

        myDb = new DatabaseHelper(context);
        int coPokazac = intent.getIntExtra("coPokazac", 0);
        setSimpleDateFormats();

        if (coPokazac == 0) {

            data = "";
            iloscDni = 0;
            intentGetExtra(0);
            int delay = 60 * 60 * 100;
            cursor = myDb.getdataID_NOTYFIKACJA(id_n);

            if (cursor.getCount() != 0) {
                cursor.moveToNext();
                data = cursor.getString(4);
                iloscDni = cursor.getInt(9);
            }

            countDiff();

            if (iloscDni <= 0) {

                removeNotificationAndReminder();

            } else {

                cursor = myDb.getdataID_NOTYFIKACJA(id_n);

                if (cursor.getCount() != 0 && diff + delay < 0) {

                    while (diff < 0) {

                        cursor = myDb.getdataID_NOTYFIKACJA(id_n);
                        if (cursor != null) {
                            cursor.moveToNext();
                            typ = cursor.getInt(8);
                        }

                        diff += typ * oneDay;
                        ileDniDodac += typ;
                        iloscDni--;

                    }

                    if (iloscDni <= 0) {

                        removeNotificationNorReminder();
                        return;

                    } else {

                        updateDateAndSetNewAlarm();
                        Log.d("========ALARM==========", "Dodanie: " + " | " + rand_val + " | " + hourAndDateSDF.format(cal.getTime()));

                    }

                    updateDaysOrRemoveReminder();

                } else {

                    myDb.insert_HISTORIA(uzytkownik, godzina, data, nazwaLeku, jakaDawka, "BRAK", "BRAK");

                    cursor = myDb.getMAXid_HISTORIA();
                    cursor.moveToFirst();
                    id_h = Integer.parseInt(cursor.getString(0));

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
                        if (Integer.parseInt(cursor.getString(0)) == 1) {
                            myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);
                        } else {
                            cursor = myDb.getType_PRZYPOMNIENIE(id_p);

                            if (cursor.getCount() != 0) {
                                cursor.moveToNext();
                                int typ_p = cursor.getInt(0);
                                Calendar calx = Calendar.getInstance();

                                try {
                                    calx.setTime(onlyDateSDF.parse(data));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                calx.add(Calendar.DATE, typ_p);
                                dataPrzyszla = onlyDateSDF.format(calx.getTime());
                                myDb.updateDate_NOTYFIKACJA(id_n, dataPrzyszla);
                            }
                        }

                        sendReminderNotification();

                        if (iloscDni >= 1) setFutureAlarm();
                        else removeNotificationNorReminder();

                        cursor = myDb.getDataName_LEK(nazwaLeku);
                        cursor.moveToFirst();
                        double iloscLeku = cursor.getDouble(2) - jakaDawka;

                        if (iloscLeku < 0) iloscLeku = 0;

                        myDb.update_LEK(cursor.getInt(0), iloscLeku);
                        int id = Integer.parseInt(cursor.getString(0));
                        double sumujTypy = countAmountOfMedicine();

                        if (sumujTypy > iloscLeku) {
                            sendNotificationToUpdateQuantity(id, sumujTypy, iloscLeku);
                        }

                    }
                }
            }
        } else {
            intentGetExtra(1);
            countDiff();
            int delay = 15 * 60 * 100;

            if (diff + delay < 0) {
                cursor = myDb.getRand_WIZYTY(id_v);
                if (cursor.getCount() != 0) {
                    myDb.remove_WIZYTY(id_v);
                }
            } else sendVisitNotification();
        }

        closeCursors();

    }

    private void closeCursors() {
        if (cursor != null) cursor.close();
        if (cursorTemp != null) cursorTemp.close();
    }

    private void setFutureAlarm() {

        cursor = myDb.getType_PRZYPOMNIENIE(id_p);
        cursor.moveToFirst();
        int typPrz = cursor.getInt(0);

        String przetwarzanaData = godzina + " " + data;

        Date date = null;
        try {
            date = hourAndDateSDF.parse(przetwarzanaData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, typPrz);

        Date dateTemp = cal.getTime();

        myDb.updateDate_NOTYFIKACJA(id_n, onlyDateSDF.format(dateTemp));

        intx = new Intent(context, NotificationReceiver.class);
        intentPutExtra(0);

        setAlarm(context);

        Log.d("========ALARM==========", "Dodanie: " + " | " + rand_val + "\n" + hourAndDateSDF.format(cal.getTime()));

    }

    private double countAmountOfMedicine() {

        cursor = myDb.getIDfromMedicine_PRZYPOMNIENIE(nazwaLeku);
        double ileNotyfikacji, typPrzypomnienia, pozostalaIloscDni;
        double sumujTypy = 0.0;

        while (cursor.moveToNext()) {

            int idTemp = cursor.getInt(0);

            cursor = myDb.getCountType_NOTYFIKACJA(cursor.getInt(0));
            cursor.moveToFirst();
            ileNotyfikacji = cursor.getDouble(0);

            cursor = myDb.getType_PRZYPOMNIENIE(idTemp);
            cursor.moveToFirst();
            typPrzypomnienia = cursor.getInt(0);

            cursor = myDb.getDays_PRZYPOMNIENIE(idTemp);
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

        return sumujTypy;

    }

    private void sendNotificationToUpdateQuantity(int id, double sumujTypy, double iloscLeku) {

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        repeating_intent = new Intent(context, RepeatingActivityReminder.class);

        repeating_intent.putExtra("coPokazac", 1);
        repeating_intent.putExtra("id", id);
        repeating_intent.putExtra("nazwa", nazwaLeku);
        repeating_intent.putExtra("sumujTypy", String.valueOf(sumujTypy));
        repeating_intent.putExtra("rand_val", rand_val - 3);

        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        pendingIntent = PendingIntent.getActivity(context, rand_val - 3, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm1);

        initChannels(context, wybranyDzwiek);

        builder = new NotificationCompat.Builder(context, "default")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.pill_black)
                .setContentTitle("Weź pigułke")
                .setContentText("UWAGA | Pozostało tylko " + iloscLeku + " tabletek leku " + nazwaLeku)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

        if (wybranyDzwiek != 0) builder.setSound(alarmSound);
        if (czyWibracja == 1) builder.setVibrate(new long[]{1000, 1000});

        Notification notification2 = builder.build();
        notification2.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

        notificationManager.notify(rand_val - 3, notification2);

    }

    private void sendReminderNotification() {

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        repeating_intent = new Intent(context, RepeatingActivityReminder.class);
        intentPutExtra(2);

        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(context, rand_val, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        builder.setSmallIcon(R.drawable.medicine_black);
        builder.setContentTitle("Weź pigułke");
        builder.setContentText(powiadomienie);
        builder.setAutoCancel(true);
        builder.addAction(0, "Wziąłem", yesIntent);
        builder.addAction(0, "Zapomniałem", noIntent);

        if (wybranyDzwiek != 0) builder.setSound(alarmSound);
        if (czyWibracja == 1) builder.setVibrate(new long[]{1000, 1000});

        Notification notification = builder.build();
        notification.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

        notificationManager.notify(rand_val, notification);

    }

    private void sendVisitNotification() {

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        repeating_intent = new Intent(context, RepeatingActivityVisit.class);
        intentPutExtra(1);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(context, rand_val, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        setAlarmSound(wybranyDzwiek, context);
        initChannels(context, wybranyDzwiek);

        builder = new NotificationCompat.Builder(context, "default")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.visit)
                .setContentTitle("Weź pigułke")
                .setContentText(powiadomienie)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

        if (wybranyDzwiek != 0) builder.setSound(alarmSound);
        if (czyWibracja == 1) builder.setVibrate(new long[]{1000, 1000});

        Notification notification = builder.build();
        notification.flags |= NotificationCompat.FLAG_AUTO_CANCEL;

        notificationManager.notify(rand_val, notification);

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

        if (wybranyDzwiek != 0) channel.setSound(alarmSound, audioAttributes);
        channel.setDescription("Powiadomienie");
        notificationManager.createNotificationChannel(channel);

    }

}


