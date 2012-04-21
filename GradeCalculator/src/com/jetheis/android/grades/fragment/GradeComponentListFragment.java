package com.jetheis.android.grades.fragment;

import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.jetheis.android.grades.R;
import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.model.GradeComponent;
import com.jetheis.android.grades.model.PercentageGradeComponent;
import com.jetheis.android.grades.model.PointTotalGradeComponent;

public class GradeComponentListFragment extends SherlockListFragment {

    public interface OnGradeComponentSelectedListener {
        public void onGradeComponentSelected(GradeComponentListFragment fragment,
                GradeComponent gradeComponent);
    }

    private Course mCourse;
    private OnGradeComponentSelectedListener mOnGradeComponentSelectedListener;

    public void initialize(Course parentCourse,
            OnGradeComponentSelectedListener onGradeComponentSelectedListener) {
        mCourse = parentCourse;
        mOnGradeComponentSelectedListener = onGradeComponentSelectedListener;

        final ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        refreshGradeComponents();
    }

    public void refreshGradeComponents() {
        mCourse.loadConnectedObjects();
        setListAdapter(new GradeComponentArrayAdapter(getActivity(), mCourse.getGradeComponents()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grade_component_list_fragment, null);
    }

    private class GradeComponentArrayAdapter extends ArrayAdapter<GradeComponent> {

        private Context mContext;

        public GradeComponentArrayAdapter(Context context, List<GradeComponent> gradeComponents) {
            super(context, R.layout.grade_component_list_line_item,
                    R.id.grade_component_list_line_item_name_text_view, gradeComponents);

            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result = convertView;

            final int index = position;

            if (result == null) {
                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                result = inflater.inflate(R.layout.grade_component_list_line_item, null);
            }

            final GradeComponent currentComponent = getItem(position);

            TextView nameTextView = (TextView) result
                    .findViewById(R.id.grade_component_list_line_item_name_text_view);
            nameTextView.setText(currentComponent.getName());

            final TextView maxTextView = (TextView) result
                    .findViewById(R.id.grade_component_list_line_item_max_text_view);
            final TextView earnedTextView = (TextView) result
                    .findViewById(R.id.grade_component_list_line_item_earned_text_view);

            final NumberFormat numberFormat = NumberFormat.getInstance();
            final NumberFormat percentageFormat = NumberFormat.getPercentInstance();

            SeekBar earnedSeekBar = (SeekBar) result
                    .findViewById(R.id.grade_component_list_line_item_earned_seek_bar);

            if (currentComponent instanceof PointTotalGradeComponent) {
                final PointTotalGradeComponent pointComponent = (PointTotalGradeComponent) currentComponent;
                maxTextView.setText(getString(R.string.grade_component_list_fragment_total_points,
                        numberFormat.format(pointComponent.getTotalPoints())));
                earnedTextView.setText(getString(
                        R.string.grade_component_list_fragment_points_earned,
                        numberFormat.format(pointComponent.getPointsEarned())));

                earnedSeekBar.setMax((int) pointComponent.getTotalPoints());
                earnedSeekBar.setProgress((int) pointComponent.getPointsEarned());
                earnedSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        currentComponent.save();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        pointComponent.setPointsEarned(seekBar.getProgress());
                        earnedTextView.setText(getString(
                                R.string.grade_component_list_fragment_points_earned,
                                numberFormat.format(pointComponent.getPointsEarned())));
                    }
                });
            } else {
                final PercentageGradeComponent percentageComponent = (PercentageGradeComponent) currentComponent;
                maxTextView.setText(getString(R.string.grade_component_list_fragment_weight,
                        percentageFormat.format(percentageComponent.getWeight() / 100.0)));
                earnedTextView
                        .setText(getString(
                                R.string.grade_component_list_fragment_percentage_earned,
                                percentageFormat.format(percentageComponent.getEarnedPercentage() / 100.0)));

                earnedSeekBar.setMax(100);
                earnedSeekBar.setProgress((int) percentageComponent.getEarnedPercentage());
                earnedSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        currentComponent.save();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        percentageComponent.setEarnedPercentage(seekBar.getProgress());
                        earnedTextView
                                .setText(getString(
                                        R.string.grade_component_list_fragment_percentage_earned,
                                        percentageFormat.format(percentageComponent
                                                .getEarnedPercentage() / 100.0)));
                    }
                });
            }

            result.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    mOnGradeComponentSelectedListener.onGradeComponentSelected(
                            GradeComponentListFragment.this, getItem(index));
                    return false;
                }

            });

            return result;
        }
    }

}
