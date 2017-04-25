package com.zealens.face.common;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.zealens.face.R;

/**
 * Created on 2017/3/22
 * in BlaBla by Kyle
 */

public class ResourceConst {
    public static final int[] PLAYER_IDS = {R.id.player_side_a_one, R.id.player_side_b_one
            , R.id.player_side_a_two, R.id.player_side_b_two};

    public static final int IGNORE_FLAG = -1;

    public static final String CONFIG_DIR = "config";
    public static final String TEMP_DIR = "tmp";
    public static final String CONFIG_FILE_NAME = "config.cfg";
    public static final String CONFIG_PARSING_FILE_LINE_TAG = "event_id = ";

    public static final int SERVE_STATE_LIMIT = 4;
    public static final int BOUT_SCORE_LIMIT = 4;

    public static final String AUDIO_DIR = "audio";

    // audio
    @StringDef({Audio.ACE, Audio.IN, Audio.NET, Audio.NEXT, Audio.OUT,})
    public @interface Audio {
        String ACE = "ace.mp3";
        String IN = "in.mp3";
        String NET = "net.mp3";
        String NEXT = "next.mp3";
        String OUT = "out.mp3";
    }

    //--**-BLOCK_DIVIDER-**-----------------------bout score--------------------------//
    public static final String[] BOUT_SCORE = {"0", "15", "30", "40", "Ad",};

    @IntDef({BoutScoreIndex._1st, BoutScoreIndex._2nd, BoutScoreIndex._3rd
            , BoutScoreIndex._4th, BoutScoreIndex._ad,})
    public @interface BoutScoreIndex {
        int _1st = 0;
        int _2nd = 1;
        int _3rd = 2;
        int _4th = 3;
        int _ad = 4;
    }
}
