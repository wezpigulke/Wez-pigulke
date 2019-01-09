package com.wezpigulke;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoToMedicine extends Fragment {

    DatabaseHelper myDb;
    private MedicineListAdapter adapter;
    private List<Medicine> results;
    private ListView lv;
    private Integer id;
    private Integer id_l;

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
        lv = v.findViewById(R.id.medicineList);
        FloatingActionButton fab = v.findViewById(R.id.fabMedicine);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AddMedicine.class);
            startActivity(intent);
        });

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

                            id = results.get(position).getId();
                            dialogRemove();

                        }

                    }

                });

        lv.setOnTouchListener(touchListener);

        lv.setOnItemClickListener((parent, view, position, id) -> {

            id_l = results.get(position).getId();

            Cursor cl = myDb.getNumber_LEK(id_l);
            cl.moveToFirst();
            String ilosc = cl.getString(0);

            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);
            builder.setTitle("Aktualizacja ilości");

            final EditText input = new EditText(getContext());

            input.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

            input.setText(ilosc);
            input.setGravity(Gravity.CENTER_HORIZONTAL);

            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                myDb.update_LEK(id_l, input.getText().toString());
                AktualizujBaze();
            });
            builder.setNegativeButton("Anuluj", (dialog, which) -> {
                dialog.cancel();
            });
            builder.show();

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

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć?").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> usunDane())
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    private void usunDane() {

        Cursor cl = myDb.getDataNameFromId_LEK(id);
        cl.moveToFirst();
        String nazwa = cl.getString(0);

        Cursor cp = myDb.getAllDataMedicine_PRZYPOMNIENIE(nazwa);

        if (cp.getCount() == 0) {
            myDb.remove_LEK(id);
            AktualizujBaze();
        } else openDialog("Nie można usunąć. Posiadasz aktywne przypomnienie z tym lekiem");

    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        assert getFragmentManager() != null;
        openDialog.show(getFragmentManager(), "GoToMedicine");
    }

}
