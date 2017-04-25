package com.zealens.face.util;

import android.view.View;

import com.zealens.face.base.Rule;

import junit.framework.Assert;

/**
 * Created by Kyle on 13/04/2017
 */

public class AppViewUtil extends ViewUtil {

    @SuppressWarnings("unchecked")
    public static <T> void findViewSideAB(View root, T[] arr, int[] ids) {
        Assert.assertTrue(arr.length == ids.length);
        arr[Rule.Side.A] = (T) root.findViewById(ids[Rule.Side.A]);
        arr[Rule.Side.B] = (T) root.findViewById(ids[Rule.Side.B]);
    }
}
