package com.wezpigulke.add;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;
import com.wezpigulke.notification.NotificationReceiver;
import com.wezpigulke.other.DecimalDigitsInputFilter;
import com.wezpigulke.other.OpenDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Calendar.getInstance;

public class AddReminder extends AppCompatActivity {

    DatabaseHelper myDb;

    private TextView dataTabletka;
    private TextView godzinaTabletka;
    private String nazwaLeku;
    private EditText ileDni;
    private EditText coIleDniEditText;
    private Button dodaj;
    private DatePickerDialog.OnDateSetListener dataTabletkaListener;
    private TimePickerDialog.OnTimeSetListener godzinaTabletkaListener;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private Spinner spinner;
    private Spinner spinnerReminder;
    private Spinner spinnerDzwiek;
    private ArrayList<String> label;
    private String uzytkownik;
    private Spinner dawka;
    private Spinner spinnerNazwaLeku;
    private CheckBox checkBox;

    private ArrayList<String> labelDawka;
    private ArrayList<String> labelReminder;
    private ArrayList<String> labelIleRazyDziennie;
    private ArrayList<String> labelDzwiek;
    private ArrayList<String> labelNazwaLeku;
    private ArrayList<TextView> array;

    private Double jakaDawka;
    private Integer iloscDni;
    private Spinner spinnerIleRazyDziennie;
    private Integer coWybrane;
    private Integer ileRazyDziennie;
    private Integer coIleDni;
    private Button dalej;
    private Integer dzwiek;
    private MediaPlayer mp;
    private Integer labelSize;
    private Integer labelSizeCopy;
    private String wlasnaDawka;
    private Boolean czyWibracja;
    private Long diffDays;
    private Long diffInMillis;
    private Calendar cal;
    private SimpleDateFormat sdf;

    private TextView setTime1;
    private TextView setTime2;
    private TextView setTime3;
    private TextView setTime4;
    private TextView setTime5;
    private TextView setTime6;
    private TextView setTime7;
    private TextView setTime8;
    private TextView setTime9;
    private TextView setTime10;
    private TextView setTime11;
    private TextView setTime12;

