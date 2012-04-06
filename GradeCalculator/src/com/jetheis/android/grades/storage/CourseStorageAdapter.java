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

package com.jetheis.android.grades.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.model.Course.CourseType;

public class CourseStorageAdapter extends StorageAdapter {

    private static final String TABLE_NAME = "courses";
    private static final String ID_COLUMN = "_id";
    private static final String NAME_COLUMN = "name";
    private static final String GRADE_TYPE_COLUMN = "grade_type";

    public CourseStorageAdapter(Context context) {
        super(context);
    }

    /**
     * Save a {@link Course} that has never been saved in the database as a new
     * database record. The provided {@link Course} will have its database
     * identifier updated with the value stored in the database.
     * 
     * @param course
     *            An unsaved {@link Course}. This course will have its
     *            identifier field (accessed through {@link Course#getId()})
     *            updated in place with the appropriate value.
     */
    public void createCourse(Course course) {
        course.setId(getDb().insert(TABLE_NAME, null, getContentValuesFromCourse(course)));
    }

    /**
     * Update the values of an existing {@link Course} in the database.
     * 
     * @param course
     *            The {@link Course} to be updated.
     */
    public void updateCourse(Course course) {
        getDb().update(TABLE_NAME, getContentValuesFromCourse(course), ID_COLUMN + " = ?",
                new String[] { Long.toString(course.getId()) });
    }

    /**
     * Convenience method for saving any type of {@link Course}. Based on the
     * unique identifier of the provided {@link Course}, this method will decide
     * whether to create a new database record or update an existing one.
     * 
     * @param course
     *            The {@link Course} (new or existing) to be saved.
     */
    public void saveCourse(Course course) {
        if (course.getId() < 1) {
            createCourse(course);
        } else {
            updateCourse(course);
        }
    }

    /**
     * Get all existing {@link Course}s, in {@link CourseStorageIterator} form.
     * 
     * @return A {@link CourseStorageIterator} containing all {@link Course}s in
     *         the database.
     */
    public CourseStorageIterator getAllCourses() {
        return new CourseStorageIterator(getDb().query(TABLE_NAME, null, null, null, null, null,
                null));
    }

    /**
     * Retrieve a {@link Course} from the database based on its unique
     * identifier.
     * 
     * @param id
     *            The {@link Course} to be retrieved's unique identifer.
     * @return The desired {@link Course}, if found, and {@code null} otherwise.
     */
    public Course getCourseById(long id) {
        CourseStorageIterator result = new CourseStorageIterator(getDb().query(TABLE_NAME, null,
                ID_COLUMN + " = ?", new String[] { Long.toString(id) }, null, null, null));

        if (result.getCount() < 1)
            return null;

        return result.next();
    }

    /**
     * Delete all {@link Course} records from the database.
     * 
     * @return The number of {@link Course}s deleted.
     */
    public int deleteAllCourses() {
        return getDb().delete(TABLE_NAME, null, null);
    }

    /**
     * Convert a {@link Course} to a {@link ContentValues} object for storage in
     * a {@link SQLiteDatabase}. This conversion leaves out the unique database
     * identifier, because this field will never be changed in the database, and
     * as such does not belong in a {@link ContentValues} object to be written
     * to the database.
     * 
     * @param course
     *            A {@link Course} to be converted.
     * @return
     */
    private ContentValues getContentValuesFromCourse(Course course) {
        ContentValues result = new ContentValues();

        result.put(NAME_COLUMN, course.getName());
        result.put(GRADE_TYPE_COLUMN, course.getCourseType().toInt());

        return result;
    }

    /**
     * A {@link StorageIterator} subclass for iterating over {@link Course}
     * results generated by a database query.
     * 
     */
    public class CourseStorageIterator extends StorageIterator<Course> {

        public CourseStorageIterator(Cursor cursor) {
            super(cursor);
        }

        @Override
        protected Course getObjectFromNextRow(Cursor cursor) {
            Course result = new Course();

            result.setId(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)));
            result.setName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
            result.setCourseType(CourseType.fromInt(cursor.getInt(cursor
                    .getColumnIndex(GRADE_TYPE_COLUMN))));

            return result;
        }

    }

}
