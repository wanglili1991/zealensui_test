package com.zealens.face.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

/**
 * Created on 2016/11/13
 * in BlaBla by Kyle
 */

public class DisplayUtil {
    private static final float density = Resources.getSystem().getDisplayMetrics().density;
    private static final float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;

    public static int sp2px(float sp) {
        return (int) (sp * scaledDensity + 0.5f);
    }

    public static int px2sp(float pix) {
        return (int) (pix / scaledDensity + 0.5f);
    }

    public static int px2Dp(float f) {
        return (int) ((f / density) + 0.5f);
    }

    public static int dp2px(float dp) {
        return (int) (dp * density + 0.5f);
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static float dpiFromPx(int size, DisplayMetrics metrics) {
        float densityRatio = (float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        return (size / densityRatio);
    }

    public static int pxFromDp(float size, DisplayMetrics metrics) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                size, metrics));
    }

    public static int pxFromSp(float size, DisplayMetrics metrics) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, metrics));
    }

    public static int pxFromDp(Context context, @DimenRes int dimenRes) {
        return context.getResources().getDimensionPixelOffset(dimenRes);
    }

    public static Drawable getPackageIcon(Context context, String packageName) throws PackageManager.NameNotFoundException {
        if (context == null || TextUtils.isEmpty(packageName)) return null;

        return context.getPackageManager().getApplicationIcon(packageName);
    }

    public static Drawable getPackageIconIgnoreException(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) return null;

        try {
            return context.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return null;
    }

    public static void addTaskWithPreDrawListener(final View view, final Runnable task) {
        if (view == null || task == null) return;
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                task.run();
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    @SuppressWarnings("deprecation")
    public static void addTaskWithGlobalLayoutListener(final View view, final Runnable task) {
        if (view == null || task == null) return;
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                task.run();
            }
        });
    }

    public static void setDrawableToImageView(ImageView iv, Drawable targetDrawable, Drawable defaultDrawable) {
        if (iv == null || (targetDrawable == null && defaultDrawable == null)) return;
        if (targetDrawable != null) iv.setImageDrawable(targetDrawable);
        else iv.setImageDrawable(defaultDrawable);
    }

    public static void setDrawableToImageView(ImageView iv, Context context, String pkgName, @DrawableRes int defaultDrawableRes) {
        setDrawableToImageView(iv, getPackageIconIgnoreException(context, pkgName), defaultDrawableRes);
    }

    public static void setDrawableToImageView(ImageView iv, Drawable targetDrawable, @DrawableRes int defaultDrawableRes) {
        if (iv == null || (targetDrawable == null && defaultDrawableRes == 0)) return;
        if (targetDrawable != null) iv.setImageDrawable(targetDrawable);
        else iv.setImageResource(defaultDrawableRes);
    }

    @SuppressWarnings("deprecation")
    public static <V extends View> void setViewDrawable(V v, Drawable drawable) {
        if (v == null || drawable == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            v.setBackground(drawable);
        else v.setBackgroundDrawable(drawable);
    }

    public static void changeProgressBarDrawableResourceSafely(Context context, ProgressBar pb, @DrawableRes int res) {
        Rect bounds = pb.getProgressDrawable().getBounds();
        pb.setProgressDrawable(CompatUtil.getDrawable(context, res));
        pb.getProgressDrawable().setBounds(bounds);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isRtl(Context ctx) {
        return CompatUtil.ATLEAST_JB_MR1 && (ctx.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
    }

    public static int getDisplayWidth(@NonNull Context context) {
        return getDisplaySize(context)[0];
    }

    public static int getDisplayHeight(@NonNull Context context) {
        return getDisplaySize(context)[1];
    }

    public static int[] getDisplaySize(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new int[]{size.x, size.y};
    }

    public static int getScreenWidth(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getScreenHeight(context, false);
    }

    public static int getScreenHeight(Context context, boolean includeVirtualButtonBar) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        if (includeVirtualButtonBar)
            wm.getDefaultDisplay().getRealMetrics(metrics);
        return metrics.heightPixels;
    }

    @SuppressWarnings("NewApi")
    public void parseScreenSize(Point screenSize, final Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        android.view.Display display = wm.getDefaultDisplay();
        if (CompatUtil.ATLEAST_JB_MR1) display.getRealSize(screenSize);
        else display.getSize(screenSize);
    }

    public static <V extends View> void setMargin(V v, int margin) {
        if (v == null) return;
        ViewGroup.LayoutParams lp = v.getLayoutParams();

        if (lp instanceof FrameLayout.LayoutParams)
            ((FrameLayout.LayoutParams) lp).setMargins(margin, margin, margin, margin);
        else if (lp instanceof RelativeLayout.LayoutParams)
            ((RelativeLayout.LayoutParams) lp).setMargins(margin, margin, margin, margin);
        else if (lp instanceof LinearLayout.LayoutParams)
            ((LinearLayout.LayoutParams) lp).setMargins(margin, margin, margin, margin);
        else if (lp instanceof TableLayout.LayoutParams)
            ((TableLayout.LayoutParams) lp).setMargins(margin, margin, margin, margin);
    }

    public static Bitmap getBitmap(Context ctx, String pkgName) throws PackageManager.NameNotFoundException {
        return drawableToBitmap(getPackageIcon(ctx, pkgName));
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static <BELA extends BaseExpandableListAdapter, ELV extends ExpandableListView> void expandGroup(BELA e, ELV elv) {
        if (e == null || e.getGroupCount() == 0 || elv == null) return;
        int size = e.getGroupCount();
        for (int i = 0; i < size; i++) elv.expandGroup(i);
    }
}