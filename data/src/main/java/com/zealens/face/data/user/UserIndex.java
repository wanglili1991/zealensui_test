package com.zealens.face.data.user;

/**
 * Created on 2017/3/18
 * in BlaBla by Kyle
 */

public enum UserIndex {
    USER_0(0), USER_1(1), USER_2(2), USER_3(3), USER_4(4), USER_5(5),;
    public int index;

    UserIndex(int i) {
        index = i;
    }

    public static UserIndex find(int i) {
        if (i == USER_0.index) return USER_0;
        else if (i == USER_1.index) return USER_1;
        else if (i == USER_2.index) return USER_2;
        else if (i == USER_3.index) return USER_3;
        else if (i == USER_4.index) return USER_4;
        else return USER_5;
    }
}
