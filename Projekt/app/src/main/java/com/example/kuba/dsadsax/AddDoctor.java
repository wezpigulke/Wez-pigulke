package com.example.kuba.dsadsax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDoctor extends AppCompatActivity {

    DatabaseHelper myDb;
    private static final String TAG = "AddProfile";

    private Button add;

    private EditText name;
    private EditText surname;
    private EditText specialization;
    private EditText phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_doctor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add = (Button) findViewById(R.id.button);

        name = (EditText) findViewById(R.id.editText5);
        surname = (EditText) findViewById(R.id.editText6);
        specialization = (EditText) findViewById(R.id.editText7);
        phone_number = (EditText) findViewById(R.id.editText8);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length()>0 && surname.getText().length()>0 && specialization.getText().length()>0 && phone_number.getText().length()==9) {
                    myDb.insert_DOKTORZY(
                            name.getText().toString(),
                            surname.getText().toString(),
                            specialization.getText().toString(),
                            phone_number.getText().toString()
                    );
                    onBackPressed();
                }
                else if(name.getText().length()==0) openDialog("Wpisz imie lekarza");
                else if(surname.getText().length()==0) openDialog("Wpisz nazwisko lekarza");
                else if(specialization.getText().length()==0) openDialog("Wpisz specjalizacje lekarza");
                else if(phone_number.getText().length()==0) openDialog("Wpisz prawidłowy numer (9 cyfr)");
            }
        });
    }

    public void openDialog(String text) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(text);
        openDialog.show(getSupportFragmentManager(), "AddDoctor");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
