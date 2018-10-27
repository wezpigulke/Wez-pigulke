package com.example.kuba.dsadsax;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("Registered")
public class RepeatingActivityMedicine extends AppCompatActivity{

    DatabaseHelper myDb;
    private String m_Text = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_activity_layout);

        myDb = new DatabaseHelper(this);

        TextView tNazwa = findViewById(R.id.textView12m);
        TextView tIlosc = findViewById(R.id.textView6m);
        Button update = findViewById(R.id.button5m);

        Integer id = getIntent().getIntExtra("id", 0);
        String nazwa = getIntent().getStringExtra("nazwa");
        String ilosc = getIntent().getStringExtra("ilosc");

        tNazwa.setText(Html.fromHtml("Nazwa leku: " + "<b>" + nazwa + "</b> "));
        tIlosc.setText(Html.fromHtml("Ilość tabletek: " + "<b>" + ilosc + "</b> "));

        update.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Title");

            final EditText input = new EditText(this);

            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> m_Text = input.getText().toString());
            builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.cancel());
            builder.show();

            myDb.update_LEK(id, m_Text);

        });

    }

}
