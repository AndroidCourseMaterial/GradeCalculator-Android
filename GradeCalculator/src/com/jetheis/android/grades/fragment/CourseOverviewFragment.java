package com.jetheis.android.grades.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jetheis.android.grades.R;

public class CourseOverviewFragment extends SherlockFragment {

    TextView mInfoTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.course_overview_fragment, null);
        mInfoTextView = (TextView) result.findViewById(R.id.course_overview_fragment_text_view);
        return result;
    }

    public void setText(CharSequence text) {
        mInfoTextView.setText(text);
    }

}
