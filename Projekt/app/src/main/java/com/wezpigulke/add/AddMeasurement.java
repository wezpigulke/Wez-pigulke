package com.wezpigulke.add;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.other.DecimalDigitsInputFilter;
import com.wezpigulke.other.OpenDialog;
import com.wezpigulke.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddMeasurement extends AppCompatActivity {

    DatabaseHelper myDb;

    private Button add;
    private Button goThen;

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

    private int labelSize;
    private int labelSizeCopy;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_measurement);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        intializeAllElements();

        wynikPomiaru.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});
        wynikPomiaru.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

        Cursor res = myDb.getAllName_UZYTKOWNICY();
        res.moveToFirst();
        uzytkownik = res.getString(0);

        res.close();

        wynikPomiaru.setVisibility(View.GONE);
        add.setVisibility(View.GONE);

        loadSpinnerData();
        labelSizeCopy = labelSize;

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
                if (typ_badania.equals("Dodaj nowy typ")) {
                    Intent cel = new Intent(parentView.getContext(), AddTypeMeasurement.class);
                    startActivity(cel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        goThen.setOnClickListener(v -> {

            if (!typBadania.getSelectedItem().toString().equals("Wybierz typ badania")) {

                typProfilu.setVisibility(View.GONE);
                typBadania.setVisibility(View.GONE);
                dataBadania.setVisibility(View.GONE);
                godzinaBadania.setVisibility(View.GONE);
                goThen.setVisibility(View.GONE);

                wynikPomiaru.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);

                wynikPomiaru.requestFocus();
                showKeyboard();

            } else openDialog("Wybierz typ badania lub dodaj nowy");

        });

        add.setOnClickListener(v -> {

            if (wynikPomiaru.getText().length() <= 16 && wynikPomiaru.getText().length() > 0 && !typBadania.getSelectedItem().toString().equals("Wybierz typ badania") && !typBadania.getSelectedItem().toString().equals("Dodaj nowy typ")) {

                myDb.insert_POMIARY(
                        typBadania.getSelectedItem().toString(),
                        Double.valueOf(wynikPomiaru.getText().toString()),
                        uzytkownik,
                        godzinaBadania.getText().toString(),
                        dataBadania.getText().toString()
                );

                onBackPressed();
            } else if (wynikPomiaru.getText().length() <= 0) openDialog("Wpisz wynik pomiaru");
            else openDialog("Wybierz lub dodaj nowy typ badania");
        });

        godzinaBadania.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddMeasurement.this, godzinaBadaniaListener, hour, minutes, true);
            dialog.show();
        });

        godzinaBadaniaListener = (view, hourOfDay, minute) -> {
            String time1;
            if (minute < 10) time1 = hourOfDay + ":0" + minute;
            else time1 = hourOfDay + ":" + minute;
            godzinaBadania.setText(time1);
        };

        dataBadania.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(AddMeasurement.this, dataBadaniaListener, year, month - 1, day);
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

    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void loadSpinnerData() {

        Cursor cxz = myDb.getAllName_UZYTKOWNICY();
        label.clear();
        labelx.clear();

        if (cxz.getCount() != 0) {
            while (cxz.moveToNext()) {
                label.add(cxz.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typProfilu.setAdapter(dataAdapter);
            }
        }

        cxz.close();

        Cursor cxs = myDb.getPomiar_TYP_POMIAR();

        if (cxs.getCount() != 0) {
            while (cxs.moveToNext()) {
                labelx.add(cxs.getString(0));
            }
        }

        cxs.close();

        labelx.add("Dodaj nowy typ");
        labelx.add("Wybierz typ badania");

        labelSize = labelx.size() - 1;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelx) {
            @Override
            public int getCount() {
                return (labelSize);
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typBadania.setAdapter(dataAdapter);
        if (labelSize != labelSizeCopy) typBadania.setSelection(labelSize - 2);
        else typBadania.setSelection(labelSize);

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
        closeKeyboard();
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

    private void intializeAllElements() {

        add = findViewById(R.id.dodajMeasurement);
        goThen = findViewById(R.id.goThen3);
        typProfilu = findViewById(R.id.spinnerProfileMeasurement);
        typBadania = findViewById(R.id.spinnerTypeMeasurement);
        dataBadania = findViewById(R.id.dateMeasurement);
        godzinaBadania = findViewById(R.id.timeMeasurement);
        wynikPomiaru = findViewById(R.id.resultMeasurement);

        label = new ArrayList<>();
        labelx = new ArrayList<>();

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

        dataBadania.setText(date);
        godzinaBadania.setText(time);

    }

}

