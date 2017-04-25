package com.zealens.face.util;

import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by KyleCe on 2015/10/15.
 */
public class ViewUtil {
    public static void hide(View... views) {
        for (View v : views) if (v != null) v.setVisibility(View.GONE);
    }

    public static void invisible(View... views) {
        for (View v : views) if (v != null) v.setVisibility(View.INVISIBLE);
    }

    public static void show(View... views) {
        for (View v : views) if (v != null) v.setVisibility(View.VISIBLE);
    }

    public static void setVisibility(int visibility, View... views) {
        for (View v : views) if (v != null) v.setVisibility(visibility);
    }

    public static void setVisibility(boolean visible, View... views) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        for (View v : views) if (v != null) v.setVisibility(visibility);
    }

    public static void reverseVisibility(View... views) {
        for (View v : views)
            if (v != null)
                v.setVisibility(v.isShown() ? View.GONE : View.VISIBLE);
    }

    public static void enable(View... views) {
        enable(true, views);
    }

    public static void disable(View... views) {
        enable(false, views);
    }

    public static void enable(boolean enable, View... views) {
        for (View v : views) if (v != null) v.setEnabled(enable);
    }

    public static void showAndHide(View sv, View hv) {
        show(sv);
        hide(hv);
    }

    public static void showAndInvisible(View sv, View iv) {
        show(sv);
        invisible(iv);
    }

    public static void hideAndShow(View hv, View sv) {
        showAndHide(sv, hv);
    }

    public static boolean isVisible(View v) {
        return v != null && v.getVisibility() == View.VISIBLE;
    }

    public static boolean isShown(View v) {
        return v != null && v.isShown();
    }

    public static void enClick(View... views) {
        for (View v : views) if (v != null) v.setClickable(true);
    }

    public static void disClick(View... views) {
        for (View v : views) if (v != null) v.setClickable(false);
    }

    public static void clearClickListener(View... views) {
        for (View v : views) if (v != null) v.setOnClickListener(null);
    }


    public static void setClickListener(View.OnClickListener listener, View... views) {
        for (View v : views) if (v != null) v.setOnClickListener(listener);
    }

    /**
     * set background color by color int
     *
     * @param views views you want to set background color
     * @param color the color that user want to set
     */
    public static void setBackgroundColor(int color, View... views) {
        for (View v : views) if (v != null) v.setBackgroundColor(color);
    }

    /**
     * set click listener for views
     *
     * @param listener the click listener to assign
     * @param views    views
     */
    public static void clickListen(View.OnClickListener listener, View... views) {
        for (View v : views) if (v != null && listener != null) v.setOnClickListener(listener);
    }

    public static void setPaintAlpha(int alpha, Paint... paints) {
        for (Paint p : paints) if (p != null && legalAlpha(alpha)) p.setAlpha(alpha);
    }

    /**
     * @param alpha [0..255]
     */
    private static boolean legalAlpha(int alpha) {
        return 0 <= alpha && alpha <= 255;
    }

    public static void cancelAnimationDeeply(View... views) {
        for (View view : views) {
            if (view == null || view.getAnimation() == null) continue;
            view.getAnimation().cancel();
            view.getAnimation().setAnimationListener(null);
            view.clearAnimation();
            view.setAnimation(null);
        }
    }

    public static void clearAnimation(View... views) {
        for (View v : views)
            if (v != null && v.getAnimation() != null) v.clearAnimation();
    }

    public static <V extends View> void clearAnimation(Collection<V> views) {
        for (V v : views)
            if (v != null && v.getAnimation() != null) v.clearAnimation();
    }

    public static <V extends ValueAnimator> void cancelAnimator(Collection<V> cva) {
        for (V a : cva)
            if (a != null) a.cancel();
    }

    public static void startWithNewAnim(Animation anim, View... views) {
        Assert.assertNotNull(anim);

        for (View v : views) {
            if (v == null) continue;

            if (v.getAnimation() != null) v.clearAnimation();
            v.startAnimation(anim);
        }
    }

    public static void clearPadding(View... views) {
        for (View v : views) {
            if (v == null) continue;

            v.setPadding(0, 0, 0, 0);
        }
    }

    public static <VS extends ViewStub> void inflateStubWithRes(VS vs, @LayoutRes int res) {
        if (vs == null || res <= 0) return;
        vs.setLayoutResource(res);
        vs.inflate();
    }

    public static <TV extends TextView> void customTextViewsWithStr(@Nullable String str, TV... tvs) {
        if (tvs == null || str == null) return;
        for (TV tv : tvs) if (tv != null) tv.setText(str);
    }

    public static <VG extends ViewGroup> List<View> getAllChildViews(VG vg) {
        if (vg == null || vg.getChildCount() == 0) return new ArrayList<>(0);
        List<View> list = new ArrayList();
        int count = vg.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = vg.getChildAt(i);
            if (child instanceof ViewGroup) list.addAll(getAllChildViews((ViewGroup) child));
            else list.add(child);
        }
        return list;
    }

    public static <VG extends ViewGroup, V extends View> void removeChild(VG parent, V child) {
        if (parent == null || child == null) return;
        if (child.getParent() != null)
            try {
                parent.removeView(child);
            } catch (Exception ignore) {
            }
    }

    public static <V extends View> void hideAllChildView(V v) {
        if (!(v instanceof ViewGroup)) return;

        ViewGroup vg = (ViewGroup) v;
        int size = vg.getChildCount();
        View child;
        for (int i = 0; i < size; i++) {
            child = vg.getChildAt(i);
            if (child instanceof ViewGroup)
                hideAllChildView(child);
            else hide(child);
        }
    }

    public void cancelAnimation(View... views) {
        for (View view : views) {
            if (view.getAnimation() == null) continue;
            view.getAnimation().cancel();
        }
    }
}
