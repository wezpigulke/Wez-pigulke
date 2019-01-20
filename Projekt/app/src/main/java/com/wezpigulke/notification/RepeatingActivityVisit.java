package com.wezpigulke.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.wezpigulke.Database;
import com.wezpigulke.R;

public class RepeatingActivityVisit extends AppCompatActivity {

    Database myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_activity_layout);

        myDb = new Database(this);

        TextView tProfil = findViewById(R.id.textView12v);
        TextView tGodzina = findViewById(R.id.textView6v);
        TextView tData = findViewById(R.id.textView14v);
        TextView tLekarz = findViewById(R.id.textView9v);
        TextView tSpecjalizacja = findViewById(R.id.textView10v);

        ImageView im = findViewById(R.id.imageView3v);
        im.setImageResource(R.drawable.logo);

        String godzina = getIntent().getStringExtra("godzina");
        String data = getIntent().getStringExtra("data");
        String imie_nazwisko = getIntent().getStringExtra("imie_nazwisko");
        String specjalizacja = getIntent().getStringExtra("specjalizacja");
        String uzytkownik = getIntent().getStringExtra("uzytkownik");

        tProfil.setText(Html.fromHtml("Profil: " + "<b>" + uzytkownik + "</b> ", 0));
        tGodzina.setText(Html.fromHtml("Godzina: " + "<b>" + godzina + "</b> ", 0));
        tData.setText(Html.fromHtml("Data: " + "<b>" + data + "</b> ", 0));
        tLekarz.setText(Html.fromHtml("Lekarz: " + "<b>" + imie_nazwisko + "</b> ", 0));
        tSpecjalizacja.setText(Html.fromHtml("Specjalizacja: " + "<b>" + specjalizacja + "</b> ", 0));
    }

}
