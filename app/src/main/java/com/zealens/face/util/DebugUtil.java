package com.zealens.face.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by elex on 30/03/2017.
 */

public class DebugUtil extends LogcatUtil {
    public static Toast toast = null;
    public static boolean ON = true;

    /**
     * desc：同single toast ，简化输入
     */
    public static void t(Context context, String msg) {
        singleToast(context, msg);
    }

    public static void td(Context context, String msg) {
        if (ON) singleToast(context, msg);
    }

    public static void tsd(Context context, String msg) {
        t(context, msg);
        sd(msg, msg);
    }

    /**
     * toast and print the msg, separate with 't'
     */
    public static void tp(Context context, String msg) {
        t(context, msg);
        s('t', msg);
    }

    /**
     * 单toast，解决toast重复显示问题
     */
    public static void singleToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * desc: 并没有加标志位判断，保证一定能够显示toast，所以调试时最好不要toast来查看调试信息
     * 默认为short
     */
    static public void toast(Context c, String s) {
        shortToast(c, s);
    }

    /**
     * desc: 加标志位判断，调试时才toast提示
     */
    static public void toastDebug(Context c, String s) {
        if (!ON) return;
        toast(c, s);
    }

    /**
     * Toast 函数 short
     */
    static public void shortToast(Context c, String s) {
        showToast(c, s, true);
    }

    /**
     * Toast 函数 long
     */
    static public void longToast(Context c, String s) {
        showToast(c, s, false);
    }


    /**
     * b: true 时short，false 时long
     */
    static public void showToast(Context c, String s, boolean b) {
        if (c == null) return;
        Toast.makeText(c, s, b ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }
}
