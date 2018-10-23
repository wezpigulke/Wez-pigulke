package com.example.kuba.dsadsax;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.Calendar.*;


public class AddReminder2 extends AppCompatActivity {

    private static final String TAG = "AddReminder";

    DatabaseHelper myDb;

    private TextView dataTabletka;
    private TextView godzinaTabletka;
    private String nazwaLeku;
    private EditText ileDni;
    private EditText edt;
    private DatePickerDialog.OnDateSetListener dataTabletkaListener;
    private Button dodaj;
    private TimePickerDialog.OnTimeSetListener godzinaTabletkaListener;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private Spinner spinnerReminder;
    private String uzytkownik;
    private Spinner dawka;
    private ArrayList<String> labelReminder;
    private ArrayList<String> labelCoIleGodzin;
    private String jakaDawka;
    private Integer iloscDni;
    private Spinner spinnerCoIleGodzin;
    private Integer coWybrane;
    private Integer ileRazyDziennie;
    private Integer coIleDni;

    private TextView setTime1;
    private TimePickerDialog.OnTimeSetListener setTime1Listener;
    private TextView setTime2;
    private TimePickerDialog.OnTimeSetListener setTime2Listener;
    private TextView setTime3;
    private TimePickerDialog.OnTimeSetListener setTime3Listener;
    private TextView setTime4;
    private TimePickerDialog.OnTimeSetListener setTime4Listener;
    private TextView setTime5;
    private TimePickerDialog.OnTimeSetListener setTime5Listener;
    private TextView setTime6;
    private TimePickerDialog.OnTimeSetListener setTime6Listener;
    private TextView setTime7;
    private TimePickerDialog.OnTimeSetListener setTime7Listener;
    private TextView setTime8;
    private TimePickerDialog.OnTimeSetListener setTime8Listener;
    private TextView setTime9;
    private TimePickerDialog.OnTimeSetListener setTime9Listener;
    private TextView setTime10;
    private TimePickerDialog.OnTimeSetListener setTime10Listener;
    private TextView setTime11;
    private TimePickerDialog.OnTimeSetListener setTime11Listener;
    private TextView setTime12;
    private TimePickerDialog.OnTimeSetListener setTime12Listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        labelReminder = new ArrayList<String>();
        labelCoIleGodzin = new ArrayList<String>();

        coWybrane=0;
        ileRazyDziennie =2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDb = new DatabaseHelper(this);

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

        dataTabletka = (TextView) findViewById(R.id.dateVisit);
        godzinaTabletka = (TextView) findViewById(R.id.timeMedicine);
        ileDni = (EditText) findViewById(R.id.editText2);

        dodaj = (Button) findViewById(R.id.dodajButton);

        edt = (EditText) findViewById(R.id.edt);
        edt.setVisibility(View.GONE);

        spinnerReminder = findViewById(R.id.spinnerJakCzesto);

        uzytkownik = getIntent().getStringExtra("uzytkownik");
        nazwaLeku = getIntent().getStringExtra("nazwaLeku");
        jakaDawka = getIntent().getStringExtra("jakaDawka");

        setTime1 = (TextView) findViewById(R.id.setTime1);
        setTime2 = (TextView) findViewById(R.id.setTime2);
        setTime3 = (TextView) findViewById(R.id.setTime3);
        setTime4 = (TextView) findViewById(R.id.setTime4);
        setTime5 = (TextView) findViewById(R.id.setTime5);
        setTime6 = (TextView) findViewById(R.id.setTime6);
        setTime7 = (TextView) findViewById(R.id.setTime7);
        setTime8 = (TextView) findViewById(R.id.setTime8);
        setTime9 = (TextView) findViewById(R.id.setTime9);
        setTime10 = (TextView) findViewById(R.id.setTime10);
        setTime11 = (TextView) findViewById(R.id.setTime11);
        setTime12 = (TextView) findViewById(R.id.setTime12);

        dataTabletka.setText(date);
        godzinaTabletka.setText(time);

        //startTime = (TextView) findViewById(R.id.startTime);
        //endTime = (TextView) findViewById(R.id.endTime);
        spinnerCoIleGodzin = (Spinner) findViewById(R.id.spinnerCoIleGodzin);

