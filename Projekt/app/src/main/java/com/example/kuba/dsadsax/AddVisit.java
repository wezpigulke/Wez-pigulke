package com.example.kuba.dsadsax;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.util.Calendar.*;

public class AddVisit extends AppCompatActivity {

    DatabaseHelper myDb;
    private static final String TAG = "AddVisit";

    private TextView data;
    private TextView godzina;

    private String name;
    private String specialization;
    private String address;

    private DatePickerDialog.OnDateSetListener dataListener;
    private TimePickerDialog.OnTimeSetListener godzinaListener;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;

    private Spinner spinnerDzwiek;
    private ArrayList<String> labelDzwiek;
    private Integer dzwiek;
    private MediaPlayer mp;

    private Button add;
    private Button goThen;
    List<StringWithTag> list;

    private Spinner spinner;
    private Spinner spinnerDoctor;
    private ArrayList<String> label;
    private String uzytkownik;
    private String lekarz;

    private int labelSize;
    private int labelSizeCopy;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        label = new ArrayList<>();
        labelDzwiek = new ArrayList<>();
        ArrayList<String> labelDoctor = new ArrayList<>();
        list = new ArrayList<StringWithTag>();

        String rok = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String miesiac = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String dzien = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        String godzinaa = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        String minuta = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());

        year = Integer.parseInt(rok);
        month = Integer.parseInt(miesiac);
        day = Integer.parseInt(dzien);
        hour = Integer.parseInt(godzinaa);
        minutes = Integer.parseInt(minuta);

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_visit);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        add = findViewById(R.id.dodajVisit);

        data = findViewById(R.id.dateVisit);
        godzina = findViewById(R.id.timeVisit);

        final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        data.setText(date);
        godzina.setText(time);

        spinner = findViewById(R.id.spinner2);
        spinnerDzwiek = findViewById(R.id.spinnerDzwiek2);
        dzwiek = 1;

        spinnerDoctor = findViewById(R.id.spinnerDoctor);

        Cursor res = myDb.getName_UZYTKOWNICY();

        while (res.moveToNext()) {
            uzytkownik = res.getString(0);
        }

        loadSpinnerData();
        loadSpinnerDzwiek();
        loadSpinnerDoctor();

        labelSizeCopy = labelSize;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                uzytkownik = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                StringWithTag s = (StringWithTag) parentView.getItemAtPosition(position);
                Object tag = s.tag;
                int id_s = Integer.parseInt(tag.toString());

                lekarz = spinnerDoctor.getItemAtPosition(position).toString();

                if(lekarz.equals("Dodaj nowego lekarza")) {

                    Intent cel = new Intent(parentView.getContext(), AddDoctor.class);
                    startActivity(cel);

                } else if (!lekarz.equals("Wybierz lekarza")) {

                    Cursor cd = myDb.getIdData_DOKTORZY(id_s);
                    cd.moveToFirst();
                    name = cd.getString(1);
                    specialization = cd.getString(2);
                    address = cd.getString(4);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        add.setOnClickListener(v -> {

            stopPlaying();

            if(spinnerDoctor.getSelectedItem().toString().equals("Dodaj nowego lekarza") ||
                    spinnerDoctor.getSelectedItem().toString().equals("Dodaj nowego lekarza")) {
                openDialog("Wybierz lekarza lub dodaj nowego");
            } else {
                myDb.insert_WIZYTY(
                        godzina.getText().toString(),
                        data.getText().toString(),
                        name,
                        specialization,
                        address,
                        uzytkownik
                );

                Cursor c = myDb.getMAXid_WIZYTY();
                c.moveToFirst();
                int id = Integer.parseInt(c.getString(0));

                Calendar cal = getInstance();
                cal.set(year, month - 1, day, hour, minutes, 0);
                cal.add(Calendar.DATE, -1);

                c = myDb.getSelectedData_WIZYTY(id);
                c.moveToFirst();

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
                    secondDate = sdf.parse(c.getString(2));
                    secondTime = tdf.parse(c.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                assert secondDate != null;
                assert firstDate != null;

                long diff = secondDate.getTime() - firstDate.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);

                assert secondTime != null;
                assert firstTime != null;

                long diffInMillis = secondTime.getTime() - firstTime.getTime();

                Intent intx;
                PendingIntent pendingIntent;
                AlarmManager alarmManager;

                if(diffDays>0) {
                    intx = new Intent(getApplicationContext(), NotificationReceiverVisit.class);
                    intx.putExtra("Value", uzytkownik + "  |  wizyta u " + c.getString(4) + " jutro o " + c.getString(1));
                    intx.putExtra("id", (id * 2) - 1);
                    intx.putExtra("godzina", c.getString(1));
                    intx.putExtra("data", c.getString(2));
                    intx.putExtra("imie_nazwisko", c.getString(3));
                    intx.putExtra("specjalizacja", c.getString(4));
                    intx.putExtra("adres", c.getString(5));
                    intx.putExtra("uzytkownik", c.getString(6));
                    intx.putExtra("wybranyDzwiek", dzwiek);

                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (id * 2) - 1, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    assert alarmManager != null;
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }

                cal.add(Calendar.DATE, +1);
                cal.add(Calendar.HOUR, -2);

                if((diffDays==0 && diffInMillis>0) || diffDays>0) {
                    intx = new Intent(getApplicationContext(), NotificationReceiverVisit.class);
                    intx.putExtra("Value", uzytkownik + "  |  wizyta u " + c.getString(4) + " o " + c.getString(1));
                    intx.putExtra("id", (id * 2) - 1);
                    intx.putExtra("godzina", c.getString(1));
                    intx.putExtra("data", c.getString(2));
                    intx.putExtra("imie_nazwisko", c.getString(3));
                    intx.putExtra("specjalizacja", c.getString(4));
                    intx.putExtra("adres", c.getString(5));
                    intx.putExtra("uzytkownik", c.getString(6));
                    intx.putExtra("wybranyDzwiek", dzwiek);

                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id * 2, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    assert alarmManager != null;
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                onBackPressed();
            }

        });

        spinnerDzwiek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                dzwiek = position;
                stopPlaying();

                if (position == 0) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm1);
                    mp.start();
                }
                if (position == 1) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm2);
                    mp.start();
                } else if (position == 2) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm3);
                    mp.start();
                } else if (position == 3) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm4);
                    mp.start();
                } else if (position == 4) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm5);
                    mp.start();
                } else if (position == 5) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm6);
                    mp.start();
                } else if (position == 6) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm7);
                    mp.start();
                } else if (position == 7) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm8);
                    mp.start();
                } else if (position == 8) {
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm9);
                    mp.start();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        /* ========== WYBIERANIE GODZINY ALARMU ============ */

        godzina.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddVisit.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, godzinaListener, hour, minutes, true);
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


        /* ========== WYBIERANIE DATY ALARMU ============ */


        data.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(AddVisit.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, dataListener, year, month - 1, day);
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

        if(cxz.getCount() != 0) {
            while(cxz.moveToNext()) {
                label.add(cxz.getString(0));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, label);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        }
    }

    private void loadSpinnerDoctor() {

        list.clear();

        Cursor cxz = myDb.getAllData_DOKTORZY();

        if(cxz.getCount() != 0) {
            while(cxz.moveToNext()) {
                list.add(new StringWithTag(cxz.getString(1), cxz.getString(0)));
            }
        }

        list.add(new StringWithTag("Dodaj nowego lekarza", "D"));
        list.add(new StringWithTag("Wybierz lekarza", "W"));

        labelSize = list.size() - 1;

        ArrayAdapter<StringWithTag> adap = new ArrayAdapter<StringWithTag> (this, android.R.layout.simple_spinner_item, list) {
            @Override
            public int getCount() {
                return(labelSize);
            }
        };

        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctor.setAdapter(adap);
        if(labelSize!=labelSizeCopy) spinnerDoctor.setSelection(labelSize-2);
        else spinnerDoctor.setSelection(labelSize);

    }

    private void loadSpinnerDzwiek() {

        labelDzwiek.add("Alarm nr 1");
        labelDzwiek.add("Alarm nr 2");
        labelDzwiek.add("Alarm nr 3");
        labelDzwiek.add("Alarm nr 4");
        labelDzwiek.add("Alarm nr 5");
        labelDzwiek.add("Alarm nr 6");
        labelDzwiek.add("Alarm nr 7");
        labelDzwiek.add("Alarm nr 8");
        labelDzwiek.add("Alarm nr 9");
        labelDzwiek.add("Domyślny");

        final int labelSize = labelDzwiek.size() - 1;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labelDzwiek) {
            @Override
            public int getCount() {
                return(labelSize);
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDzwiek.setAdapter(dataAdapter);
        spinnerDzwiek.setSelection(labelSize);

    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddDoctor");
    }

    @Override
    public void onBackPressed() {
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
    };

}
