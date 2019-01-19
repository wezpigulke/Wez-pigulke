package com.wezpigulke.Author;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wezpigulke.R;

import java.util.Objects;

import static com.wezpigulke.R.layout.about_author;


public class GoToAuthor extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Autor");

        ImageView imageView = view.findViewById(R.id.imageView2);
        imageView.setImageAlpha(0);

        new CountDownTimer(2550, 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                Integer val = Math.toIntExact(255 - (millisUntilFinished / 10));
                imageView.setImageAlpha(val);
            }

            @Override
            public void onFinish() {
            }

        }.start();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(about_author, container, false);

    }
}
