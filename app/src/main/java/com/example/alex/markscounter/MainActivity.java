package com.example.alex.markscounter;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final String MARKS_KEY = "marks";

    private TextView mAverageTextView;
    private TextView mMarksTextView;

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
            mMarks = (LinkedList<Integer>) savedInstanceState.getSerializable(MARKS_KEY);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.<ViewGroup>findViewById(R.id.root_LinearLayout)
                    .getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);
        }

        mAverageTextView = findViewById(R.id.average_text_view);
        mMarksTextView = findViewById(R.id.marks_text_view);

        findViewById(R.id.mark_2_button).setOnClickListener(getMarkButtonOnClickListener(2));
        findViewById(R.id.mark_3_button).setOnClickListener(getMarkButtonOnClickListener(3));
        findViewById(R.id.mark_4_button).setOnClickListener(getMarkButtonOnClickListener(4));
        findViewById(R.id.mark_5_button).setOnClickListener(getMarkButtonOnClickListener(5));

        findViewById(R.id.backspace_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mMarks.isEmpty()) {
                    mMarks.removeLast();
                }
                updateTextViews();
            }
        });

        findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarks.clear();
                updateTextViews();
            }
        });

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
        for (int i : mMarks) {
            stringBuilder.append(i);
            sum += i;
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

}
