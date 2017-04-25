package com.zealens.face.base;

import org.intellij.lang.annotations.MagicConstant;

/**
 * Created on 2017/3/15
 * in BlaBla by Kyle
 */

public class Rule {

    @MagicConstant(intValues = {MatchMode.TIE_BREAK_T6, MatchMode.TTE_BREAK_T4, MatchMode.TIE_BREAK,})
    public @interface MatchMode {
        int TIE_BREAK_T6 = 0;
        int TTE_BREAK_T4 = 1;
        int TIE_BREAK = 2;
    }

    @MagicConstant(intValues = {ScoreType.BOUT, ScoreType.GAME, ScoreType.SET,})
    public @interface ScoreType {
        int BOUT = 0;
        int GAME = 1;
        int SET = 2;
    }

    @MagicConstant(intValues = {ScoreCallbackTag.DUMMY, ScoreCallbackTag.NEXT_BOUT, ScoreCallbackTag.NEXT_GAME
            , ScoreCallbackTag.SET, ScoreCallbackTag.END_GAME,})
    public @interface ScoreCallbackTag {
        int DUMMY = 0;
        int NEXT_BOUT = 1;
        int NEXT_GAME = 2;
        int SET = 3;
        int END_GAME = 4;
    }

    /**
     * distinguish team, cannot use side a/b ,players may exchange side
     */
    @MagicConstant(intValues = {Team.TAN, Team.RED})
    public @interface Team {
        int TAN = 0;
        int RED = 1;
    }

    @MagicConstant(intValues = {Side.A, Side.B})
    public @interface Side {
        int A = 0;
        int B = 1;
    }

    @MagicConstant(stringValues = {SideStr.A, SideStr.B})
    public @interface SideStr {
        String A = "A";
        String B = "B";
    }

    @MagicConstant(intValues = {VideoTag.ACE, VideoTag.WINNER, VideoTag.ABOVE_20_HITS
            , VideoTag.PAN_SPECIAL, VideoTag.GENERAL, VideoTag.MASK})
    public @interface VideoTag {
        int ACE = 1;
        int WINNER = 1 << 1;
        int ABOVE_20_HITS = 1 << 2;
        int GENERAL = 1 << 3;
        int PAN_SPECIAL = 0b111;

        int MASK = 0xFF;
    }

    public static boolean isTagSpecial(@VideoTag int tag) {
        return (tag & VideoTag.PAN_SPECIAL) != 0;
    }

    @MagicConstant(intValues = {VideoDivider.GENERAL, VideoDivider.JEWEL, VideoDivider.GAME})
    public @interface VideoDivider {
        int GENERAL = 0;
        int JEWEL = 1;
        int GAME = 2;
    }

    @MagicConstant(stringValues = {Event.SERVE, Event.SERVE_OUT, Event.SERVE_IN
            , Event.SPEED, Event.START_TIME, Event.LASTING_TIME})
    public @interface Event {
        String SERVE = "serve";
        String SERVE_OUT = "serve_out";
        String SERVE_IN = "serve_in";
        String SPEED = "speed";
        String START_TIME = "start_time";
        String LASTING_TIME = "lasting_time";
    }

    @MagicConstant(stringValues = {MatchEvent.SERVE, MatchEvent.SERVE_OUT, MatchEvent.SERVE_IN
            , MatchEvent.ACE, MatchEvent.SPEED, MatchEvent.SCORE, MatchEvent.START_TIME, MatchEvent.LASTING_TIME})
    public @interface MatchEvent {
        String SERVE = Event.SERVE;
        String SERVE_OUT = Event.SERVE_OUT;
        String SERVE_IN = Event.SERVE_IN;
        String SPEED = Event.SPEED;
        String START_TIME = Event.START_TIME;
        String LASTING_TIME = Event.LASTING_TIME;

        String SCORE = "score";
        String ACE = "ace";
    }

    @MagicConstant(stringValues = {TrainEvent.SERVE, TrainEvent.SERVE_OUT, TrainEvent.SERVE_IN
            , TrainEvent.SPEED, TrainEvent.START_TIME, TrainEvent.LASTING_TIME, TrainEvent.SUCCESS_TIMES})
    public @interface TrainEvent {
        String SERVE = Event.SERVE;
        String SERVE_OUT = Event.SERVE_OUT;
        String SERVE_IN = Event.SERVE_IN;
        String SPEED = Event.SPEED;
        String START_TIME = Event.START_TIME;
        String LASTING_TIME = Event.LASTING_TIME;
        String SUCCESS_TIMES = "success_times";
    }

