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
import android.widget.Toast;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.adapters.DoctorListAdapter;
import com.wezpigulke.adapters.DoctorSpinnerAdapter;
import com.wezpigulke.classes.Doctor;
import com.wezpigulke.notification.NotificationReceiver;
import com.wezpigulke.other.OpenDialog;
import com.wezpigulke.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Calendar.*;

public class AddVisit extends AppCompatActivity {

    DatabaseHelper myDb;

    private TextView data;
    private TextView godzina;
    private Button dodaj;

    private String name;
    private String specialization;
    private Integer czyWibracja;

    private DatePickerDialog.OnDateSetListener dataListener;
    private TimePickerDialog.OnTimeSetListener godzinaListener;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;

    private Spinner spinnerDzwiek;
    private ArrayList<String> labelDzwiek;
    private ArrayList<String> label;
    private Integer dzwiek;
    private MediaPlayer mp;

    private Spinner spinner;
    private Spinner spinnerDoctor;
    private String uzytkownik;
    private Long diff;
    private Calendar cal;

    private ArrayList<Doctor> doctorArrayList;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_visit);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        intializeAllVariables();
        loadSpinnerData();
        loadSpinnerDzwiek();
        loadSpinnerDoctor();
        setSpinnerOnItemSelectedListener();
        setSpinnerDoctorOnItemSelectedListener();
        setSpinnerDzwiekOnItemSelectedListener();
        setDodajOnClickListener();
        setAllTimeOnClickListener();

    }

    private void setDodajOnClickListener() {

        dodaj.setOnClickListener(v -> {

            stopPlaying();

            Doctor selectedItem = (Doctor)spinnerDoctor.getSelectedItem();
            if (selectedItem.getSpecialization().equals("Wybierz lekarza")) {
                openDialog("Wybierz lekarza lub dodaj nowego");
            } else {

                calculateDiff();

                if (diff < 0) openDialog("Godzina dzisiejszej wizyty minęła, wybierz inną godzinę lub ustaw przyszłą datę");
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
                    Cursor cw = myDb.getMaxId_WIZYTY();
                    if(cw.getCount() != 0) {
                        cw.moveToFirst();
                        id = cw.getInt(0);
                    }

                    Intent intxz = putExtraToIntent(id, rand_val, uzytkownik + "  |  wizyta u " + specialization + ": " + name + " jutro o " + godzina.getText().toString());
                    cal.add(Calendar.DATE, -1);

                    if(diff > 24*60*60*100) addAlarm(rand_val, intxz);

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

    private void setSpinnerOnItemSelectedListener() {
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

    private void setSpinnerDoctorOnItemSelectedListener() {

        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Doctor doctor = (Doctor)parentView.getItemAtPosition(position);

                if(doctor.getSpecialization().equals("Dodaj nowego lekarza")) {
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
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

    private void setSpinnerDzwiekOnItemSelectedListener() {

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

    private void setAllTimeOnClickListener() {

        godzina.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddVisit.this,godzinaListener, hour, minutes, true);
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

        Cursor cxz = myDb.getAllName_UZYTKOWNICY();

        if (cxz.getCount() != 0) {
            while (cxz.moveToNext()) {
                label.add(cxz.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        }
    }

    private void loadSpinnerDoctor() {

        doctorArrayList.clear();

        Cursor c = myDb.getAllData_DOKTORZY();

        if (c.getCount() != 0) {
            while (c.moveToNext()) {
                if(c.getString(3).equals("0")) {
                    doctorArrayList.add(new Doctor(c.getInt(0), c.getString(1), c.getString(2), c.getString(4)));
                } else doctorArrayList.add(new Doctor(c.getInt(0), c.getString(1), c.getString(2), c.getString(4)));
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

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddVisit");
    }

    @Override
    public void onBackPressed() {
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
        switch (item.getItemId()) {

            case android.R.id.home:
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

    public static int random()
    {
        return (int) (Math.random() * (MAX_VALUE));
    }

    private void intializeAllVariables() {

        czyWibracja = 0;

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

        final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        data.setText(date);
        godzina.setText(time);

        spinner = findViewById(R.id.spinner2);
        spinnerDzwiek = findViewById(R.id.spinnerDzwiek2);
        dzwiek = 1;

        spinnerDoctor = findViewById(R.id.spinnerDoctor);

        Cursor res = myDb.getAllName_UZYTKOWNICY();
        res.moveToFirst();
        uzytkownik = res.getString(0);

        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked) czyWibracja = 1;
            else czyWibracja = 0;
        });

    }
}
