/*
 * Copyright (C) 2012 Jimmy Theis. Licensed under the MIT License:
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        
        setStyle(STYLE_NORMAL, com.actionbarsherlock.R.style.Theme_Sherlock_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.edit_course_dialog_fragment_title));
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
        saveButton.setText(getString(R.string.edit_course_dialog_fragment_save_course_button));
        saveButton.setEnabled(true);
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
