package com.zealens.face.core.internal;

/**
 * one set control engine interface
 */
public interface MatchPresenter extends BasePresenterOfTennis {
    /**
     * init the internal hardware
     * @return true for success
     */
    boolean init(TennisBase.InitParam initParam, BoutCallback matchCallback);

    /**
     * set ball power
     *
     * @param ballPower
     * @return true for success
     */
    boolean ballPower(@TennisBase.BallPower int ballPower);
}
