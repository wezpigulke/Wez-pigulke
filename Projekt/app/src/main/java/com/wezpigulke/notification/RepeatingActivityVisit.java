package com.wezpigulke.notification;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;

public class RepeatingActivityVisit extends AppCompatActivity {

    private DatabaseHelper myDb;
    private Button callButton;
    private Button navigateButton;
    private String adres;
    private Cursor cursor;
    private int id_l;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_activity_layout);
        myDb = new DatabaseHelper(this);
        callButton = findViewById(R.id.button4v);
        navigateButton = findViewById(R.id.button3v);

        ImageView im = findViewById(R.id.imageView3v);
        im.setImageResource(R.drawable.logo);

        id_l = getIntent().getIntExtra("id_l", 0);

        callButtonListener();
        navigateButtonListener();
        setTextForTextView();

    }

    private void setTextForTextView() {

        TextView tProfil = findViewById(R.id.textView12v);
        TextView tGodzina = findViewById(R.id.textView6v);
        TextView tData = findViewById(R.id.textView14v);
        TextView tLekarz = findViewById(R.id.textView9v);
        TextView tSpecjalizacja = findViewById(R.id.textView10v);

        String godzina = getIntent().getStringExtra("godzina");
        String data = getIntent().getStringExtra("data");
        String imie_nazwisko = getIntent().getStringExtra("imie_nazwisko");
        String specjalizacja = getIntent().getStringExtra("specjalizacja");
        String uzytkownik = getIntent().getStringExtra("uzytkownik");

        if (Build.VERSION.SDK_INT >= 24) {
            tProfil.setText(Html.fromHtml("Profil: " + "<b>" + uzytkownik + "</b> ", 0));
            tGodzina.setText(Html.fromHtml("Godzina: " + "<b>" + godzina + "</b> ", 0));
            tData.setText(Html.fromHtml("Data: " + "<b>" + data + "</b> ", 0));
            tLekarz.setText(Html.fromHtml("Lekarz: " + "<b>" + imie_nazwisko + "</b> ", 0));
            tSpecjalizacja.setText(Html.fromHtml("Specjalizacja: " + "<b>" + specjalizacja + "</b> ", 0));
        } else {
            tProfil.setText(Html.fromHtml("Profil: " + "<b>" + uzytkownik + "</b> "));
            tGodzina.setText(Html.fromHtml("Godzina: " + "<b>" + godzina + "</b> "));
            tData.setText(Html.fromHtml("Data: " + "<b>" + data + "</b> "));
            tLekarz.setText(Html.fromHtml("Lekarz: " + "<b>" + imie_nazwisko + "</b> "));
            tSpecjalizacja.setText(Html.fromHtml("Specjalizacja: " + "<b>" + specjalizacja + "</b> "));
        }
    }

    private void callButtonListener() {

        callButton.setOnClickListener(v -> {

            cursor = myDb.getIdData_DOKTORZY(id_l);
            if(cursor!=null) {
                cursor.moveToNext();
                dialContactPhone(cursor.getString(3));
                cursor.close();
            }

            finish();

        });

    }

    private void navigateButtonListener() {

        navigateButton.setOnClickListener(v -> {

            cursor = myDb.getIdData_DOKTORZY(id_l);
            if(cursor!=null) {
                cursor.moveToNext();
                adres = cursor.getString(4);
                openMap();
                cursor.close();
            }

            finish();

        });

    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private void openMap() {

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + adres);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

}
