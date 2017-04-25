package com.zealens.face.video.presenter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zealens.face.R;
import com.zealens.face.base.Rule;
import com.zealens.face.core.CoreContext;
import com.zealens.face.domain.module.Video;
import com.zealens.face.domain.umpire.UmpireOperator;
import com.zealens.face.presenter.BasePresenterImpl;
import com.zealens.face.tennis.UmpireManagerAppLevel;
import com.zealens.face.util.CompatUtil;
import com.zealens.face.video.player.VideoCallback;
import com.zealens.face.video.player.VideoPlayer;

import java.io.File;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class VideoPlayerImpl extends BasePresenterImpl implements VideoPlayerPresenter, VideoCallback {
    private CoreContext mCoreContext;
    private ViewGroup mRootVG;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private View mVideoPlayLayout;
    private VideoPlayer mVideoPlayer;
    private Video mVideo;
    private UmpireOperator mUmpireOperator;

    public VideoPlayerImpl(CoreContext coreContext, ViewGroup rootVG, LayoutInflater layoutInflater
            , VideoPlayer player) {
        mCoreContext = coreContext;
        mContext = mCoreContext.getApplicationContext();
        mRootVG = rootVG;
        mLayoutInflater = layoutInflater;
        if (player != null) mVideoPlayer = player;
    }

    @Override
    public void initialize() {
        super.initialize();
        mUmpireOperator = (UmpireOperator) mCoreContext.getApplicationService(UmpireManagerAppLevel.class);
        refreshWithFirstItem(Rule.Team.TAN);
    }

    @Override
    public void refreshWithFirstItem(@Rule.Team int team) {
        Video[] videos = mUmpireOperator.getVideoOfGeneral(team);
        mVideo = videos[0];
        playVideo();
    }

    @Override
    public void playVideo() {
        if (mVideo == null || TextUtils.isEmpty(mVideo.path)) return;
        File file = new File(mVideo.path);
        if (!file.exists()) {
            Toast.makeText(mContext, CompatUtil.getString(mContext, R.string.file_no_exist), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mVideoPlayer == null) {
            mVideoPlayLayout = mLayoutInflater.inflate(R.layout.video_play_layout, null, false);
            mVideoPlayer = (VideoPlayer) mVideoPlayLayout.findViewById(R.id.player);
            mRootVG.addView(mVideoPlayLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mVideoPlayLayout.findViewById(R.id.parent).setOnClickListener((v) -> cancelPlay());
        }

        mVideoPlayer.setCallback(this);
        Uri uri = Uri.fromFile(file);
        mVideoPlayer.setSource(uri);
    }

    @Override
    public void pausePlay() {
        if (mVideoPlayer != null)
            mVideoPlayer.pause();
    }

    @Override
    public void cancelPlay() {
        if (mVideoPlayLayout != null)
            mRootVG.removeView(mVideoPlayLayout);
    }


    @Override
    public void dispose() {
        super.dispose();
        cancelPlay();
        mVideoPlayLayout = null;
        mLayoutInflater = null;
        mVideoPlayer = null;
    }

    @Override
    public void onStarted(VideoPlayer player) {

    }

    @Override
    public void onPaused(VideoPlayer player) {

    }

    @Override
    public void onPreparing(VideoPlayer player) {

    }

    @Override
    public void onPrepared(VideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(VideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(VideoPlayer player) {

    }

    @Override
    public void onRetry(VideoPlayer player, Uri source) {

    }

    @Override
    public void onSlower(VideoPlayer player, Uri source) {

    }

    @Override
    public void onFullScreen(VideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(VideoPlayer player, Uri source) {

    }

    @Override
    public void onClickVideoFrame(VideoPlayer player) {

    }

    @Override
    public void onRefresh(Object obj) {
        if (!(obj instanceof Video)) return;
        mVideo = (Video) obj;
        playVideo();
    }
}
