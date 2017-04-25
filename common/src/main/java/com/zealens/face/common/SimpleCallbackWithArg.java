package com.zealens.face.common;

/**
 * Created by KyleCe on 2016/8/17.
 *
 * @author: KyleCe
 */

public interface SimpleCallbackWithArg {
    void onRefresh(Object obj);

    SimpleCallbackWithArg NULL = obj -> {
    };
}
