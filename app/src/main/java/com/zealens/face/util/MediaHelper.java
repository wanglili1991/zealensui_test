package com.zealens.face.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
 * Created by Kyle on 01/04/2017
 */

public class MediaHelper {
    public static void playAudioUnderAssets(Context ctx, MediaPlayer player, String fileName) {
        try {
            if (player.isPlaying()) {
                player.stop();
                player.release();
                player = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = ctx.getAssets().openFd(fileName);
            player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset()
                    , descriptor.getLength());
            descriptor.close();

            player.prepare();
            player.setVolume(1f, 1f);
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
