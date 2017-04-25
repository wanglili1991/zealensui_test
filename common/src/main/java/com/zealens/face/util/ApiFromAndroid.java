package com.zealens.face.util;

/**
 * Created on 2017/3/17
 * in BlaBla by Kyle
 */

public class ApiFromAndroid {
    public static boolean isEmpty(CharSequence str) {
        return TextUtil_isEmpty(str);
    }

    public static boolean TextUtil_isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}
