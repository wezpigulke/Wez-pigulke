package com.wezpigulke;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RepeatingActivityReminder extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DatabaseHelper myDb = new DatabaseHelper(this);

        Integer coPokazac = getIntent().getIntExtra("coPokazac", -1);
        String godzina = getIntent().getStringExtra("godzina");
        String data = getIntent().getStringExtra("data");
        String uzytkownik = getIntent().getStringExtra("uzytkownik");
        String nazwaLeku = getIntent().getStringExtra("nazwaLeku");
        Double jakaDawka = getIntent().getDoubleExtra("jakaDawka", 0);
        Integer iloscDni = getIntent().getIntExtra("iloscDni", 0);
        Integer id = getIntent().getIntExtra("id", 0);
        Integer id_h = getIntent().getIntExtra("id_h", 0);
        String obecnyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        if (coPokazac == 0) {

            setContentView(R.layout.repeating_activity_layout);

            TextView tNazwa = findViewById(R.id.textView6);
            TextView tGodzina = findViewById(R.id.textView14);
            TextView tProfil = findViewById(R.id.textView12);
            TextView tData = findViewById(R.id.textView9);
            TextView tPozostalo = findViewById(R.id.textView10);
            TextView tDawka = findViewById(R.id.textView7);

            ImageView im = findViewById(R.id.imageView3);
            im.setImageResource(R.drawable.logo);

            Button b3 = findViewById(R.id.button3);
            Button b4 = findViewById(R.id.button4);

            tNazwa.setText(Html.fromHtml("Nazwa tabletki: " + "<b>" + nazwaLeku + "</b> "));
            tGodzina.setText(Html.fromHtml("Godzina: " + "<b>" + godzina + "</b> "));
            tProfil.setText(Html.fromHtml("Profil: " + "<b>" + uzytkownik + "</b> "));
            tData.setText(Html.fromHtml("Data: " + "<b>" + data + "</b> "));
            tPozostalo.setText(Html.fromHtml("Pozostało dni: " + "<b>" + iloscDni + "</b> "));
            tDawka.setText(Html.fromHtml("Dawka: " + "<b>" + jakaDawka + "</b> "));

            b3.setOnClickListener(v -> {

                Cursor cn = myDb.get_STATYSTYKI_NIEWZIETE(0);
                cn.moveToFirst();
                myDb.update_STATYSTYKI_NIEWZIETE(0, Integer.parseInt(cn.getString(0)) + 1);

                Cursor cl = myDb.getDataName_LEK(nazwaLeku);
                cl.moveToFirst();
                double iloscLeku = cl.getDouble(2) + jakaDawka;
                myDb.update_LEK(Integer.parseInt(cl.getString(0)), iloscLeku);

                myDb.update_HISTORIA(id_h, obecnyCzas, "NIEWZIETE");

                finish();
                goHome();

            });

            b4.setOnClickListener(v -> {

                Cursor cw = myDb.get_STATYSTYKI_WZIETE(0);
                cw.moveToFirst();
                myDb.update_STATYSTYKI_WZIETE(0, Integer.parseInt(cw.getString(0)) + 1);

                myDb.update_HISTORIA(id_h, obecnyCzas, "WZIETE");

                finish();
                goHome();

            });

        } else if (coPokazac == 1) {

            setContentView(R.layout.medicine_activity_layout);

            TextView tNazwa = findViewById(R.id.textView12m);
            TextView tIlosc = findViewById(R.id.textView6m);
            TextView tSuma = findViewById(R.id.textView6m2);
            Button update = findViewById(R.id.button5m);

            ImageView im = findViewById(R.id.imageView6);
            im.setImageResource(R.drawable.logo);

            String nazwa = getIntent().getStringExtra("nazwa");
            String sumujTypy = getIntent().getStringExtra("sumujTypy");

            Cursor cl = myDb.getNumber_LEK(id);
            cl.moveToFirst();
            String ilosc = cl.getString(0);

            tNazwa.setText(Html.fromHtml("Nazwa leku: " + "<b>" + nazwa + "</b> "));
            tIlosc.setText(Html.fromHtml("Ilość tabletek: " + "<b>" + ilosc + "</b> "));
            tSuma.setText(Html.fromHtml("Tygodniowo zajadasz: " + "<b>" + sumujTypy + "</b> "));

            update.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
                builder.setTitle("Aktualizacja ilości");

                final EditText input = new EditText(this);

                input.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
                input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

                input.setText(ilosc);
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
