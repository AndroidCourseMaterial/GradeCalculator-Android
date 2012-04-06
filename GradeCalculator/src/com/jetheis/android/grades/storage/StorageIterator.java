package com.jetheis.android.grades.storage;

import java.util.Iterator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * An abstract iterator for {@link Storable} subclasses retrieved from a
 * {@link SQLiteDatabase}. Subclasses should only need to implement
 * {@link #getObjectFromNextRow(Cursor)}, and will likely be internal to their
 * {@link Storable} subclass's {@link StorageAdapter} subclass.
 * 
 * @param <T>
 *            The {@link Storable} subclass that this {@link StorageIterator}
 *            provides.
 */
public abstract class StorageIterator<T extends Storable> implements Iterable<T>, Iterator<T> {

    private Cursor mCursor;

    /**
     * Default constructor. This initializes the iterator with a {@link Cursor}
     * pointing to a set of database query results. After this initialization,
     * the {@link StorageIterator} is ready to use.
     * 
     * @param cursor
     *            The fresh {@link Cursor} returned from a
     *            {@link SQLiteDatabase} query.
     */
    public StorageIterator(Cursor cursor) {
        mCursor = cursor;
    }

    /**
     * Convert the next row returned by the Cursor to a proper {@link Storable},
     * for use by the rest of the app. Implementations of this method need not
     * and should not call {@link Cursor#moveToNext()}, as this will be called
     * before the {@link Cursor} is passed ot this method.
     * 
     * @param cursor
     *            This {@link StorageIterator}'s internal {@link Cursor}, with
     *            the next row ready to be read and converted to a
     *            {@link Storable} ({@link Cursor#moveToNext()} already called).
     * @return A {@link Storable} subclass instance populated with the values
     *         from the next row of the {@link Cursor}.
     */
    protected abstract T getObjectFromNextRow(Cursor cursor);

    /**
     * Get the total number of results contained by this iterator's
     * {@link Cursor}.
     * 
     * @return The total number of results contained by this iterator's
     *         {@link Cursor}.
     */
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public boolean hasNext() {
        return mCursor.getPosition() < mCursor.getCount() - 1;
    }

    @Override
    public T next() {
        mCursor.moveToNext();
        return getObjectFromNextRow(mCursor);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove not allowed on StorageIterators");
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }
}
