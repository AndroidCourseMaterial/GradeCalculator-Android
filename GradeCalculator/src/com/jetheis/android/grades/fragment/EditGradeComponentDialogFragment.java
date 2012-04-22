package com.jetheis.android.grades.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jetheis.android.grades.R;
import com.jetheis.android.grades.model.GradeComponent;
import com.jetheis.android.grades.model.PercentageGradeComponent;
import com.jetheis.android.grades.model.PointTotalGradeComponent;

public class EditGradeComponentDialogFragment extends AddGradeComponentDialogFragment {

    GradeComponent mGradeComponent;

    public EditGradeComponentDialogFragment(GradeComponent gradeComponent,
            OnGradeComponentsChangedListener onGradeComponentsChangedListener) {
        super(gradeComponent.getCourse(), onGradeComponentsChangedListener);
        mGradeComponent = gradeComponent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = super.onCreateView(inflater, container, savedInstanceState);

        final EditText nameText = (EditText) result
                .findViewById(R.id.add_grade_component_dialog_fragment_name_text);
        final EditText maxValueText = (EditText) result
                .findViewById(R.id.add_grade_component_dialog_fragment_max_value);
        final EditText earnedText = (EditText) result
                .findViewById(R.id.add_grade_component_dialog_fragment_earned_edit_text);

        nameText.setText(mGradeComponent.getName());

        if (mGradeComponent instanceof PointTotalGradeComponent) {
            maxValueText.setText(Double.toString(((PointTotalGradeComponent) mGradeComponent)
                    .getTotalPoints()));
            earnedText.setText(Double.toString(((PointTotalGradeComponent) mGradeComponent)
                    .getPointsEarned()));
        } else {
            maxValueText.setText(Double.toString(((PercentageGradeComponent) mGradeComponent)
                    .getWeight()));
            earnedText.setText(Double.toString(((PercentageGradeComponent) mGradeComponent)
                    .getEarnedPercentage()));
        }

        final Button saveButton = (Button) result
                .findViewById(R.id.add_grade_component_dialog_fragment_create_button);

        saveButton.setText(getString(R.string.edit_grade_component_dialog_save_button));
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mGradeComponent instanceof PointTotalGradeComponent) {
                    PointTotalGradeComponent pointComponent = (PointTotalGradeComponent) mGradeComponent;
                    pointComponent.setName(nameText.getText().toString());
                    pointComponent.setTotalPoints(Double.parseDouble(maxValueText.getText()
                            .toString()));
                    pointComponent.setPointsEarned(Double.parseDouble(earnedText.getText()
                            .toString()));
                    pointComponent.save();
                } else {
                    PercentageGradeComponent percentageComponent = (PercentageGradeComponent) mGradeComponent;
                    percentageComponent.setName(nameText.getText().toString());
                    percentageComponent.setWeight(Double.parseDouble(maxValueText.getText()
                            .toString()));
                    percentageComponent.setEarnedPercentage(Double.parseDouble(earnedText.getText()
                            .toString()));
                    percentageComponent.save();
                }

                mOnChangeListener.onGradeComponentsChanged();
                dismiss();
            }
        });

        return result;
    }
}
