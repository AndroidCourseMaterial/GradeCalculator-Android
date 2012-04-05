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

package com.jetheis.android.grades.model;

import android.content.Context;

/**
 * A grade component that is scored based on a percentage of points earned. Its
 * weight relative to the rest of the course is based on a specific percentage
 * weight.
 * 
 */
public class PercentageGradeComponent extends GradeComponent {

    private double mEarnedPercentage;
    private double mWeight;

    /**
     * Get the percentage of points earned for this grade component. Any actual
     * point totals are not reflected here; rather, the value tracked is simply
     * the ratio of points earned to total points.
     * 
     * @return The percentage of points earned for this grade component,
     *         represented as a double less than or equal to {@code 1.0}.
     */
    public double getEarnedPercentage() {
        return mEarnedPercentage;
    }

    /**
     * Set the percentage of points earned for this grade component.Any actual
     * point totals are not reflected here; rather, the value tracked is simply
     * the ratio of points earned to total points.
     * 
     * @param earnedPercentage
     *            The percentage of points earned for this grade component,
     *            represented as a double less than or equal to 1.0f.
     */
    public void setEarnedPercentage(double earnedPercentage) {
        mEarnedPercentage = earnedPercentage;
    }

    /**
     * Get the percentage weighting for this grade component. This value
     * specifically represents the ratio of potential points in this grade
     * component to total points available across the entire course.
     * 
     * @return The percentage weighting of this grade component relative to the
     *         rest of the course, represented as a double less than or equal to
     *         {@code 1.0}.
     */
    public double getWeight() {
        return mWeight;
    }

    /**
     * Set the percentage weighting for this grade component. This value
     * specifically represents the ratio of potential points in this grade
     * component to total points available across the entire course.
     * 
     * @param weight
     *            The percentage weighting of this grade component relative to
     *            the rest of the course, represented as a double less than or
     *            equal to {@code 1.0}.
     */
    public void setWeight(double weight) {
        mWeight = weight;
    }

    @Override
    public void save(Context context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void destroy(Context context) {
        // TODO Auto-generated method stub

    }

}
