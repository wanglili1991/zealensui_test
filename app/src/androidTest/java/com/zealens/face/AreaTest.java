package com.zealens.face;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;

import junit.framework.Assert;

import org.junit.Test;

/**
 * in BlaBla by Kyle
 */

public class AreaTest {

    private final float COORDINATE_SCALE_RATIO = 520f / 1188; /*520/11887*/

    @Test
    public void area() throws Exception {
        Matrix matrix = new Matrix();

        System.out.println(COORDINATE_SCALE_RATIO * 1188);
        Assert.assertTrue(COORDINATE_SCALE_RATIO * 1188 < 1188);

        matrix.setTranslate(100, 100);
        new PointF(1231.f, 93.f);
        Point[] arr = {
                new Point(-504, -412),
                new Point(-1206, -625),
                new Point(1231, 93),
                new Point(-604, -411),
                new Point(-1206, -625),
                new Point(1227, -112),
                new Point(-1331, 381),
                new Point(1227, -112),
                new Point(-640, 349),
                new Point(-1331, 381),
                new Point(-498, 1),
                new Point(-1249, -128),
                new Point(-598, 0),
                new Point(-1249, -128),
                new Point(1221, -113),
                new Point(-552, 121),
                new Point(1221, 113),
                new Point(-552, -121),
                new Point(-1188, -204),
                new Point(1188, -204),
                new Point(1385, -204),
                new Point(1189, -143),
                new Point(1335, -100),
                new Point(1227, -113),
                new Point(-552, 121),
                new Point(507, -411),
                new Point(1073, -591),
                new Point(1227, 113),
                new Point(-552, -121),
                new Point(-1260, -208),
                new Point(507, 412),
                new Point(1073, 591),
                new Point(1227, -113),
                new Point(-552, 121),
                new Point(507, -549),
                new Point(1073, -660),
                new Point(1227, 113),
                new Point(-552, -121),
                new Point(-1260, -208),
                new Point(507, 548),
                new Point(1073, 660),
        };

    }
}
