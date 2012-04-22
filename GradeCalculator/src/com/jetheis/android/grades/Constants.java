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
    
    public static final String PREFS_KEY_FULL_VERSION = "full_version_unlocked";

}
