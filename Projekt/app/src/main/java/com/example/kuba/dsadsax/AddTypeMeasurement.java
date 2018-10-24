package com.example.kuba.dsadsax;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Objects;

public class AddTypeMeasurement extends AppCompatActivity {

    DatabaseHelper myDb;
    private static final String TAG = "AddTypeMeasurement";

    private EditText typBadania;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_type_measurement);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Button dodajButton = findViewById(R.id.dodajTypButton);
        typBadania = findViewById(R.id.typBadania);

        dodajButton.setOnClickListener(v -> {
            if (typBadania.getText().length() > 0) {
                myDb.insert_TYP_POMIAR(
                        typBadania.getText().toString()
                );
                onBackPressed();
            } else openDialog();
        });

    }

    public void openDialog() {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue("Wpisz typ badania");
        openDialog.show(getSupportFragmentManager(), "AddTypeMeasurement");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
