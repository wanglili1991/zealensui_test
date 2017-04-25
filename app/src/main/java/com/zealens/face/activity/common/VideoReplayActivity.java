package com.zealens.face.activity.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zealens.face.BuildConfig;
import com.zealens.face.R;
import com.zealens.face.activity.base.ChannelModeBaseActivity;
import com.zealens.face.base.scorevideopanel.ControlPanelPresenterImpl;
import com.zealens.face.base.scorevideopanel.VideoPanelPresenter;
import com.zealens.face.video.player.VideoPlayer;
import com.zealens.face.video.presenter.VideoPlayerImpl;
import com.zealens.face.video.presenter.VideoPlayerPresenter;

public class VideoReplayActivity extends ChannelModeBaseActivity {
    private ViewGroup mParent;
    private VideoPlayerPresenter mVideoPlayerPresenter;
    private VideoPlayer mVideoPlayer;
    private VideoPanelPresenter mControlPanelPresenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_video_replay;
    }

    @Override
    protected void viewAffairs() {
        mParent = (ViewGroup) findViewById(R.id.parent);
        mVideoPlayer = (VideoPlayer) findViewById(R.id.player);
    }

    @Override
    protected void assembleDataAfterUiAffairs() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mVideoPlayerPresenter = new VideoPlayerImpl(getCoreContext(), mParent, inflater, mVideoPlayer);
        mVideoPlayerPresenter.initialize();
        if (BuildConfig.DEBUG) replayClicked();
        mControlPanelPresenter = new ControlPanelPresenterImpl(getCoreContext(), mActivityContent
                , mVideoPlayerPresenter, mChannelMode);
        mControlPanelPresenter.initialize();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mVideoPlayerPresenter.cancelPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlayerPresenter.pausePlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoPlayerPresenter.dispose();
        mControlPanelPresenter.dispose();
    }

    private void replayClicked() {
        mVideoPlayerPresenter.playVideo();
    }
}
