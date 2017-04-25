package com.zealens.face.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.zealens.face.R;
import com.zealens.face.util.DisplayUtil;
import com.zealens.face.util.FontMaster;
import com.zealens.face.util.ImageUtil;

/**
 * Created on 2017/3/11
 * in BlaBla by Kyle
 */

public class ResourcefulRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    @DrawableRes
    private int mResourceId;
    private Bitmap mBgBitmap;
    private int mMargin;

    public ResourcefulRadioButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ResourcefulRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ResourcefulRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        FontMaster.assignFont(context, attrs, defStyleAttr, this);
        setEllipsize(TextUtils.TruncateAt.END);
        setMaxLines(context.getResources().getInteger(R.integer.mode_choose_panel_item_text_lines));

        if (attrs != null) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ResourcefulRadioButton, defStyleAttr, 0);

            mResourceId = ta.getResourceId(R.styleable.ResourcefulRadioButton_background_pic_resource, 0);
            ta.recycle();
        }

        DisplayUtil.addTaskWithPreDrawListener(this, () -> {
            if (mResourceId != 0) {
                int size = this.getWidth();
                mMargin = context.getResources().getDimensionPixelSize(R.dimen.train_mode_break_mode_portrait_edge_margin);
                int picSize = size - mMargin * 2;
                mBgBitmap = ImageUtil.getScaledBitmap(context, mResourceId, picSize, picSize);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBgBitmap != null) canvas.drawBitmap(mBgBitmap, mMargin, mMargin, null);
        super.onDraw(canvas);
    }
}
