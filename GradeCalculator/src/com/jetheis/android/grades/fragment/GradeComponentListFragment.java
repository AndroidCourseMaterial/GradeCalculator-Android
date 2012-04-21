package com.jetheis.android.grades.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.jetheis.android.grades.R;
import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.model.GradeComponent;

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
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, true);
                mOnGradeComponentSelectedListener.onGradeComponentSelected(
                        GradeComponentListFragment.this,
                        (GradeComponent) getListAdapter().getItem(position));
                return false;
            }

        });

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

            if (result == null) {
                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                result = inflater.inflate(R.layout.grade_component_list_line_item, null);
            }

            final GradeComponent currentComponent = getItem(position);

            TextView nameTextView = (TextView) result
                    .findViewById(R.id.grade_component_list_line_item_name_text_view);
            nameTextView.setText(currentComponent.getName());

            return result;
        }

    }

}
