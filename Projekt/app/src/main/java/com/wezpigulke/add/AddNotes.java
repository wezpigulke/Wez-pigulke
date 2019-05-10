package com.wezpigulke.add;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.other.OpenDialog;
import com.wezpigulke.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddNotes extends AppCompatActivity {

    DatabaseHelper myDb;

    private EditText nazwaNotatki;
    private EditText trescNotatki;

    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;
    private ImageView add;
    private Cursor cursor;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_notes);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initializeVariables();
        loadSpinnerData();
        addListener();
        spinnerListener();

    }

    private void spinnerListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                uzytkownik = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private void addListener() {
        add.setOnClickListener(v -> {

            if (nazwaNotatki.getText().length() > 0 && trescNotatki.getText().length() > 0) {

                String dzisiejszaData = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

                myDb.insert_NOTATKI(
                        nazwaNotatki.getText().toString(),
                        trescNotatki.getText().toString(),
                        uzytkownik,
                        dzisiejszaData
                );

                onBackPressed();

            } else if (nazwaNotatki.getText().length() == 0) openDialog("Wpisz nazwe notatki");
            else if (trescNotatki.getText().length() == 0) openDialog("Wpisz treść notatki");
        });
    }

    private void initializeVariables() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        label = new ArrayList<>();
        myDb = new DatabaseHelper(this);
        add = findViewById(R.id.saveNotes);
        nazwaNotatki = findViewById(R.id.notesName);
        trescNotatki = findViewById(R.id.editNotes);
        nazwaNotatki.requestFocus();
        spinner = findViewById(R.id.profileNotes);

        cursor = myDb.getAllName_UZYTKOWNICY();
        cursor.moveToFirst();
        uzytkownik = cursor.getString(0);
    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddNotes");
    }

    private void loadSpinnerData() {

        cursor = myDb.getAllName_UZYTKOWNICY();

        if (cursor.getCount() != 0) {
            if (cursor.getCount() == 1) {
                spinner.setVisibility(View.GONE);
            } else {
                while (cursor.moveToNext()) {
                    label.add(cursor.getString(0));
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, label);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        if(cursor!=null) cursor.close();
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
