package com.wezpigulke;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StartMenu extends AppCompatActivity {

    DatabaseHelper myDb;

    private TextView yourName;
    private Integer ktoryObrazek;

    private TextView textViewWitam;
    private TextView textViewNadszedl;
    private TextView textViewWpisz;
    private TextView textViewKliknij;

    private Button idzDalej;
    private Button zacznijKorzystac;

    private ImageView facetObr;
    private ImageView kobietaObr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);

        ktoryObrazek = 0;

        myDb = new DatabaseHelper(this);
        myDb.insert_STATYSTYKI(0, 0, 0);

        facetObr = findViewById(R.id.imageView10);
        kobietaObr = findViewById(R.id.imageView11);
        idzDalej = findViewById(R.id.przejdzDalej);
        yourName = findViewById(R.id.imie);
        textViewWitam = findViewById(R.id.textView2);
        textViewWpisz = findViewById(R.id.textView3);
        textViewNadszedl = findViewById(R.id.textView16);
        textViewKliknij = findViewById(R.id.textView17);
        zacznijKorzystac = findViewById(R.id.zacznijKorzystac);

        textViewNadszedl.setVisibility(View.INVISIBLE);
        textViewKliknij.setVisibility(View.INVISIBLE);
        zacznijKorzystac.setVisibility(View.INVISIBLE);
        kobietaObr.setVisibility(View.INVISIBLE);
        facetObr.setVisibility(View.INVISIBLE);

        Cursor res = myDb.getAllData_UZYTKOWNICY();
        if (res.getCount() > 0) {
            Intent cel = new Intent(this, MainMenu.class);
            startActivity(cel);
        }

        facetObr.setOnClickListener(v -> {
            Drawable highlight = getResources().getDrawable( R.drawable.highlight);
            facetObr.setBackground(highlight);
            kobietaObr.setBackground(null);
            ktoryObrazek = 1;
        });

        kobietaObr.setOnClickListener(v -> {
            Drawable highlight = getResources().getDrawable( R.drawable.highlight);
            kobietaObr.setBackground(highlight);
            facetObr.setBackground(null);
            ktoryObrazek = 2;

        });

        idzDalej.setOnClickListener(v -> {

            InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            textViewNadszedl.setVisibility(View.VISIBLE);
            textViewKliknij.setVisibility(View.VISIBLE);
            zacznijKorzystac.setVisibility(View.VISIBLE);
            kobietaObr.setVisibility(View.VISIBLE);
            facetObr.setVisibility(View.VISIBLE);

            idzDalej.setVisibility(View.INVISIBLE);
            yourName.setVisibility(View.INVISIBLE);
            textViewWitam.setVisibility(View.INVISIBLE);
            textViewWpisz.setVisibility(View.INVISIBLE);

        });

        zacznijKorzystac.setOnClickListener(v -> {

            if (yourName.getText().toString().trim().length() > 0) {

                if(ktoryObrazek == 0) {
                    openDialog("Musisz wybrać jeden z avatarów");
                } else {
                    myDb.insert_UZYTKOWNICY(yourName.getText().toString(), ktoryObrazek);

                    Intent cel = new Intent(v.getContext(), MainMenu.class);
                    startActivity(cel);
                }
            }

        });

    }

    public void openDialog(String warning) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(warning);
        openDialog.show(getSupportFragmentManager(), "StartMenu");
    }
}
