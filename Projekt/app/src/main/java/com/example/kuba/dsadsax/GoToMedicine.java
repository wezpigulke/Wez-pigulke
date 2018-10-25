package com.example.kuba.dsadsax;

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

public class GoToMedicine extends Fragment {

    DatabaseHelper myDb;
    private MedicineListAdapter adapter;
    private List<Medicine> results;
    private ArrayList<String> label;
    private ListView lv;
    private FloatingActionButton fab;
    private Integer id;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Lista leków");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.medicine, container, false);

        results = new ArrayList<>();
        label = new ArrayList<>();
        lv = v.findViewById(R.id.medicineList);
        fab = v.findViewById(R.id.fabMedicine);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AddMedicine.class);
            startActivity(intent);
        });

        return v;

    }

    public void onResume() {

        super.onResume();
        AktualizujBaze();

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

                            id = results.get(position).getId();
                            dialogRemove();

                        }

                    }

                });

        lv.setOnTouchListener(touchListener);

        lv.setOnItemClickListener((parent, view, position, id) -> {

            /*
            idd = results.get(position).getId();
            Cursor c = myDb.getNotes_NOTATKI(idd);
            c.moveToFirst();
            notatka = c.getString(0);
            dialogShowNotes();
            */

        });

    }

    public void AktualizujBaze() {

        results.clear();
        lv.setAdapter(adapter);

        myDb = new DatabaseHelper(getActivity());
        Cursor c;

        c = myDb.getData_LEK();

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                results.add(new Medicine(c.getInt(0), c.getString(1), c.getString(2)));
            }
        }

        adapter = new MedicineListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> usunDane())
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    private void usunDane() {

        myDb.remove_LEK(id);
        AktualizujBaze();

    }

}
