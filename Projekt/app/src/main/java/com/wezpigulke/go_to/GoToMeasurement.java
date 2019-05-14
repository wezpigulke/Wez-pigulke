package com.wezpigulke.go_to;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;
import com.wezpigulke.add.AddMeasurement;
import com.wezpigulke.classes.Measurement;
import com.wezpigulke.adapters.MeasurementListAdapter;
import com.wezpigulke.other.SwipeDismissListViewTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoToMeasurement extends Fragment {

    DatabaseHelper myDb;
    private List<Measurement> results;
    private MeasurementListAdapter adapter;
    private ListView lv;
    private Spinner measurementSpinner;
    private Spinner measurementTypeSpinner;
    private ArrayList<String> label;
    private ArrayList<String> labelx;
    private String uzytkownik;
    private String typ;
    private Integer idd;
    private Cursor cursor;
    private FloatingActionButton fabz;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Lista pomiarów");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.measurement, container, false);
        initializeVariables(v);
        fabzClickListener();
        return v;

    }

    private void initializeVariables(View v) {
        myDb = new DatabaseHelper(getActivity());
        label = new ArrayList<>();
        labelx = new ArrayList<>();
        fabz = v.findViewById(R.id.fabMeasurement);
        results = new ArrayList<>();
        lv = v.findViewById(R.id.measurementList);
        measurementSpinner = v.findViewById(R.id.measurementSpinner);
        measurementTypeSpinner = v.findViewById(R.id.measurementTypeSpinner);
        uzytkownik = "Wszyscy";
        typ = "Wszystko";
    }

    private void fabzClickListener() {
        fabz.setOnClickListener(v1 -> {
            Intent cel = new Intent(v1.getContext(), AddMeasurement.class);
            startActivity(cel);
        });
    }

    @Override
    public void onDestroy() {
        if(cursor!=null) cursor.close();
        super.onDestroy();
    }

    private void loadSpinnerData() {

        label.clear();
        labelx.clear();

        measurementSpinner.setVisibility(View.VISIBLE);
        measurementTypeSpinner.setVisibility(View.VISIBLE);


        label.add("Wszyscy");
        labelx.add("Wszystko");

        cursor = myDb.getAllName_UZYTKOWNICY();

        if (cursor.getCount() == 1) {
            measurementSpinner.setVisibility(View.GONE);
        } else {
            while (cursor.moveToNext()) {
                label.add(cursor.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                measurementSpinner.setAdapter(dataAdapter);
            }
        }

        cursor = myDb.getAllData_TYP_POMIAR();

        if (cursor.getCount() <= 1) {
            measurementTypeSpinner.setVisibility(View.GONE);
        } else {
            while (cursor.moveToNext()) {
                labelx.add(cursor.getString(1));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, labelx);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                measurementTypeSpinner.setAdapter(dataAdapter);
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    public void onResume() {

        super.onResume();
        aktualizujBaze();
        loadSpinnerData();
        measurementSpinnerListener();
        measurementTypeSpinnerListener();
        lvTouchListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void lvTouchListener() {
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

    private void measurementTypeSpinnerListener() {
        measurementTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                typ = measurementTypeSpinner.getItemAtPosition(position).toString();
                aktualizujBaze();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private void measurementSpinnerListener() {
        measurementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                uzytkownik = measurementSpinner.getItemAtPosition(position).toString();
                aktualizujBaze();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    public void aktualizujBaze() {

        results.clear();
        lv.setAdapter(adapter);

        myDb = new DatabaseHelper(getActivity());

        if (uzytkownik.equals("Wszyscy") && typ.equals("Wszystko")) cursor = myDb.getAllData_POMIARY();
        else if (!uzytkownik.equals("Wszyscy") && typ.equals("Wszystko")) cursor = myDb.getUserData_POMIARY(uzytkownik);
        else if (uzytkownik.equals("Wszyscy") && !typ.equals("Wszystko")) cursor = myDb.getUserType_POMIARY(typ);
        else cursor = myDb.getUserTypeData_POMIARY(uzytkownik, typ);

        if (cursor.getCount() != 0) {
            cursor.moveToLast();
            results.add(new Measurement(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(5), cursor.getString(4)));
            while (cursor.moveToPrevious()) {
                results.add(new Measurement(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(5), cursor.getString(4)));
            }
        }

        adapter = new MeasurementListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> {
                    myDb.remove_POMIARY(idd);
                    aktualizujBaze();
                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

}
