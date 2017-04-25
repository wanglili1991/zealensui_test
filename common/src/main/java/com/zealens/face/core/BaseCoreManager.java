package com.zealens.face.core;

import com.zealens.face.core.log.CoreLogger;
import com.zealens.face.core.log.Logger;

import org.intellij.lang.annotations.MagicConstant;

public abstract class BaseCoreManager extends CoreObjectImpl implements Comparable, CoreObject, CoreLogger {
    @MagicConstant(intValues = {ORDER.PREFERENCES, ORDER.ANALYTICS, ORDER.NETWORK, ORDER.USER_CACHE, ORDER.UMPIRE,})
    public @interface ORDER {
        int PREFERENCES = 0;
        int ANALYTICS = 1;
        int NETWORK = 2;
        int USER_CACHE = 3;
        int UMPIRE = 4;
    }

    protected final String TAG;

    public BaseCoreManager() {
        TAG = getClass().getSimpleName();
    }

    @Override
    public void v(String msg) {
        Logger.v(TAG, msg);
    }

    @Override
    public void i(String msg) {
        Logger.i(TAG, msg);
    }

    @Override
    public void d(String msg) {
        Logger.d(TAG, msg);
    }

    @Override
    public void w(String msg) {
        Logger.w(TAG, msg);
    }

    @Override
    public void e(String msg) {
        Logger.e(TAG, msg);
    }

    @Override
    public int compareTo(final Object o) {
        final int thisOrder = order();
        final int otherOrder = ((BaseCoreManager) o).order();
        if (thisOrder < otherOrder)
            return -1;
        if (thisOrder > otherOrder)
            return 1;
        return 0;
    }

    @ORDER
    public abstract int order();

    public abstract void freeMemory();
}
