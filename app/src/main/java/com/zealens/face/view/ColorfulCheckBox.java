package com.zealens.face.view;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.zealens.face.R;
import com.zealens.face.util.FontMaster;

/**
 * Created on 2016/11/22
 * in BlaBla by Kyle
 */

public class ColorfulCheckBox extends AppCompatCheckBox {
    public ColorfulCheckBox(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ColorfulCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ColorfulCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        FontMaster.assignFont(context, attrs, defStyleAttr, this);
        setEllipsize(TextUtils.TruncateAt.END);
        setMaxLines(context.getResources().getInteger(R.integer.mode_choose_panel_item_text_lines));
    }
}
