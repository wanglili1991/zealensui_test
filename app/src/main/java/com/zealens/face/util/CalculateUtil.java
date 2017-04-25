package com.zealens.face.util;

import android.graphics.Point;

/**
 * Created on 2017/3/9
 * in BlaBla by Kyle
 */

public class CalculateUtil {
    public static float sign(Point p1, Point p2, Point p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    public static boolean pointInTriangle(Point pt, Point v1, Point v2, Point v3) {
        boolean b1, b2, b3;
        b1 = sign(pt, v1, v2) < 0.0f;
        b2 = sign(pt, v2, v3) < 0.0f;
        b3 = sign(pt, v3, v1) < 0.0f;

        return ((b1 == b2) && (b2 == b3));
    }
}
