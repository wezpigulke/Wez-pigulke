package com.wezpigulke;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class AddProfile extends AppCompatActivity {

    DatabaseHelper myDb;
    private Button add;
    private EditText txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add = findViewById(R.id.adBut);
        txt = findViewById(R.id.namField);

        add.setOnClickListener(v -> {
            if(txt.getText().length()>0) {
                Cursor cp = myDb.getId_UZYTKOWNICY(txt.getText().toString());
                if(cp.getCount()==0) {
                    myDb.insert_UZYTKOWNICY(txt.getText().toString());
                    onBackPressed();
                } else openDialog("Już istnieje osoba o takim imieniu w naszej bazie danych");
            } else openDialog("Wpisz imie");
        });
    }

    public void openDialog(String warning) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(warning);
        openDialog.show(getSupportFragmentManager(), "AddProfile");
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
