package com.zealens.face.core.analyze;

/**
 * Created by yuflh on 2016/11/24.
 */

public interface AnalyticsInterface {
    void logEvent(final String msg);

    void logException(final String msg, final Exception e);

    void logException(final Exception e);
}
