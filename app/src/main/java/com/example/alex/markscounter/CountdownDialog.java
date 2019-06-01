package com.example.alex.markscounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CountdownDialog extends DialogFragment {
    @SuppressLint("SetTextI18n")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about, null);

        view.findViewById(R.id.background_Linear_layout)
                .setBackgroundColor(MainActivity.isDarkTheme ? getResources().getColor(R.color.background_day) : Color.WHITE);

        TextView title = view.findViewById(R.id.title_text_view);
        title.setText("It's the final countdown!");
        title.setTextColor(getResources().getColor(MainActivity.isDarkTheme ? R.color.text_night : R.color.text_day));

        view.findViewById(R.id.message_text_view).setVisibility(View.GONE);

        final TextView button = view.findViewById(R.id.good_job_button);
        button.setText("Открыть");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/watch?v=9jK-NcRmVcw";
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