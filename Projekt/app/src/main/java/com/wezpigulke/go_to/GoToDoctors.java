package com.wezpigulke.go_to;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;
import android.widget.ListView;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.classes.Doctor;
import com.wezpigulke.adapters.DoctorListAdapter;
import com.wezpigulke.R;
import com.wezpigulke.other.SwipeDismissListViewTouchListener;
import com.wezpigulke.add.AddDoctor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GoToDoctors extends Fragment {

    DatabaseHelper myDb;
    private List<Doctor> results;
    private ListView lv;
    private Integer idd;
    private String nrtel;
    private String adres;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Lista lekarzy");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.doctors, container, false);

        FloatingActionButton fab = v.findViewById(R.id.fabb);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AddDoctor.class);
            startActivity(intent);
        });

        results = new ArrayList<>();
        lv = v.findViewById(R.id.doctorList);

        return v;

    }

    @SuppressLint("ClickableViewAccessibility")
    public void onResume() {

        super.onResume();
        aktualizujBaze();

        lv.setOnItemClickListener((parent, view, i, l) -> {

            int idd = (int) view.getTag();
            Cursor c = myDb.getIdData_DOKTORZY(idd);
            c.moveToFirst();
            nrtel = c.getString(3);
            adres = c.getString(4);
            if(nrtel.equals("0")) dialogCallOrNavigate(false);
            else dialogCallOrNavigate(true);
            c.close();

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

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public void aktualizujBaze() {

        results.clear();

        myDb = new DatabaseHelper(getActivity());
        Cursor c = myDb.getAllData_DOKTORZY();

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                if(c.getString(3).equals("0")) {
                    results.add(new Doctor(c.getInt(0), c.getString(1), c.getString(2), c.getString(4)));
                } else results.add(new Doctor(c.getInt(0), c.getString(1), c.getString(2), c.getString(4)));
            }
        }
        c.close();

        DoctorListAdapter adapter = new DoctorListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> {
                    myDb.remove_DOKTORZY(idd);
                    aktualizujBaze();
                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    public void dialogCallOrNavigate(boolean showIt) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Co chcesz zrobić?").setCancelable(true);
        if(showIt) builder.setNegativeButton("Zadzwoń", (dialog, which) -> dialContactPhone(nrtel));
        builder.setPositiveButton("Nawiguj", (dialog, which) -> openMap());

        builder.show();

    }

    private void openMap() {

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + adres);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

}
