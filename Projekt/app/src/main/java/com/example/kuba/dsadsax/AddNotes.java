package com.example.kuba.dsadsax;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.*;

public class AddNotes extends AppCompatActivity {

    DatabaseHelper myDb;
    private static final String TAG = "AddNotes";

    private ImageView add;

    private EditText nazwaNotatki;
    private EditText trescNotatki;

    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        label = new ArrayList<String>();
        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_notes);

        add = findViewById(R.id.saveNotes);
        nazwaNotatki = findViewById(R.id.notesName);
        trescNotatki = findViewById(R.id.editNotes);

        spinner = findViewById(R.id.profileNotes);

        Cursor res = myDb.getName_UZYTKOWNICY();

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            uzytkownik = res.getString(0);
        }

        loadSpinnerData();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                uzytkownik = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nazwaNotatki.getText().length() > 0 && trescNotatki.getText().length() > 0) {

                    String dzisiejszaData = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

                    //int id, String title, String profile, String date) {

                    myDb.insert_NOTATKI(
                            nazwaNotatki.getText().toString(),
                            trescNotatki.getText().toString(),
                            uzytkownik,
                            dzisiejszaData
                    );

                    onBackPressed();
                }
                else if(nazwaNotatki.getText().length()==0) openDialog("Wpisz nazwe notatki");
                else if(trescNotatki.getText().length()==0) openDialog("Wpisz treść notatki");
            }
        });
    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddNotes");
    }

    private void loadSpinnerData() {

        Cursor cxz = myDb.getAllName_UZYTKOWNICY();

        if(cxz.getCount() != 0) {
            while(cxz.moveToNext()) {
                label.add(cxz.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
