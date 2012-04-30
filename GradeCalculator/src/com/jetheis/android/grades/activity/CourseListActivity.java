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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jetheis.android.grades.Constants;
import com.jetheis.android.grades.Constants.LicenseType;
import com.jetheis.android.grades.R;
import com.jetheis.android.grades.billing.BillingWrapper;
import com.jetheis.android.grades.billing.BillingWrapper.OnBillingReadyListener;
import com.jetheis.android.grades.billing.BillingWrapper.OnPurchaseStateChangedListener;
import com.jetheis.android.grades.billing.FreeBillingWrapper;
import com.jetheis.android.grades.billing.Security;
import com.jetheis.android.grades.billing.googleplay.GooglePlayBillingWrapper;
import com.jetheis.android.grades.fragment.AddCourseDialogFragment;
import com.jetheis.android.grades.fragment.AddCourseDialogFragment.OnCoursesChangeListener;
import com.jetheis.android.grades.fragment.BuyFullVersionFragment;
import com.jetheis.android.grades.fragment.CourseListFragment;
import com.jetheis.android.grades.fragment.EditCourseDialogFragment;
import com.jetheis.android.grades.listadapter.CourseArrayAdapter;
import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.storage.DatabaseHelper;

public class CourseListActivity extends SherlockFragmentActivity {

    private static final String CREATE_COURSE_DIALOG_TAG = "CreateCourseDialog";
    private static final String EDIT_COURSE_DIALOG_TAG = "EditCourseDialog";

