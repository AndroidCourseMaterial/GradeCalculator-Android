package com.jetheis.android.grades.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.jetheis.android.grades.R;
import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.model.Course.CourseType;

public class EditCourseDialogFragment extends AddCourseDialogFragment {

    private Course mCourse;

    public EditCourseDialogFragment(Course course, OnCoursesChangeListener onCoursesChangedListener) {
        super(onCoursesChangedListener);

        if (course == null) {
            throw new NullPointerException("Provided course cannot be null");
        }

        mCourse = course;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: set title
        View result = super.onCreateView(inflater, container, savedInstanceState);

        final EditText nameTextEdit = (EditText) result
                .findViewById(R.id.add_course_dialog_fragment_name_edit_text);
        final RadioButton pointTotalRadioButton = (RadioButton) result
                .findViewById(R.id.add_course_dialog_fragment_point_total_radio_button);
        final RadioButton percentageRadioButton = (RadioButton) result
                .findViewById(R.id.add_course_dialog_fragment_percentage_radio_button);

        nameTextEdit.setText(mCourse.getName());

        if (mCourse.getCourseType() == CourseType.POINT_TOTAL) {
            pointTotalRadioButton.setChecked(true);
        } else {
            percentageRadioButton.setChecked(true);
        }

        Button saveButton = (Button) result
                .findViewById(R.id.add_course_dialog_fragment_create_button);
        saveButton.setText("Save");
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCourse.setName(nameTextEdit.getText().toString());
                if (pointTotalRadioButton.isChecked()) {
                    mCourse.setCourseType(CourseType.POINT_TOTAL);
                } else {
                    mCourse.setCourseType(CourseType.PERCENTAGE_WEIGHTING);
                }

                mCourse.save();
                dismiss();
                mOnCoursesChangedListener.onCoursesChanged();
            }
        });

        return result;
    }

}