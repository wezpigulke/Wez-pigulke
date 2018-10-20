package com.example.kuba.dsadsax;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    private Spinner spinner;
    private ArrayList<String> label;
    private ListView lv;
    private String uzytkownik;

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
        label = new ArrayList<String>();
        lv = (ListView) v.findViewById(R.id.medicineList);
        spinner = v.findViewById(R.id.spinnerr);
        uzytkownik = "Wszyscy";

        return v;

    }

    private void loadSpinnerData() {

        label.clear();
        Cursor cxz = myDb.getAllName_UZYTKOWNICY();

        label.add("Wszyscy");

        if (cxz.getCount() != 0) {
            while (cxz.moveToNext()) {
                label.add(cxz.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        }

    }


    public void onResume() {

        super.onResume();

        AktualizujBaze();
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
    }

    public void AktualizujBaze() {

        results.clear();
        lv.setAdapter(adapter);

        myDb = new DatabaseHelper(getActivity());
        Cursor c;

        if (uzytkownik == "Wszyscy") c = myDb.getMedicine_PRZYPOMNIENIE();
        else c = myDb.getUserMedicine_PRZYPOMNIENIE(uzytkownik);

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                results.add(new Medicine(c.getString(0)));
            }
        }


        adapter = new MedicineListAdapter(getActivity(), results);
        lv.setAdapter(adapter);

    }

}
