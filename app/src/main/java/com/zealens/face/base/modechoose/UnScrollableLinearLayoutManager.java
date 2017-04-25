package com.zealens.face.base.modechoose;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created on 2017/3/11
 * in BlaBla by Kyle
 */

public class UnScrollableLinearLayoutManager extends LinearLayoutManager {

    public UnScrollableLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}