package com.example.kuba.dsadsax;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

public class RepeatingActivityReminder extends AppCompatActivity{

    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeating_activity_layout);

        myDb = new DatabaseHelper(this);

        TextView tNazwa = findViewById(R.id.textView6);
        TextView tGodzina = findViewById(R.id.textView14);
        TextView tProfil = findViewById(R.id.textView12);
        TextView tData = findViewById(R.id.textView9);
        TextView tPozostalo = findViewById(R.id.textView10);

        Button b3 = findViewById(R.id.button3);
        Button b4 = findViewById(R.id.button4);

        String godzina = getIntent().getStringExtra("godzina");
        String data = getIntent().getStringExtra("data");
        String uzytkownik = getIntent().getStringExtra("uzytkownik");
        String nazwaLeku = getIntent().getStringExtra("nazwaLeku");
        String jakaDawka = getIntent().getStringExtra("jakaDawka");
        Integer iloscDni = getIntent().getIntExtra("iloscDni", 0);

        tNazwa.setText(Html.fromHtml("Nazwa tabletki: " + "<b>" + nazwaLeku + " (" + jakaDawka + ")" + "</b> "));
        tGodzina.setText(Html.fromHtml("Godzina: " + "<b>" + godzina + "</b> "));
        tProfil.setText(Html.fromHtml("Profil: " + "<b>" + uzytkownik + "</b> "));
        tData.setText(Html.fromHtml("Data: " + "<b>" + data + "</b> "));
        tPozostalo.setText(Html.fromHtml("Pozostało dni: " + "<b>" + iloscDni + "</b> "));

        b3.setOnClickListener(v -> {
            Cursor cn = myDb.get_STATYSTYKI_NIEWZIETE(0);
            cn.moveToFirst();
            myDb.update_STATYSTYKI_NIEWZIETE(0, Integer.parseInt(cn.getString(0)) + 1);
            finish();
            goHome();
        });

        b4.setOnClickListener(v -> {
            Cursor cw = myDb.get_STATYSTYKI_WZIETE(0);
            cw.moveToFirst();
            myDb.update_STATYSTYKI_WZIETE(0, Integer.parseInt(cw.getString(0)) + 1);
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
}
