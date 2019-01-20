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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.wezpigulke.Database;
import com.wezpigulke.notification.NotificationReceiver;
import com.wezpigulke.R;
import com.wezpigulke.other.SwipeDismissListViewTouchListener;
import com.wezpigulke.classes.Visit;
import com.wezpigulke.list_adapter.VisitListAdapter;
import com.wezpigulke.add.AddVisit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoToVisit extends Fragment {

    Database myDb;
    private List<Visit> results;
    private VisitListAdapter adapter;
    private ListView lv;
    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;
    private Integer idd;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(getActivity()).setTitle("Lista wizyt");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.visit, container, false);

        label = new ArrayList<>();

        FloatingActionButton fabz = v.findViewById(R.id.fabz);

        fabz.setOnClickListener(v1 -> {
            Intent cel = new Intent(v1.getContext(), AddVisit.class);
            startActivity(cel);
        });

        results = new ArrayList<>();
        lv = v.findViewById(R.id.visitList);
        spinner = v.findViewById(R.id.spinner4);
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
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    public void onResume() {

        super.onResume();

        aktualizujBaze();

        loadSpinnerData();

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

        myDb = new Database(getActivity());
        Cursor c;

        if (uzytkownik.equals("Wszyscy")) c = myDb.getAllData_WIZYTY();
        else c = myDb.getUserData_WIZYTY(uzytkownik);

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                results.add(new Visit(c.getInt(0), c.getString(5), c.getString(3), c.getString(4), c.getString(1) + " | " + c.getString(2)));
            }
        }

        adapter = new VisitListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    private void usunDane() {

        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);

        Cursor crand = myDb.getRand_WIZYTY(idd);
        crand.moveToFirst();

        Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getActivity(), crand.getInt(0), myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getContext(), "Anulacja:" + String.valueOf(crand.getInt(0)), Toast.LENGTH_LONG).show();

        myIntent = new Intent(getActivity(), NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                getActivity(), crand.getInt(0)+1, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getContext(), "Anulacja:" + String.valueOf(crand.getInt(0)+1), Toast.LENGTH_LONG).show();

        myDb.remove_WIZYTY(idd);

        aktualizujBaze();

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> usunDane())
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

}
