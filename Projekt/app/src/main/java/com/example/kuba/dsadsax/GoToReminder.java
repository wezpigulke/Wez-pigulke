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
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GoToReminder extends Fragment {

    DatabaseHelper myDb;
    private List<Reminder> results;
    private ReminderListAdapter adapter;
    private ListView lv;
    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;
    private FloatingActionButton fab;
    private Integer id_prz;
    private Integer ilosc_dn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Lista przypomnień");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main_screen, container, false);

        label = new ArrayList<>();
        results = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.todayList);
        spinner = v.findViewById(R.id.todaySpinner);
        uzytkownik = "Wszyscy";

        fab = (FloatingActionButton) v.findViewById(R.id.fabProfile2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddReminder.class);
                startActivity(intent);
            }
        });

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
                            final Cursor c = myDb.getID_NOTYFIKACJA(idd);

                            if (c.getCount() != 0) {
                                while (c.moveToNext()) {

                                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                                    Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                            getActivity(), Integer.parseInt(c.getString(0)), myIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.cancel(pendingIntent);

                                    myDb.insert_USUNIETE_PRZ(Integer.parseInt(c.getString(0)));

                                }
                            }

                            myDb.remove_PRZYPOMNIENIE((int) idd);
                            myDb.remove_NOTYFIKACJA((int) idd);

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

        if (uzytkownik == "Wszyscy") c = myDb.getAllData_PRZYPOMNIENIE();
        else c = myDb.getUserData_PRZYPOMNIENIE(uzytkownik);

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                if(Integer.parseInt(c.getString(5))==0) {
                    myDb.remove_PRZYPOMNIENIE(c.getInt(0));
                }
                results.add(new Reminder(c.getInt(0), c.getString(3) + " (" + c.getString(4) + ")", c.getString(8), "Pozostało dni: " + c.getString(5), c.getString(6)));
            }
        }

        adapter = new ReminderListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }
}
