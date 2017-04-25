package com.zealens.face.core.internal;

/**
 * Created by Kyle on 12/04/2017
 */

public interface BasePresenterOfTennis extends BaseLifeCycle {

    /**
     * test internal data
     *
     * @return true for success
     */
    boolean runOnce();

    /**
     * restart another set, avoiding the re-initialize operation
     *
     * @return true for success
     */
    boolean restart(@TennisBase.CourtArea int serveAre);

    /**
     * start all cameras for capture videos, analyze video
     *
     * @return true for success
     */
    @Override
    boolean start();
}
