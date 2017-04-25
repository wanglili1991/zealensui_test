package com.zealens.face.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zealens.face.R;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.domain.module.BasePoint;
import com.zealens.face.domain.module.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/11/22
 * in BlaBla by Kyle
 */

public class BallCastPointAnalyzeView extends FrameLayout {
    @IntDef({Mode.with_ruler, Mode.without_ruler})
    public @interface Mode {
        int with_ruler = 0;
        int without_ruler = 1;
    }

    @IntDef({Dot.green, Dot.orange, Dot.yellow, Dot.blue})
    public @interface Dot {
        int green = 0;
        int orange = 1;
        int yellow = 2;
        int blue = 3;
    }

    @DrawableRes
    private int[] BALL_DOT_RESOURCE_ID_ARR = {
            R.drawable.analyze_ball_dot_green,
            R.drawable.analyze_ball_dot_orange,
            R.drawable.analyze_ball_dot_yellow,
            R.drawable.analyze_ball_dot_blue,
    };

    /**
     * points order
     * * |-------------------|
     * * |                   |
     * * |                   |
     * * |                   |
     * A 1----2----3----4----|
     * * |    |    |    |    |
     * * |    |    |    |    |
     * * |    |    |    |    |
     * * |    |    |    |    |
     * * |    |    |    |    |
     * * |    |    |    |    |
     * B |----1----2----3----4
     */
    private final float COORDINATE_SCALE_RATIO = 520f / 1188; /*520/11887*/
    private final PointF PLAYGROUND_CENTER_DOT = new PointF(270, 6);
    private final PointF PLAYGROUND_CENTER_DOT_4_WITHOUT_RULER = new PointF(268, 6 + 513);
    private final PointF PLAYGROUND_DOT_A_1 = new PointF(91, 243);
    private final PointF PLAYGROUND_DOT_B_4 = new PointF(443, 517);
    private PointF MATRIX_POINT_F;
    private PointF MATRIX_POINT_F_4_WITHOUT_RULER;
    private Bitmap[] DOT_BITMAP_ARR;
    private List<BasePoint> mPointFs;
    private List<BasePoint> mYellowPointFs;
    private List<BasePoint> mOrangePointFs;
    private List<BasePoint> mBluePointFs;
    @Mode
    private int mMode;

    public BallCastPointAnalyzeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public BallCastPointAnalyzeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BallCastPointAnalyzeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("ResourceType")
    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BallCastPointAnalyzeView, defStyleAttr, 0);
            mMode = ta.getInteger(R.styleable.BallCastPointAnalyzeView_cast_type, Mode.with_ruler);
            ta.recycle();
        }

        inflate(context, mMode == Mode.with_ruler ? R.layout.ball_cast_point_analyze_with_ruler_layout
                : R.layout.ball_cast_point_analyze_layout, this);
        DOT_BITMAP_ARR = new Bitmap[mMode == Mode.with_ruler ? 1 : Dot.class.getFields().length];
        DOT_BITMAP_ARR[Dot.green] = BitmapFactory.decodeResource(context.getResources(), BALL_DOT_RESOURCE_ID_ARR[Dot.green]);
        if (mMode != Mode.with_ruler) {
            DOT_BITMAP_ARR[Dot.orange] = BitmapFactory.decodeResource(context.getResources(), BALL_DOT_RESOURCE_ID_ARR[Dot.orange]);
            DOT_BITMAP_ARR[Dot.yellow] = BitmapFactory.decodeResource(context.getResources(), BALL_DOT_RESOURCE_ID_ARR[Dot.yellow]);
            DOT_BITMAP_ARR[Dot.blue] = BitmapFactory.decodeResource(context.getResources(), BALL_DOT_RESOURCE_ID_ARR[Dot.blue]);
        }
        int dx = DOT_BITMAP_ARR[Dot.green].getWidth() / 2;
        int dy = DOT_BITMAP_ARR[Dot.green].getHeight() / 2;
        MATRIX_POINT_F = new PointF(PLAYGROUND_CENTER_DOT.x - dx, PLAYGROUND_CENTER_DOT.y - dy);
        mPointFs = new ArrayList<>(1);
        mYellowPointFs = new ArrayList<>(1);
        mOrangePointFs = new ArrayList<>(1);
        mBluePointFs = new ArrayList<>(1);
        MATRIX_POINT_F_4_WITHOUT_RULER = new PointF(PLAYGROUND_CENTER_DOT_4_WITHOUT_RULER.x - dx, PLAYGROUND_CENTER_DOT_4_WITHOUT_RULER.y - dy);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mMode == Mode.with_ruler) {
            drawDots(canvas, mPointFs, Dot.green, MATRIX_POINT_F, false);
        } else {
            drawDots(canvas, mOrangePointFs, Dot.orange, MATRIX_POINT_F_4_WITHOUT_RULER, true);
            drawDots(canvas, mYellowPointFs, Dot.yellow, MATRIX_POINT_F_4_WITHOUT_RULER, true);
            drawDots(canvas, mBluePointFs, Dot.blue, MATRIX_POINT_F_4_WITHOUT_RULER, true);
        }
    }

    private void drawDots(Canvas canvas, List<BasePoint> pointFs, @Dot int dot, PointF matrixPF, boolean withAreaIgnore) {
        float fx;
        float fy;
        for (BasePoint reversedPoint : pointFs) {
            fx = reversedPoint.y / 10 * COORDINATE_SCALE_RATIO + matrixPF.x;
            fy = reversedPoint.x / 10 * COORDINATE_SCALE_RATIO + matrixPF.y;
            if (withAreaIgnore) {
                boolean outOfArea = fx < PLAYGROUND_DOT_A_1.x || fx > PLAYGROUND_DOT_B_4.x
                        || fy < PLAYGROUND_DOT_A_1.y || fy > PLAYGROUND_DOT_B_4.y;
                if (outOfArea) continue;
            }
            canvas.drawBitmap(DOT_BITMAP_ARR[dot], fx, fy, null);
        }
    }

    public void paintReceiveDots(List<BasePoint> pointFs) {
        mPointFs = pointFs;
        invalidate();
    }

    public void paintServeDots(List<Point3D> pointDataS) {
        mYellowPointFs.clear();
        mOrangePointFs.clear();
        mBluePointFs.clear();
        for (Point3D pd : pointDataS) {
            if (pd.getZ() == TennisBase.PlayerServe.ACE)
                mYellowPointFs.add(pd);
            else if (pd.getZ() == TennisBase.PlayerServe.FIRST)
                mOrangePointFs.add(pd);
            else if (pd.getZ() == TennisBase.PlayerServe.SECOND
                    || pd.getZ() == TennisBase.PlayerServe.IGNORE)
                mBluePointFs.add(pd);
        }
        invalidate();
    }
}
