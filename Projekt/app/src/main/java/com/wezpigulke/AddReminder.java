package com.wezpigulke;

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
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
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
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Calendar.*;

public class AddReminder extends AppCompatActivity {

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
    private Spinner spinner;
    private Spinner spinnerReminder;
    private Spinner spinnerDzwiek;
    private ArrayList<String> label;
    private String uzytkownik;
    private Spinner dawka;
    private Spinner spinnerNazwaLeku;
    private ArrayList<String> labelDawka;
    private ArrayList<String> labelReminder;
    private ArrayList<String> labelCoIleGodzin;
    private ArrayList<String> labelDzwiek;
    private ArrayList<String> labelNazwaLeku;
    ArrayList<TextView> array;
    private Double jakaDawka;
    private Integer iloscDni;
    private Spinner spinnerCoIleGodzin;
    private Integer coWybrane;
    private Integer ileRazyDziennie;
    private Integer coIleDni;
    private Button dalej;
    private Integer dzwiek;
    private MediaPlayer mp;
    private Integer czyUsunacDzien;
    private Integer labelSize;
    private Integer labelSizeCopy;
    private Boolean czyWlasnaDawka;
    private String wlasnaDawka;
    private Integer czyPetlaPoszla;

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        label = new ArrayList<>();
        labelDawka = new ArrayList<>();
        labelReminder = new ArrayList<>();
        labelCoIleGodzin = new ArrayList<>();
        labelDzwiek = new ArrayList<>();
        labelNazwaLeku = new ArrayList<>();

        coWybrane = 0;
        ileRazyDziennie = 2;
        czyPetlaPoszla = 0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        myDb = new DatabaseHelper(this);

