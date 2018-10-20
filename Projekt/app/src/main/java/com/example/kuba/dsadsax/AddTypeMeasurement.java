package com.example.kuba.dsadsax;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddTypeMeasurement extends AppCompatActivity {

    private static int c = 0;
    DatabaseHelper myDb;
    private static final String TAG = "AddTypeMeasurement";

    private Button dodajButton;
    private EditText typBadania;
    private Spinner spn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_type_measurement);

        dodajButton = findViewById(R.id.dodajTypButton);
        typBadania = findViewById(R.id.typBadania);
        spn = findViewById(R.id.spinnerTypeMeasurement);

        c = 1;

        dodajButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typBadania.getText().length() > 0) {
                    myDb.insert_TYP_POMIAR(
                            typBadania.getText().toString()
                    );
                    onBackPressed();
                } else openDialog();
            }
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
}
