package com.wezpigulke.add;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;
import com.wezpigulke.adapters.DoctorSpinnerAdapter;
import com.wezpigulke.classes.Doctor;
import com.wezpigulke.notification.NotificationReceiver;
import com.wezpigulke.other.OpenDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Calendar.getInstance;

public class AddVisit extends AppCompatActivity {

    DatabaseHelper myDb;

    private TextView data;
    private TextView godzina;
    private Button dodaj;
    private DatePickerDialog.OnDateSetListener dataListener;
    private TimePickerDialog.OnTimeSetListener godzinaListener;
    private Spinner spinner;
    private Spinner spinnerDoctor;
    private Spinner spinnerDzwiek;

    private String name;
    private String specialization;
    private Integer czyWibracja;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private ArrayList<String> labelDzwiek;
    private ArrayList<String> label;
    private Integer dzwiek;
    private MediaPlayer mp;
    private String uzytkownik;
    private Long diff;
    private Calendar cal;
    private ArrayList<Doctor> doctorArrayList;
    private Cursor cursor;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_visit);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        intializeAllVariables();
        loadSpinnerData();
        loadSpinnerDzwiek();
        loadSpinnerDoctor();
        spinnerListener();
        spinnerDoctorListener();
        spinnerDzwiekListener();
        dodajListener();
        allTimeListener();

    }

    private void dodajListener() {

        dodaj.setOnClickListener(v -> {

            stopPlaying();

            Doctor selectedItem = (Doctor) spinnerDoctor.getSelectedItem();
            if (selectedItem.getSpecialization().equals("Wybierz lekarza")) {
                openDialog("Wybierz lekarza lub dodaj nowego");
            } else {

                calculateDiff();

                if (diff < 0)
                    openDialog("Godzina dzisiejszej wizyty minęła, wybierz inną godzinę lub ustaw przyszłą datę");
                else {

                    Integer rand_val = random();
                    randomChanger(rand_val);

                    myDb.insert_WIZYTY(
                            godzina.getText().toString(),
                            data.getText().toString(),
                            name,
                            specialization,
                            uzytkownik,
                            rand_val,
                            dzwiek,
                            czyWibracja
                    );

                    int id = 0;
                    cursor = myDb.getMaxId_WIZYTY();
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        id = cursor.getInt(0);
                    }

                    Intent intxz = putExtraToIntent(id, rand_val, uzytkownik + "  |  wizyta u " + specialization + ": " + name + " jutro o " + godzina.getText().toString());
                    cal.add(Calendar.DATE, -1);

                    if (diff > 24 * 60 * 60 * 100) addAlarm(rand_val, intxz);

                    rand_val--;
                    intxz = putExtraToIntent(id, rand_val, uzytkownik + "  |  wizyta u " + specialization + ": " + name + " o " + godzina.getText().toString());

                    cal.add(Calendar.DATE, +1);
                    cal.add(Calendar.HOUR_OF_DAY, -3);
                    addAlarm(rand_val, intxz);

                    onBackPressed();

                }

            }

        });

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

    private void spinnerDoctorListener() {

        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Doctor doctor = (Doctor) parentView.getItemAtPosition(position);

                if (doctor.getSpecialization().equals("Dodaj nowego lekarza")) {
                    Intent cel = new Intent(parentView.getContext(), AddDoctor.class);
                    startActivity(cel);
                }

                name = doctor.getName();
                specialization = doctor.getSpecialization();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }

    private void calculateDiff() {

        cal = getInstance();
        cal.set(year, month - 1, day, hour, minutes, 0);

        String dzisiejszaData = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

        Date firstDate = null;
        Date secondDate = null;

        try {
            firstDate = sdf.parse(dzisiejszaData);
            secondDate = sdf.parse(godzina.getText().toString() + " " + data.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert secondDate != null;
        assert firstDate != null;
        diff = secondDate.getTime() - firstDate.getTime();

    }

    private void addAlarm(Integer rand_val, Intent intxz) {

        PendingIntent pendingIntentt = PendingIntent.getBroadcast(getApplicationContext(), rand_val, intxz, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManagerr = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManagerr != null;
        alarmManagerr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntentt);

    }

    private Intent putExtraToIntent(Integer id, Integer rand_val, String tresc) {

        Intent intxz = new Intent(getApplicationContext(), NotificationReceiver.class);
        intxz.putExtra("coPokazac", 1);
        intxz.putExtra("tresc", tresc);
        intxz.putExtra("id", id);
        intxz.putExtra("godzina", godzina.getText().toString());
        intxz.putExtra("data", data.getText().toString());
        intxz.putExtra("imie_nazwisko", name);
        intxz.putExtra("specjalizacja", specialization);
        intxz.putExtra("uzytkownik", uzytkownik);
        intxz.putExtra("wybranyDzwiek", dzwiek);
        intxz.putExtra("czyWibracja", czyWibracja);
        intxz.putExtra("rand_val", rand_val);

        return intxz;

    }

    private void spinnerDzwiekListener() {

        spinnerDzwiek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                dzwiek = position;
                stopPlaying();

                switch (position) {
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

    private void allTimeListener() {

        godzina.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddVisit.this, godzinaListener, hour, minutes, true);
            dialog.show();
        });

        godzinaListener = (view, hourOfDay, minute) -> {
            String time1;
            if (minute < 10) time1 = hourOfDay + ":0" + minute;
            else time1 = hourOfDay + ":" + minute;
            hour = hourOfDay;
            minutes = minute;
            godzina.setText(time1);
        };

        data.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(AddVisit.this, dataListener, year, month - 1, day);
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            dialog.show();
        });

        dataListener = (view, yeear, moonth, dayOfMonth) -> {
            moonth = moonth + 1;
            String date1;
            if (moonth < 10) date1 = dayOfMonth + "/0" + moonth + "/" + yeear;
            else date1 = dayOfMonth + "/" + moonth + "/" + yeear;
            year = yeear;
            month = moonth;
            day = dayOfMonth;
            data.setText(date1);
        };

    }

    private void loadSpinnerData() {

        cursor = myDb.getAllName_UZYTKOWNICY();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                label.add(cursor.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        }
    }

    private void loadSpinnerDoctor() {

        doctorArrayList.clear();

        cursor = myDb.getAllData_DOKTORZY();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(3).equals("0")) {
                    doctorArrayList.add(new Doctor(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(4)));
                } else
                    doctorArrayList.add(new Doctor(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(4)));
            }
        }

        doctorArrayList.add(new Doctor(-2, "", "Dodaj nowego lekarza", ""));
        doctorArrayList.add(new Doctor(-1, "", "Wybierz lekarza", ""));

        final int labelSize = doctorArrayList.size() - 1;

        DoctorSpinnerAdapter doctorSpinnerAdapter = new DoctorSpinnerAdapter(getApplicationContext(), doctorArrayList);
        spinnerDoctor.setAdapter(doctorSpinnerAdapter);
        spinnerDoctor.setSelection(labelSize);

    }

    private void loadSpinnerDzwiek() {

        labelDzwiek.add("Brak");

        for (int i = 1; i <= 9; i++) {
            labelDzwiek.add("Alarm nr " + i);
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

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddVisit");
    }

    @Override
    public void onBackPressed() {
        if (cursor != null) cursor.close();
        stopPlaying();
        super.onBackPressed();
        finish();
    }

    public void onResume() {
        super.onResume();
        loadSpinnerDoctor();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    private void randomChanger(int rand_val) {

        cursor = myDb.getRandId_NOTYFIKACJA(rand_val);
        cursor.moveToNext();
        int rand_val_n = cursor.getInt(0);

        cursor = myDb.getRandId_NOTYFIKACJA(rand_val);
        cursor.moveToNext();
        int rand_val_w = cursor.getInt(0);

        while (rand_val == rand_val_n &&
                rand_val == rand_val_n - 1 &&
                rand_val == rand_val_n - 2 &&
                rand_val == rand_val_n - 3 &&
                rand_val == rand_val_w &&
                rand_val == rand_val_w - 1) {

            rand_val = random();

        }

    }

    public static int random() {
        return (int) (Math.random() * (MAX_VALUE));
    }

    private void intializeAllVariables() {

        myDb = new DatabaseHelper(this);

        czyWibracja = 0;

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        String rok = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String miesiac = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String dzien = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        String godzinaa = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        String minuta = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());

        label = new ArrayList<>();
        labelDzwiek = new ArrayList<>();
        doctorArrayList = new ArrayList<>();
        year = Integer.parseInt(rok);
        month = Integer.parseInt(miesiac);
        day = Integer.parseInt(dzien);
        hour = Integer.parseInt(godzinaa);
        minutes = Integer.parseInt(minuta);

        dodaj = findViewById(R.id.dodajVisit);
        data = findViewById(R.id.dateVisit);
        godzina = findViewById(R.id.timeVisit);
        CheckBox checkBox = findViewById(R.id.checkBoxx);

        data.setText(date);
        godzina.setText(time);

        spinner = findViewById(R.id.spinner2);
        spinnerDzwiek = findViewById(R.id.spinnerDzwiek2);
        dzwiek = 1;

        spinnerDoctor = findViewById(R.id.spinnerDoctor);

        cursor = myDb.getAllName_UZYTKOWNICY();
        cursor.moveToFirst();
        uzytkownik = cursor.getString(0);

        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) czyWibracja = 1;
            else czyWibracja = 0;
        });

    }
}
