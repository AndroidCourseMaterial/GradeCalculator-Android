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

package com.jetheis.android.grades.activity;

import java.text.NumberFormat;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jetheis.android.grades.R;
import com.jetheis.android.grades.fragment.AddGradeComponentDialogFragment;
import com.jetheis.android.grades.fragment.AddGradeComponentDialogFragment.OnGradeComponentsChangedListener;
import com.jetheis.android.grades.fragment.CourseOverviewFragment;
import com.jetheis.android.grades.fragment.EditGradeComponentDialogFragment;
import com.jetheis.android.grades.fragment.GradeComponentListFragment;
import com.jetheis.android.grades.fragment.GradeComponentListFragment.OnGradeComponentSelectedListener;
import com.jetheis.android.grades.fragment.GradeComponentListFragment.OnOverallGradeChangedListener;
import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.model.GradeComponent;

public class CourseActivity extends SherlockFragmentActivity {

    public static final String INTENT_KEY_COURSE = "course";

    private static final String CREATE_GRADE_COMPONENT_DIALOG_TAG = "CreateGradeComponentDialog";
    private static final String EDIT_GRADE_COMPONENT_DIALOG_TAG = "EditGradeComponentDialog";

    private Course mCourse;
    private ActionBar mActionBar;
    private GradeComponentListFragment mListFragment;
    private CourseOverviewFragment mOverviewFragment;
    private GradeComponent mCurrentGradeComponent;
    private ActionMode.Callback mGradeComponentSelectedCallback = new ActionMode.Callback() {

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.course_context_menu, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
            case R.id.course_context_menu_edit:
                EditGradeComponentDialogFragment editDialog = new EditGradeComponentDialogFragment(
                        mCurrentGradeComponent, new OnGradeComponentsChangedListener() {

                            @Override
                            public void onGradeComponentsChanged() {
                                mListFragment.refreshGradeComponents();
                                updateOverview();
                            }

                        });

                editDialog.show(getSupportFragmentManager(), EDIT_GRADE_COMPONENT_DIALOG_TAG);
                mode.finish();
                return true;
            case R.id.course_context_menu_delete:
                mCurrentGradeComponent.destroy();
                mListFragment.refreshGradeComponents();
                updateOverview();
                mode.finish();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_activity);

        mCourse = getIntent().getParcelableExtra(INTENT_KEY_COURSE);

        mActionBar = getSupportActionBar();
        mActionBar.setTitle(mCourse.getName());
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mOverviewFragment = (CourseOverviewFragment) getSupportFragmentManager().findFragmentById(
                R.id.course_activity_course_overview_fragment);

        mListFragment = (GradeComponentListFragment) getSupportFragmentManager().findFragmentById(
                R.id.course_activity_grade_component_list_fragment);
        mListFragment.initialize(mCourse, new OnGradeComponentSelectedListener() {

            @Override
            public void onGradeComponentSelected(GradeComponentListFragment fragment,
                    GradeComponent gradeComponent) {
                mCurrentGradeComponent = gradeComponent;
                startActionMode(mGradeComponentSelectedCallback);
            }

        }, new OnOverallGradeChangedListener() {

            @Override
            public void onOverallGradeChanged() {
                updateOverview();
            }
        });

        updateOverview();
    }

    private void updateOverview() {
        mOverviewFragment.setText(getString(R.string.course_overview_fragment_overall_grade,
                NumberFormat.getPercentInstance().format(mCourse.getOverallScore())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        case R.id.course_menu_add:
            AddGradeComponentDialogFragment addGradeComponentDialog = new AddGradeComponentDialogFragment(
                    mCourse, new OnGradeComponentsChangedListener() {

                        @Override
                        public void onGradeComponentsChanged() {
                            mListFragment.refreshGradeComponents();
                            updateOverview();
                        }

                    });
            addGradeComponentDialog.show(getSupportFragmentManager(),
                    CREATE_GRADE_COMPONENT_DIALOG_TAG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
