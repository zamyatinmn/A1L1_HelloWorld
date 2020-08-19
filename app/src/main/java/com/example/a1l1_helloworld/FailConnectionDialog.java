package com.example.a1l1_helloworld;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class FailConnectionDialog extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        return builder
                .setTitle(R.string.error)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.error_text)
                .setNegativeButton("ОК", null)
                .create();
    }
}