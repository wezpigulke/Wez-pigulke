package com.wezpigulke.get;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetMedicineInformation extends AsyncTask<Void, Void, Void> {

    private List<String> contentHeaders;
    private List<String> contentInformation;
    private String url;

    public GetMedicineInformation(String url) {
        this.url = url;
    }

    public List<String> getContentHeaders() {
        return contentHeaders;
    }

    public List<String> getContentInformation() {
        return contentInformation;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            contentHeaders = new ArrayList<>();
            contentInformation = new ArrayList<>();

            Document doc = Jsoup.connect("http://bazalekow.leksykon.com.pl/" + url).get();
            int contentSize = doc.select("span.descr_common > span.descr_section").size();

            for(int i = 0; i < contentSize; i++) {
                String content = doc.select("span.descr_common > span.descr_section").get(i).select("span.descr_head").text();
                if(content.length()>0) {
                    contentHeaders.add(content);
                    content = doc.select("span.descr_common > span.descr_section").get(i).select("span.descr_body").text();
                    contentInformation.add(content);
                }
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

}