    private ActionBar mActionBar;
    private List<Course> mCourses;
    private Course mCurrentlySelectedCourse;
    private CourseArrayAdapter mCourseArrayAdapter;
    private CourseListFragment mListFragment;
    private BuyFullVersionFragment mBuyFullVersionFragment;
    private boolean mFullVersion;
    private BillingWrapper mBillingWrapper;
    private String mItemId;
    private ActionMode.Callback mCourseSelectionCallback = new ActionMode.Callback() {

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.course_list_context_menu, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
            case R.id.course_list_context_menu_edit:
                EditCourseDialogFragment editCourseDialog = new EditCourseDialogFragment(
                        mCurrentlySelectedCourse, new OnCoursesChangeListener() {

                            @Override
                            public void onCoursesChanged() {
                                refreshCourseList();
                            }
                        });

                editCourseDialog.show(getSupportFragmentManager(), EDIT_COURSE_DIALOG_TAG);
                mode.finish();
                return true;
            case R.id.course_list_context_menu_delete:
                mCurrentlySelectedCourse.destroy();
                mCurrentlySelectedCourse = null;
                refreshCourseList();
                mode.finish();
                return true;
            }

            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list_activity);

        mFullVersion = Security.isFullVersionUnlocked(this);

        mActionBar = getSupportActionBar();
        mActionBar.setTitle(getString(R.string.course_list_activity_title));

        mListFragment = (CourseListFragment) getSupportFragmentManager().findFragmentById(
                R.id.course_list_activity_course_list_fragment);
        mListFragment.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mBuyFullVersionFragment = (BuyFullVersionFragment) getSupportFragmentManager()
                .findFragmentById(R.id.course_list_activity_buy_full_version_fragment);

        DatabaseHelper.initializeDatabaseHelper(this);

        if (Constants.LICENSE_TYPE == LicenseType.FREE) {
            mBillingWrapper = FreeBillingWrapper.isInstanceInitialized() ? FreeBillingWrapper
                    .getInstance() : FreeBillingWrapper.initializeIntance(this);
            mItemId = Constants.FREE_ITEM_ID;
        } else if (Constants.LICENSE_TYPE == LicenseType.GOOGLE_PLAY) {
            mBillingWrapper = GooglePlayBillingWrapper.isInstanceInitialized() ? GooglePlayBillingWrapper.getInstance() : GooglePlayBillingWrapper.initializeIntance(this);
        } else if (Constants.LICENSE_TYPE == LicenseType.AMAZON_APPSTORE) {

        }

        mBillingWrapper
                .registerOnPurchaseStateChangedListener(new OnPurchaseStateChangedListener() {

                    @Override
                    public void onPurchaseSuccessful(String itemId) {
                        if (itemId.equals(mItemId)) {
                            unlockFullVersion();
                        }
                    }

                    @Override
                    public void onPurchaseReturend(String itemId) {
                        if (mFullVersion) {
                            Toast.makeText(
                                    CourseListActivity.this,
                                    getString(R.string.course_list_activity_toast_full_version_refunded),
                                    Toast.LENGTH_LONG).show();
                        }

                        lockFullVersion();
                    }

                    @Override
                    public void onPurchaseCancelled(String itemId) {
                        // Don't care
                    }
                });

        mBillingWrapper.registerOnBillingReadyListener(new OnBillingReadyListener() {

            @Override
            public void onBillingReady() {
                Log.i(Constants.TAG, "Billing ready");
                mBillingWrapper.restorePurchases();
            }

            @Override
            public void onBillingNotSupported() {
                Toast.makeText(CourseListActivity.this,
                        getString(R.string.course_list_activity_toast_billing_unavailable),
                        Toast.LENGTH_LONG).show();
                removeFullVersionOptions();
            }
        });

        if (mFullVersion) {
            enterFullVersionMode();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCourseList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mFullVersion) {
            Course.destroyAllCourses();
        }

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
        case R.id.course_list_menu_add:

            if (!mFullVersion && mCourses.size() > 0) {
                Toast.makeText(this,
                        getString(R.string.course_list_activity_toast_add_course_denied),
                        Toast.LENGTH_LONG).show();
                return true;
            }

            AddCourseDialogFragment fragment = new AddCourseDialogFragment(
                    new OnCoursesChangeListener() {

                        @Override
                        public void onCoursesChanged() {
                            refreshCourseList();
                        }

                    });
            fragment.show(getSupportFragmentManager(), CREATE_COURSE_DIALOG_TAG);
            return true;

        case R.id.course_list_menu_about:
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return false;
    }

    private void unlockFullVersion() {
        Log.i(Constants.TAG, "Full version unlocked");

        if (!mFullVersion) {
            Toast.makeText(this, getString(R.string.course_list_activity_toast_unlocked),
                    Toast.LENGTH_SHORT).show();
        }

        enterFullVersionMode();
    }

    private void lockFullVersion() {
        Log.d(Constants.TAG, "Re-locking full version mode");
        mFullVersion = false;
        mBuyFullVersionFragment.getView().setVisibility(View.VISIBLE);
    }

    private void removeFullVersionOptions() {
        Log.d(Constants.TAG, "Removing full version mode options");
        mFullVersion = false;
        mBuyFullVersionFragment.getView().setVisibility(View.GONE);
    }

    private void enterFullVersionMode() {
        Log.d(Constants.TAG, "Applying full version mode");
        mFullVersion = true;
        mBuyFullVersionFragment.getView().setVisibility(View.GONE);
    }

    public void refreshCourseList() {
        Log.v(Constants.TAG, "Refreshing course list");

        mCourses = Course.getAllCourses();
        mCourseArrayAdapter = new CourseArrayAdapter(this, mCourses);

        mListFragment.setListAdapter(mCourseArrayAdapter);
        mListFragment.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mListFragment.getListView().setItemChecked(position, true);
                mCurrentlySelectedCourse = mCourseArrayAdapter.getItem(position);
                startActionMode(mCourseSelectionCallback);
                return true;
            }

        });

        mListFragment.getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent courseIntent = new Intent(CourseListActivity.this, CourseActivity.class);
                courseIntent.putExtra(CourseActivity.INTENT_KEY_COURSE,
                        mCourseArrayAdapter.getItem(position));
                startActivity(courseIntent);
            }
        });

        Log.v(Constants.TAG, mCourses.size() + " courses loaded from the database");
    }
}