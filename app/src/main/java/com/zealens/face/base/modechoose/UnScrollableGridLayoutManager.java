package com.zealens.face.base.modechoose;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created on 2017/3/11
 * in BlaBla by Kyle
 */

public class UnScrollableGridLayoutManager extends GridLayoutManager {
    public UnScrollableGridLayoutManager(Context context, int spanCount, int orientation,
                                         boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}