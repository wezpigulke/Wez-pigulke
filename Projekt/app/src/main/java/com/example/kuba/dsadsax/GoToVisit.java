package com.example.kuba.dsadsax;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GoToVisit extends Fragment {

    DatabaseHelper myDb;
    private List<Visit> results;
    private VisitListAdapter adapter;
    private ListView lv;
    private FloatingActionButton fabz;
    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Lista wizyt");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.visit, container, false);

        label = new ArrayList<String>();

        fabz = (FloatingActionButton) v.findViewById(R.id.fabz);

        fabz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cel = new Intent(v.getContext(), AddVisit.class);
                startActivity(cel);
            }
        });

        results = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.visitList);
        spinner = v.findViewById(R.id.spinner4);
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

        myDb = new DatabaseHelper(getActivity());
        Cursor c = myDb.getAllData_WIZYTY();

        String dzisiejszaData = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String dzisiejszyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat tdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Date firstDate = null;
        Date secondDate = null;

        Date firstTime = null;
        Date secondTime = null;

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                try {
                    firstDate = sdf.parse(dzisiejszaData);
                    firstTime = tdf.parse(dzisiejszyCzas);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    secondDate = sdf.parse(c.getString(2));
                    secondTime = tdf.parse(c.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long diff = secondDate.getTime() - firstDate.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                long diffInMillis = secondTime.getTime() - firstTime.getTime();

                if (diffDays <= 0 && diffInMillis < 0) {
                    myDb.remove_WIZYTY(c.getInt(0));
                    AktualizujBaze();
                }
            }
        }


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

                            int id = (int) results.get(position).getId();

                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                            int usunID = (id * 2) - 1;

                            Intent myIntent1 = new Intent(getActivity(), NotificationReceiver.class);
                            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
                                    getActivity(), usunID, myIntent1,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            alarmManager.cancel(pendingIntent1);

                            Intent myIntent2 = new Intent(getActivity(), NotificationReceiver.class);
                            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                                    getActivity(), usunID + 1, myIntent2,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            alarmManager.cancel(pendingIntent2);

                            myDb.remove_WIZYTY(id);

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
        Cursor c;

        if (uzytkownik == "Wszyscy") c = myDb.getAllData_WIZYTY();
        else c = myDb.getUserData_WIZYTY(uzytkownik);

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                results.add(new Visit(c.getInt(0), c.getString(7), c.getString(3) + " " + c.getString(4), c.getString(5), c.getString(1) + " | " + c.getString(2), c.getString(6)));
            }
        }

        adapter = new VisitListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

}
