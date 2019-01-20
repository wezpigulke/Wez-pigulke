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
import com.wezpigulke.other.DecimalDigitsInputFilter;
import com.wezpigulke.other.OpenDialog;
import com.wezpigulke.R;

import java.util.Objects;

public class AddMedicine extends AppCompatActivity {

    DatabaseHelper myDb;

    private EditText nazwaLeku;
    private EditText iloscTabletek;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicine);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Button dodajButton = findViewById(R.id.dodajLekButton);
        nazwaLeku = findViewById(R.id.nazwaLeku);
        iloscTabletek = findViewById(R.id.iloscTabletek);

        iloscTabletek.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        iloscTabletek.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        Cursor c = myDb.getMAXid_LEK();
        c.moveToFirst();
        int id = Integer.parseInt(c.getString(0)) + 1;

        dodajButton.setOnClickListener(v -> {
            if (nazwaLeku.getText().length() > 0 && iloscTabletek.getText().length() > 0) {
                Cursor cl = myDb.getDataName_LEK(nazwaLeku.getText().toString());
                if (cl.getCount() == 0) {
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

    public void openDialog(String warning) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(warning);
        openDialog.show(getSupportFragmentManager(), "AddMedicine");
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
