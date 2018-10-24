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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.util.Calendar.*;

public class AddVisit extends AppCompatActivity {

    DatabaseHelper myDb;
    private static final String TAG = "AddVisit";

    private TextView data;
    private TextView godzina;
    private EditText name;
    private EditText surname;
    private EditText specialization;
    private EditText address;
    private DatePickerDialog.OnDateSetListener dataListener;
    private TimePickerDialog.OnTimeSetListener godzinaListener;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;

    private Spinner spinner;
    private ArrayList<String> label;
    private String uzytkownik;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        label = new ArrayList<>();

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

        Button add = findViewById(R.id.dodajVisit);
        data = findViewById(R.id.dateVisit);
        godzina = findViewById(R.id.timeVisit);
        name = findViewById(R.id.specializationVisit);
        surname = findViewById(R.id.surnameVisit);
        specialization = findViewById(R.id.nameVisit);
        address = findViewById(R.id.addressVisit);
        final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        data.setText(date);
        godzina.setText(time);

        spinner = findViewById(R.id.spinner2);

        Cursor res = myDb.getName_UZYTKOWNICY();

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

        add.setOnClickListener(v -> {

            if (name.getText().length() > 0 && surname.getText().length() > 0 && specialization.getText().length() > 0 && address.getText().length() > 0) {

                myDb.insert_WIZYTY(
                        godzina.getText().toString(),
                        data.getText().toString(),
                        name.getText().toString(),
                        surname.getText().toString(),
                        specialization.getText().toString(),
                        address.getText().toString(),
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
                    intx = new Intent(getApplicationContext(), NotificationReceiver.class);
                    intx.putExtra("Value", uzytkownik + "  |  wizyta u " + c.getString(5) + " jutro o " + c.getString(1));
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (id * 2) - 1, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    assert alarmManager != null;
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }

                cal.add(Calendar.DATE, +1);
                cal.add(Calendar.HOUR, -2);

                if((diffDays==0 && diffInMillis>0) || diffDays>0) {
                    intx = new Intent(getApplicationContext(), NotificationReceiver.class);
                    intx.putExtra("Value", uzytkownik + "  |  wizyta u " + c.getString(5) + " o " + c.getString(1));
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id * 2, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    assert alarmManager != null;
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                onBackPressed();
            }
            else if(name.getText().length()==0) openDialog("Wpisz imie lekarza");
            else if(surname.getText().length()==0) openDialog("Wpisz nazwisko lekarza");
            else if(specialization.getText().length()==0) openDialog("Wpisz specjalizacje lekarza");
            else if(address.getText().length()==0) openDialog("Wpisz adres");
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
