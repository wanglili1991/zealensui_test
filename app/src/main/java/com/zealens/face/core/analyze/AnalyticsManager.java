package com.zealens.face.core.analyze;

import com.zealens.face.BuildConfig;
import com.zealens.face.core.CoreContext;
import com.zealens.face.core.CoreManager;

/**
 * Created on 2016/12/16
 * in BlaBla by Kyle
 */

public class AnalyticsManager extends CoreManager implements AnalyticsInterface {
    public AnalyticsManager(CoreContext coreContext) {
        super(coreContext);
    }

    @Override
    public int order() {
        return ORDER.ANALYTICS;
    }

    @Override
    public void freeMemory() {

    }

    @Override
    public void logEvent(String msg) {
    }

    @Override
    public void logException(final String msg, final Exception e) {
        if (BuildConfig.PUBLISH_FLAG) {
        }
    }

    @Override
    public void logException(final Exception e) {
    }
}