        setTime1.setText("08:00");
        setTime2.setText("09:00");
        setTime3.setText("10:00");
        setTime4.setText("11:00");
        setTime5.setText("12:00");
        setTime6.setText("13:00");
        setTime7.setText("14:00");
        setTime8.setText("15:00");
        setTime9.setText("16:00");
        setTime10.setText("17:00");
        setTime11.setText("18:00");
        setTime12.setText("19:00");

        loadSpinnerReminder();
        loadSpinnerCoIleGodzin();

        spinnerCoIleGodzin.setVisibility(View.GONE);
        setTime1.setVisibility(View.GONE);
        setTime2.setVisibility(View.GONE);
        setTime3.setVisibility(View.GONE);
        setTime4.setVisibility(View.GONE);
        setTime5.setVisibility(View.GONE);
        setTime6.setVisibility(View.GONE);
        setTime7.setVisibility(View.GONE);
        setTime8.setVisibility(View.GONE);
        setTime9.setVisibility(View.GONE);
        setTime10.setVisibility(View.GONE);
        setTime11.setVisibility(View.GONE);
        setTime12.setVisibility(View.GONE);

        spinnerCoIleGodzin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String test = spinnerCoIleGodzin.getItemAtPosition(position).toString();
                String[] bits = test.split(" ");
                String lastWord = bits[bits.length - 2];

                setTime1.setVisibility(View.GONE);
                setTime2.setVisibility(View.GONE);
                setTime3.setVisibility(View.GONE);
                setTime4.setVisibility(View.GONE);
                setTime5.setVisibility(View.GONE);
                setTime6.setVisibility(View.GONE);
                setTime7.setVisibility(View.GONE);
                setTime8.setVisibility(View.GONE);
                setTime9.setVisibility(View.GONE);
                setTime10.setVisibility(View.GONE);
                setTime11.setVisibility(View.GONE);
                setTime12.setVisibility(View.GONE);

