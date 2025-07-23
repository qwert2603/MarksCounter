package com.example.alex.markscounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class ForSaleDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_for_sale, null);

        view.findViewById(R.id.background_Linear_layout)
                .setBackgroundColor(MainActivity.isDarkTheme ? getResources().getColor(R.color.background_night) : Color.WHITE);

        int textViewColor = getResources().getColor(MainActivity.isDarkTheme ? R.color.text_night : R.color.text_day);
        view.<TextView>findViewById(R.id.details_text_view).setTextColor(textViewColor);

        view.findViewById(R.id.ok_TextView).setOnClickListener(v -> dismissAllowingStateLoss());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}