    private TimePickerDialog.OnTimeSetListener setTime1Listener;
    private TimePickerDialog.OnTimeSetListener setTime2Listener;
    private TimePickerDialog.OnTimeSetListener setTime3Listener;
    private TimePickerDialog.OnTimeSetListener setTime4Listener;
    private TimePickerDialog.OnTimeSetListener setTime5Listener;
    private TimePickerDialog.OnTimeSetListener setTime6Listener;
    private TimePickerDialog.OnTimeSetListener setTime7Listener;
    private TimePickerDialog.OnTimeSetListener setTime8Listener;
    private TimePickerDialog.OnTimeSetListener setTime9Listener;
    private TimePickerDialog.OnTimeSetListener setTime10Listener;
    private TimePickerDialog.OnTimeSetListener setTime11Listener;
    private TimePickerDialog.OnTimeSetListener setTime12Listener;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        myDb = new DatabaseHelper(this);
        intializeAllVariables();
        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> czyWibracja = isChecked);

        setSpinnerNazwaLekuOnClickListener();
        setSpinnerIleRazyDziennieOnClickListener();
        setSpinnerOnClickListener();
        setSpinnerDzwiekOnClickListener();
        setDawkaOnClickListener();
        setDalejOnClickListener();
        setSpinnerReminderOnClickListener();
        setDodajOnClickListener();
        setAllTimeOnClickListener();

    }

    private void loadSpinnerData() {

        Cursor cxz = myDb.getAllName_UZYTKOWNICY();

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

    private void loadSpinnerNazwaLeku() {

        labelNazwaLeku.clear();

        Cursor cxz = myDb.getData_LEK();

        if (cxz.getCount() != 0) {
            while (cxz.moveToNext()) {
                labelNazwaLeku.add(cxz.getString(1));
            }
        }

        labelNazwaLeku.add("Dodaj nowy typ");
        labelNazwaLeku.add("Wybierz lek");

        labelSize = labelNazwaLeku.size() - 1;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelNazwaLeku) {
            @Override
            public int getCount() {
                return (labelSize);
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNazwaLeku.setAdapter(dataAdapter);

        if (!labelSize.equals(labelSizeCopy)) spinnerNazwaLeku.setSelection(labelSize - 2);
        else spinnerNazwaLeku.setSelection(labelSize);

    }

    private void loadSpinnerIleRazyDziennie() {

        for (int i = 2; i <= 12; i++) {
            labelIleRazyDziennie.add(String.valueOf(i) + " razy dziennie");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labelIleRazyDziennie);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIleRazyDziennie.setAdapter(dataAdapter);

    }

    private void loadSpinnerReminder() {

        labelReminder.add("Jednorazowo");
        labelReminder.add("Codziennie");
        labelReminder.add("Kilka razy dziennie");
        labelReminder.add("Co kilka dni");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labelReminder);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReminder.setAdapter(dataAdapter);

    }

    private void loadSpinnerDzwiek() {

        labelDzwiek.add("Brak");

        for (int i = 1; i <= 9; i++) {
            labelDzwiek.add("Alarm nr " + String.valueOf(i));
        }

        labelDzwiek.add("Dźwięk domyślny");

        final int labelSize = labelDzwiek.size() - 1;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelDzwiek) {
            @Override
            public int getCount() {
                return (labelSize);
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDzwiek.setAdapter(dataAdapter);
        spinnerDzwiek.setSelection(labelSize);

    }

    private void loadSpinnerDawka() {

        labelDawka.clear();

        for (double i = 0.5; i <= 3; i += 0.5) {
            labelDawka.add("Dawka: " + String.valueOf(i));
        }

        if(!wlasnaDawka.equals("")) {
            labelDawka.add(wlasnaDawka);
        }

        labelDawka.add("Własna dawka");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labelDawka);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dawka.setAdapter(dataAdapter);

    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddReminder");
    }

    public void goBack() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onBackPressed() {

        stopPlaying();

        if(dodaj.getVisibility()==View.GONE) {
            super.onBackPressed();
            finish();
        } else {

            dataTabletka.setVisibility(View.GONE);
            godzinaTabletka.setVisibility(View.GONE);
            ileDni.setVisibility(View.GONE);
            spinnerIleRazyDziennie.setVisibility(View.GONE);
            coIleDniEditText.setVisibility(View.GONE);

            for (int i = 0; i < 12; i++) {
                array.get(i).setVisibility(View.GONE);
            }

            spinnerReminder.setVisibility(View.GONE);
            dodaj.setVisibility(View.GONE);

            dawka.setVisibility(View.VISIBLE);
            spinnerNazwaLeku.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);

            Cursor cxz = myDb.getAllName_UZYTKOWNICY();
            if (cxz.getCount() > 1) {
                spinner.setVisibility(View.VISIBLE);
            }

            dalej.setVisibility(View.VISIBLE);
            spinnerDzwiek.setVisibility(View.VISIBLE);

        }
    }

    public void onResume() {
        super.onResume();
        loadSpinnerNazwaLeku();
    }

    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                goBack();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static int random()
    {
        return (int) (Math.random() * (MAX_VALUE));
    }

    private void randomChanger(int rand_val) {

        Cursor cursorCheckRandN = myDb.getRandId_NOTYFIKACJA(rand_val);
        cursorCheckRandN.moveToNext();
        int rand_val_n = cursorCheckRandN.getInt(0);

        Cursor cursorCheckRandW = myDb.getRandId_NOTYFIKACJA(rand_val);
        cursorCheckRandW.moveToNext();
        int rand_val_w = cursorCheckRandW.getInt(0);

        while(rand_val == rand_val_n &&
                rand_val == rand_val_n-1 &&
                rand_val == rand_val_n-2 &&
                rand_val == rand_val_n-3 &&
                rand_val == rand_val_w &&
                rand_val == rand_val_w-1) {

            rand_val = random();

        }

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

    @SuppressLint("SetTextI18n")
    private void intializeAllVariables() {

        final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        label = new ArrayList<>();
        labelDawka = new ArrayList<>();
        labelReminder = new ArrayList<>();
        labelIleRazyDziennie = new ArrayList<>();
        labelDzwiek = new ArrayList<>();
        labelNazwaLeku = new ArrayList<>();

        coWybrane = 0;
        ileRazyDziennie = 2;

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

        dzwiek = 1;
        jakaDawka = 1.00;

        dataTabletka = findViewById(R.id.dateVisit);
        godzinaTabletka = findViewById(R.id.timeMedicine);
        ileDni = findViewById(R.id.editText2);
        checkBox = findViewById(R.id.checkBox);
        dodaj = findViewById(R.id.dodajButton);
        dalej = findViewById(R.id.goThen);
        coIleDniEditText = findViewById(R.id.edt);
        coIleDniEditText.setVisibility(View.GONE);
        spinner = findViewById(R.id.spinnerProfile);
        dawka = findViewById(R.id.spinnerDawka);
        spinnerReminder = findViewById(R.id.spinnerJakCzesto);
        spinnerDzwiek = findViewById(R.id.spinnerDzwiek);
        spinnerNazwaLeku = findViewById(R.id.spinnerLek);
        setTime1 = findViewById(R.id.setTime1);
        setTime2 = findViewById(R.id.setTime2);
        setTime3 = findViewById(R.id.setTime3);
        setTime4 = findViewById(R.id.setTime4);
        setTime5 = findViewById(R.id.setTime5);
        setTime6 = findViewById(R.id.setTime6);
        setTime7 = findViewById(R.id.setTime7);
        setTime8 = findViewById(R.id.setTime8);
        setTime9 = findViewById(R.id.setTime9);
        setTime10 = findViewById(R.id.setTime10);
        setTime11 = findViewById(R.id.setTime11);
        setTime12 = findViewById(R.id.setTime12);
        spinnerIleRazyDziennie = findViewById(R.id.spinnerCoIleGodzin);

        array = new ArrayList<>();
        array.add(setTime1);
        array.add(setTime2);
        array.add(setTime3);
        array.add(setTime4);
        array.add(setTime5);
        array.add(setTime6);
        array.add(setTime7);
        array.add(setTime8);
        array.add(setTime9);
        array.add(setTime10);
        array.add(setTime11);
        array.add(setTime12);

        dataTabletka.setText(date);
        godzinaTabletka.setText(time);

        for (int i = 0; i < 12; i++) {
            array.get(i).setText("00:00");
        }

        Cursor res = myDb.getAllName_UZYTKOWNICY();
        res.moveToFirst();
        uzytkownik = res.getString(0);

        wlasnaDawka = "";

        loadSpinnerData();
        loadSpinnerDawka();
        loadSpinnerReminder();
        loadSpinnerIleRazyDziennie();
        loadSpinnerDzwiek();
        loadSpinnerNazwaLeku();

        labelSizeCopy = labelSize;

        dataTabletka.setVisibility(View.GONE);
        godzinaTabletka.setVisibility(View.GONE);
        ileDni.setVisibility(View.GONE);
        spinnerIleRazyDziennie.setVisibility(View.GONE);
        coIleDniEditText.setVisibility(View.GONE);

        for (int i = 0; i < 12; i++) {
            array.get(i).setVisibility(View.GONE);
        }

        spinnerReminder.setVisibility(View.GONE);
        dodaj.setVisibility(View.GONE);

        dawka.setSelection(1);

        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    }

    private void setSpinnerNazwaLekuOnClickListener() {
        spinnerNazwaLeku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                nazwaLeku = spinnerNazwaLeku.getItemAtPosition(position).toString();
                String selection = (String) parentView.getItemAtPosition(position);
                if (selection.equals("Dodaj nowy typ")) {
                    Intent cel = new Intent(parentView.getContext(), AddMedicine.class);
                    startActivity(cel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void setSpinnerIleRazyDziennieOnClickListener() {
        spinnerIleRazyDziennie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                closeKeyboard();

                for (int i = 0; i < 12; i++) {
                    array.get(i).setVisibility(View.GONE);
                }

                for (int i = 0; i < position + 2; i++) {
                    ileRazyDziennie = position + 2;
                    array.get(i).setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private void setSpinnerOnClickListener() {
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

    private void setSpinnerDzwiekOnClickListener() {
        spinnerDzwiek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                dzwiek = position;
                stopPlaying();

                switch(position) {
                    case 1:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm1);
                        mp.start();
                        break;
                    case 2:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm2);
                        mp.start();
                        break;
                    case 3:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm3);
                        mp.start();
                        break;
                    case 4:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm4);
                        mp.start();
                        break;
                    case 5:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm5);
                        mp.start();
                        break;
                    case 6:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm6);
                        mp.start();
                        break;
                    case 7:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm7);
                        mp.start();
                        break;
                    case 8:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm8);
                        mp.start();
                        break;
                    case 9:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm9);
                        mp.start();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private void setDawkaOnClickListener() {
        dawka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(dawka.getItemAtPosition(position).toString().equals("Własna dawka")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddReminder.this, R.style.AlertDialog);
                    builder.setTitle("Własna dawka");

                    final EditText input = new EditText(AddReminder.this);

                    input.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
                    input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

                    input.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setView(input);

                    builder.setPositiveButton("OK", (dialog, which) -> {
                        if(input.getText().length()!=0) {

                            wlasnaDawka = "Dawka: " + input.getText();
                            loadSpinnerDawka();
                            dawka.setSelection(labelDawka.size()-2);

                            String temp = dawka.getItemAtPosition(position).toString();
                            jakaDawka = Double.valueOf(temp.substring(7));

                        } else {
                            dawka.setSelection(1);
                            jakaDawka = 1.00;
                        }
                    });
                    builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.cancel());
                    builder.show();

                    wlasnaDawka="";

                } else {
                    String temp = dawka.getItemAtPosition(position).toString();
                    jakaDawka = Double.valueOf(temp.substring(7));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private void setDalejOnClickListener() {
        dalej.setOnClickListener(v -> {

            stopPlaying();

            if (spinnerNazwaLeku.getSelectedItem().toString().equals("Wybierz lek") || spinnerNazwaLeku.getSelectedItem().toString().equals("Dodaj nowy lek")) {

                openDialog("Wybierz lek lub dodaj nowy");

            } else {

                dawka.setVisibility(View.GONE);
                spinnerNazwaLeku.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                dalej.setVisibility(View.GONE);
                spinnerDzwiek.setVisibility(View.GONE);
                checkBox.setVisibility(View.GONE);

                spinnerReminder.setVisibility(View.VISIBLE);
                dodaj.setVisibility(View.VISIBLE);

                for (int i = 0; i < 12; i++) {
                    array.get(i).setVisibility(View.GONE);
                }

                int position = spinnerReminder.getSelectedItemPosition();

                switch(position) {
                    case 0:
                        coWybrane = 0;
                        dataTabletka.setVisibility(View.VISIBLE);
                        godzinaTabletka.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        coWybrane = 1;
                        dataTabletka.setVisibility(View.VISIBLE);
                        godzinaTabletka.setVisibility(View.VISIBLE);
                        ileDni.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        coWybrane = 2;
                        coIleDniEditText.setVisibility(View.GONE);
                        spinnerIleRazyDziennie.setVisibility(View.VISIBLE);
                        setTime1.setVisibility(View.VISIBLE);
                        setTime2.setVisibility(View.VISIBLE);
                        ileDni.setVisibility(View.VISIBLE);
                        dataTabletka.setVisibility(View.VISIBLE);

                        int positionHowManyTimes = spinnerIleRazyDziennie.getSelectedItemPosition();

                        for (int i = 0; i < positionHowManyTimes + 2; i++) {
                            ileRazyDziennie = positionHowManyTimes + 2;
                            array.get(i).setVisibility(View.VISIBLE);
                        }

                        break;
                    case 3:
                        coWybrane = 3;
                        coIleDniEditText.setVisibility(View.VISIBLE);
                        dataTabletka.setVisibility(View.VISIBLE);
                        godzinaTabletka.setVisibility(View.VISIBLE);
                        ileDni.setVisibility(View.VISIBLE);
                        spinnerIleRazyDziennie.setVisibility(View.GONE);
                        break;
                }

            }

        });
    }

    private void setSpinnerReminderOnClickListener() {
        spinnerReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                closeKeyboard();

                dataTabletka.setVisibility(View.GONE);
                godzinaTabletka.setVisibility(View.GONE);
                ileDni.setVisibility(View.GONE);
                spinnerIleRazyDziennie.setVisibility(View.GONE);
                coIleDniEditText.setVisibility(View.GONE);

                for (int i = 0; i < 12; i++) {
                    array.get(i).setVisibility(View.GONE);
                }

                switch(position) {
                    case 0:
                        coWybrane = 0;
                        dataTabletka.setVisibility(View.VISIBLE);
                        godzinaTabletka.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        coWybrane = 1;
                        dataTabletka.setVisibility(View.VISIBLE);
                        godzinaTabletka.setVisibility(View.VISIBLE);
                        ileDni.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        coWybrane = 2;
                        spinnerIleRazyDziennie.setSelection(0);
                        coIleDniEditText.setVisibility(View.GONE);
                        spinnerIleRazyDziennie.setVisibility(View.VISIBLE);
                        setTime1.setVisibility(View.VISIBLE);
                        setTime2.setVisibility(View.VISIBLE);
                        ileDni.setVisibility(View.VISIBLE);
                        dataTabletka.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        coWybrane = 3;
                        coIleDniEditText.setVisibility(View.VISIBLE);
                        dataTabletka.setVisibility(View.VISIBLE);
                        godzinaTabletka.setVisibility(View.VISIBLE);
                        ileDni.setVisibility(View.VISIBLE);
                        spinnerIleRazyDziennie.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private void calculateDiff(String godzina, String data) {

        cal = getInstance();
        cal.set(year, month - 1, day, hour, minutes, 0);

        String dzisiejszaData = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String dzisiejszyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat tdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Date firstDate = null;
        Date secondDate = null;

        Date firstTime = null;
        Date secondTime = null;

        try {
            firstDate = sdf.parse(dzisiejszaData);
            firstTime = tdf.parse(dzisiejszyCzas);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            secondDate = sdf.parse(data);
            secondTime = tdf.parse(godzina);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = Objects.requireNonNull(secondDate).getTime() - Objects.requireNonNull(firstDate).getTime();
        diffDays = diff / (24 * 60 * 60 * 1000);
        diffInMillis = Objects.requireNonNull(secondTime).getTime() - Objects.requireNonNull(firstTime).getTime();

    }

    private void setDodajOnClickListener() {
        dodaj.setOnClickListener(view -> {

            Cursor cl = myDb.getDataName_LEK(nazwaLeku);
            cl.moveToFirst();
            Double iloscTabletek = Double.valueOf(cl.getString(2));
            Double jakaDawkaTabletki = jakaDawka;

            if ((iloscTabletek - jakaDawkaTabletki) >= 0) {

                if (coWybrane == 0) {

                    String dataPrzypomnienia = dataTabletka.getText().toString();
                    String godzinaPrzypomnienia = godzinaTabletka.getText().toString();
                    calculateDiff(godzinaPrzypomnienia, dataPrzypomnienia);

                    if (diffDays == 0 && diffInMillis < 0) openDialog("Godzina dzisiejszego powiadomienia minęła, wybierz inną godzinę lub ustaw przyszłą datę");
                    else {

                        Integer idd = 0;
                        Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();
                        if(cx.getCount() != 0) {
                            cx.moveToFirst();
                            idd = Integer.parseInt(cx.getString(0));
                            idd++;
                        }

                        myDb.insert_PRZYPOMNIENIE(
                                idd,
                                godzinaTabletka.getText().toString(),
                                dataPrzypomnienia,
                                nazwaLeku,
                                jakaDawka,
                                1,
                                uzytkownik,
                                1,
                                "Jednorazowo: " + godzinaTabletka.getText().toString(),
                                dzwiek,
                                czyWibracja
                        );

                        insertNotyfikacjaAndAddAlarm(
                                idd,
                                godzinaPrzypomnienia,
                                dataPrzypomnienia,
                                uzytkownik + " |  " + godzinaPrzypomnienia + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")",
                                0,
                                cal);

                        goBack();

                    }

                } else if (coWybrane == 1 || coWybrane == 3) {

                    if((coWybrane == 1 && ileDni.getText().length() > 0) || (coWybrane == 3 && ileDni.getText().length() > 0 && coIleDniEditText.getText().length() > 0)) {

                        iloscDni = Integer.parseInt(ileDni.getText().toString());
                        coIleDni = 1;

                        if (coIleDniEditText.getVisibility() == View.VISIBLE) {
                            if (coIleDniEditText.getTextSize() != 0)
                                coIleDni = Integer.parseInt(coIleDniEditText.getText().toString());
                        }

                        String dataPrzypomnienia = dataTabletka.getText().toString();
                        String godzinaPrzypomnienia = godzinaTabletka.getText().toString();
                        calculateDiff(godzinaPrzypomnienia, dataPrzypomnienia);

                        if (diffDays == 0 && diffInMillis < 0) openDialog("Godzina dzisiejszego powiadomienia minęła, wybierz inną godzinę lub ustaw przyszłą datę");
                        else {

                            if (coWybrane == 1 && iloscDni >= 1) {

                                Integer idd = 0;

                                Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();
                                if(cx.getCount()!=0) {
                                    cx.moveToFirst();
                                    idd = Integer.parseInt(cx.getString(0));
                                    idd++;
                                }

                                myDb.insert_PRZYPOMNIENIE(
                                        idd,
                                        godzinaTabletka.getText().toString(),
                                        dataPrzypomnienia,
                                        nazwaLeku,
                                        jakaDawka,
                                        iloscDni,
                                        uzytkownik,
                                        1,
                                        "Codziennie: " + godzinaTabletka.getText().toString(),
                                        dzwiek,
                                        czyWibracja
                                );

                                insertNotyfikacjaAndAddAlarm(
                                        idd,
                                        godzinaPrzypomnienia,
                                        dataPrzypomnienia,
                                        uzytkownik + " |  " + godzinaPrzypomnienia + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")",
                                        iloscDni-1,
                                        cal);

                            } else if (coWybrane == 3 && iloscDni >= 1) {

                                Integer idd = 0;

                                Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();
                                if(cx.getCount() != 0) {
                                    cx.moveToFirst();
                                    idd = Integer.parseInt(cx.getString(0));
                                    idd++;
                                }

                                myDb.insert_PRZYPOMNIENIE(
                                        idd,
                                        godzinaTabletka.getText().toString(),
                                        dataPrzypomnienia,
                                        nazwaLeku,
                                        jakaDawka,
                                        iloscDni,
                                        uzytkownik,
                                        coIleDni,
                                        "Co " + String.valueOf(coIleDni) + " dni: " + godzinaTabletka.getText().toString(),
                                        dzwiek,
                                        czyWibracja
                                );

                                insertNotyfikacjaAndAddAlarm(
                                        idd,
                                        godzinaPrzypomnienia,
                                        dataPrzypomnienia,
                                        uzytkownik + " |  " + godzinaPrzypomnienia + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")",
                                        iloscDni-1,
                                        cal);

                            }

                            goBack();

                        }

                    } else openDialog("Wypełnij wszystkie pola");

                } else if (coWybrane == 2) {

                    if(ileDni.getText().length() > 0) {

                        int czyUsunacDzien = 0;
                        Integer czyUsunacWszystkieDni = 0;

                        for (Integer i = 1; i <= ileRazyDziennie; i++) {

                            String godzinaPrzypomnienia;
                            StringBuilder wszystkieGodziny;

                            StringBuilder sb = new StringBuilder();

                            for (int j = 0; j < ileRazyDziennie - 1; j++) {
                                sb.append(array.get(j).getText().toString()).append(", ");
                            }

                            sb.append(array.get(ileRazyDziennie - 1).getText().toString());
                            wszystkieGodziny = new StringBuilder(sb.toString());

                            sb.setLength(0);

                            sb.append(array.get(i - 1).getText().toString());
                            godzinaPrzypomnienia = sb.toString();

                            iloscDni = Integer.parseInt(ileDni.getText().toString());

                            String czyDwucyfrowa = String.valueOf(godzinaPrzypomnienia.charAt(1));
                            int minutes;
                            int hour;

                            if (czyDwucyfrowa.equals(":")) {
                                hour = Integer.parseInt(String.valueOf(godzinaPrzypomnienia.charAt(0)));
                                minutes = Integer.parseInt(String.valueOf(godzinaPrzypomnienia.charAt(2))
                                        + String.valueOf(godzinaPrzypomnienia.charAt(3)));
                            } else {
                                hour = Integer.parseInt(String.valueOf(godzinaPrzypomnienia.charAt(0))
                                        + String.valueOf(godzinaPrzypomnienia.charAt(1)));
                                minutes = Integer.parseInt(String.valueOf(godzinaPrzypomnienia.charAt(3))
                                        + String.valueOf(godzinaPrzypomnienia.charAt(4)));
                            }


                            String dataPrzypomnienia = dataTabletka.getText().toString();
                            calculateDiff(godzinaPrzypomnienia, dataPrzypomnienia);

                            if(diffDays == 0 && diffInMillis < 0) {

                                if (iloscDni==1) {
                                    czyUsunacDzien = 1;
                                    czyUsunacWszystkieDni++;
                                } else if (iloscDni>1) {
                                    Calendar cx = Calendar.getInstance();
                                    try {
                                        cx.setTime(sdf.parse(dataPrzypomnienia));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    cx.add(Calendar.DATE, 1);
                                    dataPrzypomnienia = sdf.format(cx.getTime());
                                    cal.set(year, month - 1, day + 1, hour, minutes, 0);
                                    czyUsunacDzien = 0;
                                }

                            } else cal.set(year, month - 1, day, hour, minutes, 0);

                            Integer idd = 0;

                            Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();
                            if(cx.getCount() != 0) {
                                cx.moveToFirst();
                                idd = Integer.parseInt(cx.getString(0));
                                idd++;
                            }

                            if (i.equals(ileRazyDziennie)) {
                                if (i.equals(czyUsunacWszystkieDni)) {
                                    iloscDni--;
                                }

                                String[] wszystkie = wszystkieGodziny.toString().replaceAll(" ", "").split(",");
                                List<String> tempList = Arrays.asList(wszystkie);

                                List<String> tempWszystkieGodziny = Arrays.asList(wszystkie);
                                Collections.sort(tempWszystkieGodziny);

                                wszystkieGodziny = new StringBuilder();

                                for (String s : tempWszystkieGodziny) wszystkieGodziny.append(s).append(", ");
                                wszystkieGodziny = new StringBuilder(wszystkieGodziny.substring(0, wszystkieGodziny.length() - 2));
                                String najwyzszaGodzina = Collections.max(tempList);

                                myDb.insert_PRZYPOMNIENIE(
                                        idd,
                                        najwyzszaGodzina,
                                        dataPrzypomnienia,
                                        nazwaLeku,
                                        jakaDawka,
                                        iloscDni,
                                        uzytkownik,
                                        1,
                                        String.valueOf(ileRazyDziennie) + " razy dziennie: " + wszystkieGodziny,
                                        dzwiek,
                                        czyWibracja
                                );
                            }

                            if (czyUsunacDzien != 1 && iloscDni>=1) {

                                insertNotyfikacjaAndAddAlarm(
                                        idd,
                                        godzinaPrzypomnienia,
                                        dataPrzypomnienia,
                                        uzytkownik + " |  " + godzinaPrzypomnienia + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")",
                                        iloscDni-1,
                                        cal);

                            }

                            goBack();
                        }
                    } else openDialog("Wypełnij wszystkie pola");
                }
            } else showNotification();
        });
    }

    private void insertNotyfikacjaAndAddAlarm(Integer idd, String godzinaPrzypomnienia, String dataPrzypomnienia, String trescAlarmu, Integer iloscDni, Calendar cal) {

        Integer rand_val = random();
        Integer id;

        randomChanger(rand_val);

        myDb.insert_NOTYFIKACJA(
                idd,
                godzinaPrzypomnienia,
                dataPrzypomnienia,
                rand_val
        );

        Cursor c1 = myDb.getMAXid_NOTYFIKACJA();
        c1.moveToFirst();
        id = Integer.parseInt(c1.getString(0));

        Intent intx = new Intent(getApplicationContext(), NotificationReceiver.class);

        intx.putExtra("tresc", trescAlarmu);
        intx.putExtra("coPokazac", 0);
        intx.putExtra("id", id);
        intx.putExtra("idd", idd);
        intx.putExtra("godzina", godzinaPrzypomnienia);
        intx.putExtra("data", dataPrzypomnienia);
        intx.putExtra("uzytkownik", uzytkownik);
        intx.putExtra("nazwaLeku", nazwaLeku);
        intx.putExtra("jakaDawka", jakaDawka);
        intx.putExtra("iloscDni", iloscDni);
        intx.putExtra("wybranyDzwiek", dzwiek);
        intx.putExtra("czyWibracja", czyWibracja);
        intx.putExtra("rand_val", rand_val);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), rand_val, intx, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;

        if(Build.VERSION.SDK_INT < 23){
            if(Build.VERSION.SDK_INT >= 19) alarmManager.setExact(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
            else alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
        } else alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);

        Toast.makeText(getApplicationContext(), "Dodanie: " + String.valueOf(rand_val) + "\n" + cal.getTime().toString().substring(0,16), Toast.LENGTH_LONG).show();
        Log.d("========ALARM==========", "Dodanie: " + String.valueOf(rand_val) + "\n" + cal.getTime().toString().substring(0,16));

    }

    private void showNotification() {

        AlertDialog.Builder builderw = new AlertDialog.Builder(AddReminder.this, R.style.AlertDialog);
        builderw.setMessage("Posiadasz zbyt mało tabletek danego leku, aby dodać przypomnienie");

        builderw.setPositiveButton("Zaktualizuj ilość", (dialogg, whichh) -> {

            Cursor clr = myDb.getID_LEK(nazwaLeku);
            clr.moveToFirst();
            Integer id_l = Integer.parseInt(clr.getString(0));

            Cursor clrr = myDb.getNumber_LEK(id_l);
            clrr.moveToFirst();

            String ilosc = clrr.getString(0);

            AlertDialog.Builder builder = new AlertDialog.Builder(AddReminder.this, R.style.AlertDialog);
            builder.setTitle("Aktualizacja ilości");

            final EditText input = new EditText(getApplicationContext());

            input.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

            input.setText(ilosc);
            input.setSelection(input.getText().length());
            input.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(input);

            showKeyboard();

            builder.setPositiveButton("OK", (dialog, which) -> {
                myDb.update_LEK(id_l, Double.valueOf(input.getText().toString()));
                closeKeyboard();
            });
            builder.setNegativeButton("Anuluj", (dialog, which) -> {
                dialog.cancel();
                closeKeyboard();
            });

            AlertDialog alertDialog = builder.show();
            alertDialog.setCanceledOnTouchOutside(false);

        });

        builderw.setNegativeButton("Anuluj dodawanie", (dialogg, whichh) -> {
            dialogg.cancel();
            goBack();
        });

        AlertDialog alertDialog = builderw.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }

    private void setAllTimeOnClickListener() {

        setTime1.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime1Listener, hour, minutes, true);
            dialog.show();
        });

        setTime1Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime1.setText(Time);
        };

        setTime2.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime2Listener, hour, minutes, true);
            dialog.show();
        });

        setTime2Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime2.setText(Time);
        };

        setTime3.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime3Listener, hour, minutes, true);
            dialog.show();
        });

        setTime3Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime3.setText(Time);
        };

        setTime4.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime4Listener, hour, minutes, true);
            dialog.show();
        });

        setTime4Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime4.setText(Time);
        };

        setTime5.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime5Listener, hour, minutes, true);
            dialog.show();
        });

        setTime5Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime5.setText(Time);
        };

        setTime6.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime6Listener, hour, minutes, true);
            dialog.show();
        });

        setTime6Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime6.setText(Time);
        };

        setTime7.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime7Listener, hour, minutes, true);
            dialog.show();
        });

        setTime7Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime7.setText(Time);
        };

        setTime8.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime8Listener, hour, minutes, true);
            dialog.show();
        });

        setTime8Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime8.setText(Time);
        };

        setTime9.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime9Listener, hour, minutes, true);
            dialog.show();
        });

        setTime9Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime9.setText(Time);
        };

        setTime10.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime10Listener, hour, minutes, true);
            dialog.show();
        });

        setTime10Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime10.setText(Time);
        };

        setTime11.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime11Listener, hour, minutes, true);
            dialog.show();
        });

        setTime11Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime11.setText(Time);
        };

        setTime12.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, setTime12Listener, hour, minutes, true);
            dialog.show();
        });

        setTime12Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime12.setText(Time);
        };

        godzinaTabletka.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, godzinaTabletkaListener, hour, minutes, true);
            dialog.show();
        });

        godzinaTabletkaListener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;

            hour = hourOfDay;
            minutes = minute;

            godzinaTabletka.setText(Time);
        };

        dataTabletka.setOnClickListener(view -> {
            closeKeyboard();
            DatePickerDialog dialog = new DatePickerDialog(AddReminder.this, dataTabletkaListener, year, month - 1, day);
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            dialog.show();
        });

        dataTabletkaListener = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            String dataTemp;
            if (dayOfMonth < 10 && month < 10)
                dataTemp = "0" + dayOfMonth + "/0" + month + "/" + year;
            else if (dayOfMonth < 10) dataTemp = "0" + dayOfMonth + "/" + month + "/" + year;
            else if (month < 10) dataTemp = dayOfMonth + "/0" + month + "/" + year;
            else dataTemp = dayOfMonth + "/" + month + "/" + year;

            this.year = year;
            this.month = month;
            day = dayOfMonth;

            dataTabletka.setText(dataTemp);

        };
    }

}