                if(position==0) {
                    ileRazyDziennie =2;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                }
                else if(position==1) {
                    ileRazyDziennie =3;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    setTime3.setVisibility(View.VISIBLE);
                }
                else if(position==2) {
                    ileRazyDziennie =4;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    setTime3.setVisibility(View.VISIBLE);
                    setTime4.setVisibility(View.VISIBLE);
                }
                else if(position==3) {
                    ileRazyDziennie =5;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    setTime3.setVisibility(View.VISIBLE);
                    setTime4.setVisibility(View.VISIBLE);
                    setTime5.setVisibility(View.VISIBLE);
                }
                else if(position==4) {
                    ileRazyDziennie =6;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    setTime3.setVisibility(View.VISIBLE);
                    setTime4.setVisibility(View.VISIBLE);
                    setTime5.setVisibility(View.VISIBLE);
                    setTime6.setVisibility(View.VISIBLE);
                }
                else if(position==5) {
                    ileRazyDziennie =7;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    setTime3.setVisibility(View.VISIBLE);
                    setTime4.setVisibility(View.VISIBLE);
                    setTime5.setVisibility(View.VISIBLE);
                    setTime6.setVisibility(View.VISIBLE);
                    setTime7.setVisibility(View.VISIBLE);
                }
                else if(position==6) {
                    ileRazyDziennie =8;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    setTime3.setVisibility(View.VISIBLE);
                    setTime4.setVisibility(View.VISIBLE);
                    setTime5.setVisibility(View.VISIBLE);
                    setTime6.setVisibility(View.VISIBLE);
                    setTime7.setVisibility(View.VISIBLE);
                    setTime8.setVisibility(View.VISIBLE);
                }
                else if(position==7) {
                    ileRazyDziennie =9;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    setTime3.setVisibility(View.VISIBLE);
                    setTime4.setVisibility(View.VISIBLE);
                    setTime5.setVisibility(View.VISIBLE);
                    setTime6.setVisibility(View.VISIBLE);
                    setTime7.setVisibility(View.VISIBLE);
                    setTime8.setVisibility(View.VISIBLE);
                    setTime9.setVisibility(View.VISIBLE);
                }
                else if(position==8) {
                    ileRazyDziennie =10;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    setTime3.setVisibility(View.VISIBLE);
                    setTime4.setVisibility(View.VISIBLE);
                    setTime5.setVisibility(View.VISIBLE);
                    setTime6.setVisibility(View.VISIBLE);
                    setTime7.setVisibility(View.VISIBLE);
                    setTime8.setVisibility(View.VISIBLE);
                    setTime9.setVisibility(View.VISIBLE);
                    setTime10.setVisibility(View.VISIBLE);
                }
                else if(position==9) {
                    ileRazyDziennie =11;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    setTime3.setVisibility(View.VISIBLE);
                    setTime4.setVisibility(View.VISIBLE);
                    setTime5.setVisibility(View.VISIBLE);
                    setTime6.setVisibility(View.VISIBLE);
                    setTime7.setVisibility(View.VISIBLE);
                    setTime8.setVisibility(View.VISIBLE);
                    setTime9.setVisibility(View.VISIBLE);
                    setTime10.setVisibility(View.VISIBLE);
                    setTime11.setVisibility(View.VISIBLE);
                }
                else if(position==10) {
                    ileRazyDziennie =12;
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    setTime3.setVisibility(View.VISIBLE);
                    setTime4.setVisibility(View.VISIBLE);
                    setTime5.setVisibility(View.VISIBLE);
                    setTime6.setVisibility(View.VISIBLE);
                    setTime7.setVisibility(View.VISIBLE);
                    setTime8.setVisibility(View.VISIBLE);
                    setTime9.setVisibility(View.VISIBLE);
                    setTime10.setVisibility(View.VISIBLE);
                    setTime11.setVisibility(View.VISIBLE);
                    setTime12.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        spinnerReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                dataTabletka.setVisibility(View.GONE);
                godzinaTabletka.setVisibility(View.GONE);
                ileDni.setVisibility(View.GONE);
                spinnerCoIleGodzin.setVisibility(View.GONE);
                edt.setVisibility(View.GONE);
                setTime1.setVisibility(View.GONE);
                setTime2.setVisibility(View.GONE);
                setTime3.setVisibility(View.GONE);
                setTime4.setVisibility(View.GONE);
                setTime5.setVisibility(View.GONE);
                setTime6.setVisibility(View.GONE);
                setTime7.setVisibility(View.GONE);
                setTime8.setVisibility(View.GONE);
                setTime9.setVisibility(View.GONE);
                setTime10.setVisibility(View.GONE);
                setTime11.setVisibility(View.GONE);
                setTime12.setVisibility(View.GONE);

                if(position==0) {
                    coWybrane=0;
                    dataTabletka.setVisibility(View.VISIBLE);
                    godzinaTabletka.setVisibility(View.VISIBLE);
                    ileDni.setVisibility(View.VISIBLE);
                }
                else if(position==1) {
                    coWybrane=1;
                    edt.setVisibility(View.GONE);
                    spinnerCoIleGodzin.setVisibility(View.VISIBLE);
                    setTime1.setVisibility(View.VISIBLE);
                    setTime2.setVisibility(View.VISIBLE);
                    ileDni.setVisibility(View.VISIBLE);
                    dataTabletka.setVisibility(View.VISIBLE);
                }
                else if(position==2) {
                    coWybrane=2;
                    edt.setVisibility(View.VISIBLE);
                    dataTabletka.setVisibility(View.VISIBLE);
                    godzinaTabletka.setVisibility(View.VISIBLE);
                    ileDni.setVisibility(View.VISIBLE);
                    spinnerCoIleGodzin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


        /* ========== KLIKNIĘCIE PRZYCISKU ============ */

        dodaj.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                if (ileDni.getText().length() > 0 &&
                        !ileDni.getText().toString().contains(".") &&
                        !ileDni.getText().toString().contains("-") &&
                        !ileDni.getText().toString().contains("*") &&
                        !ileDni.getText().toString().contains("#")) {

                    if(coWybrane==0 || coWybrane==2){

                        iloscDni = Integer.parseInt(ileDni.getText().toString());

                        Calendar cal = getInstance();
                        cal.set(year, month - 1, day, hour, minutes, 0);
                        String dataPrzypomnienia = dataTabletka.getText().toString();
                        String godzinaPrzypomnienia = godzinaTabletka.getText().toString();

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
                            secondDate = sdf.parse(dataPrzypomnienia);
                            secondTime = tdf.parse(godzinaPrzypomnienia);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff = secondDate.getTime() - firstDate.getTime();
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        long diffInMillis = secondTime.getTime() - firstTime.getTime();

                        if (diffDays == 0 && diffInMillis < 0) {
                            Calendar cx = Calendar.getInstance();
                            try {
                                cx.setTime(sdf.parse(dataPrzypomnienia));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            cx.add(Calendar.DATE, 1);
                            dataPrzypomnienia = sdf.format(cx.getTime());
                            cal.set(year, month - 1, day + 1, hour, minutes, 0);
                            dataTabletka.getText().toString();
                            iloscDni--;
                        }


                        coIleDni =1;

                        if(edt.getVisibility() == View.VISIBLE) {
                            if(edt.getTextSize()!=0) coIleDni =Integer.parseInt(edt.getText().toString());
                        }

                        Integer id = null, idd = null;

                        Cursor c = myDb.getMAXid_NOTYFIKACJA();

                        if (c.getCount() != 0) {
                            while(c.moveToNext()) {
                                id = Integer.parseInt(c.getString(0)) + 1;
                            }
                        }

                        if(coWybrane==0) {
                            myDb.insert_PRZYPOMNIENIE(
                                    godzinaTabletka.getText().toString(),
                                    dataPrzypomnienia,
                                    nazwaLeku,
                                    jakaDawka,
                                    iloscDni,
                                    uzytkownik,
                                    1,
                                    "Codziennie: " + godzinaTabletka.getText().toString()
                            );

                            Cursor cc = myDb.getMAXid_PRZYPOMNIENIE();
                            cc.moveToFirst();
                            idd = Integer.parseInt(cc.getString(0));

                            String dt = dataPrzypomnienia;

                            for(int i=iloscDni; i>0; i--) {

                                Calendar cxx = Calendar.getInstance();
                                try {
                                    cxx.setTime(sdf.parse(dt));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(i != iloscDni) cxx.add(Calendar.DATE, 1);

                                dt = sdf.format(cxx.getTime());

                                myDb.insert_NOTYFIKACJA(
                                        id,
                                        idd,
                                        godzinaTabletka.getText().toString(),
                                        dt
                                );
                            }

                        }
                        else if(coWybrane==2) {

                            myDb.insert_PRZYPOMNIENIE(
                                    godzinaTabletka.getText().toString(),
                                    dataPrzypomnienia,
                                    nazwaLeku,
                                    jakaDawka,
                                    iloscDni,
                                    uzytkownik,
                                    coIleDni,
                                    "Co " + String.valueOf(coIleDni) + " dni: " + godzinaTabletka.getText().toString()
                            );


                            Cursor czz = myDb.getMAXid_NOTYFIKACJA();

                            if (czz.getCount() != 0) {
                                while(czz.moveToNext()) {
                                    id = Integer.parseInt(czz.getString(0)) + 1;
                                }
                            }

                            Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();
                            cx.moveToFirst();
                            idd = Integer.parseInt(cx.getString(0));

                            String dt = dataPrzypomnienia;


                            for(int i = (int) Math.round(iloscDni/coIleDni); i>=0; i--) {

                                Calendar cxx = Calendar.getInstance();
                                try {
                                    cxx.setTime(sdf.parse(dt));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(i != (int) Math.round(iloscDni/coIleDni)) cxx.add(Calendar.DATE, coIleDni);

                                dt = sdf.format(cxx.getTime());

                                myDb.insert_NOTYFIKACJA(
                                        id,
                                        idd,
                                        godzinaTabletka.getText().toString(),
                                        dt
                                );
                            }

                        }

                        Intent intx = new Intent(getApplicationContext(), NotificationReceiver.class);
                        intx.putExtra("Value", uzytkownik + " " + godzinaTabletka.getText().toString() + "  |  już czas, aby wziąć: " + nazwaLeku + " (" + jakaDawka + ")");
                        intx.putExtra("id", id);
                        intx.putExtra("idd", idd);
                        intx.putExtra("godzina", godzinaTabletka.getText().toString());
                        intx.putExtra("data", dataPrzypomnienia);
                        intx.putExtra("uzytkownik", uzytkownik);
                        intx.putExtra("nazwaLeku", nazwaLeku);
                        intx.putExtra("jakaDawka", jakaDawka);
                        intx.putExtra("iloscDni", iloscDni);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * coIleDni, pendingIntent);

                        myDb.insert_DODANE_PRZ(id);

                        // TO ZROBIĆ

                        GoToReminder goToReminder = new GoToReminder();
                        getSupportFragmentManager().beginTransaction().add(R.id.container, goToReminder).commit();

                    }


                    else if(coWybrane==1){

                        for(Integer i = 1; i<= ileRazyDziennie; i++) {

                            String godzinaPrzypomnienia = null;
                            String wszystkieGodziny = null;

                            if(ileRazyDziennie ==2) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString();
                            }
                            else if(ileRazyDziennie ==3) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString() + ", " +
                                        setTime3.getText().toString();
                            }
                            else if(ileRazyDziennie ==4) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString() + ", " +
                                        setTime3.getText().toString() + ", " +
                                        setTime4.getText().toString();
                            }
                            else if(ileRazyDziennie ==5) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString() + ", " +
                                        setTime3.getText().toString() + ", " +
                                        setTime4.getText().toString() + ", " +
                                        setTime5.getText().toString();
                            }
                            else if(ileRazyDziennie ==6) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString() + ", " +
                                        setTime3.getText().toString() + ", " +
                                        setTime4.getText().toString() + ", " +
                                        setTime5.getText().toString() + ", " +
                                        setTime6.getText().toString();
                            }
                            else if(ileRazyDziennie ==7) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString() + ", " +
                                        setTime3.getText().toString() + ", " +
                                        setTime4.getText().toString() + ", " +
                                        setTime5.getText().toString() + ", " +
                                        setTime6.getText().toString() + ", " +
                                        setTime7.getText().toString();
                            }
                            else if(ileRazyDziennie ==8) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString() + ", " +
                                        setTime3.getText().toString() + ", " +
                                        setTime4.getText().toString() + ", " +
                                        setTime5.getText().toString() + ", " +
                                        setTime6.getText().toString() + ", " +
                                        setTime7.getText().toString() + ", " +
                                        setTime8.getText().toString();
                            }
                            else if(ileRazyDziennie ==9) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString() + ", " +
                                        setTime3.getText().toString() + ", " +
                                        setTime4.getText().toString() + ", " +
                                        setTime5.getText().toString() + ", " +
                                        setTime6.getText().toString() + ", " +
                                        setTime7.getText().toString() + ", " +
                                        setTime8.getText().toString() + ", " +
                                        setTime9.getText().toString();
                            }
                            else if(ileRazyDziennie ==10) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString() + ", " +
                                        setTime3.getText().toString() + ", " +
                                        setTime4.getText().toString() + ", " +
                                        setTime5.getText().toString() + ", " +
                                        setTime6.getText().toString() + ", " +
                                        setTime7.getText().toString() + ", " +
                                        setTime8.getText().toString() + ", " +
                                        setTime9.getText().toString() + ", " +
                                        setTime10.getText().toString();
                            }
                            else if(ileRazyDziennie ==11) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString() + ", " +
                                        setTime3.getText().toString() + ", " +
                                        setTime4.getText().toString() + ", " +
                                        setTime5.getText().toString() + ", " +
                                        setTime6.getText().toString() + ", " +
                                        setTime7.getText().toString() + ", " +
                                        setTime8.getText().toString() + ", " +
                                        setTime9.getText().toString() + ", " +
                                        setTime10.getText().toString() + ", " +
                                        setTime11.getText().toString();
                            }
                            else if(ileRazyDziennie ==12) {
                                wszystkieGodziny = setTime1.getText().toString() + ", " +
                                        setTime2.getText().toString() + ", " +
                                        setTime3.getText().toString() + ", " +
                                        setTime4.getText().toString() + ", " +
                                        setTime5.getText().toString() + ", " +
                                        setTime6.getText().toString() + ", " +
                                        setTime7.getText().toString() + ", " +
                                        setTime8.getText().toString() + ", " +
                                        setTime9.getText().toString() + ", " +
                                        setTime10.getText().toString() + ", " +
                                        setTime11.getText().toString() + ", " +
                                        setTime12.getText().toString();
                            }

                            if(i==1) godzinaPrzypomnienia = setTime1.getText().toString();
                            else if(i==2) godzinaPrzypomnienia = setTime2.getText().toString();
                            else if(i==3) godzinaPrzypomnienia = setTime3.getText().toString();
                            else if(i==4) godzinaPrzypomnienia = setTime4.getText().toString();
                            else if(i==5) godzinaPrzypomnienia = setTime5.getText().toString();
                            else if(i==6) godzinaPrzypomnienia = setTime6.getText().toString();
                            else if(i==7) godzinaPrzypomnienia = setTime7.getText().toString();
                            else if(i==8) godzinaPrzypomnienia = setTime8.getText().toString();
                            else if(i==9) godzinaPrzypomnienia = setTime9.getText().toString();
                            else if(i==10) godzinaPrzypomnienia = setTime10.getText().toString();
                            else if(i==11) godzinaPrzypomnienia = setTime11.getText().toString();
                            else if(i==12) godzinaPrzypomnienia = setTime12.getText().toString();

                            iloscDni = Integer.parseInt(ileDni.getText().toString());
                            String dataPrzypomnienia = dataTabletka.getText().toString();

                            String czyDwucyfrowa = String.valueOf(godzinaPrzypomnienia.charAt(1));

                            if(czyDwucyfrowa.equals(":")) {
                                hour = Integer.parseInt(String.valueOf(godzinaPrzypomnienia.charAt(0)));
                                minutes = Integer.parseInt(String.valueOf(godzinaPrzypomnienia.charAt(2))
                                        + String.valueOf(godzinaPrzypomnienia.charAt(3)));
                            } else {
                                hour = Integer.parseInt(String.valueOf(godzinaPrzypomnienia.charAt(0))
                                        + String.valueOf(godzinaPrzypomnienia.charAt(1)));
                                minutes = Integer.parseInt(String.valueOf(godzinaPrzypomnienia.charAt(3))
                                        + String.valueOf(godzinaPrzypomnienia.charAt(4)));
                            }

                            Calendar cal = getInstance();
                            cal.set(year, month - 1, day, hour, minutes, 0);

                            Toast.makeText(getApplicationContext(), String.valueOf(hour + " " + minutes), Toast.LENGTH_LONG).show();

                            String dzisiejszaData = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                            Date firstDate = null;
                            Date secondDate = null;

                            try {
                                firstDate = sdf.parse(dzisiejszaData);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try {
                                secondDate = sdf.parse(dataPrzypomnienia);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long diff = secondDate.getTime() - firstDate.getTime();

                            String[] wszystkie = wszystkieGodziny.replaceAll(" ", "").split(",");
                            String najwyzszaGodzina = Stream.of(wszystkie).max(String::compareTo).get();

                            if(i==1) {
                                myDb.insert_PRZYPOMNIENIE(
                                        najwyzszaGodzina,
                                        dataPrzypomnienia,
                                        nazwaLeku,
                                        jakaDawka,
                                        iloscDni,
                                        uzytkownik,
                                        1,
                                        String.valueOf(ileRazyDziennie) + " razy dziennie: " + wszystkieGodziny
                                );

                            }

                            Integer id = -1;
                            Integer idd = -1;


                            Cursor c = myDb.getMAXid_NOTYFIKACJA();

                            if (c.getCount() != 0) {
                                while(c.moveToNext()) {
                                    id = Integer.parseInt(c.getString(0)) + 1;
                                }
                            }

                            Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();

                            if (cx.getCount() != 0) {
                                while(cx.moveToNext()) {
                                    idd = Integer.parseInt(cx.getString(0));
                                }
                            }

                            String dt = dataPrzypomnienia;

                            for(Integer j = 1; j <= iloscDni; j++) {

                                Calendar cxx = Calendar.getInstance();
                                try {
                                    cxx.setTime(sdf.parse(dt));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(j != 1) cxx.add(Calendar.DATE, 1);

                                dt = sdf.format(cxx.getTime());

                                myDb.insert_NOTYFIKACJA(
                                        id,
                                        idd,
                                        godzinaPrzypomnienia,
                                        dt
                                );
                            }

                            Intent intx = new Intent(getApplicationContext(), NotificationReceiver.class);
                            intx.putExtra("Value", uzytkownik + " " + godzinaPrzypomnienia + "  |  już czas, aby wziąć: " + nazwaLeku + " (" + jakaDawka + ")");
                            intx.putExtra("id", id);
                            intx.putExtra("idd", idd);
                            intx.putExtra("godzina", godzinaPrzypomnienia);
                            intx.putExtra("data", dataPrzypomnienia);
                            intx.putExtra("uzytkownik", uzytkownik);
                            intx.putExtra("nazwaLeku", nazwaLeku);
                            intx.putExtra("jakaDawka", jakaDawka);
                            intx.putExtra("iloscDni", iloscDni);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                            myDb.insert_DODANE_PRZ(id);

                            Intent intent = new Intent(view.getContext(), GoToReminder.class);
                            startActivity(intent);

                        }
                    }
                }
                else if(ileDni.getText().toString().contains(".") ||
                        ileDni.getText().toString().contains("-") ||
                        ileDni.getText().toString().contains("*") ||
                        ileDni.getText().toString().contains("#") ||
                        ileDni.getText().length()==0) openDialog("Wpisz prawidłową ilość dni");
            }
        });


        /* ================================================= */

        setTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime1Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime1Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime1.setText(time);
            }
        };

        setTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime2Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime2Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime2.setText(time);
            }
        };

        setTime3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime3Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime3Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime3.setText(time);
            }
        };

        setTime4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime4Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime4Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime4.setText(time);
            }
        };

        setTime5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime5Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime5Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime5.setText(time);
            }
        };

        setTime6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime6Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime6Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime6.setText(time);
            }
        };

        setTime7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime7Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime7Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime7.setText(time);
            }
        };

        setTime8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime8Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime8Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime8.setText(time);
            }
        };

        setTime9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime9Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime9Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime9.setText(time);
            }
        };

        setTime10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime10Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime10Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime10.setText(time);
            }
        };

        setTime11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime11Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime11Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime11.setText(time);
            }
        };

        setTime12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime12Listener, hour, minutes, true);
                dialog.show();
            }
        });

        setTime12Listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;
                setTime12.setText(time);
            }
        };

        /* ========== WYBIERANIE GODZINY ALARMU ============ */

        godzinaTabletka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, godzinaTabletkaListener, hour, minutes, true);
                dialog.show();
            }
        });

        godzinaTabletkaListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time;
                if (minute < 10) time = hourOfDay + ":0" + minute;
                else time = hourOfDay + ":" + minute;

                hour = hourOfDay;
                minutes = minute;

                godzinaTabletka.setText(time);
            }
        };


        /* ========== WYBIERANIE DATY ALARMU ============ */


        dataTabletka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AddReminder2.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, dataTabletkaListener, year, month - 1, day);
                dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                dialog.show();
            }
        });

        dataTabletkaListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yeear, int moonth, int dayOfMonth) {
                moonth = moonth + 1;
                String date;
                if (dayOfMonth < 10 && moonth < 10) date = "0" + dayOfMonth + "/0" + moonth + "/" + yeear;
                else if (dayOfMonth < 10) date = "0" + dayOfMonth + "/" + moonth + "/" + yeear;
                else if (moonth < 10) date = dayOfMonth + "/0" + moonth + "/" + yeear;
                else date = dayOfMonth + "/" + moonth + "/" + yeear;

                year = yeear;
                month = moonth;
                day = dayOfMonth;

                dataTabletka.setText(date);
            }
        };
    }

    private void loadSpinnerCoIleGodzin() {

        labelCoIleGodzin.add("2 razy dziennie");
        labelCoIleGodzin.add("3 razy dziennie");
        labelCoIleGodzin.add("4 razy dziennie");
        labelCoIleGodzin.add("5 razy dziennie");
        labelCoIleGodzin.add("6 razy dziennie");
        labelCoIleGodzin.add("7 razy dziennie");
        labelCoIleGodzin.add("8 razy dziennie");
        labelCoIleGodzin.add("9 razy dziennie");
        labelCoIleGodzin.add("10 razy dziennie");
        labelCoIleGodzin.add("11 razy dziennie");
        labelCoIleGodzin.add("12 razy dziennie");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelCoIleGodzin);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoIleGodzin.setAdapter(dataAdapter);

    }

    private void loadSpinnerReminder() {

        labelReminder.add("Codziennie");
        labelReminder.add("X razy dziennie");
        labelReminder.add("Co X dni");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelReminder);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReminder.setAdapter(dataAdapter);

    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddReminder2");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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