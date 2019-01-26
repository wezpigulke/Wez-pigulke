package com.wezpigulke.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.Toast;

import com.wezpigulke.DatabaseHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class BootCompletedNotificationReceiver extends BroadcastReceiver {

    DatabaseHelper myDb;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "DODAJE ALARMY PO RESTARCIE", Toast.LENGTH_LONG).show();

        myDb = new DatabaseHelper(context);

        Cursor c = myDb.getAllData_NOTYFIKACJA();
        Cursor cv = myDb.getAllData_WIZYTY();

        String dzisiejszaData = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

        Date firstDate = null;
        Date secondDate = null;

        if (cv.getCount() != 0) {
            while(cv.moveToNext()) {

                Integer id = cv.getInt(0);
                String godzina = cv.getString(1);
                String data = cv.getString(2);
                String imie_nazwisko = cv.getString(3);
                String specjalizacja = cv.getString(4);
                String profil = cv.getString(5);
                Integer rand_val = cv.getInt(6);
                Integer dzwiek = cv.getInt(7);
                Boolean czyWibracja = Boolean.parseBoolean(cv.getString(8));

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

                if (diff < 0) {

                        myDb.remove_WIZYTY(id);

                } else {

                    Intent intxz = new Intent(context, NotificationReceiver.class);

                    intxz.putExtra("coPokazac", 1);
                    intxz.putExtra("tresc", profil + "  |  wizyta u " + imie_nazwisko + " jutro o " + godzina);
                    intxz.putExtra("id", id);
                    intxz.putExtra("godzina", godzina);
                    intxz.putExtra("data", data);
                    intxz.putExtra("imie_nazwisko", imie_nazwisko);
                    intxz.putExtra("specjalizacja", specjalizacja);
                    intxz.putExtra("uzytkownik", profil);
                    intxz.putExtra("wybranyDzwiek", dzwiek);
                    intxz.putExtra("czyWibracja", czyWibracja);
                    intxz.putExtra("rand_val", rand_val);

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfz = new SimpleDateFormat("HH:mm dd/MM/yyyy");

                    String przetwarzanaData = godzina + " " + data;

                    Date date = null;
                    try {
                        date = sdfz.parse(przetwarzanaData);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);

                    cal.add(Calendar.DATE, -1);

                    if(diff > 24*60*60*100) {

                        PendingIntent pendingIntentt = PendingIntent.getBroadcast(context, rand_val, intxz, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManagerr = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                        assert alarmManagerr != null;
                        alarmManagerr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntentt);

                        Toast.makeText(context, "Dodanie: " + rand_val, Toast.LENGTH_LONG).show();

                    }

                    rand_val--;
                    intxz.putExtra("tresc", profil + "  |  wizyta u " + imie_nazwisko + " o " + godzina);
                    intxz.putExtra("rand_val", rand_val);

                    if(diff > 3*60*60*100) {
                        cal.add(Calendar.DATE, +1);
                        cal.add(Calendar.HOUR_OF_DAY, -3);
                    } else cal.add(Calendar.MILLISECOND, (int)diff);


                    PendingIntent pendingIntentt = PendingIntent.getBroadcast(context, rand_val, intxz, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManagerr = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    assert alarmManagerr != null;
                    alarmManagerr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntentt);

                    Toast.makeText(context, "Dodanie: " + rand_val, Toast.LENGTH_LONG).show();

                }


            }

        }

        if (c.getCount() != 0) {
            while (c.moveToNext()) {


                Integer id_n = c.getInt(0);
                String nazwaLeku = c.getString(1);
                Double jakaDawka = c.getDouble(2);
                String godzina = c.getString(3);
                String data = c.getString(4);
                String uzytkownik = c.getString(5);
                Integer id_p = c.getInt(6);
                Integer iloscDni = c.getInt(7);
                Integer rand_val = c.getInt(9);
                Integer dzwiek = c.getInt(10);
                Boolean czyWibracja = Boolean.getBoolean(c.getString(11));

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

                }

                if (diff < 0) {

                    String dataPrzyszla = data;

                    while (diff < 0) {

                        diff += 24 * 60 * 60 * 100;
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

                    if (cds.getInt(0) == 0) myDb.remove_PRZYPOMNIENIE(id_p);
                    else if (cds.getInt(0) == 1) myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);
                    else {
                        Cursor cee = myDb.getCount_NOTYFIKACJA(id_p, dataPrzyszla);
                        cee.moveToNext();
                        Cursor ceee = myDb.getCountType_NOTYFIKACJA(id_p);
                        ceee.moveToNext();
                        if (cee.getInt(0) == ceee.getInt(0))
                            myDb.updateDays_PRZYPOMNIENIE(id_p, iloscDni);
                    }
                } else {

                    Cursor csszz = myDb.getType_PRZYPOMNIENIE(id_p);
                    csszz.moveToFirst();

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfz = new SimpleDateFormat("HH:mm dd/MM/yyyy");

                    String przetwarzanaData = godzina + " " + data;

                    Date date = null;
                    try {
                        date = sdfz.parse(przetwarzanaData);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);

                    Intent intx = new Intent(context, NotificationReceiver.class);

                    intx.putExtra("tresc", uzytkownik + " |  " + godzina + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")");
                    intx.putExtra("coPokazac", 0);
                    intx.putExtra("czyPowtarzanyAlarm", true);
                    intx.putExtra("id", id_n);
                    intx.putExtra("idd", id_p);
                    intx.putExtra("godzina", godzina);
                    intx.putExtra("data", data);
                    intx.putExtra("uzytkownik", uzytkownik);
                    intx.putExtra("nazwaLeku", nazwaLeku);
                    intx.putExtra("jakaDawka", jakaDawka);
                    intx.putExtra("iloscDni", iloscDni - 1);
                    intx.putExtra("wybranyDzwiek", dzwiek);
                    intx.putExtra("czyWibracja", czyWibracja);
                    intx.putExtra("rand_val", rand_val);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, rand_val, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    assert alarmManager != null;

                    if (Build.VERSION.SDK_INT < 23) {
                        if (Build.VERSION.SDK_INT >= 19) alarmManager.setExact(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
                        else alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
                    } else alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);

                    Toast.makeText(context, "Dodanie: " + String.valueOf(rand_val) + "\n" + cal.getTime().toString().substring(0, 16), Toast.LENGTH_LONG).show();

                }

            }

        }

    }

}