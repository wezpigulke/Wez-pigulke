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

public class GoToNotes extends Fragment {

    DatabaseHelper myDb;
    private List<Notes> results;
    private NotesListAdapter adapter;
    private ListView lv;
    private FloatingActionButton fabz;
    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Własne notatki");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.notes, container, false);

        label = new ArrayList<String>();

        fabz = (FloatingActionButton) v.findViewById(R.id.fabNotes);

        fabz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cel = new Intent(v.getContext(), AddNotes.class);
                startActivity(cel);
            }
        });

        results = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.notesList);
        spinner = v.findViewById(R.id.notesSpinner);
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
                            myDb.remove_NOTATKI(id);
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

        if (uzytkownik == "Wszyscy") c = myDb.getAllData_NOTATKI();
        else c = myDb.getUserData_NOTATKI(uzytkownik);

        if (c.getCount() != 0) {
            c.moveToLast();
            results.add(new Notes(c.getInt(0), c.getString(1), c.getString(3), c.getString(4), c.getString(2)));
            while (c.moveToPrevious()) {
                results.add(new Notes(c.getInt(0), c.getString(1), c.getString(3), c.getString(4), c.getString(2)));
            }
        }

        adapter = new NotesListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

}
