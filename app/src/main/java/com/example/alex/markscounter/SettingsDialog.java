package com.example.alex.markscounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SettingsDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_settings, null);

        view.findViewById(R.id.background_Linear_layout)
                .setBackgroundColor(MainActivity.isDarkTheme ? getResources().getColor(R.color.background_night) : Color.WHITE);

        int textViewColor = getResources().getColor(MainActivity.isDarkTheme ? R.color.text_night : R.color.text_day);
        view.<TextView>findViewById(R.id.title_text_view).setTextColor(textViewColor);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        CheckBox mark1CheckBox = view.findViewById(R.id.mark_1_CheckBox);
        mark1CheckBox.setTextColor(textViewColor);
        mark1CheckBox.setChecked(prefs.getBoolean(MainActivity.KEY_MARK_1, false));
        mark1CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean(MainActivity.KEY_MARK_1, isChecked).apply();
            }
        });

        view.findViewById(R.id.ok_TextView).setOnClickListener(v -> dismissAllowingStateLoss());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}
