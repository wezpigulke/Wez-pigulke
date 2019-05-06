package com.wezpigulke.go_to;

import android.annotation.SuppressLint;
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

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.classes.Notes;
import com.wezpigulke.adapters.NotesListAdapter;
import com.wezpigulke.R;
import com.wezpigulke.other.SwipeDismissListViewTouchListener;
import com.wezpigulke.add.AddNotes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoToNotes extends Fragment {

    DatabaseHelper myDb;
    private List<Notes> results;
    private NotesListAdapter adapter;
    private ListView lv;
    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;
    private Integer idd;
    private String notatka;
    private Cursor cursor;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(getActivity()).setTitle("Własne notatki");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.notes, container, false);

        label = new ArrayList<>();

        FloatingActionButton fabz = v.findViewById(R.id.fabNotes);

        fabz.setOnClickListener(v1 -> {
            Intent cel = new Intent(v1.getContext(), AddNotes.class);
            startActivity(cel);
        });

        results = new ArrayList<>();
        lv = v.findViewById(R.id.notesList);
        spinner = v.findViewById(R.id.notesSpinner);
        uzytkownik = "Wszyscy";

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

    @SuppressLint("ClickableViewAccessibility")
    public void onResume() {

        super.onResume();
        aktualizujBaze();

        myDb = new DatabaseHelper(getActivity());

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

        lv.setOnItemClickListener((parent, view, position, id) -> {

            idd = results.get(position).getId();
            cursor = myDb.getNotes_NOTATKI(idd);
            cursor.moveToFirst();
            notatka = cursor.getString(0);
            dialogShowNotes();

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

        myDb = new DatabaseHelper(getActivity());

        if (uzytkownik.equals("Wszyscy")) cursor = myDb.getAllData_NOTATKI();
        else cursor = myDb.getUserData_NOTATKI(uzytkownik);

        String[] parts;
        String part1, part2;

        if (cursor.getCount() != 0) {
            cursor.moveToLast();

            parts = cursor.getString(4).split(" ");
            part1 = parts[0].substring(0,parts[0].length()-5);
            part2 = parts[1];

            results.add(new Notes(cursor.getInt(0), cursor.getString(1), cursor.getString(3), part1 + " | " + part2));
            while (cursor.moveToPrevious()) {

                parts = cursor.getString(4).split(" ");
                part1 = parts[0].substring(0,parts[0].length()-5);
                part2 = parts[1];

                results.add(new Notes(cursor.getInt(0), cursor.getString(1), cursor.getString(3), part1 + " | " + part2));
            }
        }

        adapter = new NotesListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> {
                    myDb.remove_NOTATKI(idd);
                    aktualizujBaze();
                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    @Override
    public void onDestroy() {
        cursor.close();
        super.onDestroy();
    }

    public void dialogShowNotes() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage(notatka).setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel());

        builder.show();

    }

}
