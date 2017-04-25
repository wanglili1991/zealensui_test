package com.zealens.face.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zealens.face.util.FontMaster;

/**
 * Created on 2016/11/22
 * in BlaBla by Kyle
 */

public class FontTextView extends AppCompatTextView {
    public FontTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        FontMaster.assignFont(context, attrs, defStyleAttr, this);
    }
}
