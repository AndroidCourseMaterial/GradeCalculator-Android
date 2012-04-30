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

import android.util.Log;

/**
 * A container for application-wide constants.
 * 
 */
public class Constants {

    public enum LicenseType {
        FREE, GOOGLE_PLAY, AMAZON_APPSTORE
    }

    /**
     * The tag to be used with {@link Log} method calls throughout the app.
     */
    public static final String TAG = "GradeCalculator";

    /**
     * License type for this build of the app.
     */
    public static final LicenseType LICENSE_TYPE = LicenseType.FREE;

    /**
     * Free (not tracked anywhere) item identifier
     */
    public static final String FREE_ITEM_ID = "weighted_grade_calculator_full_version";

    /**
     * Google Play item identifier
     */
    public static final String GOOGLE_PLAY_ITEM_ID = "weighted_grade_calculator_full_version";

    /**
     * Amazon App Store item identifier
     */
    public static final String AMAZON_ITEM_ID = "weighted_grade_calculator_full_version";

}
