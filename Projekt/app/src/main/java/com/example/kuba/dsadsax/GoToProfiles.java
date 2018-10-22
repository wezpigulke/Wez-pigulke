package com.example.kuba.dsadsax;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GoToProfiles extends Fragment {

    DatabaseHelper myDb;
    private ProfileListAdapter adapter;
    private List<Profile> results;
    private ListView lv;
    private FloatingActionButton fabz;
    private Integer idd;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Lista profili");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.profiles, container, false);

        fabz = (FloatingActionButton) v.findViewById(R.id.fabProfile);

        fabz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cel = new Intent(v.getContext(), AddProfile.class);
                startActivity(cel);
            }
        });

        results = new ArrayList<>();
        lv = (ListView) v.findViewById(R.id.profileList);

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

                            if(position!=0) {

                                idd = (int) results.get(position).getId();
                                dialogRemove();

                            }

                        }

                    }

                });

        lv.setOnTouchListener(touchListener);

    }

    public void AktualizujBaze() {

        results.clear();
        lv.setAdapter(adapter);

        myDb = new DatabaseHelper(getActivity());
        Cursor c = myDb.getAllData_UZYTKOWNICY();

        if(c.getCount() != 0) {
            while(c.moveToNext()) {
                results.add(new Profile(c.getInt(0), c.getString(1)));
            }
        }


        adapter = new ProfileListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.remove_UZYTKOWNICY(idd);
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


}
