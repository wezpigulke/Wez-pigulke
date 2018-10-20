package com.example.kuba.dsadsax;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import me.itangqi.waveloadingview.WaveLoadingView;

public class GoToSettings extends Fragment{

    DatabaseHelper myDb;
    WaveLoadingView waveLoadingView;
    Integer progress;
    TextView t13;
    TextView t14;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Ustawienia");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myDb = new DatabaseHelper(getActivity());

        View v = inflater.inflate(R.layout.settings, container, false);

        final Button bt = v.findViewById(R.id.button2);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDb.removeData(getActivity());
                 System.exit(0);
            }
        });

        t13 = v.findViewById(R.id.textView13);
        t14 = v.findViewById(R.id.textView14);

        Cursor cw = myDb.get_STATYSTYKI_WZIETE(0);
        cw.moveToFirst();
        int wziete = Integer.parseInt(cw.getString(0));

        Cursor cn = myDb.get_STATYSTYKI_NIEWZIETE(0);
        cn.moveToFirst();
        int niewziete = Integer.parseInt(cn.getString(0));

        t13.setText(Html.fromHtml("</b> " + "Wzięte: " + "<b>" + wziete));
        t14.setText(Html.fromHtml("</b> " + "Zapomniane: " + "<b>" + niewziete));


        if(niewziete>=0 && wziete==0) progress = 0;
        else if(wziete>=0 && niewziete==0) progress = 100;
        else progress = ((wziete*100)/(wziete+niewziete));

        waveLoadingView = (WaveLoadingView)v.findViewById(R.id.waveLoadingView);
        waveLoadingView.setProgressValue(progress);

        if(progress < 25) {
            waveLoadingView.setBottomTitle(String.format("%d%%", progress));
            waveLoadingView.setCenterTitle("");
            waveLoadingView.setTopTitle("");
        }
        else if(progress < 40) {
            waveLoadingView.setBottomTitle("");
            waveLoadingView.setCenterTitle(String.format("%d%%", progress));
            waveLoadingView.setTopTitle("");
        }
        else {
            waveLoadingView.setBottomTitle("");
            waveLoadingView.setCenterTitle("");
            waveLoadingView.setTopTitle(String.format("%d%%", progress));
        }

        return v;

    }

    public void onResume() {

        super.onResume();

    }

}
