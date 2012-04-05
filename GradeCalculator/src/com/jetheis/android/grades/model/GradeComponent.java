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

import com.jetheis.android.grades.storage.Storable;

/**
 * An abstract representation of a grade component.
 */
public abstract class GradeComponent extends Storable {

    private String mName;
    private Course mCourse;

    /**
     * Get the human readable name of this {@link GradeComponent}.
     * 
     * @return The human readable name of this {@link GradeComponent}.
     */
    public String getName() {
        return mName;
    }

    /**
     * Set the human readable name of this {@link GradeComponent}.
     * 
     * @param name
     *            The new human readable name of this {@link GradeComponent}.
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Get the {@link Course} that contains this {@link GradeComponent}. If the
     * {@link Course} has not yet been loaded from the database, it will be
     * loaded here.
     * 
     * @return The {@link Course} that contains this {@link GradeComponent}, or
     *         null if one does not exist.
     */
    public Course getCourse() {
        // TODO: Load this if it exists
        return mCourse;
    }

    /**
     * Set the {@link Course} that contains this {@link GradeComponent}. This
     * method should only be called by the {@link GradeComponent}'s containing
     * {@link Course}, because membership to a particular {@link Course}
     * "trickles down" from the containing object.
     * 
     * @param course
     *            The {@link Course} this {@link GradeComponent} belongs to.
     */
    public void setCourse(Course course) {
        mCourse = course;
    }
}