        final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        String rok = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String miesiac = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String dzien = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        String godzina = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        String minuta = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());

        czyWlasnaDawka = false;

        year = Integer.parseInt(rok);
        month = Integer.parseInt(miesiac);
        day = Integer.parseInt(dzien);
        hour = Integer.parseInt(godzina);
        minutes = Integer.parseInt(minuta);

        dataTabletka = findViewById(R.id.dateVisit);
        godzinaTabletka = findViewById(R.id.timeMedicine);
        ileDni = findViewById(R.id.editText2);

        dodaj = findViewById(R.id.dodajButton);

        dalej = findViewById(R.id.goThen);

        edt = findViewById(R.id.edt);
        edt.setVisibility(View.GONE);

        spinner = findViewById(R.id.spinnerProfile);
        dawka = findViewById(R.id.spinnerDawka);
        spinnerReminder = findViewById(R.id.spinnerJakCzesto);
        spinnerDzwiek = findViewById(R.id.spinnerDzwiek);
        spinnerNazwaLeku = findViewById(R.id.spinnerLek);

        dzwiek = 1;
        jakaDawka = 1.00;

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

        spinnerCoIleGodzin = findViewById(R.id.spinnerCoIleGodzin);

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
        loadSpinnerCoIleGodzin();
        loadSpinnerDzwiek();
        loadSpinnerNazwaLeku();

        labelSizeCopy = labelSize;

        dataTabletka.setVisibility(View.GONE);
        godzinaTabletka.setVisibility(View.GONE);
        ileDni.setVisibility(View.GONE);
        spinnerCoIleGodzin.setVisibility(View.GONE);
        edt.setVisibility(View.GONE);

        for (int i = 0; i < 12; i++) {
            array.get(i).setVisibility(View.GONE);
        }

        spinnerReminder.setVisibility(View.GONE);
        dodaj.setVisibility(View.GONE);

        dawka.setSelection(1);

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

        spinnerCoIleGodzin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                uzytkownik = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        spinnerDzwiek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                dzwiek = position;
                stopPlaying();

                switch(position) {
                    case 0:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm1);
                        mp.start();
                        break;
                    case 1:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm2);
                        mp.start();
                        break;
                    case 2:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm3);
                        mp.start();
                        break;
                    case 3:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm4);
                        mp.start();
                        break;
                    case 4:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm5);
                        mp.start();
                        break;
                    case 5:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm6);
                        mp.start();
                        break;
                    case 6:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm7);
                        mp.start();
                        break;
                    case 7:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm8);
                        mp.start();
                        break;
                    case 8:
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm9);
                        mp.start();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        dawka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(dawka.getItemAtPosition(position).toString().equals("Własna dawka")) {

                    czyWlasnaDawka = true;

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Objects.requireNonNull(AddReminder.this), R.style.AlertDialog);
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
                            jakaDawka = Double.valueOf(temp.substring(7, temp.length()));

                        } else {
                            dawka.setSelection(1);
                            jakaDawka = 1.00;
                        }
                    });
                    builder.setNegativeButton("Anuluj", (dialog, which) -> {
                        dialog.cancel();
                    });
                    builder.show();

                    wlasnaDawka="";

                } else {
                    String temp = dawka.getItemAtPosition(position).toString();
                    jakaDawka = Double.valueOf(temp.substring(7, temp.length()));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

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
                coWybrane = 0;
                dataTabletka.setVisibility(View.VISIBLE);
                godzinaTabletka.setVisibility(View.VISIBLE);
                spinnerReminder.setVisibility(View.VISIBLE);
                dodaj.setVisibility(View.VISIBLE);

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
                        edt.setVisibility(View.GONE);
                        spinnerCoIleGodzin.setVisibility(View.VISIBLE);
                        setTime1.setVisibility(View.VISIBLE);
                        setTime2.setVisibility(View.VISIBLE);
                        ileDni.setVisibility(View.VISIBLE);
                        dataTabletka.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        coWybrane = 3;
                        edt.setVisibility(View.VISIBLE);
                        dataTabletka.setVisibility(View.VISIBLE);
                        godzinaTabletka.setVisibility(View.VISIBLE);
                        ileDni.setVisibility(View.VISIBLE);
                        spinnerCoIleGodzin.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        dodaj.setOnClickListener(view -> {

            Cursor cl = myDb.getDataName_LEK(nazwaLeku);
            cl.moveToFirst();
            Double iloscTabletek = Double.valueOf(cl.getString(2));
            Double jakaDawkaTabletki = jakaDawka;

            if ((iloscTabletek - jakaDawkaTabletki) >= 0) {

                if (coWybrane == 0) {

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
                        openDialog("Godzina dzisiejszego powiadomienia minęła, wybierz inną godzinę lub ustaw przyszłą datę");
                    }
                    else {

                        Integer id = 0;
                        Integer idd;

                        Cursor c1 = myDb.getMAXid_NOTYFIKACJA();

                        if (c1.getCount() != 0) {
                            while (c1.moveToNext()) {
                                id = Integer.parseInt(c1.getString(0)) + 1;
                            }
                        }

                        Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();
                        cx.moveToFirst();
                        idd = Integer.parseInt(cx.getString(0));
                        idd++;

                        myDb.insert_PRZYPOMNIENIE(
                                idd,
                                godzinaTabletka.getText().toString(),
                                dataPrzypomnienia,
                                nazwaLeku,
                                jakaDawka,
                                1,
                                uzytkownik,
                                1,
                                "Jednorazowo: " + godzinaTabletka.getText().toString()
                        );

                        Cursor cc = myDb.getMAXid_PRZYPOMNIENIE();
                        cc.moveToFirst();
                        idd = Integer.parseInt(cc.getString(0));

                        Integer rand_val = random();

                        myDb.insert_NOTYFIKACJA(
                                idd,
                                godzinaPrzypomnienia,
                                dataPrzypomnienia,
                                rand_val
                        );

                        Intent intx = new Intent(getApplicationContext(), NotificationReceiver.class);

                        intx.putExtra("coPokazac", 0);
                        intx.putExtra("Value", uzytkownik + " |  " + godzinaTabletka.getText().toString() + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")");
                        intx.putExtra("id", id);
                        intx.putExtra("idd", idd);
                        intx.putExtra("godzina", godzinaTabletka.getText().toString());
                        intx.putExtra("data", dataPrzypomnienia);
                        intx.putExtra("uzytkownik", uzytkownik);
                        intx.putExtra("nazwaLeku", nazwaLeku);
                        intx.putExtra("jakaDawka", jakaDawka);
                        intx.putExtra("iloscDni", 0);
                        intx.putExtra("wybranyDzwiek", dzwiek);
                        intx.putExtra("rand_val", rand_val);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), rand_val, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                        Toast.makeText(getApplicationContext(), "Dodanie: " + rand_val, Toast.LENGTH_LONG).show();

                        goBack();

                    }

                } else if (coWybrane == 1 || coWybrane == 3) {

                    iloscDni = Integer.parseInt(ileDni.getText().toString());

                    coIleDni = 1;

                    if (edt.getVisibility() == View.VISIBLE) {
                        if (edt.getTextSize() != 0)
                            coIleDni = Integer.parseInt(edt.getText().toString());
                    }

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
                        cal.set(year, month - 1, day + coIleDni, hour, minutes, 0);
                        dataTabletka.getText().toString();
                        iloscDni--;

                        openDialog("Godzina dzisiejszego powiadomienia minęła, wybierz inną godzinę lub ustaw przyszłą datę");

                    }
                    else {

                        Integer id = null, idd = null;

                        Cursor c1 = myDb.getMAXid_NOTYFIKACJA();

                        if (c1.getCount() != 0) {
                            while (c1.moveToNext()) {
                                id = Integer.parseInt(c1.getString(0)) + 1;
                            }
                        }

                        if (coWybrane == 1 && iloscDni >= 1) {

                            Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();
                            cx.moveToFirst();
                            idd = Integer.parseInt(cx.getString(0));
                            idd++;

                            myDb.insert_PRZYPOMNIENIE(
                                    idd,
                                    godzinaTabletka.getText().toString(),
                                    dataPrzypomnienia,
                                    nazwaLeku,
                                    jakaDawka,
                                    iloscDni,
                                    uzytkownik,
                                    1,
                                    "Codziennie: " + godzinaTabletka.getText().toString()
                            );

                            Integer rand_val = random();

                            myDb.insert_NOTYFIKACJA(
                                    idd,
                                    godzinaTabletka.getText().toString(),
                                    dataPrzypomnienia,
                                    rand_val
                            );

                            Intent intx = new Intent(getApplicationContext(), NotificationReceiver.class);
                            intx.putExtra("coPokazac", 0);
                            intx.putExtra("Value", uzytkownik + " |  " + godzinaTabletka.getText().toString() + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")");
                            intx.putExtra("id", id);
                            intx.putExtra("idd", idd);
                            intx.putExtra("godzina", godzinaTabletka.getText().toString());
                            intx.putExtra("data", dataPrzypomnienia);
                            intx.putExtra("uzytkownik", uzytkownik);
                            intx.putExtra("nazwaLeku", nazwaLeku);
                            intx.putExtra("jakaDawka", jakaDawka);
                            intx.putExtra("iloscDni", iloscDni - 1);
                            intx.putExtra("wybranyDzwiek", dzwiek);
                            intx.putExtra("rand_val", rand_val);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), rand_val, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * coIleDni, pendingIntent);

                            Toast.makeText(getApplicationContext(), "Dodanie: " + rand_val, Toast.LENGTH_LONG).show();

                        } else if (coWybrane == 3 && iloscDni >= 1) {


                            Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();
                            cx.moveToFirst();
                            idd = Integer.parseInt(cx.getString(0));
                            idd++;

                            myDb.insert_PRZYPOMNIENIE(
                                    idd,
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
                                while (czz.moveToNext()) {
                                    id = Integer.parseInt(czz.getString(0)) + 1;
                                }
                            }

                            Integer rand_val = random();

                            String dt = dataPrzypomnienia;
                            myDb.insert_NOTYFIKACJA(
                                    idd,
                                    godzinaPrzypomnienia,
                                    dataPrzypomnienia,
                                    rand_val
                            );

                            Intent intx = new Intent(getApplicationContext(), NotificationReceiver.class);
                            intx.putExtra("coPokazac", 0);
                            intx.putExtra("Value", uzytkownik + " |  " + godzinaTabletka.getText().toString() + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")");
                            intx.putExtra("id", id);
                            intx.putExtra("idd", idd);
                            intx.putExtra("godzina", godzinaTabletka.getText().toString());
                            intx.putExtra("data", dataPrzypomnienia);
                            intx.putExtra("uzytkownik", uzytkownik);
                            intx.putExtra("nazwaLeku", nazwaLeku);
                            intx.putExtra("jakaDawka", jakaDawka);
                            intx.putExtra("iloscDni", iloscDni - 1);
                            intx.putExtra("wybranyDzwiek", dzwiek);
                            intx.putExtra("rand_val", rand_val);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), rand_val, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * coIleDni, pendingIntent);

                            Toast.makeText(getApplicationContext(), "Dodanie: " + rand_val, Toast.LENGTH_LONG).show();

                        }

                        goBack();

                    }

                } else if (coWybrane == 2) {

                    czyPetlaPoszla = 0;

                    for (Integer i = 1; i <= ileRazyDziennie; i++) {

                        String godzinaPrzypomnienia;
                        String wszystkieGodziny;

                        StringBuilder sb = new StringBuilder();

                        for (int j = 0; j < ileRazyDziennie - 1; j++) {
                            sb.append(array.get(j).getText().toString() + ", ");
                        }

                        sb.append(array.get(ileRazyDziennie - 1).getText().toString());
                        wszystkieGodziny = sb.toString();

                        sb.setLength(0);

                        sb.append(array.get(i - 1).getText().toString());
                        godzinaPrzypomnienia = sb.toString();

                        iloscDni = Integer.parseInt(ileDni.getText().toString());

                        String czyDwucyfrowa = String.valueOf(godzinaPrzypomnienia.charAt(1));
                        Integer minutes;
                        Integer hour;

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

                        Calendar cal = getInstance();
                        cal.set(year, month - 1, day, hour, minutes, 0);
                        String dataPrzypomnienia = dataTabletka.getText().toString();

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
                            czyUsunacDzien = 1;

                        } else czyUsunacDzien = 0;

                        String[] wszystkie = wszystkieGodziny.replaceAll(" ", "").split(",");
                        String najwyzszaGodzina = Stream.of(wszystkie).max(String::compareTo).get();

                        Integer id = -1;
                        Integer idd = -1;

                        if (i.equals(ileRazyDziennie)) {
                            if (czyUsunacDzien == 1) {
                                iloscDni--;
                            }

                            Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();
                            cx.moveToFirst();
                            idd = Integer.parseInt(cx.getString(0));
                            idd++;

                            myDb.insert_PRZYPOMNIENIE(
                                    idd,
                                    najwyzszaGodzina,
                                    dataPrzypomnienia,
                                    nazwaLeku,
                                    jakaDawka,
                                    iloscDni,
                                    uzytkownik,
                                    1,
                                    String.valueOf(ileRazyDziennie) + " razy dziennie: " + wszystkieGodziny
                            );
                            czyPetlaPoszla = 1;
                        }

                        Cursor c1 = myDb.getMAXid_NOTYFIKACJA();
                        c1.moveToFirst();
                        id = Integer.parseInt(c1.getString(0)) + 1;

                        Cursor cx = myDb.getMAXid_PRZYPOMNIENIE();
                        cx.moveToFirst();
                        idd = Integer.parseInt(cx.getString(0));

                        if(czyPetlaPoszla!=1) idd++;

                        if (czyUsunacDzien != 1 && iloscDni >= 1) {

                            Integer rand_val = random();

                            myDb.insert_NOTYFIKACJA(
                                    idd,
                                    godzinaPrzypomnienia,
                                    dataPrzypomnienia,
                                    rand_val
                            );

                            Intent intx = new Intent(getApplicationContext(), NotificationReceiver.class);
                            intx.putExtra("Value", uzytkownik + " |  " + godzinaPrzypomnienia + "  |  już czas, aby wziąć: " + nazwaLeku + " (Dawka: " + jakaDawka + ")");
                            intx.putExtra("coPokazac", 0);
                            intx.putExtra("id", id);
                            intx.putExtra("idd", idd);
                            intx.putExtra("godzina", godzinaPrzypomnienia);
                            intx.putExtra("data", dataPrzypomnienia);
                            intx.putExtra("uzytkownik", uzytkownik);
                            intx.putExtra("nazwaLeku", nazwaLeku);
                            intx.putExtra("jakaDawka", jakaDawka);
                            intx.putExtra("iloscDni", iloscDni - 1);
                            intx.putExtra("wybranyDzwiek", dzwiek);
                            intx.putExtra("rand_val", rand_val);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), rand_val, intx, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                            Toast.makeText(getApplicationContext(), "Dodanie: " + rand_val, Toast.LENGTH_LONG).show();

                        }

                        goBack();

                    }
                }
            } else {

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
                    input.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setView(input);

                    builder.setPositiveButton("OK", (dialog, which) -> {
                        myDb.update_LEK(id_l, Double.valueOf(input.getText().toString()));
                    });
                    builder.setNegativeButton("Anuluj", (dialog, which) -> {
                        dialog.cancel();
                    });
                    builder.show();

                });

                builderw.setNegativeButton("Anuluj dodawanie", (dialogg, whichh) -> {
                    dialogg.cancel();
                    goBack();
                });

                builderw.show();

            }
        });

        setTime1.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime1Listener, hour, minutes, true);
            dialog.show();
        });

        setTime1Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime1.setText(Time);
        };

        setTime2.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime2Listener, hour, minutes, true);
            dialog.show();
        });

        setTime2Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime2.setText(Time);
        };

        setTime3.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime3Listener, hour, minutes, true);
            dialog.show();
        });

        setTime3Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime3.setText(Time);
        };

        setTime4.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime4Listener, hour, minutes, true);
            dialog.show();
        });

        setTime4Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime4.setText(Time);
        };

        setTime5.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime5Listener, hour, minutes, true);
            dialog.show();
        });

        setTime5Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime5.setText(Time);
        };

        setTime6.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime6Listener, hour, minutes, true);
            dialog.show();
        });

        setTime6Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime6.setText(Time);
        };

        setTime7.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime7Listener, hour, minutes, true);
            dialog.show();
        });

        setTime7Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime7.setText(Time);
        };

        setTime8.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime8Listener, hour, minutes, true);
            dialog.show();
        });

        setTime8Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime8.setText(Time);
        };

        setTime9.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime9Listener, hour, minutes, true);
            dialog.show();
        });

        setTime9Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime9.setText(Time);
        };

        setTime10.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime10Listener, hour, minutes, true);
            dialog.show();
        });

        setTime10Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime10.setText(Time);
        };

        setTime11.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime11Listener, hour, minutes, true);
            dialog.show();
        });

        setTime11Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime11.setText(Time);
        };

        setTime12.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, setTime12Listener, hour, minutes, true);
            dialog.show();
        });

        setTime12Listener = (view, hourOfDay, minute) -> {
            String Time;
            if (minute < 10) Time = hourOfDay + ":0" + minute;
            else Time = hourOfDay + ":" + minute;
            setTime12.setText(Time);
        };

        godzinaTabletka.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, godzinaTabletkaListener, hour, minutes, true);
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
            DatePickerDialog dialog = new DatePickerDialog(AddReminder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, dataTabletkaListener, year, month - 1, day);
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

    private void loadSpinnerData() {

        Cursor cxz = myDb.getAllName_UZYTKOWNICY();

        if (cxz.getCount() == 1) {
            spinner.setVisibility(View.GONE);
        } else {
            while (cxz.moveToNext()) {
                label.add(cxz.getString(0));

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, label);
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

        if (labelSize != labelSizeCopy) spinnerNazwaLeku.setSelection(labelSize - 2);
        else spinnerNazwaLeku.setSelection(labelSize);

    }

    private void loadSpinnerCoIleGodzin() {

        for (int i = 2; i <= 12; i++) {
            labelCoIleGodzin.add(String.valueOf(i) + " razy dziennie");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelCoIleGodzin);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoIleGodzin.setAdapter(dataAdapter);

    }

    private void loadSpinnerReminder() {

        labelReminder.add("Jednorazowo");
        labelReminder.add("Codziennie");
        labelReminder.add("X razy dziennie");
        labelReminder.add("Co X dni");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelReminder);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReminder.setAdapter(dataAdapter);

    }

    private void loadSpinnerDzwiek() {

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

        for (Double i = 0.5; i <= 3; i += 0.5) {
            labelDawka.add("Dawka: " + String.valueOf(i));
        }

        if(!wlasnaDawka.equals("")) {
            labelDawka.add(wlasnaDawka);
        }

        labelDawka.add("Własna dawka");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelDawka);
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
            spinnerCoIleGodzin.setVisibility(View.GONE);
            edt.setVisibility(View.GONE);

            for (int i = 0; i < 12; i++) {
                array.get(i).setVisibility(View.GONE);
            }

            spinnerReminder.setVisibility(View.GONE);
            dodaj.setVisibility(View.GONE);

            dawka.setVisibility(View.VISIBLE);
            spinnerNazwaLeku.setVisibility(View.VISIBLE);

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
}