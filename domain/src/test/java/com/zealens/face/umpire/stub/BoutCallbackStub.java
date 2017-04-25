package com.zealens.face.umpire.stub;

import com.zealens.face.core.internal.BoutCallback;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.util.LogcatUtil;

/**
 * Created by elex on 30/03/2017.
 */

public class BoutCallbackStub implements BoutCallback {
    public static final String TAG = BoutCallbackStub.class.getSimpleName();
    private final boolean DUMMY = true;

    @Override
    public void onBall(TennisBase.Ball ball) {
        if (!DUMMY) LogcatUtil.sop(TAG, "onBall ball:" + ball.toString());
    }

    @Override
    public void onBallAce(@TennisBase.Player int playerServe) {
        if (!DUMMY) LogcatUtil.sop(TAG, "onBallAce playerServe:" + playerServe);
    }

    @Override
    public void onServe(TennisBase.Ball ballServe) {
        if (!DUMMY) LogcatUtil.sop(TAG, "onServe ballServe:" + ballServe.toString());
    }

    @Override
    public void onServeFailed() {
        if (!DUMMY) LogcatUtil.sop(TAG, "onServeFailed");
    }

    @Override
    public void onServeSuccessful() {
        if (!DUMMY) LogcatUtil.sop(TAG, "onServeSuccessful");
    }

    @Override
    public void onServeTouchDownPosition(@TennisBase.Player int player
            , @TennisBase.PlayerServe int serveType, TennisBase.Ball ballServeTouchDown) {
        if (!DUMMY)
            LogcatUtil.sop(TAG, "onServeTouchDownPosition player:" + player + " serveType:"
                    + serveType + " ballServeTouchDown:" + ballServeTouchDown);
    }

    @Override
    public void onHitPosition(@TennisBase.Player int playerHit, TennisBase.Ball ballHit) {
        if (!DUMMY)
            LogcatUtil.sop(TAG, "onHitPosition playerHit:" + playerHit + " ballHit:" + ballHit);
    }

    @Override
    public void onAddScore(@TennisBase.Player int player, TennisBase.MatchBoxScore boxScore1
            , TennisBase.MatchBoxScore boxScore2, TennisBase.Ball ball) {
        if (!DUMMY)
            LogcatUtil.sop(TAG, "onAddScore player:" + player + " boxScore1:"
                    + boxScore1 + " boxScore2:" + boxScore2 + " ball:" + ball);
    }

    @Override
    public void onError(int code, String errorMessage) {
        if (!DUMMY) LogcatUtil.sop(TAG, "onError code:" + code + " errorMessage:" + errorMessage);
    }
}
