package com.wezpigulke.go_to;

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

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.notification.NotificationReceiver;
import com.wezpigulke.other.OpenDialog;
import com.wezpigulke.classes.Profiles;
import com.wezpigulke.adapters.ProfilesListAdapter;
import com.wezpigulke.R;
import com.wezpigulke.other.SwipeDismissListViewTouchListener;
import com.wezpigulke.add.AddProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoToProfiles extends Fragment {

    DatabaseHelper myDb;
    private ProfilesListAdapter adapter;
    private List<Profiles> results;
    private ListView lv;
    private Integer idd;
    private Cursor cursor;
    private Cursor cursorTemp;

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
        aktualizujBaze();
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
        cursor = myDb.getAllData_UZYTKOWNICY();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                results.add(new Profiles(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            }
        }

        adapter = new ProfilesListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć? Usunie to wszystkie powiązane rzeczy z tym profilem.").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> {

                    cursor = myDb.getAllData_UZYTKOWNICY();

                    if (cursor.getCount() > 1) {

                        cursor = myDb.getNameFromID_UZYTKOWNICY(idd);
                        cursor.moveToFirst();
                        String nazwaUzytkownika = cursor.getString(0);

                        cursor = myDb.getIDforUser_PRZYPOMNIENIE(nazwaUzytkownika);

                        if (cursor.getCount() != 0) {
                            while (cursor.moveToNext()) {

                                Integer id_p = cursor.getInt(0);
                                cursorTemp = myDb.getID_NOTYFIKACJA(id_p);

                                cursor = myDb.getRand_NOTYFIKACJA(cursorTemp.getInt(0));
                                cursor.moveToFirst();

                                if (cursorTemp.getCount() != 0) {
                                    while (cursorTemp.moveToNext()) {

                                        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
                                        Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                                getActivity(), cursor.getInt(0), myIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                        assert alarmManager != null;
                                        alarmManager.cancel(pendingIntent);

                                    }
                                }

                            }
                        }

                        cursorTemp = myDb.getIdForUser_WIZYTY(nazwaUzytkownika);

                        if (cursorTemp.getCount() != 0) {
                            while (cursorTemp.moveToNext()) {

                                cursor = myDb.getRand_WIZYTY(cursorTemp.getInt(0));
                                cursor.moveToFirst();

                                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                                Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                        getActivity(), cursor.getInt(0), myIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                assert alarmManager != null;
                                alarmManager.cancel(pendingIntent);

                                pendingIntent = PendingIntent.getBroadcast(
                                        getActivity(), cursor.getInt(0)-1, myIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.cancel(pendingIntent);

                            }
                        }

                        myDb.removeUser_PRZYPOMNIENIE(nazwaUzytkownika);
                        myDb.removeUser_WIZYTY(nazwaUzytkownika);
                        myDb.removeUser_POMIARY(nazwaUzytkownika);
                        myDb.removeUser_NOTATKI(nazwaUzytkownika);
                        myDb.remove_UZYTKOWNICY(idd);

                        aktualizujBaze();

                    } else openDialog("Musisz posiadać chociaż jeden profil w bazie.");

                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    @Override
    public void onDestroy() {
        if(cursor!=null) cursor.close();
        if(cursorTemp!=null) cursorTemp.close();
        super.onDestroy();
    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        assert getFragmentManager() != null;
        openDialog.show(getFragmentManager(), "GoToProfiles");
    }

}
