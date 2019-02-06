package com.wezpigulke.go_to;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.wezpigulke.DatabaseHelper;
import com.wezpigulke.R;
import com.wezpigulke.classes.MedicineInformation;
import com.wezpigulke.list_adapter.MedicineInformationListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GoToMedicineInformation extends AppCompatActivity {

    DatabaseHelper myDb;
    private String medicineName;
    private List<String> contentHeaders;
    private List<String> contentInformation;
    private List<MedicineInformation> results;
    private ListView lv;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_information);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        results = new ArrayList<>();
        lv = findViewById(R.id.medicineInformationList);
        contentHeaders = new ArrayList<>();
        contentInformation = new ArrayList<>();

        medicineName = getIntent().getStringExtra("medicineName");

        new getInformationAboutMedicine().execute();

        lv.setOnItemClickListener((parent, view, position, id) -> {

            url = results.get(position).getLink();
            new getMedicineInformation().execute();

        });

    }

    public void aktualizujListe() {

        MedicineInformationListAdapter adapter = new MedicineInformationListAdapter(getApplicationContext(), results);
        lv.setAdapter(adapter);

    }


    @SuppressLint("StaticFieldLeak")
    public class getMedicineInformation extends AsyncTask<Void, Void, Void> {

        @SuppressLint("ShowToast")
        @Override
        protected Void doInBackground(Void... params) {

            try {

                Document doc = Jsoup.connect("http://bazalekow.leksykon.com.pl/" + url).get();
                int contentSize = doc.select("span.descr_common > span.descr_section").size();

                contentHeaders.clear();
                contentInformation.clear();

                for(int i = 0; i< contentSize; i++) {

                    String content = doc.select("span.descr_common > span.descr_section").get(i).select("span.descr_head").text();
                    contentHeaders.add(content);
                    content = doc.select("span.descr_common > span.descr_section").get(i).select("span.descr_body").text();
                    contentInformation.add(content);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialogShowInformation();
        }

    }

    public void dialogShowInformation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Jaką informacje chcesz zobaczyć?");

        String[] buttonText = new String[contentHeaders.size()];

        for (int i = 0; i < contentHeaders.size(); i++) {
            buttonText[i] = contentHeaders.get(i);
        }

        builder.setItems(buttonText, (dialog, which) -> {
            showDialogWithInformation(which);
        });

        builder.setNegativeButton("ANULUJ",(dialog, id) -> {
            dialog.cancel();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showDialogWithInformation(Integer tmp) {

        AlertDialog.Builder builder = new AlertDialog.Builder(GoToMedicineInformation.this, R.style.AlertDialog);
        builder.setMessage(contentInformation.get(tmp)).setCancelable(true).setPositiveButton("OK", (dialog, which) -> {
            dialog.cancel();
            dialogShowInformation();
        });
        builder.show();

    }


    @SuppressLint("StaticFieldLeak")
    public class getInformationAboutMedicine extends AsyncTask<Void, Void, Void> {

        @SuppressLint("ShowToast")
        @Override
        protected Void doInBackground(Void... params) {

            try {

                Document doc = Jsoup.connect("http://bazalekow.leksykon.com.pl/szukaj-leku.html?a=search&o=0&p=50&cmn=" +  medicineName).get();
                Elements elements = doc.select("div.results-drug-list-block.block-shadow > div.header-block > span.quantity-block > span.quantity");
                Integer medicineCount = Integer.parseInt(elements.text());

                elements = doc.select("div.results-drug-list-block.block-shadow > table > tbody > tr");

                if(medicineCount >50) medicineCount =50;

                for(int i = 0; i< medicineCount; i++) {
                    medicineName = elements.get(i).select("td.name-column > div.name > a").text();
                    String medicineLink = elements.get(i).select("td.name-column > div.name > a").attr("href");
                    String medicineType = elements.get(i).select("td").get(3).text();
                    String medicineDose = elements.get(i).select("td").get(4).text();
                    String medicinePack = elements.get(i).select("td").get(5).text();
                    String medicinePrice = elements.get(i).select("td.price-column > span.full-price-block > span.price").text();
                    results.add(new MedicineInformation(medicineName, medicineLink, medicineType, medicineDose, medicinePack, medicinePrice));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            aktualizujListe();
        }

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
