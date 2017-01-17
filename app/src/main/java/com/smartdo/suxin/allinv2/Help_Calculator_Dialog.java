package com.smartdo.suxin.allinv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by suxin on 10/1/16.
 */

public class Help_Calculator_Dialog extends DialogFragment {

    public static Help_Calculator_Dialog newInstance() {
        return new Help_Calculator_Dialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.help_calculator_dialog, null);

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton(getString(R.string.got_it), null)
                .show();
    }
}
