package com.wezpigulke.go_to;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.classes.Notes;
import com.wezpigulke.list_adapter.NotesListAdapter;
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

        lv.setOnItemClickListener((parent, view, position, id) -> {

            idd = results.get(position).getId();
            Cursor c = myDb.getNotes_NOTATKI(idd);
            c.moveToFirst();
            notatka = c.getString(0);
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

    public void AktualizujBaze() {

        results.clear();
        lv.setAdapter(adapter);

        myDb = new DatabaseHelper(getActivity());
        Cursor c;

        if (uzytkownik.equals("Wszyscy")) c = myDb.getAllData_NOTATKI();
        else c = myDb.getUserData_NOTATKI(uzytkownik);

        if (c.getCount() != 0) {
            c.moveToLast();
            results.add(new Notes(c.getInt(0), c.getString(1), c.getString(3), c.getString(4)));
            while (c.moveToPrevious()) {
                results.add(new Notes(c.getInt(0), c.getString(1), c.getString(3), c.getString(4)));
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
                    AktualizujBaze();
                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    public void dialogShowNotes() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage(notatka).setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel());

        builder.show();

    }

}
