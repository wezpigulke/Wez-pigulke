package com.example.kuba.dsadsax;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class OpenDialog extends AppCompatDialogFragment {

    String warning;
    public void setValue(String warning) {
        this.warning = warning;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        builder.setMessage(warning);
        builder.setNegativeButton("OK", (dialog, which) -> dialog.cancel());

        return builder.create();
    }
}
