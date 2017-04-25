package com.zealens.face.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 2016/11/22
 * in BlaBla by Kyle
 */

public class CompatUtil {
    public static final boolean ATLEAST_MARSHMALLOW = Build.VERSION.SDK_INT >= 23;

    public static final boolean ATLEAST_LOLLIPOP_MR1 =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;

    public static final boolean ATLEAST_LOLLIPOP =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    public static final boolean ATLEAST_N =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;

    public static final boolean ATLEAST_KITKAT =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    public static final boolean ATLEAST_KITKAT_WATCH =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH;

    public static final boolean ATLEAST_JB =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;

    public static final boolean ATLEAST_JB_MR1 =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;

    public static final boolean ATLEAST_JB_MR2 =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    public static final boolean ATLEAST_HONEYCOMB =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    @SuppressWarnings("all")
    public static Locale getLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }

    public static String getString(Context context, @StringRes int stringRes) {
        return context.getResources().getString(stringRes);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int res) {
        return ContextCompat.getDrawable(context, res);
    }

    public static int getColor(Context context, @ColorRes int res) {
        return ContextCompat.getColor(context, res);
    }

    public static int getDimenOffset(Context context, @DimenRes int res) {
        return context.getResources().getDimensionPixelOffset(res);
    }

    public static String getGeneralFormattedTime(Context context, @StringRes int res) {
        Calendar c = Calendar.getInstance(getLocale(context));
        c.setTime(new Date());

        int month = c.get(Calendar.MONTH) + 1;/*starts from 0*/
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return String.format(getLocale(context), getString(context, res), month, dayOfMonth, year, hour, minute);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
}
