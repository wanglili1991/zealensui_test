package com.zealens.face;

import android.content.Context;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zealens.face.util.CalculateUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BaseTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.zealens.zealensui", appContext.getPackageName());
    }

    @Test
    public void inTriangle() throws Exception {
        int[] locations = {595,369};
        int width = 729;
        int height = 341;
        int left = locations[0];
        int right = left + width;
        int top = locations[1];
        int bottom = top + height;
        int center = (top + bottom) >> 1;
        int dx = (int) (.17f * width);
        Point A = new Point(left, top);
        Point B = new Point(right, top);
        Point C = new Point(right, bottom);
        Point D = new Point(left, bottom);
        Point A1 = new Point(left + dx, top);
        Point B1 = new Point(right - dx, top);
        Point C1 = new Point(right - dx, bottom);
        Point D1 = new Point(left + dx, bottom);
        Point AD = new Point(left, center);
        Point BC = new Point(right, center);
        Point touchPoint = new Point(620, 648);
        Point touchPointRight = new Point(1299, 648);

        boolean inLeftCorner = CalculateUtil.pointInTriangle(touchPoint, A, A1, AD)|| CalculateUtil.pointInTriangle(touchPoint, D, D1, AD);
        boolean inRightCorner = CalculateUtil.pointInTriangle(touchPointRight, B, B1, BC) || CalculateUtil.pointInTriangle(touchPointRight, C, C1, BC);

        assertTrue(inLeftCorner);
        assertTrue(inRightCorner);
    }

    public void inArea() throws Exception {
        Point a = new Point(0,0);
        Point b = new Point(100,0);
        Point c = new Point(0,100);

        Point d = new Point(55,120);

        assertFalse(CalculateUtil.pointInTriangle(d,a,b,c));
        Point e = new Point(55,1);
        assertTrue(CalculateUtil.pointInTriangle(e,a,b,c));
        Point f = new Point(-5,20);
        assertFalse(CalculateUtil.pointInTriangle(f,a,b,c));
    }
}
