package com.zealens.face.base.scoreboard.match;

import com.zealens.face.domain.umpire.CorrectComponents;
import com.zealens.face.presenter.BasePresenter;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public interface MatchScoreBoardPresenter extends BasePresenter {
    void onResume();

    void onPause();

    void onStop();

    CorrectComponents getCorrectComponents();
}
