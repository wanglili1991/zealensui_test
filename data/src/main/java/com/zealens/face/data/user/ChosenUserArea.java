package com.zealens.face.data.user;

import static com.zealens.face.data.user.UserIndex.USER_0;

/**
 * Created on 2017/3/20
 * in BlaBla by Kyle
 */

public enum ChosenUserArea {
    /**
     * index
     * |----------------------|----------------------|
     * |------ 1st-(0)--------|----------- 1st-(1)---|
     * |----------------------|----------------------|
     * |------ 2nd-(2)--------|----------- 2nd-(3)---|
     * |----------------------|----------------------|
     * |-------Side A---------|---------Side B-------|
     */
    SIDE_A_1ST(0),
    SIDE_B_1ST(1),
    SIDE_A_2ND(2),
    SIDE_B_2ND(3),;
    public int index;

    ChosenUserArea(int i) {
        index = i;
    }

    public static ChosenUserArea find(int i) {
        if (i == USER_0.index) return SIDE_A_1ST;
        else if (i == SIDE_B_1ST.index) return SIDE_B_1ST;
        else if (i == SIDE_A_2ND.index) return SIDE_A_2ND;
        else return SIDE_B_2ND;
    }
}
