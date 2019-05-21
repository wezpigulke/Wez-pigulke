package com.wezpigulke.go_to;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;
import com.wezpigulke.adapters.TodayListAdapter;
import com.wezpigulke.classes.Today;
import com.wezpigulke.notification.BootCompletedNotificationReceiver;
import com.wezpigulke.notification.NotificationReceiver;
import com.wezpigulke.other.SwipeDismissListViewTouchListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class GoToToday extends Fragment {

    DatabaseHelper myDb;
    private List<Today> results;
    private TodayListAdapter adapter;
    private ListView lv;
    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;
    private String nazwaLeku;
    private Double dawka;
    private String godzina;
    private String data;
    private Integer idd;
    private Cursor cursor;
    private boolean czyDodacPrzypomnienia;
    private long diff;
    private String dzisiejszaData;
    private SimpleDateFormat hourAndDateSDF;
    private String dzisiaj;
    private SimpleDateFormat sdf;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Dzisiaj");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.today, container, false);
        initializeVariables(v);
        spinnerItemSelectedListener();
        setLvTouchListener();

        return v;
    }

    private void initializeVariables(View v) {
        czyDodacPrzypomnienia = false;
        label = new ArrayList<>();
        results = new ArrayList<>();
        lv = v.findViewById(R.id.todayList);
        spinner = v.findViewById(R.id.todaySpinner);
        uzytkownik = "Wszyscy";
        dzisiejszaData = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
        hourAndDateSDF = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        dzisiaj = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    private void loadSpinnerData() {

        label.clear();
        cursor = myDb.getAllName_UZYTKOWNICY();
        label.add("Wszyscy");

        if (cursor.getCount() == 1) {
            spinner.setVisibility(View.GONE);
        } else {
            while (cursor.moveToNext()) {
                label.add(cursor.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        }

    }

    private void spinnerItemSelectedListener() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                uzytkownik = spinner.getItemAtPosition(position).toString();
                aktualizujBaze();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setLvTouchListener() {

        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                lv,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {

                            idd = results.get(position).getId();
                            dialogRemove();
                        }
                    }
                });

        lv.setOnTouchListener(touchListener);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void onResume() {

        super.onResume();
        aktualizujBaze();
        loadSpinnerData();

    }


    public void aktualizujBaze() {

        results.clear();

        lv.setAdapter(adapter);

        myDb = new DatabaseHelper(getActivity());

        if (uzytkownik.equals("Wszyscy")) cursor = myDb.getAllData_NOTYFIKACJA();
        else cursor = myDb.getUserData_NOTYFIKACJA(uzytkownik);

        while (cursor.moveToNext()) {

            Date firstTimeAndDate = null;
            Date secondTimeAndDate = null;
            Date firstDate = null;
            Date secondDate = null;

            try {
                firstDate = sdf.parse(dzisiaj);
                secondDate = sdf.parse(cursor.getString(4));
                firstTimeAndDate = hourAndDateSDF.parse(dzisiejszaData);
                secondTimeAndDate = hourAndDateSDF.parse(cursor.getString(3) + " " + cursor.getString(4));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diffTemp = Objects.requireNonNull(secondDate).getTime() - Objects.requireNonNull(firstDate).getTime();
            long diffDays = diffTemp / (24 * 60 * 60 * 1000);

            if (secondTimeAndDate != null && firstTimeAndDate != null) {
                diff = secondTimeAndDate.getTime() - firstTimeAndDate.getTime();
            }

            if (diff >= 0 && diffDays == 0) {
                results.add(new Today(cursor.getInt(0), cursor.getString(1) + " (Dawka: " + cursor.getString(2) + ")", "Godzina: " + cursor.getString(3), cursor.getString(5)));
            }
        }

        Collections.sort(results, new CustomComparator());

        adapter = new TodayListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    @SuppressLint("ShortAlarm")
    private void usunDane() throws ParseException {

        myDb = new DatabaseHelper(getActivity());

        int id = 0;
        int typ = 0;
        int id_p = 0;
        int dni = 0;

        cursor = myDb.getdataID_NOTYFIKACJA(idd);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                nazwaLeku = cursor.getString(1);
                dawka = cursor.getDouble(2);
                godzina = cursor.getString(3);
                data = cursor.getString(4);
                id = cursor.getInt(6);
                id_p = cursor.getInt(7);
                typ = cursor.getInt(8);
                dni = cursor.getInt(9);
            }
        }

        String dzisiejszaData = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Calendar c = Calendar.getInstance();
        c.setTime(dt.parse(data));
        c.add(Calendar.DATE, 1);
        String dataJutrzejsza = dt.format(c.getTime());

        String czyDwucyfrowa = String.valueOf(godzina.charAt(1));
        int minutes;
        int hour;

        if (czyDwucyfrowa.equals(":")) {
            hour = Integer.parseInt(String.valueOf(godzina.charAt(0)));
            minutes = Integer.parseInt(String.valueOf(godzina.charAt(2))
                    + godzina.charAt(3));
        } else {
            hour = Integer.parseInt(String.valueOf(godzina.charAt(0))
                    + godzina.charAt(1));
            minutes = Integer.parseInt(String.valueOf(godzina.charAt(3))
                    + godzina.charAt(4));
        }

        Calendar cz = Calendar.getInstance();
        cz.set(Integer.parseInt(data.substring(6, 10)),
                Integer.parseInt(data.substring(3, 5)) - 1,
                Integer.parseInt(data.substring(0, 2)),
                hour,
                minutes,
                0);

        cz.add(Calendar.DATE, typ);

        cursor = myDb.getRand_NOTYFIKACJA(id);
        cursor.moveToFirst();

        if (dni > 1) myDb.updateDate_NOTYFIKACJA(id, dataJutrzejsza);
        else {
            myDb.remove_NOTYFIKACJA(id);
        }

        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getActivity(), cursor.getInt(0), myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);

        cursor = myDb.getCount_NOTYFIKACJA(id_p, dzisiejszaData);
        int ile = 0;

        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            ile = Integer.parseInt(cursor.getString(0));
        }

        if (dni > 1) {

            Intent intx = new Intent(getContext(), NotificationReceiver.class);
            intx.putExtra("Value", uzytkownik + "  |  " + godzina + "  |  Weź: " + nazwaLeku + " (" + dawka + ")");

            cursor = myDb.getRand_NOTYFIKACJA(id);
            cursor.moveToFirst();

            pendingIntent = PendingIntent.getBroadcast(
                    getContext(), cursor.getInt(0), intx,
                    PendingIntent.FLAG_UPDATE_CURRENT);


            alarmManager = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(Context.ALARM_SERVICE);
            assert alarmManager != null;

            if (Build.VERSION.SDK_INT < 23) {
                alarmManager.setExact(AlarmManager.RTC, cz.getTimeInMillis(), pendingIntent);
            } else
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, cz.getTimeInMillis(), pendingIntent);

            if (ile == 0) myDb.updateDays_PRZYPOMNIENIE(id_p, dni - 1);

        }

        aktualizujBaze();

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> {
                    try {
                        usunDane();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    @Override
    public void onDestroy() {
        if (cursor != null) cursor.close();
        super.onDestroy();
    }

    public class CustomComparator implements Comparator<Today> {
        @Override
        public int compare(Today o1, Today o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    }

}
