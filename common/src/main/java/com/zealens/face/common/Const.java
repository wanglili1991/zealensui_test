package com.zealens.face.common;

/**
 * Created on 2017/3/4
 * in BlaBla by Kyle
 */

public class Const {
    public static final String SHARED_PREFERENCES_KEY = "global_preference";

    // video slow speed
    public static final int TARGET_SLOWER_SPEED_LEVEL_COUNT = 5;// including full speed
    public static final String[] TARGET_SLOWER_SPEED_TAG_ARR = {"2x", "4x", "8x", "16x", "",};// indication next level speed
    public static final float[] TARGET_SLOWER_SPEED_ARR = {1f, .5f, .25f, .125f, .0625f,};

}
