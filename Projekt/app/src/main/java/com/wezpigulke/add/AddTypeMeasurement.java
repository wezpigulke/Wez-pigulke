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

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DatabaseHelper myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_type_measurement);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Button przyciskDodawania = findViewById(R.id.addTypeMeasurementButton);
        EditText typBadania = findViewById(R.id.typBadania);

        przyciskDodawania.setOnClickListener(v -> {
            if (typBadania.getText().length() > 0) {
                Cursor cursor = myDb.getDataID_TYP_POMIAR(typBadania.getText().toString());
                if (cursor.getCount() == 0) {
                    myDb.insert_TYP_POMIAR(typBadania.getText().toString());
                    onBackPressed();
                } else openDialog("Już istnieje typ badania o takiej samej nazwie w naszej bazie danych");
            } else openDialog("Wpisz typ badania");
        });
    }

    public void openDialog(String warning) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(warning);
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





