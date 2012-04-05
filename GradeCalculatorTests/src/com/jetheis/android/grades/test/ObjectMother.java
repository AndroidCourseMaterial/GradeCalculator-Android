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

package com.jetheis.android.grades.test;

import com.jetheis.android.grades.model.Course;
import com.jetheis.android.grades.model.GradeComponent;
import com.jetheis.android.grades.model.PercentageGradeComponent;
import com.jetheis.android.grades.model.PointTotalGradeComponent;
import com.jetheis.android.grades.model.Course.CourseType;

public class ObjectMother {

    /**
     * Create a CSSE120 instance. This is a {@link CourseType#POINT_TOTAL}
     * course. The breakdown of {@link GradeComponent}s (specifically,
     * {@link PercentageGradeComponent}s) is as follows:
     * 
     * <table border="1">
     * <tr>
     * <th>Name</th>
     * <th>Point Earned</th>
     * <th>Total Points</th>
     * </tr>
     * <tr>
     * <td>Exams</td>
     * <td>180</td>
     * <td>200</td>
     * </tr>
     * <tr>
     * <td>Homework</td>
     * <td>85</td>
     * <td>100</td>
     * </tr>
     * <tr>
     * <td>Daily Quizzes</td>
     * <td>100</td>
     * <td>100</td>
     * </tr>
     * </table>
     * 
     * This makes the total grade for CSSE120 {@code 365/400}, or {@code .9125}.
     * 
     * @return The created {@link Course}.
     */
    public static Course csse120() {
        Course result = new Course();
        result.setName("CSSE120");
        result.setCourseType(CourseType.POINT_TOTAL);

        PointTotalGradeComponent exams = new PointTotalGradeComponent();
        exams.setName("Exams");
        exams.setTotalPoints(200);
        exams.setPointsEarned(180);

        result.addGradeComponent(exams);

        PointTotalGradeComponent homework = new PointTotalGradeComponent();
        homework.setName("Homework");
        homework.setTotalPoints(100);
        homework.setPointsEarned(85);

        result.addGradeComponent(homework);

        PointTotalGradeComponent quizzes = new PointTotalGradeComponent();
        quizzes.setName("Daily Quizzes");
        quizzes.setTotalPoints(100);
        quizzes.setPointsEarned(100);

        result.addGradeComponent(quizzes);

        return result;
    }
    
    public static Course CSSE333() {
        // TODO
        return null;
    }
}
