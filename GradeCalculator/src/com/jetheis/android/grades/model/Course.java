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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.jetheis.android.grades.storage.Storable;

/**
 * Representation of a single academic course. Each course has a name and a
 * "type", which represents how the courses final grade is calculated: either by
 * adding points for a total score, or by weighting categories' percentages
 * together.
 */
public class Course extends Storable {

    /**
     * A simple representation of a course type.
     * 
     */
    public enum CourseType {
        /**
         * The course calculates its final grade by summing the points of its
         * included grade components
         */
        POINT_TOTAL,
        /**
         * The course calculates its final grade by applying a specific weight
         * to the percentage grade of each of its included grade components.
         */
        PERCENTAGE_WEIGHTING
    }

    private String mName;
    private CourseType mCourseType;

    private Collection<GradeComponent> mGradeComponents;

    public Course(String name, CourseType courseType) {
        setName(name);
        setCourseType(courseType);
    }

    /**
     * Get the human readable name of the course.
     * 
     * @return The human readable name of the course.
     */
    public String getName() {
        return mName;
    }

    /**
     * Set the human readable name of the course.
     * 
     * @param name
     *            The new human readable name of the course.
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Get the "type" of the course.
     * 
     * @return The type that designates how this course calculates its final
     *         score.
     */
    public CourseType getCourseType() {
        return mCourseType;
    }

    /**
     * Set the "type" of the course.
     * 
     * @param courseType
     *            The new "type" of this course.
     */
    public void setCourseType(CourseType courseType) {
        mCourseType = courseType;
    }

    /**
     * Get the collection of grade components that this course contains. If this
     * course contains no grade components, an empty collection will be
     * returned. If this object is persisted in the database, grade components
     * will be loaded automatically (if they have not be already) here.
     * 
     * @return The collection of grade components that this course contains.
     */
    public Collection<GradeComponent> getGradeComponents() {
        // TODO: Populate this from the database if it doesn't exist already

        if (mGradeComponents == null) {
            mGradeComponents = new HashSet<GradeComponent>();
        }

        // Copy components to a new list so the original can't be modified
        ArrayList<GradeComponent> result = new ArrayList<GradeComponent>();
        result.addAll(mGradeComponents);

        return result;
    }

    /**
     * Add a grade component to this course.
     * 
     * @param gradeComponent
     *            The grade component to add to this course.
     */
    public void addGradeComponent(GradeComponent gradeComponent) {
        // TODO
    }

    /**
     * Add several grade components to this course
     * 
     * @param gradeComponents
     *            The grade components to add to this course.
     */
    public void addGradComponents(Collection<GradeComponent> gradeComponents) {
        // TODO
    }

    /**
     * Remove a grade component from this course.
     * 
     * @param gradeComponent
     *            The grade component to remove from this course.
     */
    public void removeGradeComponent(GradeComponent gradeComponent) {
        // TODO
    }

    /**
     * Remove several grade components from this course.
     * 
     * @param gradeComponents
     *            The grade components to remove from this course.
     */
    public void removeGradeComponents(Collection<GradeComponent> gradeComponents) {
        // TODO
    }

    @Override
    public void save() {
        // TODO

    }

    @Override
    public void destroy() {
        // TODO

    }
}
