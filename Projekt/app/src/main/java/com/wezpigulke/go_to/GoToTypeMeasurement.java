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
import android.widget.ListView;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.classes.MeasurementType;
import com.wezpigulke.list_adapter.MeasurementTypeListAdapter;
import com.wezpigulke.R;
import com.wezpigulke.other.SwipeDismissListViewTouchListener;
import com.wezpigulke.add.AddTypeMeasurement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoToTypeMeasurement extends Fragment {

    DatabaseHelper myDb;
    private MeasurementTypeListAdapter adapter;
    private List<MeasurementType> results;
    private ListView listView;
    private Integer id;

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Typy pomiarów");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.type_measurement, container, false);
        FloatingActionButton fabz = v.findViewById(R.id.fabMesaurementType);

        fabz.setOnClickListener(v1 -> {
            Intent cel = new Intent(v1.getContext(), AddTypeMeasurement.class);
            startActivity(cel);
        });

        results = new ArrayList<>();
        listView = v.findViewById(R.id.measurementTypeList);
        return v;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onResume() {

        super.onResume();
        aktualizujBaze();

        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                listView,
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

        listView.setOnTouchListener(touchListener);

    }

    public void aktualizujBaze() {

        results.clear();
        listView.setAdapter(adapter);

        DatabaseHelper myDb = new DatabaseHelper(getActivity());
        Cursor c = myDb.getAllData_TYP_POMIAR();

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                results.add(new MeasurementType(c.getInt(0), c.getString(1)));
            }
        }

        adapter = new MeasurementTypeListAdapter(getActivity(), results);
        listView.setAdapter(adapter);

    }

    public void dialogRemove() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.AlertDialog);

        builder.setMessage("Czy na pewno chcesz usunąć? Usunie to wszystkie pomiary powiązane z tym typem.").setCancelable(false)
                .setPositiveButton("Tak", (dialog, which) -> {

                    Cursor cp = myDb.getDataType_TYP_POMIAR(id);
                    cp.moveToFirst();
                    String nazwaTypu = cp.getString(0);

                    myDb.removeType_POMIARY(nazwaTypu);
                    myDb.remove_TYP_POMIAR(id);

                    aktualizujBaze();

                })
                .setNegativeButton("Nie", (dialog, which) -> dialog.cancel());

        builder.show();

    }


}
