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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jetheis.android.grades.GradesApplication;

/**
 * An abstract representation of a storage adapter. Storage adapters perform
 * operations on the application's shared database pertaining to a certain type
 * of object ({@link Storable} subclass). Each storage adapter operates against
 * a specific {@link Context}, which makes it generic enough to be used in
 * multiple settings. Storage adapters are meant to be lightweight enough to
 * construct a new one for each use.
 */
public abstract class StorageAdapter {

    private SQLiteDatabase mDb;

    /**
     * Default constructor. During the execution of this constructor, the
     * instance's persistent {@link SQLiteDatabase} will be retrieved and
     * stored.
     * 
     * @param context
     *            The {@link Context} this adapter will operate with respect to.
     */
    public StorageAdapter(Context context) {
        mDb = ((GradesApplication) context.getApplicationContext()).getDatabase();
    }

    /**
     * Retrieve the instance's persistent {@link SQLiteDatabase}, for use by
     * {@link StorageAdapter} subclasses.
     * 
     * @return The instance's persistent {@link SQLiteDatabase}.
     */
    protected SQLiteDatabase getDb() {
        return mDb;
    }

}
