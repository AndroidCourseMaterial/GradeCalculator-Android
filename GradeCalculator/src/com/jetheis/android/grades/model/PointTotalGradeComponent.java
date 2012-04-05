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
 * A grade component that is scored based on a point total. The grade components
 * weight relative to the rest of the course is based on how many additional
 * points are available in the course.
 */
public class PointTotalGradeComponent extends GradeComponent {

    private double mTotal;
    private double mEarned;

    /**
     * Get the total number of points available to be earned for this grade
     * component.
     * 
     * @return The total number of points available for this grade component.
     */
    public double getTotalPoints() {
        return mTotal;
    }

    /**
     * Set the total number of points available to be earned for this grade
     * component.
     * 
     * @param total
     *            The total number of points available for this grade component.
     */
    public void setTotalPoints(double total) {
        mTotal = total;
    }

    /**
     * Get the total number of points earned for this grade component.
     * 
     * @return The total number of points earned for this grade component.
     */
    public double getPointsEarned() {
        return mEarned;
    }

    /**
     * Set the total number of points earned for this grade component.
     * 
     * @param earned
     *            The total number of points earned for this grade component.
     */
    public void setPointsEarned(double earned) {
        mEarned = earned;
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
