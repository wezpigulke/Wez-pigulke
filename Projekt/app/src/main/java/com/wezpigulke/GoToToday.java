package com.wezpigulke;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(getActivity()).setTitle("Dzisiaj");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.today, container, false);

        label = new ArrayList<>();
        results = new ArrayList<>();
        lv = v.findViewById(R.id.todayList);
        spinner = v.findViewById(R.id.todaySpinner);
        uzytkownik = "Wszyscy";
        return v;
    }

    private void loadSpinnerData() {

        label.clear();
        Cursor cxz = myDb.getAllName_UZYTKOWNICY();
        label.add("Wszyscy");

        if (cxz.getCount() == 1) {
            spinner.setVisibility(View.GONE);
        } else {
            while (cxz.moveToNext()) {
                label.add(cxz.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    public void onResume() {

        super.onResume();
        AktualizujBaze();
        loadSpinnerData();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                uzytkownik = spinner.getItemAtPosition(position).toString();
                AktualizujBaze();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

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


    public void AktualizujBaze() {

        results.clear();

        lv.setAdapter(adapter);

        myDb = new DatabaseHelper(getActivity());
        final Cursor c;

        if (uzytkownik.equals("Wszyscy")) c = myDb.getAllData_NOTYFIKACJA();
        else c = myDb.getUserData_NOTYFIKACJA(uzytkownik);

        while (c.moveToNext()) {

            String dzisiejszaData = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String dzisiejszyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat tdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

            Date firstDate = null;
            Date secondDate = null;

            Date firstTime = null;
            Date secondTime = null;

            try {
                firstDate = sdf.parse(dzisiejszaData);
                firstTime = tdf.parse(dzisiejszyCzas);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                secondDate = sdf.parse(c.getString(4));
                secondTime = tdf.parse(c.getString(3));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diff = Objects.requireNonNull(secondDate).getTime() - Objects.requireNonNull(firstDate).getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffInMillis = Objects.requireNonNull(secondTime).getTime() - Objects.requireNonNull(firstTime).getTime();

            if (diffDays == 0 && diffInMillis >= 0) {
                results.add(new Today(c.getInt(0), c.getString(1) + " (Dawka: " + c.getString(2) + ")", "Godzina: " + c.getString(3), c.getString(4)));
            }
        }

        results.sort(Comparator.comparing(Today::getDate));

        adapter = new TodayListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    private void usunDane() throws ParseException {

        myDb = new DatabaseHelper(getActivity());

        int id = 0;
        int typ = 0;
        int id_p = 0;
        int dni = 0;

        Cursor ccc = myDb.getdataID_NOTYFIKACJA(idd);

        if (ccc.getCount() != 0) {
            while (ccc.moveToNext()) {
                nazwaLeku = ccc.getString(1);
                dawka = ccc.getDouble(2);
                godzina = ccc.getString(3);
                data = ccc.getString(4);
                id = Integer.parseInt(ccc.getString(6));
                id_p = Integer.parseInt(ccc.getString(7));
                typ = Integer.parseInt(ccc.getString(8));
                dni = Integer.parseInt(ccc.getString(9));
            }
        }


        String dzisiejszaData = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Calendar c = Calendar.getInstance();
        c.setTime(dt.parse(data));
        c.add(Calendar.DATE, 1);
        String dataJutrzejsza = dt.format(c.getTime());

        String czyDwucyfrowa = String.valueOf(godzina.charAt(1));
        Integer minutes;
        Integer hour;

        if (czyDwucyfrowa.equals(":")) {
            hour = Integer.parseInt(String.valueOf(godzina.charAt(0)));
            minutes = Integer.parseInt(String.valueOf(godzina.charAt(2))
                    + String.valueOf(godzina.charAt(3)));
        } else {
            hour = Integer.parseInt(String.valueOf(godzina.charAt(0))
                    + String.valueOf(godzina.charAt(1)));
            minutes = Integer.parseInt(String.valueOf(godzina.charAt(3))
                    + String.valueOf(godzina.charAt(4)));
        }

        Calendar cz = Calendar.getInstance();
        cz.set(Integer.parseInt(data.substring(6, 10)),
                Integer.parseInt(data.substring(3, 5)) - 1,
                Integer.parseInt(data.substring(0, 2)),
                hour,
                minutes,
                0);

        cz.add(Calendar.DATE, typ);

        Cursor crand = myDb.getRand_NOTYFIKACJA(id);
        crand.moveToFirst();

        if(dni > 1) myDb.updateDate_NOTYFIKACJA(id, dataJutrzejsza);
        else myDb.remove_NOTYFIKACJA(id);

        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getActivity(), crand.getInt(0), myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getContext(), "Anulacja: " + String.valueOf(crand.getInt(0)), Toast.LENGTH_LONG).show();

        Cursor policz = myDb.getCount_NOTYFIKACJA(id_p, dzisiejszaData);
        int ile = 0;

        if (policz.getCount() != 0) {
            policz.moveToNext();
            ile = Integer.parseInt(policz.getString(0));
        }

        if (dni > 1) {

            Intent intx = new Intent(getContext(), NotificationReceiver.class);
            intx.putExtra("Value", uzytkownik + " " + godzina + "  |  już czas, aby wziąć: " + nazwaLeku + " (" + dawka + ")");

            crand = myDb.getRand_NOTYFIKACJA(id);
            crand.moveToFirst();

            pendingIntent = PendingIntent.getBroadcast(
                    getContext(), crand.getInt(0), intx,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(Context.ALARM_SERVICE);
            assert alarmManager != null;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cz.getTimeInMillis(), AlarmManager.INTERVAL_DAY * typ, pendingIntent);

            Toast.makeText(getContext(), "Dodanie: " + String.valueOf(crand.getInt(0)), Toast.LENGTH_LONG).show();

            if (ile == 0) myDb.updateDays_PRZYPOMNIENIE(id_p, dni - 1);

        } else if (ile == 0) myDb.remove_PRZYPOMNIENIE(id_p);

        AktualizujBaze();

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

}
