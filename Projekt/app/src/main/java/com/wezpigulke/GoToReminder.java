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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoToReminder extends Fragment {

    DatabaseHelper myDb;
    private List<Reminder> results;
    private ReminderListAdapter adapter;
    private ListView lv;
    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;
    private Integer idd;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Lista przypomnień");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.reminder, container, false);

        label = new ArrayList<>();
        results = new ArrayList<>();
        lv = v.findViewById(R.id.todayList);
        spinner = v.findViewById(R.id.todaySpinner);
        uzytkownik = "Wszyscy";

        FloatingActionButton fab = v.findViewById(R.id.fabReminder);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AddReminder.class);
            startActivity(intent);
        });

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

        if (uzytkownik.equals("Wszyscy")) c = myDb.getAllData_PRZYPOMNIENIE();
        else c = myDb.getUserData_PRZYPOMNIENIE(uzytkownik);

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                if (Integer.parseInt(c.getString(5)) <= 0) {
                    myDb.remove_PRZYPOMNIENIE(c.getInt(0));
                }
                results.add(new Reminder(c.getInt(0), c.getString(3) + " (" + c.getString(4) + ")", c.getString(8), "Pozostało dni: " + c.getString(5), c.getString(6)));
            }
        }

        adapter = new ReminderListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> usunDane())
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    private void usunDane() {

        myDb = new DatabaseHelper(getActivity());
        final Cursor c = myDb.getID_NOTYFIKACJA(idd);

        if (c.getCount() != 0) {
            while (c.moveToNext()) {

                AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(getActivity(), NotificationReceiverReminder.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getActivity(), Integer.parseInt(c.getString(0)), myIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                assert alarmManager != null;
                alarmManager.cancel(pendingIntent);

            }
        }

        myDb.remove_PRZYPOMNIENIE(idd);
        myDb.remove_NOTYFIKACJA(idd);

        AktualizujBaze();

    }

}
