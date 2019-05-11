package com.example.alex.markscounter;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private static boolean IS_NIGHT = false;

    private static final String MARKS_KEY = "marks";

    private TextView mAverageTextView;
    private TextView mAverageLabelTextView;
    private TextView mMarksTextView;
    private LinearLayout mRootLinearLayout;
    private ImageView mToggleNightButton;

    private final List<TextView> mButtons = new ArrayList<>();
    private TextView mBackspaceButton;
    private TextView mClearButton;

    private LinkedList<Integer> mMarks = new LinkedList<>();

    private View.OnClickListener getMarkButtonOnClickListener(final int mark) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMarks.size() < 42) {
                    mMarks.add(mark);
                    updateTextViews();
                }
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            final Serializable serializable = savedInstanceState.getSerializable(MARKS_KEY);
            if (serializable != null) {
                mMarks = new LinkedList<>((List<Integer>) serializable);
            }
        }

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
                IS_NIGHT = !IS_NIGHT;
                updateNight();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MARKS_KEY, mMarks);
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
        String res = stringBuilder.toString();
        if (res.equals("")) {
            res = getString(R.string.empty);
        }
        mMarksTextView.setText(res);
        if (!mMarks.isEmpty()) {
            long i = Math.round((sum * 100.0) / mMarks.size());
            mAverageTextView.setText(String.format(Locale.getDefault(), "%.2f", i / 100.0));
        } else {
            mAverageTextView.setText(R.string.text_n_a);
        }
    }

    private void updateNight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(IS_NIGHT ? 0 : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(IS_NIGHT ? 0xFF1c2533 : Color.WHITE);
        }

        mRootLinearLayout.setBackgroundColor(IS_NIGHT ? 0xFF1c2533 : Color.WHITE);

        int textViewColor = getResources().getColor(IS_NIGHT ? R.color.text_night : R.color.text_day);
        mAverageTextView.setTextColor(textViewColor);
        mAverageLabelTextView.setTextColor(textViewColor);
        mMarksTextView.setTextColor(textViewColor);

        mToggleNightButton.setImageDrawable(getResources().getDrawable(IS_NIGHT ? R.drawable.ic_sun : R.drawable.ic_moon));
        mToggleNightButton.setBackground(getResources().getDrawable(IS_NIGHT ? R.drawable.background_button_toogle_night_night : R.drawable.background_button_toogle_night));

        for (TextView mButton : mButtons) {
            mButton.setTextColor(getResources().getColorStateList(IS_NIGHT ? R.color.button_text_color_night : R.color.button_text_color));
        }

        mBackspaceButton.setBackground(getResources().getDrawable(IS_NIGHT ? R.drawable.background_button_remote_night : R.drawable.background_button_remote));
        mClearButton.setBackground(getResources().getDrawable(IS_NIGHT ? R.drawable.background_button_remote_night : R.drawable.background_button_remote));
    }
}
