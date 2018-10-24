package com.example.kuba.dsadsax;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RepeatingActivity extends AppCompatActivity{

    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeating_activity_layout);

        myDb = new DatabaseHelper(this);

        TextView t6 = findViewById(R.id.textView6);
        TextView t7 = findViewById(R.id.textView7);
        TextView t8 = findViewById(R.id.textView8);
        TextView t9 = findViewById(R.id.textView9);
        TextView t10 = findViewById(R.id.textView10);

        Button b3 = findViewById(R.id.button3);
        Button b4 = findViewById(R.id.button4);

        String godzina = getIntent().getStringExtra("godzina");
        String data = getIntent().getStringExtra("data");
        String uzytkownik = getIntent().getStringExtra("uzytkownik");
        String nazwaLeku = getIntent().getStringExtra("nazwaLeku");
        String jakaDawka = getIntent().getStringExtra("jakaDawka");
        Integer iloscDni = getIntent().getIntExtra("iloscDni", 0);

        t6.setText(Html.fromHtml("Nazwa tabletki: " + "<b>" + nazwaLeku + " (" + jakaDawka + ")" + "</b> "));
        t7.setText(Html.fromHtml("Godzina: " + "<b>" + godzina + "</b> "));
        t8.setText(Html.fromHtml("Profil: " + "<b>" + uzytkownik + "</b> "));
        t9.setText(Html.fromHtml("Data: " + "<b>" + data + "</b> "));
        t10.setText(Html.fromHtml("Pozostało dni: " + "<b>" + iloscDni + "</b> "));

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
