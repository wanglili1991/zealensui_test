package com.zealens.face.base.analyzeglance;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zealens.face.R;

/**
 * Created on 2017/3/14
 * in BlaBla by Kyle
 */

public class IndicatorItemDecorator extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int margin = view.getResources().getDimensionPixelSize(R.dimen.indicator_item_margin);
        outRect.bottom = margin;
    }
}
