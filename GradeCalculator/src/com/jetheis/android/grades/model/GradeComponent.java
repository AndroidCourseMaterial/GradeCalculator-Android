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
     * Get the human readable name of this grade component.
     * 
     * @return The human readable name of this grade component.
     */
    public String getName() {
        return mName;
    }

    /**
     * Set the human readable name of this grade component.
     * 
     * @param name
     *            The new human readable name of this grade component.
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Get the course that contains this grade component. If the course has not
     * yet been loaded from the database, it will be loaded here.
     * 
     * @return The course that contains this grade component, or null if one
     *         does not exist.
     */
    public Course getCourse() {
        // TODO: Load this if it exists
        return mCourse;
    }

    /**
     * Set the course that contains this grade component. This method should
     * only be called by the grade component's containing course, because
     * membership to a particular course "trickles down" from the containing
     * object.
     * 
     * @param course
     *            The course this grade component belongs to.
     */
    public void setCourse(Course course) {
        mCourse = course;
    }
}
