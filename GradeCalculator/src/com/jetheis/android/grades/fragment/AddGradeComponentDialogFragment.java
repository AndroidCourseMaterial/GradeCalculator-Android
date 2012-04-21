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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.jetheis.android.grades.R;
import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.model.Course.CourseType;
import com.jetheis.android.grades.model.PercentageGradeComponent;
import com.jetheis.android.grades.model.PointTotalGradeComponent;

public class AddGradeComponentDialogFragment extends SherlockDialogFragment {

    public static interface OnGradeComponentsChangedListener {
        public void onGradeComponentsChanged();
    }

    private Course mCourse;
    protected OnGradeComponentsChangedListener mOnChangeListener;

    public AddGradeComponentDialogFragment(Course course,
            OnGradeComponentsChangedListener onChangeListener) {
        mCourse = course;
        mOnChangeListener = onChangeListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, com.actionbarsherlock.R.style.Theme_Sherlock_Light_Dialog);
    }

    private boolean isValidData(String nameText, String maxValueText) {
        try {
            double maxValue = Double.parseDouble(maxValueText);
            return maxValue > 0 && nameText.length() > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.add_grade_component_dialog_title));

        View result = inflater.inflate(R.layout.add_grade_component_dialog_fragment, container,
                false);

        TextView maxValueLabel = (TextView) result
                .findViewById(R.id.add_grade_component_dialog_fragment_max_value_label);

        if (mCourse.getCourseType() == CourseType.POINT_TOTAL) {
            maxValueLabel.setText(getString(R.string.add_grade_component_dialog_total_points));
        } else {
            maxValueLabel.setText(getString(R.string.add_grade_component_dialog_weight));
        }

        final Button cancelButton = (Button) result
                .findViewById(R.id.add_grade_component_dialog_fragment_cancel_button);
        final Button createButton = (Button) result
                .findViewById(R.id.add_grade_component_dialog_fragment_create_button);
        
        createButton.setEnabled(false);

        final EditText nameText = (EditText) result
                .findViewById(R.id.add_grade_component_dialog_fragment_name_text);
        final EditText maxValueText = (EditText) result
                .findViewById(R.id.add_grade_component_dialog_fragment_max_value);

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                createButton.setEnabled(isValidData(nameText.getText().toString(), maxValueText
                        .getText().toString()));
            }
        };
        
        nameText.addTextChangedListener(textWatcher);
        maxValueText.addTextChangedListener(textWatcher);

        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }

        });

        createButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCourse.getCourseType() == CourseType.POINT_TOTAL) {
                    PointTotalGradeComponent gradeComponent = new PointTotalGradeComponent();
                    gradeComponent.setName(nameText.getText().toString());
                    gradeComponent.setTotalPoints(Double.parseDouble(maxValueText.getText()
                            .toString()));
                    mCourse.addGradeComponent(gradeComponent);
                } else {
                    PercentageGradeComponent gradeComponent = new PercentageGradeComponent();
                    gradeComponent.setName(nameText.getText().toString());
                    gradeComponent.setWeight(Double.parseDouble(maxValueText.getText().toString()));
                    mCourse.addGradeComponent(gradeComponent);
                }

                mCourse.save();

                mOnChangeListener.onGradeComponentsChanged();
                dismiss();
            }

        });

        return result;
    }
}