    @MagicConstant(stringValues = {Event.SERVE, Event.SERVE_OUT, Event.SERVE_IN, Event.SPEED
            , Event.START_TIME, Event.LASTING_TIME
            , MatchEvent.SERVE, MatchEvent.SERVE_OUT, MatchEvent.SERVE_IN
            , MatchEvent.ACE, MatchEvent.SPEED, MatchEvent.SCORE, MatchEvent.START_TIME
            , MatchEvent.LASTING_TIME, TrainEvent.SERVE, TrainEvent.SERVE_OUT, TrainEvent.SERVE_IN
            , TrainEvent.SPEED, TrainEvent.START_TIME, TrainEvent.LASTING_TIME, TrainEvent.SUCCESS_TIMES,
    })
    public @interface VideoEvent {
    }

    @MagicConstant(intValues = {ChannelMode.SINGLE_MATCH, ChannelMode.DOUBLE_MATCH, ChannelMode.TRAINING, ChannelMode.IGNORE})
    public @interface ChannelMode {
        int SINGLE_MATCH = 0;
        int DOUBLE_MATCH = 1;
        int TRAINING = 2;
        int IGNORE = 3;
    }

    public static boolean isMatchChannel(@ChannelMode int mode) {
        return mode == ChannelMode.SINGLE_MATCH || mode == ChannelMode.DOUBLE_MATCH;
    }

    @MagicConstant(intValues = {Level.EASY, Level.MEDIUM, Level.HARD})
    public @interface Level {
        int EASY = 0;
        int MEDIUM = 1;
        int HARD = 2;
    }

    @MagicConstant(intValues = {BreakMode.FOUR_MINUTES, BreakMode.TWENTY_HITS, BreakMode.FREE_STYLE})
    public @interface BreakMode {
        int FOUR_MINUTES = 0;
        int TWENTY_HITS = 1;
        int FREE_STYLE = 2;
    }

    @MagicConstant(intValues = {TrainParamsIndex.ITEM, TrainParamsIndex.LEVEL, TrainParamsIndex.MODE})
    public @interface TrainParamsIndex {
        int ITEM = 0;
        int LEVEL = 1;
        int MODE = 2;
    }

    @MagicConstant(intValues = {TrainState.NORMAL, TrainState.IN, TrainState.OUT})
    public @interface TrainState {
        int NORMAL = 0;
        int IN = 1;
        int OUT = 2;
    }

    @MagicConstant(intValues = {
            TrainMode.SERVICE,
            TrainMode.ALTERNATE_SERVICE,
            TrainMode.FOREHAND,
            TrainMode.FOREHAND_DOWN_THE_LINE,
            TrainMode.FOREHAND_CROSS_COURT,
            TrainMode.BACKHAND,
            TrainMode.BACKHAND_DOWN_THE_LINE,
            TrainMode.BACKHAND_CROSS_COURT,
            TrainMode.VOLLEY,
            TrainMode.FREE_STYLE,
            TrainMode.MULTIPLE_BALLS,
            TrainMode.UNSET,
    })
    public @interface TrainMode {
        int SERVICE = 0;
        int ALTERNATE_SERVICE = 1;
        int FOREHAND = 2;
        int FOREHAND_DOWN_THE_LINE = 3;
        int FOREHAND_CROSS_COURT = 4;
        int BACKHAND = 5;
        int BACKHAND_DOWN_THE_LINE = 6;
        int BACKHAND_CROSS_COURT = 7;
        int VOLLEY = 8;
        int FREE_STYLE = 9;
        int MULTIPLE_BALLS = 10;
        int UNSET = 11;
    }

    /**
     * named by item index
     */
    private static final int[] RULE259 = {TrainMode.FOREHAND, TrainMode.BACKHAND, TrainMode.FREE_STYLE,};
    private static final int[] RULE369 = {TrainMode.FOREHAND_DOWN_THE_LINE, TrainMode.BACKHAND_DOWN_THE_LINE, TrainMode.FREE_STYLE,};
    private static final int[] RULE479 = {TrainMode.FOREHAND_CROSS_COURT, TrainMode.BACKHAND_CROSS_COURT, TrainMode.FREE_STYLE,};

    public static final int[][] TRAIN_MODE_ITEM_ALTERNATIVE_RULER = {
            {},/*service*/
            {},/*alternate_service*/
            RULE259,/*forehand*/
            RULE369,/*forehand_down_the_line*/
            RULE479,/*forehand_cross_court*/
            RULE259,/*backhand*/
            RULE369,/*backhand_down_the_line*/
            RULE479,/*backhand_cross_court*/
            {},/*volley*/
            {TrainMode.FOREHAND, TrainMode.FOREHAND_DOWN_THE_LINE, TrainMode.FOREHAND_CROSS_COURT, TrainMode.BACKHAND, TrainMode.BACKHAND_DOWN_THE_LINE, TrainMode.BACKHAND_CROSS_COURT},/*free_style*/
            {},/*multiple_balls*/
    };
}
