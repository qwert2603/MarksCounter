package com.example.alex.markscounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class AboutDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about, null);

        view.findViewById(R.id.background_Linear_layout)
                .setBackgroundColor(MainActivity.isDarkTheme ? getResources().getColor(R.color.background_day) : Color.WHITE);

        int textViewColor = getResources().getColor(MainActivity.isDarkTheme ? R.color.text_night : R.color.text_day);
        view.<TextView>findViewById(R.id.title_text_view).setTextColor(textViewColor);
        TextView messageTextView = view.findViewById(R.id.message_text_view);
        messageTextView.setTextColor(textViewColor);
        messageTextView.setText(Html.fromHtml(getString(R.string.about_message)));

        view.findViewById(R.id.good_job_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/details?id=com.qwert2603.good_job";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                dismissAllowingStateLoss();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}