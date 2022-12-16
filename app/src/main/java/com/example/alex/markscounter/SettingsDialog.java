package com.example.alex.markscounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class SettingsDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_settings, null);

        view.findViewById(R.id.background_Linear_layout)
                .setBackgroundColor(MainActivity.isDarkTheme ? getResources().getColor(R.color.background_night) : Color.WHITE);

        int textViewColor = getResources().getColor(MainActivity.isDarkTheme ? R.color.text_night : R.color.text_day);
        view.<TextView>findViewById(R.id.title_text_view).setTextColor(textViewColor);

        TextView messageTextView = view.findViewById(R.id.message_text_view);
        messageTextView.setTextColor(textViewColor);
        messageTextView.setText(getAboutMessage());
        messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
        messageTextView.setHighlightColor(Color.TRANSPARENT);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        CheckBox mark1CheckBox = view.findViewById(R.id.mark_1_CheckBox);
        mark1CheckBox.setTextColor(textViewColor);
        mark1CheckBox.setChecked(prefs.getBoolean(MainActivity.KEY_MARK_1, false));
        mark1CheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(MainActivity.KEY_MARK_1, isChecked).apply()
        );

        view.findViewById(R.id.ok_TextView).setOnClickListener(v -> dismissAllowingStateLoss());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private CharSequence getAboutMessage() {
        SpannableStringBuilder spannable = SpannableStringBuilder.valueOf(getString(R.string.about_message));

        spannable.setSpan(
                new URLSpan("https://www.shutterstock.com/ru/g/Annvzhdanova") {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                    }
                },
                spannable.length() - 6,
                spannable.length() - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return spannable;
    }
}
