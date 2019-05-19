package com.wezpigulke.go_to;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;
import com.wezpigulke.adapters.ReminderListAdapter;
import com.wezpigulke.add.AddReminder;
import com.wezpigulke.classes.Reminder;
import com.wezpigulke.notification.NotificationReceiver;
import com.wezpigulke.other.SwipeDismissListViewTouchListener;

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
    private Cursor cursor;
    private Cursor cursorTemp;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(getActivity()).setTitle("Lista przypomnień");
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

        spinnerSelectedListener();
        listViewTouchListener();

        return v;
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

    private void spinnerSelectedListener() {

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
    public void onResume() {

        super.onResume();
        aktualizujBaze();
        loadSpinnerData();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void listViewTouchListener() {

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

    public void aktualizujBaze() {

        results.clear();

        lv.setAdapter(adapter);

        myDb = new DatabaseHelper(getActivity());

        if (uzytkownik.equals("Wszyscy")) cursor = myDb.getAllData_PRZYPOMNIENIE();
        else cursor = myDb.getUserData_PRZYPOMNIENIE(uzytkownik);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                if (Integer.parseInt(cursor.getString(5)) >= 0) {

                    cursorTemp = myDb.getCountType_NOTYFIKACJA(cursor.getInt(0));
                    cursorTemp.moveToFirst();

                    if (cursorTemp.getInt(0) != 0)
                        results.add(new Reminder(cursor.getInt(0), cursor.getString(3) + " (Dawka: " + cursor.getString(4) + ")", cursor.getString(8), "Pozostało dni: " + cursor.getString(5), cursor.getString(6)));
                    else myDb.remove_PRZYPOMNIENIE(cursor.getInt(0));

                } else myDb.remove_PRZYPOMNIENIE(cursor.getInt(0));
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

    @Override
    public void onDestroy() {
        if (cursor != null) cursor.close();
        if (cursorTemp != null) cursorTemp.close();
        super.onDestroy();
    }

    private void usunDane() {

        myDb = new DatabaseHelper(getActivity());
        cursorTemp = myDb.getID_NOTYFIKACJA(idd);

        if (cursorTemp.getCount() != 0) {
            while (cursorTemp.moveToNext()) {

                cursor = myDb.getRand_NOTYFIKACJA(cursorTemp.getInt(0));
                cursor.moveToFirst();

                AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getActivity(), cursor.getInt(0), myIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                assert alarmManager != null;
                alarmManager.cancel(pendingIntent);

                Log.d("========ALARM==========", "Anulacja:" + cursor.getInt(0));

                myDb.remove_NOTYFIKACJA(cursorTemp.getInt(0));

                Log.d("GoToReminder", "Usunięcie notyfikacji o id:" + cursor.getInt(0));

            }
        }

        myDb.remove_PRZYPOMNIENIE(idd);
        Log.d("GoToReminder", "Usunięcie przypomnienia o id: " + idd);

        aktualizujBaze();

    }

}
