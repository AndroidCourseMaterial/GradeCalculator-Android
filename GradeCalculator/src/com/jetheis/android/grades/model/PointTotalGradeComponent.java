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

import com.jetheis.android.grades.storage.GradeComponentStorageAdapter;

/**
 * A grade component that is scored based on a point total. The grade components
 * weight relative to the rest of the course is based on how many additional
 * points are available in the course.
 */
public class PointTotalGradeComponent extends GradeComponent implements
        Comparable<PointTotalGradeComponent> {

    private double mTotal;
    private double mEarned;

    /**
     * Get a {@link PointTotalGradeComponent} by its unique identifier.
     * 
     * @param id
     *            The unique identifier of the {@link PointTotalGradeComponent}.
     * @return The retrieved {@link PointTotalGradeComponent}, or null if one is
     *         not found.
     */
    public static PointTotalGradeComponent getPointTotalGradeComponentById(long id) {
        return new GradeComponentStorageAdapter().getPointTotalGradeComponentById(id);
    }

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
    public void save() {
        new GradeComponentStorageAdapter().savePointTotalGradeComponent(this);
    }

    @Override
    public String toString() {
        return String.format("PointTotalGradeComponent %s: %.2f/%.2f", getName(),
                getPointsEarned(), getTotalPoints());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PointTotalGradeComponent)) {
            return false;
        }

        return compareTo((PointTotalGradeComponent) o) == 0;
    }

    @Override
    public int compareTo(PointTotalGradeComponent other) {
        if (getId() > 0 && other.getId() > 0) {
            return (int) Math.signum(getId() - other.getId());
        }

        // TODO: Compare courses

        if (!getName().equals(other.getName())) {
            return getName().compareTo(other.getName());
        }

        long totalPoints = Math.round(getTotalPoints() * 1000);
        long otherTotalPoints = Math.round(other.getTotalPoints() * 1000);

        if (totalPoints != otherTotalPoints) {
            return (int) Math.signum(totalPoints - otherTotalPoints);
        }

        long pointsEarned = Math.round(getPointsEarned() * 1000);
        long otherPointsEarned = Math.round(other.getPointsEarned() * 1000);

        return (int) Math.signum(pointsEarned - otherPointsEarned);
    }

}
