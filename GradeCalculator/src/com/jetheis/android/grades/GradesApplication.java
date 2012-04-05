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

package com.jetheis.android.grades;

import com.jetheis.android.grades.storage.DatabaseHelper;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

/**
 * An {@link Application} subclass that stores data that needs to persist across
 * all application activities.
 * 
 */
public class GradesApplication extends Application {

    private DatabaseHelper mDbHelper;

    /**
     * Default constructor. This takes care of initializing all application-wide
     * objects.
     */
    public GradesApplication() {
        mDbHelper = new DatabaseHelper(this);
    }

    /**
     * Get the application's persistent {@link DatabaseHelper} instance.
     * 
     * @return The application's persistent {@link DatabaseHelper} instance.
     */
    public DatabaseHelper getDatabaseHelper() {
        return mDbHelper;
    }

    /**
     * Helper method to access that application's persistent
     * {@link SQLiteDatabase} instance from the {@link DatabaseHelper} directly.
     * 
     * @return The persistent {@link DatabaseHelper}'s {@link SQLiteDatabase}
     *         instance.
     */
    public SQLiteDatabase getDatabase() {
        return getDatabaseHelper().getDb();
    }

}
