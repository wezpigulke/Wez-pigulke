package com.wezpigulke.add;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;
import com.wezpigulke.other.DecimalDigitsInputFilter;
import com.wezpigulke.other.OpenDialog;

import java.util.Objects;

public class AddMedicine extends AppCompatActivity {

    DatabaseHelper myDb;

    private EditText nazwaLeku;
    private EditText iloscTabletek;
    private Button dodajButton;
    private int id;
    private Cursor cursor;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicine);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initializeVariables();
        dodajButtonListener();

    }

    private void dodajButtonListener() {
        dodajButton.setOnClickListener(v -> {
            if (nazwaLeku.getText().length() > 0 && iloscTabletek.getText().length() > 0) {
                cursor = myDb.getDataName_LEK(nazwaLeku.getText().toString());
                if (cursor.getCount() == 0) {
                    myDb.insert_LEK(
                            id,
                            nazwaLeku.getText().toString(),
                            iloscTabletek.getText().toString()
                    );
                    onBackPressed();
                } else openDialog("Już istnieje lek o takiej nazwie w naszej bazie danych");
            } else openDialog("Wpisz typ badania");
        });
    }

    private void initializeVariables() {

        myDb = new DatabaseHelper(this);
        dodajButton = findViewById(R.id.dodajLekButton);
        nazwaLeku = findViewById(R.id.nazwaLeku);
        iloscTabletek = findViewById(R.id.iloscTabletek);

        iloscTabletek.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        iloscTabletek.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        cursor = myDb.getMAXid_LEK();
        cursor.moveToFirst();
        id = Integer.parseInt(cursor.getString(0)) + 1;

    }

    public void openDialog(String warning) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(warning);
        openDialog.show(getSupportFragmentManager(), "AddMedicine");
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
