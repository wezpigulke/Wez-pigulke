package com.wezpigulke.notification;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.other.DecimalDigitsInputFilter;
import com.wezpigulke.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RepeatingActivityReminder extends Activity {

    private Cursor cursor;
    private Button yes;
    private Button no;
    private DatabaseHelper myDb;
    private Button update;
    private Integer id_h;
    private String obecnyCzas;
    private String ilosc;
    private Integer id;
    private String nazwaLeku;
    private double jakaDawka;
    private int iloscDni;
    private String uzytkownik;
    private String data;
    private String godzina;
    private int coPokazac;

    private void initializeVariables() {

        myDb = new DatabaseHelper(this);
        coPokazac = getIntent().getIntExtra("coPokazac", -1);
        godzina = getIntent().getStringExtra("godzina");
        data = getIntent().getStringExtra("data");
        uzytkownik = getIntent().getStringExtra("uzytkownik");
        nazwaLeku = getIntent().getStringExtra("nazwaLeku");
        jakaDawka = getIntent().getDoubleExtra("jakaDawka", 0);
        iloscDni = getIntent().getIntExtra("iloscDni", 0);
        id = getIntent().getIntExtra("id", 0);
        id_h = getIntent().getIntExtra("id_h", 0);
        obecnyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

    }

    private void setTextForReminder() {

        TextView tNazwa = findViewById(R.id.textView6);
        TextView tGodzina = findViewById(R.id.textView14);
        TextView tProfil = findViewById(R.id.textView12);
        TextView tData = findViewById(R.id.textView9);
        TextView tPozostalo = findViewById(R.id.textView10);
        TextView tDawka = findViewById(R.id.textView7);

        if (Build.VERSION.SDK_INT >= 24) {
            tNazwa.setText(Html.fromHtml("Nazwa tabletki: " + "<b>" + nazwaLeku + "</b> ", 0));
            tGodzina.setText(Html.fromHtml("Godzina: " + "<b>" + godzina + "</b> ", 0));
            tProfil.setText(Html.fromHtml("Profil: " + "<b>" + uzytkownik + "</b> ", 0));
            tData.setText(Html.fromHtml("Data: " + "<b>" + data + "</b> ", 0));
            tPozostalo.setText(Html.fromHtml("Pozostało dni: " + "<b>" + iloscDni + "</b> ", 0));
            tDawka.setText(Html.fromHtml("Dawka: " + "<b>" + jakaDawka + "</b> ", 0));
        } else {
            tNazwa.setText(Html.fromHtml("Nazwa tabletki: " + "<b>" + nazwaLeku + "</b> "));
            tGodzina.setText(Html.fromHtml("Godzina: " + "<b>" + godzina + "</b> "));
            tProfil.setText(Html.fromHtml("Profil: " + "<b>" + uzytkownik + "</b> "));
            tData.setText(Html.fromHtml("Data: " + "<b>" + data + "</b> "));
            tPozostalo.setText(Html.fromHtml("Pozostało dni: " + "<b>" + iloscDni + "</b> "));
            tDawka.setText(Html.fromHtml("Dawka: " + "<b>" + jakaDawka + "</b> "));
        }

    }

    private void setTextForUpdateQuantity() {

        String nazwa = getIntent().getStringExtra("nazwa");
        String sumujTypy = getIntent().getStringExtra("sumujTypy");

        cursor = myDb.getNumber_LEK(id);
        cursor.moveToFirst();
        ilosc = cursor.getString(0);

        TextView tNazwa = findViewById(R.id.textView12m);
        TextView tIlosc = findViewById(R.id.textView6m);
        TextView tSuma = findViewById(R.id.textView6m2);

        if (Build.VERSION.SDK_INT >= 24) {
            tNazwa.setText(Html.fromHtml("Nazwa leku: " + "<b>" + nazwa + "</b> ", 0));
            tIlosc.setText(Html.fromHtml("Ilość tabletek: " + "<b>" + ilosc + "</b> ",0));
            tSuma.setText(Html.fromHtml("Tygodniowo zażywasz: " + "<b>" + sumujTypy + "</b> ",0));
        } else {
            tNazwa.setText(Html.fromHtml("Nazwa leku: " + "<b>" + nazwa + "</b> "));
            tIlosc.setText(Html.fromHtml("Ilość tabletek: " + "<b>" + ilosc + "</b> "));
            tSuma.setText(Html.fromHtml("Tygodniowo zażywasz: " + "<b>" + sumujTypy + "</b> "));
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initializeVariables();

        if (coPokazac == 0) {

            setContentView(R.layout.repeating_activity_layout);
            ImageView im = findViewById(R.id.imageView3);
            im.setImageResource(R.drawable.logo);

            yes = findViewById(R.id.button4);
            no = findViewById(R.id.button3);

            setTextForReminder();
            yesClickListener();
            noClickListener();


        } else if (coPokazac == 1) {

            setContentView(R.layout.medicine_activity_layout);
            update = findViewById(R.id.button5m);

            ImageView im = findViewById(R.id.imageView6);
            im.setImageResource(R.drawable.logo);

            setTextForUpdateQuantity();
            updateClickListener();

        }

    }

    private void updateClickListener() {

        update.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
            builder.setTitle("Aktualizacja ilości");

            final EditText input = new EditText(this);

            input.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

            input.setText(ilosc);
            input.setSelection(input.getText().length());
            input.setGravity(Gravity.CENTER_HORIZONTAL);

            builder.setView(input);

            builder.setNegativeButton("Anuluj", (dialog, which) -> {
                dialog.cancel();
                finish();
                goHome();
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                myDb.update_LEK(id, Double.valueOf(input.getText().toString()));
                finish();
                goHome();
            });
            builder.show();

        });

    }

    private void yesClickListener() {

        yes.setOnClickListener(v -> {

            cursor = myDb.getWziete_STATYSTYKI(0);
            cursor.moveToFirst();
            myDb.update_STATYSTYKI_WZIETE(0, Integer.parseInt(cursor.getString(0)) + 1);

            myDb.update_HISTORIA(id_h, obecnyCzas, "WZIETE");

            finish();
            goHome();

        });

    }

    private void noClickListener() {

        no.setOnClickListener(v -> {

            cursor = myDb.getNiewziete_STATYSTYKI(0);
            cursor.moveToFirst();
            myDb.update_STATYSTYKI_NIEWZIETE(0, Integer.parseInt(cursor.getString(0)) + 1);

            cursor = myDb.getDataName_LEK(nazwaLeku);
            cursor.moveToFirst();
            double iloscLeku = cursor.getDouble(2) + jakaDawka;
            myDb.update_LEK(Integer.parseInt(cursor.getString(0)), iloscLeku);

            myDb.update_HISTORIA(id_h, obecnyCzas, "NIEWZIETE");

            finish();
            goHome();

        });

    }

    public void goHome() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
