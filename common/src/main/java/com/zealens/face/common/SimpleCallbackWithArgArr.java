package com.zealens.face.common;

/**
 * Created by KyleCe on 2016/8/17.
 *
 * @author: KyleCe
 */

public interface SimpleCallbackWithArgArr extends SimpleCallbackWithArg {
    void onRefresh(Object... objects);

    SimpleCallbackWithArgArr NULL = new SimpleCallbackWithArgArr() {
        @Override
        public void onRefresh(Object obj) {

        }

        @Override
        public void onRefresh(Object... objects) {

        }
    };
}
