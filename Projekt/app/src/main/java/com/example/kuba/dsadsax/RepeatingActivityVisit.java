package com.example.kuba.dsadsax;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class RepeatingActivityVisit extends AppCompatActivity{

    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_activity_layout);

        myDb = new DatabaseHelper(this);

        TextView tProfil = findViewById(R.id.textView12v);
        TextView tGodzina = findViewById(R.id.textView6v);
        TextView tData = findViewById(R.id.textView14v);
        TextView tLekarz = findViewById(R.id.textView9v);
        TextView tSpecjalizacja = findViewById(R.id.textView10v);
        TextView tAdres = findViewById(R.id.textView11v);

        String godzina = getIntent().getStringExtra("godzina");
        String data = getIntent().getStringExtra("data");
        String imie = getIntent().getStringExtra("imie");
        String nazwisko = getIntent().getStringExtra("nazwisko");
        String specjalizacja = getIntent().getStringExtra("specjalizacja");
        String adres = getIntent().getStringExtra("adres");
        String uzytkownik = getIntent().getStringExtra("uzytkownik");

        tProfil.setText(Html.fromHtml("Profil: " + "<b>" + uzytkownik + "</b> "));
        tGodzina.setText(Html.fromHtml("Godzina: " + "<b>" + godzina + "</b> "));
        tData.setText(Html.fromHtml("Data: " + "<b>" + data + "</b> "));
        tLekarz.setText(Html.fromHtml("Lekarz: " + "<b>" + imie + " " + nazwisko + "</b> "));
        tSpecjalizacja.setText(Html.fromHtml("Specjalizacja: " + "<b>" + specjalizacja + "</b> "));
        tAdres.setText(Html.fromHtml("Adres: " + "<b>" + adres + "</b> "));
    }

}
