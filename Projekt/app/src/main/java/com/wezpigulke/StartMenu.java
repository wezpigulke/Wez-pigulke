package com.wezpigulke;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class StartMenu extends AppCompatActivity {

    DatabaseHelper myDb;

    private TextView yourName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);

        myDb = new DatabaseHelper(this);
        myDb.insert_STATYSTYKI(0, 0, 0);

        Cursor res = myDb.getAllData_UZYTKOWNICY();
        if(res.getCount() > 0) {
            Intent cel = new Intent(this, MainMenu.class);
            startActivity(cel);
        }

        Button idzDalej = findViewById(R.id.przejdzDalej);
        yourName = findViewById(R.id.imie);

        idzDalej.setOnClickListener(v -> {
            if(yourName.getText().toString().trim().length()>0) {

                myDb.insert_UZYTKOWNICY(yourName.getText().toString());

                Intent cel = new Intent(v.getContext(), MainMenu.class);
                startActivity(cel);
            }
        });

    }
}