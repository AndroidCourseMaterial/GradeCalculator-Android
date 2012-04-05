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

import java.util.ArrayList;

import junit.framework.TestCase;
import android.content.Context;

import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.model.GradeComponent;
import com.jetheis.android.grades.model.PercentageGradeComponent;
import com.jetheis.android.grades.model.PointTotalGradeComponent;

/**
 * Pure Java tests for the {@link Course} class. These tests do not require a
 * {@link Context} or other Android-specific resources.
 * 
 */
public class CourseModelTest extends TestCase {

    public void testCalculateOverallScore() {
        Course csse120 = ObjectMother.csse120();

        assertEquals(0.9125, csse120.getOverallScore(), 0.0000001);
        
        Course csse333 = ObjectMother.csse333();
        
        assertEquals(0.835, csse333.getOverallScore(), 0.0000001);
    }

    public void testCalculateTotalScore() {
        Course csse120 = ObjectMother.csse120();

        assertEquals(400, csse120.getTotalPossibleScore(), 0.0000001);
        
        Course csse333 = ObjectMother.csse333();
        
        assertEquals(1, csse333.getTotalPossibleScore(), 0.0000001);
    }

    public void testAddingImproperGradeComponent() {
        Course csse120 = ObjectMother.csse120();

        PercentageGradeComponent percentageComponent = new PercentageGradeComponent();

        try {
            csse120.addGradeComponent(percentageComponent);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        Course csse333 = ObjectMother.csse333();

        PointTotalGradeComponent pointComponent = new PointTotalGradeComponent();

        try {
            csse333.addGradeComponent(pointComponent);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    public void testAddingImproperGradeComponents() {

        PointTotalGradeComponent pointComponent1 = new PointTotalGradeComponent();
        PointTotalGradeComponent pointComponen2 = new PointTotalGradeComponent();
        PercentageGradeComponent percentageComponent1 = new PercentageGradeComponent();
        PercentageGradeComponent percentageComponent2 = new PercentageGradeComponent();

        Course csse120 = ObjectMother.csse120();

        // Sandwich a percentage component between two point components
        ArrayList<GradeComponent> components1 = new ArrayList<GradeComponent>();
        components1.add(pointComponent1);
        components1.add(percentageComponent1);
        components1.add(pointComponen2);

        try {
            csse120.addGradeComponents(components1);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        Course csse333 = ObjectMother.csse333();

        // Sandwich a point component between two percentage components
        ArrayList<GradeComponent> components2 = new ArrayList<GradeComponent>();
        components2.add(percentageComponent1);
        components2.add(pointComponent1);
        components2.add(percentageComponent2);

        try {
            csse333.addGradeComponents(components2);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    public void testAddingDuplicateGradeComponent() {
        Course csse120 = ObjectMother.csse120();
        Course csse333 = ObjectMother.csse333();

        // Check the initial size of the contained grade components
        assertEquals(3, csse120.getGradeComponents().size());
        assertEquals(4, csse333.getGradeComponents().size());

        PointTotalGradeComponent pointComponent = new PointTotalGradeComponent();
        PercentageGradeComponent percentageComponent = new PercentageGradeComponent();

        csse120.addGradeComponent(pointComponent);
        csse333.addGradeComponent(percentageComponent);

        // Make sure the grade component was successfully added
        assertEquals(4, csse120.getGradeComponents().size());
        assertEquals(5, csse333.getGradeComponents().size());

        csse120.addGradeComponent(pointComponent);
        csse333.addGradeComponent(percentageComponent);

        // Make sure the grade component was not added again
        assertEquals(4, csse120.getGradeComponents().size());
        assertEquals(5, csse333.getGradeComponents().size());
    }

    public void testAddingDuplicateGradeComponents() {
        Course csse120 = ObjectMother.csse120();
        Course csse333 = ObjectMother.csse333();

        // Check the initial size of the contained grade components
        assertEquals(3, csse120.getGradeComponents().size());
        assertEquals(4, csse333.getGradeComponents().size());

        PointTotalGradeComponent pointComponent = new PointTotalGradeComponent();
        PercentageGradeComponent percentageComponent = new PercentageGradeComponent();

        csse120.addGradeComponent(pointComponent);
        csse333.addGradeComponent(percentageComponent);

        // Make sure the grade component was successfully added
        assertEquals(4, csse120.getGradeComponents().size());
        assertEquals(5, csse333.getGradeComponents().size());

        // Sandwich a duplicate between two new components
        ArrayList<GradeComponent> pointComponents = new ArrayList<GradeComponent>();
        pointComponents.add(new PointTotalGradeComponent());
        pointComponents.add(pointComponent);
        pointComponents.add(new PointTotalGradeComponent());

        // Sandwich a duplicate between two new components
        ArrayList<GradeComponent> percentageComponents = new ArrayList<GradeComponent>();
        percentageComponents.add(new PercentageGradeComponent());
        percentageComponents.add(percentageComponent);
        percentageComponents.add(new PercentageGradeComponent());

        csse120.addGradeComponents(pointComponents);
        csse333.addGradeComponents(percentageComponents);

        // Make sure the duplicate grade component was not added again
        assertEquals(6, csse120.getGradeComponents().size());
        assertEquals(7, csse333.getGradeComponents().size());
    }

}
