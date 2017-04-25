package com.zealens.face.domain.umpire;

import com.zealens.face.base.Rule;
import com.zealens.face.core.internal.DrillBase;
import com.zealens.face.core.internal.TennisBase;

import org.jetbrains.annotations.NonNls;

/**
 * Created on 2017/3/25
 * in BlaBla by Kyle
 */

public interface UmpireOperator extends FunctionToolBox {
    boolean start();

    boolean pause();

    boolean resume();

    boolean stop();

    boolean restartOneMatchSet(@TennisBase.CourtArea int serveArea);

    void reset();

    void setMatchMode(@Rule.MatchMode int mode);

    void assignUmpireCallback(@NonNls UmpireCallback callback);

    void assignDrillBaseCallback(DrillBase base);

    void exchangeSide();

    void assignServePower(@TennisBase.BallPower int power);

    void exchangeServePower();

    void assignComponentsByPlayModel(@TennisBase.PlayModel int model);

    void assignComponentsByPlayModel(@TennisBase.PlayModel int model
            , @TennisBase.DrillDifficulty int difficulty);

    void assignComponentsByPlayModel(@TennisBase.PlayModel int model
            , @TennisBase.DrillDifficulty int difficulty, @Rule.BreakMode int breakMode);
}
