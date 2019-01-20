package com.wezpigulke.add;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.other.OpenDialog;
import com.wezpigulke.R;

import java.util.Objects;


public class AddProfile extends AppCompatActivity {

    DatabaseHelper myDb;
    private EditText txt;
    private ImageView facetObr;
    private ImageView kobietaObr;
    private Integer ktoryObrazek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myDb = new DatabaseHelper(this);

        ktoryObrazek = 1;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile);

        facetObr = findViewById(R.id.imageView7);
        kobietaObr = findViewById(R.id.imageView8);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Button add = findViewById(R.id.adBut);
        txt = findViewById(R.id.namField);

        showKeyboard();

        add.setOnClickListener(v -> {
            if (txt.getText().length() > 0) {

                Cursor cp = myDb.getId_UZYTKOWNICY(txt.getText().toString());

                if (cp.getCount() == 0) {

                    myDb.insert_UZYTKOWNICY(txt.getText().toString(), ktoryObrazek);
                    closeKeyboard();
                    onBackPressed();

                } else openDialog("Już istnieje osoba o takim imieniu w naszej bazie danych");

            } else openDialog("Wpisz imie");
        });

        facetObr.setOnClickListener(v -> {
            Drawable highlight = ContextCompat.getDrawable(getApplicationContext(), R.drawable.highlight);
            facetObr.setBackground(highlight);
            kobietaObr.setBackground(null);
            ktoryObrazek = 1;
        });

        kobietaObr.setOnClickListener(v -> {
            Drawable highlight = ContextCompat.getDrawable(getApplicationContext(), R.drawable.highlight);
            kobietaObr.setBackground(highlight);
            facetObr.setBackground(null);
            ktoryObrazek = 2;

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
                closeKeyboard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

}
