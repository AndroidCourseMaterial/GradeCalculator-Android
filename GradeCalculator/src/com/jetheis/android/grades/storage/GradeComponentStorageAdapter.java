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

import com.jetheis.android.grades.model.GradeComponent;
import com.jetheis.android.grades.model.PercentageGradeComponent;
import com.jetheis.android.grades.model.PointTotalGradeComponent;

public class GradeComponentStorageAdapter extends StorageAdapter {

    public static final String TABLE_NAME = "grade_components";
    public static final String ID_COLUMN = "_id";
    public static final String COURSE_COLUMN = "_course_id";
    public static final String NAME_COLUMN = "name";
    public static final String EARNED_COLUMN = "earned";
    public static final String TOTAL_COLUMN = "total";

    public GradeComponentStorageAdapter(Context context) {
        super(context);
    }

    /**
     * Convenience method for saving a {@link PointTotalGradeComponent}. This
     * method will determine, based on the object's unique identifier, whether
     * or not the object has been saved in the database.
     * 
     * @param component
     *            The {@link PointTotalGradeComponent} to be saved.
     * @return The number of database records affected.
     */
    public int savePointTotalGradeComponent(PointTotalGradeComponent component) {
        if (component.getId() < 1) {
            return createPointTotalGradeComponent(component);
        }

        return updatePointTotalGradeComponent(component);
    }

    /**
     * Convenience method for saving a {@link PercentageGradeComponent}. This
     * method will determine, based on the object's unique identifier, whether
     * or not the object has been saved in the database.
     * 
     * @param component
     *            The {@link PercentageGradeComponent} to be saved.
     * @return The number of database records affected.
     */
    public int savePercentageGradeComponent(PercentageGradeComponent component) {
        if (component.getId() < 1) {
            return createPercentageGradeComponent(component);
        }

        return updatePercentageGradeComponent(component);
    }

    /**
     * Create a new database record for a {@link PointTotalGradeComponent}. The
     * object's unique identifier will be updated in place if the creation is
     * successful to reflect its actual database identifier.
     * 
     * @param component
     *            The {@link PointTotalGradeComponent} to be saved.
     * @return The number of database records affected.
     */
    public int createPointTotalGradeComponent(PointTotalGradeComponent component) {
        long result = getDb().insert(TABLE_NAME, null,
                getContentValuesFromPointTotalGradeComponent(component));

        if (result > 0) {
            component.setId(result);
            return 1;
        }

        return 0;
    }

    /**
     * Create a new database record for a {@link PercentageGradeComponent}. The
     * object's unique identifier will be updated in place if the creation is
     * successful to reflect its actual database identifier.
     * 
     * @param component
     *            The {@link PercentageGradeComponent} to be saved.
     * @return The number of database records affected.
     */
    public int createPercentageGradeComponent(PercentageGradeComponent component) {
        long result = getDb().insert(TABLE_NAME, null,
                getContentValuesFromPercentageGradeComponent(component));

        if (result > 0) {
            component.setId(result);
            return 1;
        }

        return 0;
    }

    /**
     * Update an existing {@link PointTotalGradeComponent}'s database record
     * with the object's current values.
     * 
     * @param component
     *            The {@link PointTotalGradeComponent} to be saved.
     * @return The number of database records affected.
     */
    public int updatePointTotalGradeComponent(PointTotalGradeComponent component) {
        return getDb().update(TABLE_NAME, getContentValuesFromPointTotalGradeComponent(component),
                ID_COLUMN + " = ?", new String[] { Long.toString(component.getId()) });
    }

    /**
     * Update an existing {@link PercentageGradeComponent}'s database record
     * with the object's current values.
     * 
     * @param component
     *            The {@link PercentageGradeComponent} to be saved.
     * @return The number of database records affected.
     */
    public int updatePercentageGradeComponent(PercentageGradeComponent component) {
        return getDb().update(TABLE_NAME, getContentValuesFromPercentageGradeComponent(component),
                ID_COLUMN + " = ?", new String[] { Long.toString(component.getId()) });
    }

    /**
     * Delete all {@link GradeComponent}s from the database.
     * 
     * @return The number of database records affected.
     */
    public int deleteAllGradeComponents() {
        return getDb().delete(TABLE_NAME, null, null);
    }

    /**
     * Convert a {@link PointTotalGradeComponent} to {@link ContentValues} for
     * storing in a {@link SQLiteDatabase}. This method ignores the object's
     * unique identifier (given by {@link PointTotalGradeComponent#getId()}),
     * because this value will always be set by the database.
     * 
     * @param component
     *            The {@link PointTotalGradeComponent} to be saved.
     * @return The number of database records affected.
     */
    private ContentValues getContentValuesFromPointTotalGradeComponent(
            PointTotalGradeComponent component) {
        ContentValues result = new ContentValues();

        result.put(COURSE_COLUMN, component.getCourse().getId());
        result.put(NAME_COLUMN, component.getName());
        result.put(EARNED_COLUMN, component.getPointsEarned());
        result.put(TOTAL_COLUMN, component.getTotalPoints());

        return result;
    }

    /**
     * Convert a {@link PercentageGradeComponent} to {@link ContentValues} for
     * storing in a {@link SQLiteDatabase}. This method ignores the object's
     * unique identifier (given by {@link PercentageGradeComponent#getId()}),
     * because this value will always be set by the database.
     * 
     * @param component
     *            The {@link PercentageGradeComponent} to be saved.
     * @return The number of database records affected.
     */
    private ContentValues getContentValuesFromPercentageGradeComponent(
            PercentageGradeComponent component) {
        ContentValues result = new ContentValues();

        result.put(COURSE_COLUMN, component.getCourse().getId());
        result.put(NAME_COLUMN, component.getName());
        result.put(EARNED_COLUMN, component.getEarnedPercentage());
        result.put(TOTAL_COLUMN, component.getWeight());

        return result;
    }

    /**
     * A {@link StorageIterator} subclass for iterating over
     * {@link PointTotalGradeComponent} results generated by a database query.
     * 
     */
    public class PointTotalGradeComponentStorageIterator extends
            StorageIterator<PointTotalGradeComponent> {

        public PointTotalGradeComponentStorageIterator(Cursor cursor) {
            super(cursor);
        }

        @Override
        protected PointTotalGradeComponent getObjectFromNextRow(Cursor cursor) {
            PointTotalGradeComponent result = new PointTotalGradeComponent();

            result.setId(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)));
            result.setName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
            result.setPointsEarned(cursor.getDouble(cursor.getColumnIndex(EARNED_COLUMN)));
            result.setTotalPoints(cursor.getDouble(cursor.getColumnIndex(TOTAL_COLUMN)));

            return result;
        }

    }

    /**
     * A {@link StorageIterator} subclass for iterating over
     * {@link PercentageGradeComponent} results generated by a database query.
     * 
     */
    public class PercentageGradeComponentIterator extends StorageIterator<PercentageGradeComponent> {

        public PercentageGradeComponentIterator(Cursor cursor) {
            super(cursor);
        }

        @Override
        protected PercentageGradeComponent getObjectFromNextRow(Cursor cursor) {
            PercentageGradeComponent result = new PercentageGradeComponent();

            result.setId(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)));
            result.setName(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
            result.setEarnedPercentage(cursor.getDouble(cursor.getColumnIndex(EARNED_COLUMN)));
            result.setWeight(cursor.getDouble(cursor.getColumnIndex(TOTAL_COLUMN)));

            return result;
        }

    }

}
