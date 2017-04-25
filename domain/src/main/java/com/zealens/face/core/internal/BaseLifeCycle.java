package com.zealens.face.core.internal;

/**
 * Created by Kyle on 12/04/2017
 */

public interface BaseLifeCycle {
    boolean start();

    boolean pause();

    boolean resume();

    boolean stop();

    void dispose();
}
