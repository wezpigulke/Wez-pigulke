package com.wezpigulke.get;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.wezpigulke.classes.MedicineInformation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetInformationAboutMedicine extends AsyncTask<Void, Void, Void> {

    private String medicineName;
    private List<MedicineInformation> results;

    public GetInformationAboutMedicine(String medicineName) {
        this.medicineName = medicineName;
    }

    public List<MedicineInformation> getResults() {
        return results;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            results = new ArrayList<>();
            Document doc = Jsoup.connect("http://bazalekow.leksykon.com.pl/szukaj-leku.html?a=search&o=0&p=50&cmn=" +  medicineName).get();
            Elements elements = doc.select("div.results-drug-list-block.block-shadow > div.header-block > span.quantity-block > span.quantity");
            int medicineCount = Integer.parseInt(elements.text());
            elements = doc.select("div.results-drug-list-block.block-shadow > table > tbody > tr");
            if(medicineCount>50) medicineCount=50;
            String medicineLink, medicineType, medicineDose, medicinePack, medicinePrice;

            for(int i=0; i<medicineCount; i++) {
                medicineName = elements.get(i).select("td.name-column > div.name > a").text();
                medicineLink = elements.get(i).select("td.name-column > div.name > a").attr("href");
                medicineType = elements.get(i).select("td").get(3).text();
                medicineDose = elements.get(i).select("td").get(4).text();
                medicinePack = elements.get(i).select("td").get(5).text();
                medicinePrice = elements.get(i).select("td.price-column > span.full-price-block > span.price").text();
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
    }

    public void setResults(List<MedicineInformation> results) {
        this.results = results;
    }
}
