package com.zealens.face.common;

/**
 * Created by KyleCe on 2016/8/17.
 *
 * @author: KyleCe
 */

public interface SimpleCallback {
    void onRefresh();

    SimpleCallback NULL = () -> {
    };
}
