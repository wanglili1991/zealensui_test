package com.zealens.face.base.scoreboard.train;

import com.zealens.face.presenter.BasePresenter;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public interface TrainScoreBoardPresenter extends BasePresenter {
    void onResume();

    void onPause();

    void onStop();
}
