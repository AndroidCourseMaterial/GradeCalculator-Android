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

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jetheis.android.grades.Constants;
import com.jetheis.android.grades.R;
import com.jetheis.android.grades.fragment.AddCourseDialogFragment;
import com.jetheis.android.grades.listadapter.CourseArrayAdapter;
import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.storage.DatabaseHelper;

public class CourseListActivity extends SherlockFragmentActivity {

    private static final String CREATE_COURSE_DIALOG_TAG = "CreateCourse";

    private ActionBar mActionBar;
    private List<Course> mCourses;
    private CourseArrayAdapter mCourseArrayAdapter;
    private SherlockListFragment mListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list_activity);

        mActionBar = getSupportActionBar();
        mActionBar.setTitle(getString(R.string.course_list_activity_title));

        mListFragment = (SherlockListFragment) getSupportFragmentManager().findFragmentById(
                R.id.course_list_activity_course_list_fragment);

        DatabaseHelper.initializeDatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCourseList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Course.destroyAllCourses();
        DatabaseHelper.getInstance().close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.course_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
        case R.id.courses_menu_add:
            AddCourseDialogFragment fragment = new AddCourseDialogFragment(this);
            fragment.show(getSupportFragmentManager(), CREATE_COURSE_DIALOG_TAG);
            return true;
        case R.id.courses_menu_about:
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return false;
    }
    
    public void refreshCourseList() {
        mCourses = Course.getAllCourses();
        mCourseArrayAdapter = new CourseArrayAdapter(this, mCourses);

        mListFragment.setListAdapter(mCourseArrayAdapter);

        if (mCourses.size() > 0) {
            Log.d(Constants.TAG, "Found something");
            // TODO hide text
        }
    }
}