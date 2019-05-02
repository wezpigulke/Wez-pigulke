package com.wezpigulke.add;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.other.OpenDialog;
import com.wezpigulke.R;

import java.util.Objects;

public class AddDoctor extends AppCompatActivity {

    DatabaseHelper myDb;

    private EditText name;
    private EditText address;
    private EditText specialization;
    private EditText phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_doctor);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Button add = findViewById(R.id.button);

        name = findViewById(R.id.editText5);
        address = findViewById(R.id.editText6);
        specialization = findViewById(R.id.editText7);
        phone_number = findViewById(R.id.editText8);

        add.setOnClickListener(v -> {
            if (name.getText().length() > 0 && specialization.getText().length() > 0 && (phone_number.getText().length()==0 || phone_number.getText().length()==9)) {
                String numerTelefonu = phone_number.getText().toString();
                if(numerTelefonu.length()==0) numerTelefonu="0";

                    myDb.insert_DOKTORZY(
                            name.getText().toString(),
                            specialization.getText().toString(),
                            Integer.valueOf(numerTelefonu),
                            address.getText().toString()
                    );

                onBackPressed();
            } else if (name.getText().length() == 0) openDialog("Wpisz imie i nazwisko lekarza");
            else if (specialization.getText().length() == 0) openDialog("Wpisz specjalizacje lekarza");
            else if (phone_number.getText().length() < 9 && phone_number.length() > 0) openDialog("Wpisz prawidłowy numer telefonu (9 cyfr)");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
