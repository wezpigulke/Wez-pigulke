package com.example.kuba.dsadsax;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

public class GoToToday extends Fragment {

    DatabaseHelper myDb;
    private List<Today> results;
    private TodayListAdapter adapter;
    private ListView lv;
    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;
    private String profil;
    private String nazwaLeku;
    private String dawka;
    private Integer typ;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Dzisiaj");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.today, container, false);

        label = new ArrayList<>();
        results = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.todayList);
        spinner = v.findViewById(R.id.todaySpinner);
        uzytkownik = "Wszyscy";
        return v;
    }

    private void loadSpinnerData() {

        label.clear();
        Cursor cxz = myDb.getAllName_UZYTKOWNICY();
        label.add("Wszyscy");

        if (cxz.getCount() != 0) {
            while (cxz.moveToNext()) {
                label.add(cxz.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        }

    }

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

                            int idd = (int) results.get(position).getId();

                            myDb = new DatabaseHelper(getActivity());

                            int id = 0;
                            int typ = 0;
                            int id_p = 0;
                            int dni = 0;

                            Cursor ccc = myDb.getdataID_NOTYFIKACJA(idd);
                            if (ccc.getCount() != 0) {
                                while (ccc.moveToNext()) {
                                    nazwaLeku = ccc.getString(1);
                                    dawka = ccc.getString(2);
                                    profil = ccc.getString(5);
                                    id = Integer.parseInt(ccc.getString(6));
                                    id_p = Integer.parseInt(ccc.getString(7));
                                    typ = Integer.parseInt(ccc.getString(8));
                                    dni = Integer.parseInt(ccc.getString(9));
                                }
                            }

                            String dzisiejszaData = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            Date dzisiaj = null;

                            try {
                                dzisiaj = sdf.parse(dzisiejszaData);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Calendar cz = Calendar.getInstance();
                            cz.setTime(dzisiaj);
                            cz.add(Calendar.DATE, typ);

                            myDb.remove_NOTYFIKACJA(id);
                            myDb.insert_USUNIETE_PRZ(id);

                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                            Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    getActivity(), id, myIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);

                            if(dni>0) {

                                Intent intx = new Intent(getContext(), NotificationReceiver.class);
                                intx.putExtra("Value", profil + "  |  już czas, aby wziąć: " + nazwaLeku + " (" + dawka + ")");

                                pendingIntent = PendingIntent.getBroadcast(
                                        getContext(), id, intx,
                                        PendingIntent.FLAG_UPDATE_CURRENT);

                                alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cz.getTimeInMillis(), AlarmManager.INTERVAL_DAY * typ, pendingIntent);

                                myDb.remove_USUNIETE_PRZ(id);

                                Cursor policz = myDb.getCount_NOTYFIKACJA(id_p, dzisiejszaData);
                                int ile = 0;

                                if (policz.getCount() != 0) {
                                        policz.moveToNext();
                                        ile = Integer.parseInt(policz.getString(0));
                                    }

                                if (ile==0) myDb.updateDays_PRZYPOMNIENIE(id_p, dzisiejszaData, dni - 1);
                                if ((dni-1)==0)  myDb.remove_PRZYPOMNIENIE(id_p);
                            }
                            else {
                                myDb.remove_PRZYPOMNIENIE(id_p);
                            }

                            AktualizujBaze();

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

        if (uzytkownik == "Wszyscy") c = myDb.getAllData_NOTYFIKACJA();
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

                long diff = secondDate.getTime() - firstDate.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                long diffInMillis = secondTime.getTime() - firstTime.getTime();

                if (diffDays == 0 && diffInMillis >= 0) {
                    results.add(new Today(c.getInt(0), c.getString(1) + " (" + c.getString(2) + ")", "Godzina: " + c.getString(3), c.getString(4)));
                }
        }

        results.sort(Comparator.comparing(Today::getDate));

        adapter = new TodayListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }
}
