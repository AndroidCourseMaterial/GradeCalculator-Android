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

package com.jetheis.android.grades.test;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.model.Course.CourseType;
import com.jetheis.android.grades.model.PercentageGradeComponent;
import com.jetheis.android.grades.model.PointTotalGradeComponent;
import com.jetheis.android.grades.storage.CourseStorageAdapter;
import com.jetheis.android.grades.storage.DatabaseHelper;

public class CourseStorageTest extends AndroidTestCase {

    private Context mSandboxedContext;

    protected void setUp() throws Exception {
        super.setUp();

        if (mSandboxedContext == null) {
            mSandboxedContext = new RenamingDelegatingContext(getContext(), "course_storage_test-");
            DatabaseHelper.initializeDatabaseHelper(mSandboxedContext);
        }
    }

    public void testTotalDeletion() {
        Course course = new Course();
        course.setName("testTotalDeletion");
        course.setCourseType(CourseType.POINT_TOTAL);
        course.save();

        new CourseStorageAdapter().deleteAllCourses();

        assertEquals(0, Course.getAllCourses(mSandboxedContext).size());
    }

    public void testSimpleSaveExistance() {
        Course course = new Course();
        course.setName("testSimpleSave");
        course.setCourseType(CourseType.POINT_TOTAL);
        course.save();

        assertEquals(1, Course.getAllCourses(mSandboxedContext).size());
    }

    public void testSaveIdentifierPopulation() {
        Course course1 = new Course();
        course1.setName("testSaveIdentifierPopulation1");
        course1.setCourseType(CourseType.POINT_TOTAL);
        course1.save();

        assertTrue(course1.getId() > 0);

        Course course2 = new Course();
        course2.setName("testSaveIdentifierPopulation2");
        course2.setCourseType(CourseType.POINT_TOTAL);
        course2.save();

        assertEquals(course1.getId() + 1, course2.getId());
    }

    public void testSaveAndRetrieve() {
        Course course = new Course();
        course.setName("testSaveAndRetrieve");
        course.setCourseType(CourseType.POINT_TOTAL);
        course.save();

        assertEquals(course, new CourseStorageAdapter().getCourseById(course.getId()));
    }

    public void testUpdate() {
        Course course = new Course();
        course.setName("testUpdate");
        course.setCourseType(CourseType.POINT_TOTAL);
        course.save();

        long oldId = course.getId();

        course.setName("changed");
        course.save();

        assertEquals(oldId, course.getId());
        assertEquals(course, new CourseStorageAdapter().getCourseById(course.getId()));
    }

    public void testRetrievalFromGradeComponent() {
        Course rh131 = ObjectMother.rh131();
        PointTotalGradeComponent pointTotalExams = ObjectMother.pointTotalExams();

        rh131.save();
        pointTotalExams.setCourseId(rh131.getId());
        pointTotalExams.loadConnectedObjects();

        assertEquals(rh131, pointTotalExams.getCourse());

        Course csse230 = ObjectMother.csse230();
        PercentageGradeComponent percentageExams = ObjectMother.percentageExams();

        csse230.save();
        percentageExams.setCourseId(csse230.getId());
        percentageExams.loadConnectedObjects();

        assertEquals(csse230, percentageExams.getCourse());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        new CourseStorageAdapter().deleteAllCourses();
    }

}
