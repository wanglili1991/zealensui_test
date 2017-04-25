package com.zealens.face.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zealens.face.R;

/**
 * desc: use the fluent interface style
 * Created by KyleCe on 2015/10/22.
 */
public enum FontMaster {
    INSTANCE;

    @IntDef({Type.Default, Type.MSYH, Type.UnidreamLed, Type.HelveticaBold, Type.Bank, Type.PingFang})
    public @interface Type {
        int Default = -1;
        int MSYH = 0;
        int UnidreamLed = 1;
        int HelveticaBold = 2;
        int Bank = 3;
        int PingFang = 4;
    }

    private Typeface mCustomFont;
    private Typeface mMSYH;
    private Typeface mUnidreamLED;
    private Typeface mHelveticaBold;
    private Typeface mBank;
    private Typeface mPingFang;

    public static void init(Context context) {
        if (context != null && (INSTANCE.mUnidreamLED == null || INSTANCE.mCustomFont == null)) {
            INSTANCE.mMSYH = Typeface.createFromAsset(context.getAssets(), "fonts/MSYH.TTC");
            INSTANCE.mUnidreamLED = Typeface.createFromAsset(context.getAssets(), "fonts/UNIDREAMLED.TTF");
            INSTANCE.mHelveticaBold = Typeface.createFromAsset(context.getAssets(), "fonts/HELVETICA_CONDENSED_BOLD.OTF");
            INSTANCE.mBank = Typeface.createFromAsset(context.getAssets(), "fonts/BANKGOTHICBT_MEDIUM_OPENTYPE.OTF");
            INSTANCE.mPingFang = Typeface.createFromAsset(context.getAssets(), "fonts/PINGFANG_MEDIUM.TTF");
            INSTANCE.mCustomFont = INSTANCE.mMSYH;
        }
    }

    /**
     * @throws NullPointerException if not init
     */
    public static FontMaster font(@Type int type) {
        if (INSTANCE.mCustomFont == null)
            throw new NullPointerException("init first using with(Context) API ");

        switch (type) {
            case Type.Default:
            case Type.MSYH:
            default:
                INSTANCE.mCustomFont = INSTANCE.mMSYH;
                break;
            case Type.UnidreamLed:
                INSTANCE.mCustomFont = INSTANCE.mUnidreamLED;
                break;
            case Type.HelveticaBold:
                INSTANCE.mCustomFont = INSTANCE.mHelveticaBold;
                break;
            case Type.Bank:
                INSTANCE.mCustomFont = INSTANCE.mBank;
                break;
            case Type.PingFang:
                INSTANCE.mCustomFont = INSTANCE.mPingFang;
                break;
        }

        return INSTANCE;
    }

    public static Typeface get() {
        return get(Type.Default);
    }

    /**
     * @throws NullPointerException if not init
     */
    public static Typeface get(@Type int type) {
        if (INSTANCE.mCustomFont == null)
            throw new NullPointerException("init first using with(Context) API ");

        switch (type) {
            case Type.MSYH:
                return INSTANCE.mMSYH;
            case Type.UnidreamLed:
                return INSTANCE.mUnidreamLED;
            case Type.HelveticaBold:
                return INSTANCE.mHelveticaBold;
            case Type.Bank:
                return INSTANCE.mBank;
            case Type.PingFang:
                return INSTANCE.mPingFang;
            case Type.Default:
            default:
                return INSTANCE.mHelveticaBold;
        }
    }

    public static <TV extends TextView> void set(TV... tvs) {
        setFont(tvs);
    }

    public static <TV extends TextView> void setFont(TV... tvs) {
        for (TV tv : tvs) if (tv != null) tv.setTypeface(INSTANCE.mCustomFont);
    }

    public static <VG extends ViewGroup> void set(VG vg) {
        if (vg == null || vg.getChildCount() == 0) return;
        int count = vg.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = vg.getChildAt(i);
            if (child instanceof ViewGroup) {
                set((ViewGroup) child);
            } else if (child instanceof TextView) {
                set((TextView) child);
            }
        }
    }

    public static void assignFont(Context context, AttributeSet attrs, int defStyleAttr, TextView tv) {
        if (attrs != null) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FontTextView, defStyleAttr, 0);
            @Type
            int fontType = ta.getInteger(R.styleable.FontTextView_font_type, Type.Default);
            font(fontType).set(tv);
            ta.recycle();
        } else {
            font(Type.MSYH).set(tv);
        }
    }
}
