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

/**
 * An abstract class containing shared attributes of objects that will be stored
 * in the database.
 */
public abstract class Storable {

    private long mId;

    /**
     * Get the unique identifier (within the object's type) for this object.
     * 
     * @return This object's unique identifier.
     */
    public long getId() {
        return mId;
    }

    /**
     * Set the unique identifier for this object. This should really only be
     * done by the class that creates the object after retrieving it from the
     * database.
     * 
     * @param id
     *            This object's new unique identifier.
     */
    public void setId(long id) {
        mId = id;
    }

    /**
     * Save this object to the database. If this is a new object that is not yet
     * reflected in the database, a new entry should be created. Otherwise, the
     * existing entry for this object should be updated with the most recent
     * values of this object. This save should also recurse to save any
     * contained objects.
     */
    public abstract void save();

    /**
     * Remove this object from the database. If this object is not reflected in
     * the database, nothing should happen. This desctuction should also recurse
     * to destroy any contained objects.
     */
    public abstract void destroy();
}
