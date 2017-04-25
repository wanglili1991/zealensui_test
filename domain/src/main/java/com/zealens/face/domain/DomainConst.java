package com.zealens.face.domain;

import java.io.File;

/**
 * Created on 2017/3/22
 * in BlaBla by Kyle
 */

public class DomainConst {
    /**
     * @see com.zealens.face.base.Rule.Side for index
     */
    public static final String[] IP_CAMERA_IP = {"192.18.1.1", "192.18.1.2"};
    public static final short IP_CAMERA_DEFAULT_PORT = 80;
    public static final String IP_CAMERA_DEFAULT_USERNAME = "admin";
    public static final String IP_CAMERA_DEFAULT_PASSWORD = "admin";

    public static final String MATCH_EVENT_CONCAT = "-";
    public static final String SCORE_SPLIT = ";";
    public static final String SCORE_DIVIDER = "-";
    public static final String SCORE_DIVIDER_2_DISPLAY = ":";
    public static final String SCORE_TAG_BOUT = "bout=";
    public static final String SCORE_TAG_GAME = "game=";
    public static final String SCORE_TAG_SET = "set=";

    public static final String VIDEO_TAG = "tag==";
    public static final String SCORE_TYPE_VIDEO_START = "start==";
    public static final String SCORE_TYPE_VIDEO_END = "end==";
    public static final String VIDEO_LASTING_TIME = "lasting==";
    public static final String VIDEO_FILE_EXTENSIONS = ".mp4";
    public static final String VIDEO_TRACK_FILE_EXTENSIONS = ".txt";
    public static final String VIDEO_FILE_EXTENSIONS_REAL = "mp4";
    public static final String VIDEO_TRACK_FILE_EXTENSIONS_REAL = "txt";

    public static final String X_NIX_VIDEO_PATH = "/tmp/video";
    public static final String FILE_SEPARATOR = File.separator;

    public static boolean DEBUG_UMPIRE = true;
}
