package com.wezpigulke.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.wezpigulke.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class BootCompletedNotificationReceiver extends BroadcastReceiver {

    private DatabaseHelper myDb;
    private Cursor cursor;
    private Cursor cursorTemp;

    private Integer id;
    private String godzina;
    private String data;
    private String imie_nazwisko;
    private String specjalizacja;
    private String profil;
    private Integer rand_val;
    private Integer dzwiek;
    private Integer czyWibracja;
    private Date firstDate;
    private Date secondDate;
    private String dzisiejszaData;
    private SimpleDateFormat sdf;
    private long diff;
    private Calendar cal;

    private Integer id_n;
    private String nazwaLeku;
    private Double jakaDawka;
    private String uzytkownik;
    private Integer id_p;
    private Integer iloscDni;
    private Intent intent;
    private Context context;
    private int typ;
    private int ileDniDodac;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Dodaje alarmy po restarcie", Toast.LENGTH_LONG).show();
        initializeVariables(context);
        setNotificationForVisit();
        setNotificationForReminder();
        closeCursors();

    }

    private void setNotificationForReminder() {

        cursor = myDb.getAllData_NOTYFIKACJA();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                initializeVariables(1);
                countDiff();

                if (iloscDni <= 0) {

                    myDb.remove_PRZYPOMNIENIE(id_p);
                    myDb.removeIdPrz_NOTYFIKACJA(id_p);

                }

                if (diff < 0) {

                    String dataPrzyszla = data;

                    while (diff < 0) {

                        cursor = myDb.getdataID_NOTYFIKACJA(id_n);
                        if (cursor != null) {
                            cursor.moveToNext();
                            typ = cursor.getInt(8);
                        }

                        diff += typ * 24 * 60 * 60 * 100;
                        ileDniDodac += typ;
                        iloscDni--;

                        if (iloscDni <= 0) {

                            cursor = myDb.getCountType_NOTYFIKACJA(id_p);
                            cursor.moveToNext();
                            myDb.remove_NOTYFIKACJA(id_n);
                            if (cursor.getInt(0) == 1) myDb.remove_PRZYPOMNIENIE(id_p);
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

                            cz.add(Calendar.DATE, ileDniDodac);
                            dataPrzyszla = dt.format(cz.getTime());

                            myDb.updateDate_NOTYFIKACJA(id_n, dataPrzyszla);

                            cursor = myDb.getDataName_LEK(nazwaLeku);
                            cursor.moveToFirst();

                            double iloscLeku = cursor.getDouble(2) - jakaDawka;

                            if (iloscLeku < 0) iloscLeku = 0;
                            myDb.update_LEK(cursor.getInt(0), iloscLeku);

                        }

                    }

                    cursor = myDb.getCountType_NOTYFIKACJA(id_p);
                    cursor.moveToFirst();

                    if (cursor.getInt(0) == 0) {
                        myDb.remove_PRZYPOMNIENIE(id_p);
                    } else if (cursor.getInt(0) == 1) {
                        myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);
                    } else {
                        cursor = myDb.getCount_NOTYFIKACJA(id_p, dataPrzyszla);
                        cursor.moveToNext();
                        cursorTemp = myDb.getCountType_NOTYFIKACJA(id_p);
                        cursorTemp.moveToNext();
                        if (cursor.getInt(0) == cursorTemp.getInt(0))
                            myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);
                    }
                } else {

                    setCalendarDate();
                    this.intent = new Intent(context, NotificationReceiver.class);
                    intentPutExtra(2);
                    setAlarm(context);

                }

            }

        }

    }

    private void setNotificationForVisit() {

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                initializeVariables(0);
                countDiff();

                if (diff < 0) {

                    myDb.remove_WIZYTY(id);

                } else {

                    this.intent = new Intent(context, NotificationReceiver.class);
                    intentPutExtra(0);

                    setCalendarDate();
                    cal.add(Calendar.DATE, -1);

                    if (diff > 24 * 60 * 60 * 100) setAlarm(context);
                    rand_val--;

                    intentPutExtra(1);

                    if (diff > 3 * 60 * 60 * 100) {
                        cal.add(Calendar.DATE, +1);
                        cal.add(Calendar.HOUR_OF_DAY, -3);
                    } else cal.add(Calendar.MILLISECOND, (int) diff);

                    setAlarm(context);

                }

            }

        }

    }

    private void closeCursors() {

        if (cursor != null) cursor.close();
        if (cursorTemp != null) cursorTemp.close();

    }

    private void countDiff() {

        try {
            firstDate = sdf.parse(dzisiejszaData);
            secondDate = sdf.parse(godzina + " " + data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        diff = 0;

        if (secondDate != null && firstDate != null) {
            diff = secondDate.getTime() - firstDate.getTime();
        }

    }

    private void initializeVariables(int type) {

        if (type == 0) {

            id = cursor.getInt(0);
            godzina = cursor.getString(1);
            data = cursor.getString(2);
            imie_nazwisko = cursor.getString(3);
            specjalizacja = cursor.getString(4);
            profil = cursor.getString(5);
            rand_val = cursor.getInt(6);
            dzwiek = cursor.getInt(7);
            czyWibracja = cursor.getInt(8);

        } else if (type == 1) {

            id_n = cursor.getInt(0);
            nazwaLeku = cursor.getString(1);
            jakaDawka = cursor.getDouble(2);
            godzina = cursor.getString(3);
            data = cursor.getString(4);
            uzytkownik = cursor.getString(5);
            id_p = cursor.getInt(6);
            iloscDni = cursor.getInt(7);
            rand_val = cursor.getInt(9);
            dzwiek = cursor.getInt(10);
            czyWibracja = cursor.getInt(11);

        }


    }

    private void intentPutExtra(int type) {

        if (type == 0) {

            intent.putExtra("coPokazac", 1);
            intent.putExtra("tresc", profil + "  |  wizyta u " + imie_nazwisko + " jutro o " + godzina);
            intent.putExtra("id", id);
            intent.putExtra("godzina", godzina);
            intent.putExtra("data", data);
            intent.putExtra("imie_nazwisko", imie_nazwisko);
            intent.putExtra("specjalizacja", specjalizacja);
            intent.putExtra("uzytkownik", profil);
            intent.putExtra("wybranyDzwiek", dzwiek);
            intent.putExtra("czyWibracja", czyWibracja);
            intent.putExtra("rand_val", rand_val);

        } else if (type == 1) {

            intent.putExtra("tresc", profil + "  |  wizyta u " + imie_nazwisko + " o " + godzina);
            intent.putExtra("rand_val", rand_val);

        } else if (type == 2) {

            intent.putExtra("tresc", uzytkownik + " |  " + godzina + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")");
            intent.putExtra("coPokazac", 0);
            intent.putExtra("czyPowtarzanyAlarm", true);
            intent.putExtra("id", id_n);
            intent.putExtra("idd", id_p);
            intent.putExtra("godzina", godzina);
            intent.putExtra("data", data);
            intent.putExtra("uzytkownik", uzytkownik);
            intent.putExtra("nazwaLeku", nazwaLeku);
            intent.putExtra("jakaDawka", jakaDawka);
            intent.putExtra("iloscDni", iloscDni - 1);
            intent.putExtra("wybranyDzwiek", dzwiek);
            intent.putExtra("czyWibracja", czyWibracja);
            intent.putExtra("rand_val", rand_val);

        }

    }

    private void setAlarm(Context context) {

        Log.d("BootCompletedNotifi", "Dodanie alarmu dla: " + sdf.format(cal));
        Toast.makeText(context, "Dodanie alarmu dla: " + sdf.format(cal), Toast.LENGTH_LONG).show();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, rand_val, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        if (Build.VERSION.SDK_INT < 23) {
            if (Build.VERSION.SDK_INT >= 19)
                alarmManager.setExact(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
            else alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
        } else
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);

    }

    private void setCalendarDate() {

        String przetwarzanaData = godzina + " " + data;

        Date date = null;

        try {
            date = sdf.parse(przetwarzanaData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal = Calendar.getInstance();
        cal.setTime(date);

    }

    private void initializeVariables(Context context) {

        myDb = new DatabaseHelper(context);
        dzisiejszaData = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
        sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        cursor = myDb.getAllData_WIZYTY();
        this.context = context;

    }

}