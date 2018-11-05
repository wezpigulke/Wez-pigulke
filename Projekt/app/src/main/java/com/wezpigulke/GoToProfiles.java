package com.wezpigulke;

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
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoToProfiles extends Fragment {

    DatabaseHelper myDb;
    private ProfileListAdapter adapter;
    private List<Profile> results;
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
                results.add(new Profile(c.getInt(0), c.getString(1)));
            }
        }


        adapter = new ProfileListAdapter(getActivity(), results);
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

                                if (cn.getCount() != 0) {
                                    while (cn.moveToNext()) {

                                        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
                                        Intent myIntent = new Intent(getActivity(), NotificationReceiverReminder.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                                getActivity(), Integer.parseInt(cn.getString(0)), myIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                        assert alarmManager != null;
                                        alarmManager.cancel(pendingIntent);

                                    }
                                }
                            }
                        }

                        Cursor cw = myDb.getIDforUser_WIZYTY(nazwaUzytkownika);

                        if (cw.getCount() != 0) {
                            while (cw.moveToNext()) {

                                Integer id_w = Integer.parseInt(cw.getString(0));

                                int usunID = (id_w * 2) - 1;

                                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                                Intent myIntent1 = new Intent(getActivity(), NotificationReceiverVisit.class);
                                PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
                                        getActivity(), usunID, myIntent1,
                                        PendingIntent.FLAG_UPDATE_CURRENT);

                                assert alarmManager != null;
                                alarmManager.cancel(pendingIntent1);

                                Intent myIntent2 = new Intent(getActivity(), NotificationReceiverVisit.class);
                                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                                        getActivity(), usunID + 1, myIntent2,
                                        PendingIntent.FLAG_UPDATE_CURRENT);

                                alarmManager.cancel(pendingIntent2);

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