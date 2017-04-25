package com.zealens.face.base.modechoose;

import com.zealens.face.base.Rule;

/**
 * Created on 2017/3/17
 * in BlaBla by Kyle
 */

public class SideEffectUnit {
    @Rule.Side
    public int side;
    @Rule.TrainMode
    public int trainMode;
    public boolean checked;

    public SideEffectUnit(int side, int trainMode, boolean checked) {
        this.side = side;
        this.trainMode = trainMode;
        this.checked = checked;
    }
}
