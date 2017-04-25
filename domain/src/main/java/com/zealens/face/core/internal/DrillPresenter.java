package com.zealens.face.core.internal;

/**
 * Created by Kyle on 12/04/2017
 */

public interface DrillPresenter extends BasePresenterOfTennis {

    boolean init(TennisBase.InitParam initParam, DrillCallback drillCallback
            , @TennisBase.DrillDifficulty int drillDifficulty);
}
