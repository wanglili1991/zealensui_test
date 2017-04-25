package com.zealens.face.umpire.stub;

import com.zealens.face.core.internal.BoutCallback;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.core.internal.MatchPresenter;

import org.jetbrains.annotations.NonNls;

/**
 * Created on 2017/3/26
 * in BlaBla by Kyle
 */

public class TennisBoutPresenterStub implements MatchPresenter {
    private BoutCallback mBoutCallback;
    public static final int MAX_SPEED = 898;

    public void assignWinner(@NonNls boolean[] arr) {
        for (boolean b : arr)
            appendBout(b);
    }

    public void appendBout(boolean[] arr) {
        for (boolean b : arr)
            appendBout(b);
    }

    public void appendBout(boolean playerAWon) {
        BoxScoreStub.Builder scoreStub = new BoxScoreStub.Builder().withArea11stInsideNum(1)
                .withSpeedMax(MAX_SPEED)
                .withDoubleFaultsNum(1);
        mBoutCallback.onBallAce(TennisBase.Player.PLAYER_A);
        mBoutCallback.onServeFailed();
        mBoutCallback.onServeSuccessful();
        mBoutCallback.onAddScore(playerAWon ? TennisBase.Player.PLAYER_A : TennisBase.Player.PLAYER_B
                , scoreStub.withAce(playerAWon ? 1 : 0).build(), scoreStub.withAce(playerAWon ? 0 : 1).build(), new TennisBase.Ball());
    }

    public void appendReceivePoints(int count) {
        BallStub ballStub = new BallStub(1, 2, -1);
        for (int i = 0; i < count; i++) {
            ballStub.x = i;
            ballStub.y = i;
            mBoutCallback.onHitPosition(TennisBase.Player.PLAYER_A, ballStub);
            mBoutCallback.onServeTouchDownPosition(TennisBase.Player.PLAYER_A, TennisBase.PlayerServe.FIRST, ballStub);
        }
    }

    @Override
    public boolean init(TennisBase.InitParam initParam, BoutCallback matchCallback) {
        mBoutCallback = matchCallback;
        return true;
    }

    @Override
    public boolean ballPower(@TennisBase.BallPower int ballPower) {
        return false;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean pause() {
        return false;
    }

    @Override
    public boolean resume() {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean runOnce() {
        return false;
    }

    @Override
    public boolean restart(@TennisBase.CourtArea int serveAre) {
        return false;
    }
}