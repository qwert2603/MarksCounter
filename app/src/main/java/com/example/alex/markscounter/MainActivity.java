package com.example.alex.markscounter;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int MAX_MARKS_COUNT = 78;

    private static final String KEY_MARKS = "marks";
    private static final String KEY_DARK_THEME = "dark_theme";

    private SharedPreferences prefs;

    public static boolean isDarkTheme;

    private TextView mAverageTextView;
    private TextView mAverageLabelTextView;
    private TextView mMarksTextView;
    private LinearLayout mRootLinearLayout;
    private ImageView mToggleNightButton;
    private ImageView mAboutButton;

    private final List<TextView> mButtons = new ArrayList<>();
    private TextView mBackspaceButton;
    private TextView mClearButton;

    private LinkedList<Integer> mMarks = new LinkedList<>();

    private View.OnClickListener getMarkButtonOnClickListener(final int mark) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMarks.size() < MAX_MARKS_COUNT) {
                    mMarks.add(mark);
                    updateTextViews();
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final String marksString = prefs.getString(KEY_MARKS, "");
        if (marksString != null) {
            for (int i = 0; i < marksString.length(); i++) {
                final char c = marksString.charAt(i);
                if (c == '2') mMarks.add(2);
                if (c == '3') mMarks.add(3);
                if (c == '4') mMarks.add(4);
                if (c == '5') mMarks.add(5);
            }
        }

        isDarkTheme = prefs.getBoolean(KEY_DARK_THEME, false);

        mRootLinearLayout = findViewById(R.id.root_LinearLayout);
        mRootLinearLayout
                .getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);

        mAverageTextView = findViewById(R.id.average_text_view);
        mAverageLabelTextView = findViewById(R.id.average_label_text_view);
        mMarksTextView = findViewById(R.id.marks_text_view);
        mToggleNightButton = findViewById(R.id.toggle_night_button);
        mToggleNightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDarkTheme = !isDarkTheme;
                prefs.edit().putBoolean(KEY_DARK_THEME, isDarkTheme).apply();
                updateNight();
                updateTextViews();
            }
        });
        mAboutButton = findViewById(R.id.about_button);
        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AboutDialog().show(getFragmentManager(), null);
            }
        });

        mButtons.add(this.<TextView>findViewById(R.id.mark_2_button));
        mButtons.add(this.<TextView>findViewById(R.id.mark_3_button));
        mButtons.add(this.<TextView>findViewById(R.id.mark_4_button));
        mButtons.add(this.<TextView>findViewById(R.id.mark_5_button));
        mButtons.add(this.<TextView>findViewById(R.id.backspace_button));
        mButtons.add(this.<TextView>findViewById(R.id.clear_button));

        mBackspaceButton = findViewById(R.id.backspace_button);
        mClearButton = findViewById(R.id.clear_button);

        findViewById(R.id.mark_2_button).setOnClickListener(getMarkButtonOnClickListener(2));
        findViewById(R.id.mark_3_button).setOnClickListener(getMarkButtonOnClickListener(3));
        findViewById(R.id.mark_4_button).setOnClickListener(getMarkButtonOnClickListener(4));
        findViewById(R.id.mark_5_button).setOnClickListener(getMarkButtonOnClickListener(5));

        mBackspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mMarks.isEmpty()) {
                    mMarks.removeLast();
                }
                updateTextViews();
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarks.clear();
                updateTextViews();
            }
        });

        updateNight();
        updateTextViews();
    }

    private void updateTextViews() {
        StringBuilder stringBuilder = new StringBuilder();
        int sum = 0;
        for (int i = 0; i < mMarks.size(); i++) {
            if (i > 0 && i % 4 == 0) stringBuilder.append(' ');
            int mark = mMarks.get(i);
            stringBuilder.append(mark);
            sum += mark;
        }
        String marksString = stringBuilder.toString();
        if (marksString.equals("")) {
            marksString = getString(R.string.empty);
        }
        mMarksTextView.setText(marksString);
        prefs.edit().putString(KEY_MARKS, marksString).apply();

        if (!mMarks.isEmpty()) {
            final long i = Math.round((sum * 100.0) / mMarks.size());
            final double average = i / 100.0;
            mAverageTextView.setText(String.format(Locale.getDefault(), "%.2f", average));
            mAverageTextView.setTextColor(getResources().getColor(getAverageColor(average)));
        } else {
            mAverageTextView.setText(R.string.text_n_a);
            mAverageTextView.setTextColor(getResources().getColor(getTextColor()));
        }
    }

    private void updateNight() {
        int backgroundColor = isDarkTheme ? getResources().getColor(R.color.background_day) : Color.WHITE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(isDarkTheme ? 0 : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(backgroundColor);
        }

        mRootLinearLayout.setBackgroundColor(backgroundColor);

        int textViewColor = getResources().getColor(getTextColor());
        mAverageLabelTextView.setTextColor(textViewColor);
        mMarksTextView.setTextColor(textViewColor);

        mToggleNightButton.setImageDrawable(getResources().getDrawable(isDarkTheme ? R.drawable.ic_sun : R.drawable.ic_moon));
        mToggleNightButton.setBackground(getResources().getDrawable(isDarkTheme ? R.drawable.background_icon_button_night : R.drawable.background_icon_button));
        mAboutButton.setBackground(getResources().getDrawable(isDarkTheme ? R.drawable.background_icon_button_night : R.drawable.background_icon_button));

        for (TextView mButton : mButtons) {
            mButton.setTextColor(getResources().getColorStateList(isDarkTheme ? R.color.button_text_color_night : R.color.button_text_color));
        }

        mBackspaceButton.setBackground(getResources().getDrawable(isDarkTheme ? R.drawable.background_button_remote_night : R.drawable.background_button_remote));
        mClearButton.setBackground(getResources().getDrawable(isDarkTheme ? R.drawable.background_button_remote_night : R.drawable.background_button_remote));
    }

    private static int getAverageColor(double average) {
        if (average == 2.5 || average == 3.5 || average == 4.5) {
            return isDarkTheme ? R.color.mark_hz_night : R.color.mark_hz_day;
        }
        if (average < 2.5) return R.color.mark_2;
        if (average < 3.5) return R.color.mark_3;
        if (average < 4.5) return R.color.mark_4;
        return R.color.mark_5;
    }

    private static int getTextColor() {
        return isDarkTheme ? R.color.text_night : R.color.text_day;
    }
}
