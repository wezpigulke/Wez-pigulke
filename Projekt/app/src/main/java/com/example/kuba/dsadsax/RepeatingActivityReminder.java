package com.example.kuba.dsadsax;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RepeatingActivityReminder extends AppCompatActivity{

    DatabaseHelper myDb;
<<<<<<< HEAD
<<<<<<< HEAD
=======
    private Integer id;
>>>>>>> parent of e26ace6... 28.10.2018
=======
    private Integer id_h;
    private String obecnyCzas;
>>>>>>> 6727fdef062daaf576b84da01945e28d7b023f55

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this);
        int coPokazac = getIntent().getIntExtra("coPokazac",-1);
<<<<<<< HEAD
<<<<<<< HEAD
=======
        int coZrobic = getIntent().getIntExtra("coZrobic",-1);

>>>>>>> 6727fdef062daaf576b84da01945e28d7b023f55
        String godzina = getIntent().getStringExtra("godzina");
        String data = getIntent().getStringExtra("data");
        String uzytkownik = getIntent().getStringExtra("uzytkownik");
        String nazwaLeku = getIntent().getStringExtra("nazwaLeku");
        String jakaDawka = getIntent().getStringExtra("jakaDawka");
        Integer iloscDni = getIntent().getIntExtra("iloscDni", 0);
        Integer id = getIntent().getIntExtra("id", 0);
<<<<<<< HEAD
        Integer id_h = getIntent().getIntExtra("id_h", 0);
        String obecnyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
=======
>>>>>>> parent of e26ace6... 28.10.2018
=======

        id_h = getIntent().getIntExtra("id_h", 0);
        obecnyCzas = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(id);

        if(coZrobic==0) {

            Cursor cn = myDb.get_STATYSTYKI_NIEWZIETE(0);
            cn.moveToFirst();
            myDb.update_STATYSTYKI_NIEWZIETE(0, Integer.parseInt(cn.getString(0)) + 1);

            Cursor cl = myDb.getDataName_LEK(nazwaLeku);
            cl.moveToFirst();
            double iloscLeku = Double.valueOf(cl.getString(2)) + Double.valueOf(jakaDawka.substring(7, jakaDawka.length()));
            myDb.update_LEK(Integer.parseInt(cl.getString(0)), String.valueOf(iloscLeku));

            myDb.update_HISTORIA(id_h, obecnyCzas, "NIEWZIETE");

            finish();
            goHome();

        }

        else if(coZrobic==1) {

            Cursor cw = myDb.get_STATYSTYKI_WZIETE(0);
            cw.moveToFirst();
            myDb.update_STATYSTYKI_WZIETE(0, Integer.parseInt(cw.getString(0)) + 1);

            myDb.update_HISTORIA(id_h, obecnyCzas, "WZIETE");

            finish();
            goHome();

        }
>>>>>>> 6727fdef062daaf576b84da01945e28d7b023f55

        if(coPokazac==0) {

            setContentView(R.layout.repeating_activity_layout);

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

                Cursor cl = myDb.getDataName_LEK(nazwaLeku);
                cl.moveToFirst();
                double iloscLeku = Double.valueOf(cl.getString(2)) + Double.valueOf(jakaDawka.substring(7, jakaDawka.length()));
                myDb.update_LEK(Integer.parseInt(cl.getString(0)), String.valueOf(iloscLeku));

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

        else if (coPokazac==1) {

            setContentView(R.layout.medicine_activity_layout);

            TextView tNazwa = findViewById(R.id.textView12m);
            TextView tIlosc = findViewById(R.id.textView6m);
            TextView tSuma = findViewById(R.id.textView6m2);
            Button update = findViewById(R.id.button5m);

            id = getIntent().getIntExtra("id", 0);
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

                input.setText(ilosc);
                input.setGravity(Gravity.CENTER_HORIZONTAL);

                input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setNegativeButton("Anuluj", (dialog, which) -> {
                    dialog.cancel();
                    finish();
                    goHome();
                });

                builder.setPositiveButton("OK", (dialog, which) -> {
                    myDb.update_LEK(id, input.getText().toString());
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
<<<<<<< HEAD

<<<<<<< HEAD
=======
>>>>>>> parent of e26ace6... 28.10.2018
=======
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

>>>>>>> 6727fdef062daaf576b84da01945e28d7b023f55
}
