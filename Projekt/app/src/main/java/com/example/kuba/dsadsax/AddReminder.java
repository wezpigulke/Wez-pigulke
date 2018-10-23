package com.example.kuba.dsadsax;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.Calendar.*;


public class AddReminder extends AppCompatActivity {

    private static final String TAG = "AddReminder";

    DatabaseHelper myDb;

    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;
    private Spinner dawka;
    private ArrayList<String> labelDawka;
    private String jakaDawka;
    private EditText nazwaLeku;
    private Button dalej;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        label = new ArrayList<String>();
        labelDawka = new ArrayList<String>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDb = new DatabaseHelper(this);

        nazwaLeku = (EditText) findViewById(R.id.editText);

        spinner = findViewById(R.id.spinnerProfile);
        dawka = findViewById(R.id.spinnerDawka);
        dalej = findViewById(R.id.goThen);

        jakaDawka = "Dawka: 1";

        Cursor res = myDb.getName_UZYTKOWNICY();

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            uzytkownik = res.getString(0);
        }

        loadSpinnerData();
        loadDawkaData();


        dawka.setSelection(3);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                uzytkownik = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        dawka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                jakaDawka = dawka.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        dalej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nazwaLeku.getText().length() != 0) {
                    Intent intent = new Intent(v.getContext(), AddReminder2.class);
                    intent.putExtra("uzytkownik", uzytkownik);
                    intent.putExtra("nazwaLeku", nazwaLeku.getText().toString());
                    intent.putExtra("jakaDawka", jakaDawka);
                    startActivity(intent);
                }
                else openDialog("Wpisz nazwe leku");
            }
        });

    }

    private void loadSpinnerData() {

        Cursor cxz = myDb.getAllName_UZYTKOWNICY();

        if (cxz.getCount() != 0) {
            while (cxz.moveToNext()) {
                label.add(cxz.getString(0));

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        }
    }


    private void loadDawkaData() {

        labelDawka.add("Dawka: 0.25");
        labelDawka.add("Dawka: 0.5");
        labelDawka.add("Dawka: 0.75");
        labelDawka.add("Dawka: 1");
        labelDawka.add("Dawka: 1.25");
        labelDawka.add("Dawka: 1.5");
        labelDawka.add("Dawka: 1.75");
        labelDawka.add("Dawka: 2");
        labelDawka.add("Dawka: 2.25");
        labelDawka.add("Dawka: 2.5");
        labelDawka.add("Dawka: 2.75");
        labelDawka.add("Dawka: 3");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelDawka);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dawka.setAdapter(dataAdapter);

    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddReminder");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}