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


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        label = new ArrayList<>();
        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_notes);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ImageView add = findViewById(R.id.saveNotes);
        nazwaNotatki = findViewById(R.id.notesName);
        trescNotatki = findViewById(R.id.editNotes);

        nazwaNotatki.requestFocus();

        spinner = findViewById(R.id.profileNotes);

        Cursor res = myDb.getAllName_UZYTKOWNICY();
        res.moveToFirst();
        uzytkownik = res.getString(0);

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

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddNotes");
    }

    private void loadSpinnerData() {

        try (Cursor cxz = myDb.getAllName_UZYTKOWNICY()) {

            if (cxz.getCount() == 1) {
                spinner.setVisibility(View.GONE);
            } else {
                while (cxz.moveToNext()) {
                    label.add(cxz.getString(0));
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, label);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                }
            }
        }
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
