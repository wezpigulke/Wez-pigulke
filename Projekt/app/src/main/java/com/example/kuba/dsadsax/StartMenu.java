package com.example.kuba.dsadsax;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartMenu extends AppCompatActivity {

    DatabaseHelper myDb;

    private static final String TAG = "StartMenu";

    private TextView yourName;
    private Button idzDalej;

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

        idzDalej = (Button) findViewById(R.id.przejdzDalej);
        yourName = (TextView) findViewById(R.id.imie);

        // ************PRZECHODZENIE ZE STARTU DO GŁÓWNEGO MENU************** //

        idzDalej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yourName.getText().toString().trim().length()>0) {

                    myDb.insert_UZYTKOWNICY(yourName.getText().toString());

                    Intent cel = new Intent(v.getContext(), MainMenu.class);
                    startActivity(cel);
                }
            }
        });

    }
}
