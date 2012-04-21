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
import com.jetheis.android.grades.model.PercentageGradeComponent;
import com.jetheis.android.grades.model.PointTotalGradeComponent;
import com.jetheis.android.grades.storage.CourseStorageAdapter;
import com.jetheis.android.grades.storage.DatabaseHelper;
import com.jetheis.android.grades.storage.GradeComponentStorageAdapter;

public class GradeComponentStorageTest extends AndroidTestCase {

    private Context mSandboxedContext;

    protected void setUp() throws Exception {
        super.setUp();

        if (mSandboxedContext == null) {
            mSandboxedContext = new RenamingDelegatingContext(getContext(),
                    "grade_component_storage_test-");
            DatabaseHelper.initializeDatabaseHelper(mSandboxedContext);
        }
    }

    public void testSimpleSave() {
        Course rh131 = ObjectMother.rh131();
        rh131.save();

        PointTotalGradeComponent pointComponent = new PointTotalGradeComponent();
        pointComponent.setCourse(rh131);
        pointComponent.setName("testSimpleSaveExistancePointTotal");
        pointComponent.setPointsEarned(10);
        pointComponent.setPointsEarned(10);

        assertEquals(0, pointComponent.getId());

        pointComponent.save();

        assertTrue(pointComponent.getId() > 0);
        assertEquals(pointComponent,
                new GradeComponentStorageAdapter().getPointTotalGradeComponentById(pointComponent
                        .getId()));

        Course csse230 = ObjectMother.csse230();
        csse230.save();

        PercentageGradeComponent percentageComponent = new PercentageGradeComponent();
        percentageComponent.setCourse(csse230);
        percentageComponent.setName("testSimpleSaveExistancePointTotal");
        percentageComponent.setEarnedPercentage(1);
        percentageComponent.setWeight(1);

        assertEquals(0, percentageComponent.getId());

        percentageComponent.save();

        assertTrue(percentageComponent.getId() > 0);
        assertEquals(percentageComponent,
                new GradeComponentStorageAdapter()
                        .getPercentageGradeComponentById(percentageComponent.getId()));
    }

    public void testUpdate() {
        // Point total
        Course rh131 = ObjectMother.rh131();
        rh131.save();
        
        PointTotalGradeComponent pointExams = ObjectMother.pointTotalExams();
        
        rh131.addGradeComponent(pointExams);
        rh131.save();

        pointExams.setName("CHANGEDPOINTS");
        pointExams.save();

        assertEquals(pointExams,
                PointTotalGradeComponent.getPointTotalGradeComponentById(pointExams.getId()));

        // Percentage
        Course csse230 = ObjectMother.csse230();
        csse230.save();
        
        PercentageGradeComponent percentageExams = ObjectMother.percentageExams();
        
        csse230.addGradeComponent(percentageExams);
        csse230.save();
        
        percentageExams.setName("CHANGEDPERCENTAGE");
        percentageExams.save();

        assertEquals(percentageExams,
                PercentageGradeComponent.getPercentageGradeComponentById(percentageExams.getId()));
    }

    public void testdestroy() {
        Course rh131 = ObjectMother.rh131();
        rh131.save();
        
        PointTotalGradeComponent pointExams = ObjectMother.pointTotalExams();
        
        rh131.addGradeComponent(pointExams);
        rh131.save();

        assertTrue(pointExams.getId() > 0);
        
        long rh131ExamsId = pointExams.getId();
        
        pointExams.destroy();
        
        assertNull(PointTotalGradeComponent.getPointTotalGradeComponentById(rh131ExamsId));

        // Percentage
        Course csse230 = ObjectMother.csse230();
        csse230.save();
        
        PercentageGradeComponent percentageExams = ObjectMother.percentageExams();
        
        csse230.addGradeComponent(percentageExams);
        csse230.save();

        assertTrue(percentageExams.getId() > 0);
        
        long csse230ExamsId = percentageExams.getId();
        
        percentageExams.destroy();
        
        assertNull(PercentageGradeComponent.getPercentageGradeComponentById(csse230ExamsId));
    }

    public void testDestroyFromCourse() {
        Course rh131 = ObjectMother.rh131();
        rh131.save();
        
        PointTotalGradeComponent pointExams = ObjectMother.pointTotalExams();
        
        rh131.addGradeComponent(pointExams);
        rh131.save();

        assertTrue(pointExams.getId() > 0);
        
        long rh131ExamsId = pointExams.getId();
        
        rh131.destroy();
        
        assertNull(PointTotalGradeComponent.getPointTotalGradeComponentById(rh131ExamsId));

        // Percentage
        Course csse230 = ObjectMother.csse230();
        csse230.save();
        
        PercentageGradeComponent percentageExams = ObjectMother.percentageExams();
        
        csse230.addGradeComponent(percentageExams);
        csse230.save();

        assertTrue(percentageExams.getId() > 0);
        
        long csse230ExamsId = percentageExams.getId();
        
        csse230.destroy();
        
        assertNull(PercentageGradeComponent.getPercentageGradeComponentById(csse230ExamsId));
    }

    public void testRetrievalById() {
        // Point total
        Course rh131 = ObjectMother.rh131();
        rh131.save();
        
        PointTotalGradeComponent pointExams = ObjectMother.pointTotalExams();
        
        rh131.addGradeComponent(pointExams);
        rh131.save();

        assertTrue(pointExams.getId() > 0);
        assertEquals(pointExams,
                PointTotalGradeComponent.getPointTotalGradeComponentById(pointExams.getId()));

        // Percentage
        Course csse230 = ObjectMother.csse230();
        csse230.save();
        
        PercentageGradeComponent percentageExams = ObjectMother.percentageExams();
        
        csse230.addGradeComponent(percentageExams);
        csse230.save();

        assertTrue(percentageExams.getId() > 0);
        assertEquals(percentageExams,
                PercentageGradeComponent.getPercentageGradeComponentById(percentageExams.getId()));
    }

    public void testRetrievalFromCourse() {
        // Point total
        Course rh131 = ObjectMother.rh131();
        rh131.save();
        
        PointTotalGradeComponent pointExams = ObjectMother.pointTotalExams();
        
        rh131.addGradeComponent(pointExams);
        rh131.save();
        
        Course retrievedRh131 = Course.getCourseById(rh131.getId());
        retrievedRh131.loadConnectedObjects();

        assertEquals(pointExams, retrievedRh131.getGradeComponents().iterator().next());

        // Percentage
        Course csse230 = ObjectMother.csse230();
        csse230.save();
        
        PercentageGradeComponent percentageExams = ObjectMother.percentageExams();
        
        csse230.addGradeComponent(percentageExams);
        csse230.save();
        
        Course retrievedCsse230 = Course.getCourseById(csse230.getId());
        retrievedCsse230.loadConnectedObjects();
        
        assertEquals(percentageExams, retrievedCsse230.getGradeComponents().iterator().next());
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        new GradeComponentStorageAdapter().deleteAllGradeComponents();
        new CourseStorageAdapter().deleteAllCourses();
    }

}
