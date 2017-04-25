package com.zealens.face.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.zealens.face.R;
import com.zealens.face.util.CalculateUtil;

/**
 * Created on 2016/11/22
 * in BlaBla by Kyle
 */

public class CornerDispatchView extends FrameLayout {

    @IntDef({ Type.left, Type.right,Type.both})
    public @interface Type {
        int left = 0;
        int right = 1;
        int both = 2;
    }

    private boolean mEdgeCached;
    private Point A;
    private Point B;
    private Point C;
    private Point D;
    private Point A1;
    private Point B1;
    private Point C1;
    private Point D1;
    private Point AD;
    private Point BC;
    private Point mTouchPoint = new Point();
    @Type
    private int mIgnoreType = Type.both;

    public CornerDispatchView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CornerDispatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CornerDispatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("ResourceType")
    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray ar = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CornerDispatchView, defStyleAttr, 0);
            mIgnoreType = ar.getInteger(R.styleable.CornerDispatchView_corner_ignore, Type.both);
            ar.recycle();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return areaDistinguish(this, event);
    }

    private boolean areaDistinguish(View v, MotionEvent event) {
        if (!mEdgeCached) {
            int[] locations = new int[2];
            v.getLocationInWindow(locations);
            int width = v.getWidth();
            int height = v.getHeight();
            int left = locations[0];
            int right = left + width;
            int top = locations[1];
            int bottom = top + height;
            int center = (top + bottom) >> 1;
            int dx = (int) (.17f * width);
            A = new Point(left, top);
            B = new Point(right, top);
            C = new Point(right, bottom);
            D = new Point(left, bottom);
            A1 = new Point(left + dx, top);
            B1 = new Point(right - dx, top);
            C1 = new Point(right - dx, bottom);
            D1 = new Point(left + dx, bottom);
            AD = new Point(left, center);
            BC = new Point(right, center);
            mEdgeCached = true;
        }

        mTouchPoint.x = (int) event.getRawX();
        mTouchPoint.y = (int) event.getRawY();
        boolean inLeftCorner = CalculateUtil.pointInTriangle(mTouchPoint, A, A1, AD) || CalculateUtil.pointInTriangle(mTouchPoint, D, D1, AD);
        boolean inRightCorner = CalculateUtil.pointInTriangle(mTouchPoint, B, B1, BC) || CalculateUtil.pointInTriangle(mTouchPoint, C, C1, BC);
        if (mIgnoreType == Type.both)
            return !(inLeftCorner || inRightCorner) && super.dispatchTouchEvent(event);
        else if (mIgnoreType == Type.left)
            return !inLeftCorner && super.dispatchTouchEvent(event);
        else return !inRightCorner && super.dispatchTouchEvent(event);
    }
}
