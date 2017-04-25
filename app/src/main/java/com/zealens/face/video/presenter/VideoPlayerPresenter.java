package com.zealens.face.video.presenter;

import com.zealens.face.base.Rule;
import com.zealens.face.common.SimpleCallbackWithArg;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public interface VideoPlayerPresenter extends SimpleCallbackWithArg {

    void initialize();

    boolean isInitialized();

    void dispose();

    void refreshWithFirstItem(@Rule.Team int team);

    void playVideo();

    void pausePlay();

    void cancelPlay();
}
