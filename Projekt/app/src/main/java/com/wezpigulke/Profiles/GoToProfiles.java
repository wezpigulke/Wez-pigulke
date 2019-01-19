package com.wezpigulke.Profiles;

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
import android.widget.ListView;
import android.widget.Toast;

import com.wezpigulke.Database.DatabaseHelper;
import com.wezpigulke.NotificationSystem.NotificationReceiver;
import com.wezpigulke.Other.OpenDialog;
import com.wezpigulke.R;
import com.wezpigulke.Other.SwipeDismissListViewTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoToProfiles extends Fragment {

    DatabaseHelper myDb;
    private ProfilesListAdapter adapter;
    private List<Profiles> results;
    private ListView lv;
    private Integer idd;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(getActivity()).setTitle("Lista profili");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.profiles, container, false);

        FloatingActionButton fabz = v.findViewById(R.id.fabProfile);

        fabz.setOnClickListener(v1 -> {
            Intent cel = new Intent(v1.getContext(), AddProfile.class);
            startActivity(cel);
        });

        results = new ArrayList<>();
        lv = v.findViewById(R.id.profileList);

        return v;

    }

    @SuppressLint("ClickableViewAccessibility")
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
        Cursor c = myDb.getAllData_UZYTKOWNICY();

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                results.add(new Profiles(c.getInt(0), c.getString(1), c.getInt(2)));
            }
        }


        adapter = new ProfilesListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć? Usunie to wszystkie powiązane rzeczy z tym profilem.").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> {

                    Cursor cc = myDb.getAllData_UZYTKOWNICY();

                    if (cc.getCount() > 1) {

                        Cursor cu = myDb.getNameFromID_UZYTKOWNICY(idd);
                        cu.moveToFirst();
                        String nazwaUzytkownika = cu.getString(0);

                        Cursor cp = myDb.getIDforUser_PRZYPOMNIENIE(nazwaUzytkownika);

                        if (cp.getCount() != 0) {
                            while (cp.moveToNext()) {

                                Integer id_p = Integer.valueOf(cp.getString(0));
                                final Cursor cn = myDb.getID_NOTYFIKACJA(id_p);

                                Cursor crand = myDb.getRand_NOTYFIKACJA(cn.getInt(0));
                                crand.moveToFirst();

                                if (cn.getCount() != 0) {
                                    while (cn.moveToNext()) {

                                        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
                                        Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                                getActivity(), crand.getInt(0), myIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                        assert alarmManager != null;
                                        alarmManager.cancel(pendingIntent);

                                        Toast.makeText(getContext(), "Anulacja:" + String.valueOf(crand.getInt(0)), Toast.LENGTH_LONG).show();


                                    }
                                }
                            }
                        }

                        Cursor cw = myDb.getIdForUser_WIZYTY(nazwaUzytkownika);

                        if (cw.getCount() != 0) {
                            while (cw.moveToNext()) {

                                Cursor crand = myDb.getRand_NOTYFIKACJA(cw.getInt(0));
                                crand.moveToFirst();

                                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                                Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                        getActivity(), crand.getInt(0), myIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                assert alarmManager != null;
                                alarmManager.cancel(pendingIntent);

                                Toast.makeText(getContext(), "Anulacja:" + String.valueOf(crand.getInt(0)), Toast.LENGTH_LONG).show();


                            }
                        }

                        myDb.removeUser_PRZYPOMNIENIE(nazwaUzytkownika);
                        myDb.removeUser_WIZYTY(nazwaUzytkownika);
                        myDb.removeUser_POMIARY(nazwaUzytkownika);
                        myDb.removeUser_NOTATKI(nazwaUzytkownika);
                        myDb.remove_UZYTKOWNICY(idd);

                        AktualizujBaze();

                    } else openDialog("Musisz posiadać chociaż jeden profil w bazie.");

                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        assert getFragmentManager() != null;
        openDialog.show(getFragmentManager(), "GoToProfiles");
    }

}
