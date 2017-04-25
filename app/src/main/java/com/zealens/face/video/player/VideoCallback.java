package com.zealens.face.video.player;

import android.net.Uri;

/**
 * @author Aidan Follestad (afollestad)
 */
public interface VideoCallback {

    void onStarted(VideoPlayer player);

    void onPaused(VideoPlayer player);

    void onPreparing(VideoPlayer player);

    void onPrepared(VideoPlayer player);

    void onBuffering(int percent);

    void onError(VideoPlayer player, Exception e);

    void onCompletion(VideoPlayer player);

    void onRetry(VideoPlayer player, Uri source);

    void onSlower(VideoPlayer player, Uri source);

    void onFullScreen(VideoPlayer player, Uri source);

    void onSubmit(VideoPlayer player, Uri source);

    void onClickVideoFrame(VideoPlayer player);
}