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
    private Button add;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initializeVariables();
        addListener();
        obrazkiListener();

    }

    private void obrazkiListener() {
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

    private void addListener(){
        add.setOnClickListener(v -> {
            if (txt.getText().length() > 0) {
                if(ktoryObrazek!=0) {
                    cursor = myDb.getId_UZYTKOWNICY(txt.getText().toString());
                    if (cursor.getCount() == 0) {
                        myDb.insert_UZYTKOWNICY(txt.getText().toString(), ktoryObrazek);
                        closeKeyboard();
                        onBackPressed();
                    }
                    else openDialog("Już istnieje osoba o takim imieniu w naszej bazie danych");
                } else openDialog("Musisz wybrać obrazek");
            } else openDialog("Wpisz imie");
        });
    }

    private void initializeVariables() {
        myDb = new DatabaseHelper(this);
        ktoryObrazek = 0;
        facetObr = findViewById(R.id.imageView7);
        kobietaObr = findViewById(R.id.imageView8);

        add = findViewById(R.id.adBut);
        txt = findViewById(R.id.namField);

        showKeyboard();
    }

    public void openDialog(String warning) {
        OpenDialog openDialog = new OpenDialog();
        openDialog.setValue(warning);
        openDialog.show(getSupportFragmentManager(), "AddProfile");
    }

    @Override
    public void onBackPressed() {
        cursor.close();
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
