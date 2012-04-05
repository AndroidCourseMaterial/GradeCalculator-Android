package com.jetheis.android.grades.test;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.jetheis.android.grades.model.Course;

public class CourseStorageTest extends AndroidTestCase {
    
    private Context mSandboxedContext;

    protected void setUp() throws Exception {
        super.setUp();
        
        mSandboxedContext = new RenamingDelegatingContext(getContext(), "course_storage_test-");
    }
    
    public void testSimpleSave() {
        Course course = new Course();
        course.setName("testSimpleSave");
        course.save(mSandboxedContext);
    }

}
