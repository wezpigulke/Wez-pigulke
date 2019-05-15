package com.wezpigulke.add;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;
import com.wezpigulke.other.OpenDialog;

import java.util.Objects;

public class AddTypeMeasurement extends AppCompatActivity {

    private DatabaseHelper myDb;
    private Button dodaj;
    private EditText typBadania;
    private Cursor cursor;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_type_measurement);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initializeVariables();
        dodajListener();
    }

    private void dodajListener() {
        dodaj.setOnClickListener(v -> {
            if (typBadania.getText().length() > 0) {
                cursor = myDb.getDataID_TYP_POMIAR(typBadania.getText().toString());
                if (cursor.getCount() == 0) {
                    myDb.insert_TYP_POMIAR(typBadania.getText().toString());
                    onBackPressed();
                } else
                    openDialog("Już istnieje typ badania o takiej samej nazwie w naszej bazie danych");
            } else openDialog("Wpisz typ badania");
        });
    }

    private void initializeVariables() {
        myDb = new DatabaseHelper(this);
        dodaj = findViewById(R.id.addTypeMeasurementButton);
        typBadania = findViewById(R.id.typBadania);
    }

    public void openDialog(String warning) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(warning);
        openDialog.show(getSupportFragmentManager(), "AddTypeMeasurement");
    }

    @Override
    public void onBackPressed() {
        if (cursor != null) cursor.close();
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}





