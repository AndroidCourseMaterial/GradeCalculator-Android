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

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.jetheis.android.grades.R;
import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.model.Course.CourseType;

public class AddCourseDialogFragment extends SherlockDialogFragment {

    public interface OnCoursesChangeListener {
        public void onCoursesChanged();
    }

    protected OnCoursesChangeListener mOnCoursesChangedListener;

    public AddCourseDialogFragment(OnCoursesChangeListener onCoursesChangedListener) {
        super();

        mOnCoursesChangedListener = onCoursesChangedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, com.actionbarsherlock.R.style.Theme_Sherlock_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getActivity().getString(R.string.add_course_dialog_fragment_title));

        View result = inflater.inflate(R.layout.add_course_dialog_fragment, container, false);

        final EditText nameTextEdit = (EditText) result
                .findViewById(R.id.add_course_dialog_fragment_name_edit_text);
        final RadioButton pointTotalRadioButton = (RadioButton) result
                .findViewById(R.id.add_course_dialog_fragment_point_total_radio_button);

        Button createButton = (Button) result
                .findViewById(R.id.add_course_dialog_fragment_create_button);
        Button cancelButton = (Button) result
                .findViewById(R.id.add_course_dialog_fragment_cancel_button);

        createButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Course newCourse = new Course();

                newCourse.setName(nameTextEdit.getText().toString());

                if (pointTotalRadioButton.isChecked()) {
                    newCourse.setCourseType(CourseType.POINT_TOTAL);
                } else {
                    newCourse.setCourseType(CourseType.PERCENTAGE_WEIGHTING);
                }

                newCourse.save();

                dismiss();
                mOnCoursesChangedListener.onCoursesChanged();
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return result;
    }
}
