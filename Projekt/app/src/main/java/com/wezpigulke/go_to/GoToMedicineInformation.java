package com.wezpigulke.go_to;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.wezpigulke.R;
import com.wezpigulke.classes.MedicineInformation;
import com.wezpigulke.adapters.MedicineInformationListAdapter;
import com.wezpigulke.get.GetInformationAboutMedicine;
import com.wezpigulke.get.GetMedicineInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class GoToMedicineInformation extends AppCompatActivity {

    private List<String> contentHeaders;
    private List<String> contentInformation;
    private List<MedicineInformation> results;
    private ListView lv;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_information);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initializeVariables();
        getInformationAboutMedicine();
        aktualizujListe();
        lvClickListener();

    }

    private void initializeVariables() {
        results = new ArrayList<>();
        lv = findViewById(R.id.medicineInformationList);
        contentHeaders = new ArrayList<>();
        contentInformation = new ArrayList<>();
    }

    private void getInformationAboutMedicine() {
        String medicineName = getIntent().getStringExtra("medicineName");
        GetInformationAboutMedicine getInformationAboutMedicine = new GetInformationAboutMedicine(medicineName);
        getInformationAboutMedicine.execute();
        try {
            getInformationAboutMedicine.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        results = getInformationAboutMedicine.getResults();
    }

    private void getMedicineInformation() {

        GetMedicineInformation getMedicineInformation= new GetMedicineInformation(url);
        getMedicineInformation.execute();

        try {
            getMedicineInformation.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        contentHeaders = getMedicineInformation.getContentHeaders();
        contentInformation = getMedicineInformation.getContentInformation();

    }

    private void lvClickListener() {

        lv.setOnItemClickListener((parent, view, position, id) -> {

            url = results.get(position).getLink();
            getMedicineInformation();
            dialogShowInformation();

        });

    }

    public void aktualizujListe() {

        MedicineInformationListAdapter adapter = new MedicineInformationListAdapter(getApplicationContext(), results);
        lv.setAdapter(adapter);

    }

    public void dialogShowInformation() {

        if(contentHeaders.size()>0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Jaką informacje chcesz zobaczyć?");

            String[] buttonText = new String[contentHeaders.size()];

            for (int i = 0; i < contentHeaders.size(); i++) {
                buttonText[i] = contentHeaders.get(i);
            }

            builder.setItems(buttonText, (dialog, which) -> showDialogWithInformation(which));
            builder.setNegativeButton("Zamknij",(dialog, id) -> dialog.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();
        } else Toast.makeText(getApplicationContext(), "Brak informacji na temat leku", Toast.LENGTH_LONG).show();

    }

    private void showDialogWithInformation(Integer tmp) {

        AlertDialog.Builder builder = new AlertDialog.Builder(GoToMedicineInformation.this, R.style.AlertDialog);
        builder.setMessage(contentInformation.get(tmp)).setCancelable(true)
                .setPositiveButton("Sprawdź dalej", (dialog, which) -> {
                    dialog.cancel();
                    dialogShowInformation();
                })
                .setNegativeButton("Wyjdź", (dialog, which) -> {
                    dialog.cancel();
                });

        builder.show();

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
            closeKeyboard();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

}
