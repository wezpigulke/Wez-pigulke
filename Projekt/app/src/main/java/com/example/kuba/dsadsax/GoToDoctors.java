package com.example.kuba.dsadsax;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class GoToDoctors extends Fragment {

    DatabaseHelper myDb;
    private List<Doctor> results;
    private DoctorListAdapter adapter;
    private ListView lv;
    private Integer idd;
    private String nrtel;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Lista lekarzy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.doctors, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabb);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddDoctor.class);
                startActivity(intent);
            }
        });

        results = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.doctorList);

        return v;

    }

    public void onResume() {

        super.onResume();
        AktualizujBaze();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                int idd = (int) view.getTag();
                Cursor c = myDb.getNumer_DOKTORZY(idd);
                c.moveToFirst();
                nrtel = c.getString(0);
                dialogCallOrSms();

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

                            idd = (int) results.get(position).getId();
                            dialogRemove();

                        }
                    }
                });
        lv.setOnTouchListener(touchListener);

    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public void AktualizujBaze() {

        results.clear();
        lv.setAdapter(adapter);

        myDb = new DatabaseHelper(getActivity());
        Cursor c = myDb.getAllData_DOKTORZY();

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                results.add(new Doctor(c.getInt(0), c.getString(1) + " " + c.getString(2), c.getString(3), c.getString(4)));
            }
        }

        adapter = new DoctorListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.remove_DOKTORZY((int) idd);
                        AktualizujBaze();
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.show();

    }

    public void dialogCallOrSms() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);

        builder.setMessage("Co chcesz zrobić?").setCancelable(false)
                .setPositiveButton("Zadzwonić", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialContactPhone(nrtel);
                    }
                })
                .setNegativeButton("Nic", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.show();

    }

}
