package com.zealens.face.domain.umpire;

import com.zealens.face.base.Rule;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.domain.module.Score;

/**
 * Created on 2017/3/25
 * in BlaBla by Kyle
 */

public interface UmpireCallback {
    void onGameStart();

    void onGameEnd();

    void onServe(@Rule.Side int side);

    void onAce(@TennisBase.Player int playerServe);

    void onServeFailed();

    void onServeSuccessful();

    void onServeOut();

    void onScoreChange(Score score);

    void onBallSpeedUpdate(@Rule.Side int side, int speed);

    void onNextBout();

    void onNextGame();

    void onLet();
}
