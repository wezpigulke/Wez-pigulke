package com.example.kuba.dsadsax;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMeasurement extends AppCompatActivity {

    DatabaseHelper myDb;
    private static final String TAG = "AddMeasurement";

    private Button add;

    private TextView dataBadania;
    private DatePickerDialog.OnDateSetListener dataBadaniaListener;
    private TextView godzinaBadania;
    private TimePickerDialog.OnTimeSetListener godzinaBadaniaListener;

    private Spinner typProfilu;
    private Spinner typBadania;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;

    private EditText wynikPomiaru;
    private ArrayList<String> label;
    private ArrayList<String> labelx;
    private String uzytkownik;
    private String typ_badania;

    private int ilosc1;
    private int ilosc2;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        label = new ArrayList<String>();

        labelx = new ArrayList<String>();

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_measurement);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        String rok = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String miesiac = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String dzien = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        String godzina = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        String minuta = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());

        year = Integer.parseInt(rok);
        month = Integer.parseInt(miesiac);
        day = Integer.parseInt(dzien);
        hour = Integer.parseInt(godzina);
        minutes = Integer.parseInt(minuta);

        add = findViewById(R.id.dodajMeasurement);
        typProfilu = findViewById(R.id.spinnerProfileMeasurement);
        typBadania = findViewById(R.id.spinnerTypeMeasurement);

        dataBadania = findViewById(R.id.dateMeasurement);
        godzinaBadania = findViewById(R.id.timeMeasurement);

        wynikPomiaru = findViewById(R.id.resultMeasurement);

        Cursor res = myDb.getName_UZYTKOWNICY();

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            uzytkownik = res.getString(0);
        }

        Cursor typo = myDb.getCount_TYP_POMIAR();
        typo.moveToNext();
        ilosc1 = Integer.valueOf(typo.getString(0));

        dataBadania.setText(date);
        godzinaBadania.setText(time);

        typProfilu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                uzytkownik = typProfilu.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        typBadania.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                typ_badania = typBadania.getItemAtPosition(position).toString();
                typBadania.setSelection(0);
                String selection = (String) parentView.getItemAtPosition(position);
                    if(selection.equals("Dodaj nowy typ"))
                    {
                        Intent cel = new Intent(parentView.getContext(), AddTypeMeasurement.class);
                        startActivity(cel);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        add.setOnClickListener(v -> {

            if (wynikPomiaru.getText().length() > 0 && !typBadania.getSelectedItem().toString().equals("Wybierz typ badania")) {

                myDb.insert_POMIARY(
                        typBadania.getSelectedItem().toString(),
                        wynikPomiaru.getText().toString(),
                        uzytkownik,
                        godzinaBadania.getText().toString(),
                        dataBadania.getText().toString()
                );

                onBackPressed();
            } else if (wynikPomiaru.getText().length() <= 0) openDialog("Wpisz wynik pomiaru");
            else if (typBadania.getSelectedItem().toString().equals("Wybierz typ badania")) openDialog("Wybierz typ badania lub dodaj nowy");
        });

        godzinaBadania.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddMeasurement.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, godzinaBadaniaListener, hour, minutes, true);
            dialog.show();
        });

        godzinaBadaniaListener = (view, hourOfDay, minute) -> {
            String time1;
            if (minute < 10) time1 = hourOfDay + ":0" + minute;
            else time1 = hourOfDay + ":" + minute;
            godzinaBadania.setText(time1);
        };

        dataBadania.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(AddMeasurement.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, dataBadaniaListener, year, month - 1, day);
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            dialog.show();
        });

        dataBadaniaListener = (view, yeear, moonth, dayOfMonth) -> {
            moonth = moonth + 1;
            String date1;
            if (moonth < 10) date1 = dayOfMonth + "/0" + moonth + "/" + yeear;
            else date1 = dayOfMonth + "/" + moonth + "/" + yeear;
            dataBadania.setText(date1);
        };

    }

    private void loadSpinnerData() {

        Cursor cxz = myDb.getAllName_UZYTKOWNICY();
        label.clear();
        labelx.clear();

        if(cxz.getCount() != 0) {
            while(cxz.moveToNext()) {
                label.add(cxz.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typProfilu.setAdapter(dataAdapter);
            }
        }

        Cursor cxs = myDb.getPomiar_TYP_POMIAR();

        if(cxs.getCount() != 0) {
            while(cxs.moveToNext()) {
                labelx.add(cxs.getString(0));
            }
        } else labelx.add("Wybierz typ badania");

        labelx.add("Dodaj nowy typ");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelx);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typBadania.setAdapter(dataAdapter);

        Cursor typo = myDb.getCount_TYP_POMIAR();
        typo.moveToNext();
        ilosc2 = Integer.valueOf(typo.getString(0));

        if(ilosc1!=ilosc2) {
            typBadania.setSelection(typBadania.getAdapter().getCount() - 2);
            ilosc1=ilosc2;
        }
    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddMeasurement");
    }

    public void onResume() {
        super.onResume();
        loadSpinnerData();
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

