package com.example.kuba.dsadsax;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.itangqi.waveloadingview.WaveLoadingView;

public class GoToSettings extends Fragment{

    DatabaseHelper myDb;

    WaveLoadingView waveLoadingView;

    Integer progress;
    TextView tProgres;
    TextView tWziete;
    TextView tZapomniane;
    private int i = 0;

    private Handler mHandler = new Handler();

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

        tProgres = v.findViewById(R.id.textView8);
        tWziete = v.findViewById(R.id.textView13);
        tZapomniane = v.findViewById(R.id.textView14);

        Cursor cw = myDb.get_STATYSTYKI_WZIETE(0);
        cw.moveToFirst();
        int wziete = Integer.parseInt(cw.getString(0));

        Cursor cn = myDb.get_STATYSTYKI_NIEWZIETE(0);
        cn.moveToFirst();
        int niewziete = Integer.parseInt(cn.getString(0));

        if(niewziete>=0 && wziete==0) progress = 0;
        else if(wziete>=0 && niewziete==0) progress = 100;
        else progress = ((wziete*100)/(wziete+niewziete));

        tWziete.setText(Html.fromHtml("</b> " + "Wzięte: " + "<b>" + wziete));
        tZapomniane.setText(Html.fromHtml("</b> " + "Zapomniane: " + "<b>" + niewziete));
        tProgres.setText(Html.fromHtml("<b>" + progress + "%" + "</b>"));

        tWziete.setVisibility(View.INVISIBLE);
        tZapomniane.setVisibility(View.INVISIBLE);
        tProgres.setVisibility(View.INVISIBLE);

        ProgressBar myprogressbar = v.findViewById(R.id.myprogressbar);
        myprogressbar.setScaleY(10);

        if(progress<50) myprogressbar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        else myprogressbar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

        new Thread(() -> {
            while(i <= progress) {
                i++;
                android.os.SystemClock.sleep(5);
                mHandler.post(() -> myprogressbar.setProgress(i));
            }
            mHandler.post(() -> {
                tWziete.setVisibility(View.VISIBLE);
                tZapomniane.setVisibility(View.VISIBLE);
                tProgres.setVisibility(View.VISIBLE);
            });
        }).start();

        return v;

    }

    public void onResume() {

        super.onResume();

    }

}
