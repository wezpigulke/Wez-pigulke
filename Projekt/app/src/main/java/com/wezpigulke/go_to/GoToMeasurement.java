package com.wezpigulke.go_to;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.classes.Measurement;
import com.wezpigulke.list_adapter.MeasurementListAdapter;
import com.wezpigulke.R;
import com.wezpigulke.other.SwipeDismissListViewTouchListener;
import com.wezpigulke.add.AddMeasurement;

import java.lang.invoke.ConstantCallSite;
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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Lista pomiarów");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myDb = new DatabaseHelper(getActivity());

        View v = inflater.inflate(R.layout.measurement, container, false);

        label = new ArrayList<>();
        labelx = new ArrayList<>();

        FloatingActionButton fabz = v.findViewById(R.id.fabMeasurement);

        fabz.setOnClickListener(v1 -> {
            Intent cel = new Intent(v1.getContext(), AddMeasurement.class);
            startActivity(cel);
        });

        results = new ArrayList<>();
        lv = v.findViewById(R.id.measurementList);
        measurementSpinner = v.findViewById(R.id.measurementSpinner);
        measurementTypeSpinner = v.findViewById(R.id.measurementTypeSpinner);
        uzytkownik = "Wszyscy";
        typ = "Wszystko";

        return v;

    }

    private void loadSpinnerData() {

        label.clear();
        labelx.clear();

        measurementSpinner.setVisibility(View.VISIBLE);
        measurementTypeSpinner.setVisibility(View.VISIBLE);

        Cursor cxz = myDb.getAllName_UZYTKOWNICY();
        Cursor cxs = myDb.getAllData_TYP_POMIAR();

        label.add("Wszyscy");
        labelx.add("Wszystko");

        if (cxz.getCount() == 1) {
            measurementSpinner.setVisibility(View.GONE);
        } else {
            while (cxz.moveToNext()) {
                label.add(cxz.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                measurementSpinner.setAdapter(dataAdapter);
            }
        }

        if (cxs.getCount() <= 1) {
            measurementTypeSpinner.setVisibility(View.GONE);
        } else {
            while (cxs.moveToNext()) {
                labelx.add(cxs.getString(1));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, labelx);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                measurementTypeSpinner.setAdapter(dataAdapter);
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    public void onResume() {

        super.onResume();
        AktualizujBaze();

        myDb = new DatabaseHelper(getActivity());

        loadSpinnerData();


        measurementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                uzytkownik = measurementSpinner.getItemAtPosition(position).toString();
                AktualizujBaze();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        measurementTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                typ = measurementTypeSpinner.getItemAtPosition(position).toString();
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
        Cursor c;

        if (uzytkownik.equals("Wszyscy") && typ.equals("Wszystko")) c = myDb.getAllData_POMIARY();
        else if (!uzytkownik.equals("Wszyscy") && typ.equals("Wszystko")) c = myDb.getUserData_POMIARY(uzytkownik);
        else if (uzytkownik.equals("Wszyscy") && !typ.equals("Wszystko")) c = myDb.getUserType_POMIARY(typ);
        else c = myDb.getUserTypeData_POMIARY(uzytkownik, typ);

        if (c.getCount() != 0) {
            c.moveToLast();
            results.add(new Measurement(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(5), c.getString(4)));
            while (c.moveToPrevious()) {
                results.add(new Measurement(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(5), c.getString(4)));
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
                    AktualizujBaze();
                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

}
