package com.zealens.face.domain.umpire;

import com.zealens.face.base.Rule;
import com.zealens.face.core.BaseCoreManager;
import com.zealens.face.core.internal.BasePresenterOfTennis;
import com.zealens.face.core.internal.BoutCallback;
import com.zealens.face.core.internal.DrillBase;
import com.zealens.face.core.internal.DrillCallback;
import com.zealens.face.core.internal.DrillPresenter;
import com.zealens.face.core.internal.IPCameraPresenter;
import com.zealens.face.core.internal.MatchPresenter;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.domain.DomainConst;
import com.zealens.face.domain.module.Point2D;
import com.zealens.face.domain.module.Point3D;
import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.module.Video;
import com.zealens.face.util.ApiFromAndroid;
import com.zealens.face.util.CollectionUtil;
import com.zealens.face.util.LogcatUtil;

import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2017/3/25
 * in BlaBla by Kyle
 */

public class UmpireManager extends BaseCoreManager implements UmpireOperator, BoutCallback
        , DrillCallback, ScoreParseCallback {
    @Rule.MatchMode
    private int mMatchMode = Rule.MatchMode.TIE_BREAK_T6;
    private Score mLastHistoryScore;
    private Score mScore;
    private UmpireCallback mUmpireCallback;

    @TennisBase.Channel
    private int mChannel;
    private MatchPresenter mMatchPresenter;
    private DrillPresenter mDrillPresenter;
    private BasePresenterOfTennis mTennisPresenter;
    private TennisBase.InitParam mInitParam;
    private DrillBase mDrillBase;

    /**
     * @see com.zealens.face.base.Rule.Side for index
     */
    private IPCameraPresenter[] mIPCameras;
    private boolean[] mIPCameraInitOk;
    private String[] mRecordingVideoPath;
    private boolean[] mVideoRecordStartOk;
    private volatile boolean mNoteFileProcessed;
    private String mRecordingTimeStamp;
    /**
     * use time stamp(current time in milli) for index
     *
     * @see com.zealens.face.base.Rule.MatchEvent for content prefix
     */
    private ConcurrentHashMap<String, String> mRecordingMatchEventMap;
    /**
     * @see com.zealens.face.base.Rule.Team for index
     */
    private TennisBase.MatchBoxScore[] mBoxScores;
    private TennisBase.DrillBoxScore[] mDrillBoxScore;
    private Point2D[][] mReceiveHits;// dummy z value for memory saving consideration
    /**
     * @see com.zealens.face.core.internal.TennisBase.PlayerServe hold the coordinate z to store serve type:
     * @see com.zealens.face.base.Rule.Team for first index
     */
    private Point3D[][] mServeFallPoints;
    private int mStoredReceiveCount = 0;
    private int mReceiveExpandedCount = 0;
    private int mServeFallCount = 0;
    private int mServeFallExpandedCount = 0;
    private final int POINTS_CONTAINER_DEFAULT_CAPACITY = 1000;
    private boolean mReverseSide;
    private long mWholeGameStartTime;
    @Rule.BreakMode
    private int mBreakMode;
    private final long TRAIN_TIME_LIMIT_IN_MILLI = DomainConst.DEBUG_UMPIRE ? 3 * 1000 : 4 * 60 * 1000;// 3 seconds / 4 minutes
    private int mDrillCount;
    private static final int DRILL_COUNT_LIMIT = 20;
    private boolean mShouldEndGame;
    private boolean mStartedBefore;
    protected File mPathPrefix;
    protected Video[] m1stVideos;
    /**
     * @see Rule.Team for index of video list
     */
    private List<List<Video>> mVideos;
    private List<List<Video>> mAceVideos;
    private List<List<Video>> mWinnerVideos;
    private List<List<Video>> m20HitsVideos;
    private List<List<Video>> mGeneralVideos;
    private boolean[] mVideoArranged;
    private final boolean PRINT = false;
    private boolean mAutoReverseDisabled;

    public UmpireManager(IPCameraPresenter[] cameras) {
        mIPCameras = cameras;
    }

    @Override
    public void initialize() {
        super.initialize();

        int len = Rule.Team.class.getFields().length;
        mIPCameraInitOk = new boolean[len];
        UmpireDelegate.createVideoCacheDirectory(mPathPrefix);
        prepareMemoryCache(len);
        mVideos = assignComponents(len);
        mAceVideos = assignComponents(len);
        mWinnerVideos = assignComponents(len);
        m20HitsVideos = assignComponents(len);
        mGeneralVideos = assignComponents(len);
        mVideoArranged = new boolean[len];
    }

    private <T> List<List<T>> assignComponents(int len) {
        List<List<T>> listList = new ArrayList<>(len);
        listList.add(Rule.Team.TAN, new ArrayList<>());
        listList.add(Rule.Team.RED, new ArrayList<>());
        return listList;
    }

    private void prepareMemoryCache(int len) {
        mReceiveHits = new Point2D[len][POINTS_CONTAINER_DEFAULT_CAPACITY];
        mServeFallPoints = new Point3D[len][POINTS_CONTAINER_DEFAULT_CAPACITY];
        mRecordingVideoPath = new String[len];
        mVideoRecordStartOk = new boolean[len];
    }

    @Override
    public void assignComponentsByPlayModel(int model) {
    }

    @Override
    public void assignComponentsByPlayModel(int model, int difficulty) {
    }

    @Override
    public void assignComponentsByPlayModel(int model, int difficulty, int breakMode) {
    }

    public void assignComponent(BasePresenterOfTennis basePresenterOfTennis, TennisBase.InitParam initParam) {
        mInitParam = initParam;
        mTennisPresenter = basePresenterOfTennis;
        if (basePresenterOfTennis instanceof MatchPresenter) {
            mMatchPresenter = (MatchPresenter) basePresenterOfTennis;
            mChannel = TennisBase.Channel.MATCH;
            mMatchPresenter.init(initParam, this);
            prepareData(mChannel);
        } else if (basePresenterOfTennis instanceof DrillPresenter) {
            if (!(initParam instanceof TennisBase.DrillInitParam)) {
                throw new IllegalArgumentException("use DrillInitParam instead");
            }
            TennisBase.DrillInitParam drillInitParam = ((TennisBase.DrillInitParam) initParam);
            mBreakMode = drillInitParam.breakMode;
            mDrillPresenter = (DrillPresenter) basePresenterOfTennis;
            mChannel = TennisBase.Channel.DRILL;
            mDrillPresenter.init(initParam, this, drillInitParam.drillDifficulty);
            prepareData(mChannel);
        } else {
            throw new IllegalArgumentException("Unsupported Presenter");
        }
    }

    private void prepareData(@TennisBase.Channel int channel) {
        if (channel == TennisBase.Channel.MATCH) {
            mBoxScores = new TennisBase.MatchBoxScore[]{new TennisBase.MatchBoxScore()
                    , new TennisBase.MatchBoxScore()};
        } else {
            mDrillBoxScore = new TennisBase.DrillBoxScore[]{new TennisBase.DrillBoxScore()
                    , new TennisBase.DrillBoxScore()};
        }
        mScore = new Score();
        mRecordingMatchEventMap = new ConcurrentHashMap<>();

        if (mIPCameras != null && mIPCameras.length == Rule.Side.class.getFields().length) {
            initIPCameras(Rule.Side.A);
            initIPCameras(Rule.Side.B);
        }
    }

    private void initIPCameras(@Rule.Side int side) {
        mIPCameraInitOk[side] = mIPCameras[side].init(mInitParam.tmpPath, DomainConst.IP_CAMERA_IP[side], DomainConst.IP_CAMERA_DEFAULT_PORT
                , DomainConst.IP_CAMERA_DEFAULT_USERNAME, DomainConst.IP_CAMERA_DEFAULT_PASSWORD);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reset() {
        if (mScore != null) mScore.reset();
        if (mLastHistoryScore != null) mLastHistoryScore.reset();
        mWholeGameStartTime = System.currentTimeMillis();
        mDrillCount = 0;
        mShouldEndGame = false;
        mStoredReceiveCount = 0;
        mReceiveExpandedCount = 0;
        mServeFallCount = 0;
        mServeFallExpandedCount = 0;
        mRecordingMatchEventMap.clear();
        prepareMemoryCache(Rule.Team.class.getFields().length);

        clearVideoCollection(mVideos, mAceVideos, mWinnerVideos, m20HitsVideos, mGeneralVideos);
        mVideoArranged[Rule.Team.TAN] = false;
        mVideoArranged[Rule.Team.RED] = false;
    }

    @SuppressWarnings("unchecked")
    private <T> void clearVideoCollection(List<List<T>>... lists) {
        for (List<List<T>> l : lists)
            for (List<T> c : l)
                c.clear();
    }

    @Override
    public void setMatchMode(@Rule.MatchMode int mode) {
        mMatchMode = mode;
    }

    @Override
    public void assignUmpireCallback(@NonNls UmpireCallback callback) {
        mUmpireCallback = callback;
    }

    @Override
    public void assignDrillBaseCallback(DrillBase base) {
        mDrillBase = base;
    }

    private int appendBoutResult(boolean teamTanWin) {
        mLastHistoryScore = new Score(mScore);
        return UmpireDelegate.parseScore(mMatchMode, mScore, teamTanWin);
    }

    public TennisBase.MatchBoxScore[] getBoxScores() {
        return mBoxScores;
    }

    public TennisBase.DrillBoxScore[] getDrillBoxScore() {
        return mDrillBoxScore;
    }

    //--**-BLOCK_DIVIDER-**-----------------------score parsing--------------------------//
    @Override
    public void shouldStartNextBout() {
        sop(TAG, "shouldStartNextBout");// TODO: 2017/3/27 area +by Kyle 03.27
        if (mUmpireCallback != null) mUmpireCallback.onNextBout();
        startIPCamerasRecordAndResetEventCache();
    }

    @Override
    public void shouldStartNextGame() {
        sop(TAG, "shouldStartNextGame");
        mTennisPresenter.restart(TennisBase.CourtArea.A);
        if (mUmpireCallback != null) mUmpireCallback.onNextGame();
        startIPCamerasRecordAndResetEventCache();
    }

    @Override
    public void shouldEndGame() {
        sop(TAG, "shouldEndGame");
        mTennisPresenter.stop();
        if (mUmpireCallback != null) mUmpireCallback.onGameEnd();
        stopIPCamerasRecord();
    }

    //--**-BLOCK_DIVIDER-**-----------------------bout callback--------------------------//
    @Override
    public void onBall(TennisBase.Ball ball) {
    }

    @Override
    public void onBallAce(@TennisBase.Player int playerServe) {
        sop(TAG, "onBallAce");
        if (mUmpireCallback != null) mUmpireCallback.onAce(playerServe);
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap, Rule.MatchEvent.ACE, playerServe);
    }

    @Override
    public void onServe(TennisBase.Ball ballServe) {
        sop(TAG, "onServe");
        int side = ballServe.x > 0 ? Rule.Side.A : Rule.Side.B;
        if (mUmpireCallback != null) {
            mUmpireCallback.onServe(side);
            mUmpireCallback.onBallSpeedUpdate(side, ballServe.speed);
        }
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap, Rule.MatchEvent.SERVE, side);
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap, Rule.MatchEvent.SPEED, ballServe.speed);
    }

    @Override
    public void onServeFailed() {
        sop(TAG, "onServeFailed");
        if (mUmpireCallback != null) mUmpireCallback.onServeFailed();
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap, Rule.MatchEvent.SERVE_OUT);
    }

    @Override
    public void onServeSuccessful() {
        sop(TAG, "onServeSuccessful");
        if (mUmpireCallback != null) mUmpireCallback.onServeSuccessful();
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap, Rule.MatchEvent.SERVE_IN);
    }

    @Override
    public void onServeTouchDownPosition(@TennisBase.Player int player, @TennisBase.PlayerServe int serveType, TennisBase.Ball ballServeTouchDown) {
        sop(TAG, "onServeTouchDownPosition");
        boolean sideA = player == TennisBase.Player.PLAYER_A;
        int index = sideA ^ mReverseSide ? Rule.Team.TAN : Rule.Team.RED;

        if (mServeFallCount >= POINTS_CONTAINER_DEFAULT_CAPACITY * (mServeFallExpandedCount + 1)) {
            mServeFallPoints[index] = CollectionUtil.assembleCapacityExpandedArray(mServeFallPoints[index], POINTS_CONTAINER_DEFAULT_CAPACITY);
            mServeFallExpandedCount++;
        }

        mServeFallPoints[index][mServeFallCount++] = new Point3D(ballServeTouchDown.x, ballServeTouchDown.y, serveType);
    }

    @Override
    public void onHitPosition(@TennisBase.Player int playerHit, TennisBase.Ball ballHit) {
        sop(TAG, "onHitPosition");
        boolean sideA = playerHit == TennisBase.Player.PLAYER_A;
        int index = sideA ^ mReverseSide ? Rule.Team.TAN : Rule.Team.RED;

        if (mStoredReceiveCount >= POINTS_CONTAINER_DEFAULT_CAPACITY * (mReceiveExpandedCount + 1)) {
            mReceiveHits[index] = CollectionUtil.assembleCapacityExpandedArray(mReceiveHits[index], POINTS_CONTAINER_DEFAULT_CAPACITY);
            mReceiveExpandedCount++;
        }

        mReceiveHits[index][mStoredReceiveCount++] = new Point2D(ballHit.x, ballHit.y);
    }

    private int addScoreCount = 1;
    @Override
    public void onAddScore(@TennisBase.Player int player, TennisBase.MatchBoxScore boxScore1
            , TennisBase.MatchBoxScore boxScore2, TennisBase.Ball ball) {
        sop(TAG, "onAddScore");
        int parseCallbackTag = appendBoutResult(player == (!mReverseSide ? TennisBase.Player.PLAYER_A : TennisBase.Player.PLAYER_B));
        int tagTan = UmpireDelegate.appendBoxScore(mBoxScores[Rule.Team.TAN], !mReverseSide ? boxScore1 : boxScore2, this);
        int tagRed = UmpireDelegate.appendBoxScore(mBoxScores[Rule.Team.RED], !mReverseSide ? boxScore2 : boxScore1, this);
        stopIPCamerasRecord(tagTan, tagRed);
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap, Rule.MatchEvent.SCORE, mScore);
        if (mUmpireCallback != null) {
            mUmpireCallback.onScoreChange(mScore);
        }
        if (!mAutoReverseDisabled) {
            int gameSum = CollectionUtil.accumulateIntegerArray(mScore.game);
            if ((gameSum & 0x1) != 0) mReverseSide = !mReverseSide;
        }
        if (parseCallbackTag == Rule.ScoreCallbackTag.NEXT_BOUT) {
            shouldStartNextBout();
        } else if (parseCallbackTag == Rule.ScoreCallbackTag.NEXT_GAME) {
            shouldStartNextGame();
        } else if (parseCallbackTag == Rule.ScoreCallbackTag.END_GAME) {
            shouldEndGame();
        }
        addScoreCount++;
        sop(TAG, "addScore count", addScoreCount);
    }

    //--**-BLOCK_DIVIDER-**-----------------------Drill callback--------------------------//
    @Override
    public void onBegin() {
        sop(TAG, "onBegin");
        if (mDrillBase != null) mDrillBase.onBegin();
        mWholeGameStartTime = System.currentTimeMillis();
    }

    @Override
    public void onDataChange(TennisBase.DrillBoxScore drillBoxScore) {
        sop(TAG, "onDataChange");
        if (mDrillBase != null) mDrillBase.onDataChange(drillBoxScore);
        if (!mShouldEndGame) mDrillPresenter.restart(TennisBase.CourtArea.A1);
        mDrillBoxScore[Rule.Team.TAN] = drillBoxScore;
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap,
                drillBoxScore.lastResult == TennisBase.DrillResult.IN ? Rule.TrainEvent.SERVE_IN
                        : Rule.TrainEvent.SERVE_OUT);
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap, Rule.TrainEvent.SUCCESS_TIMES,
                drillBoxScore.successfulTimes);
    }

    @Override
    public void onSpeedChange(int speed) {
        sop(TAG, "onSpeedChange ");
        if (mDrillBase != null) mDrillBase.onSpeedChange(speed);
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap, Rule.TrainEvent.SPEED, speed);
    }

    @Override
    public void onEnd() {
        sop(TAG, "onEnd ");
        if (mDrillBase != null) mDrillBase.onEnd();

        int team = Rule.Team.TAN;
        TennisBase.BallPoint[] hitArr = mDrillBoxScore[team].ballPointHit;
        TennisBase.BallPoint[] downArr = mDrillBoxScore[team].ballPointDown;
        for (TennisBase.BallPoint aHit : hitArr) {
            if (mStoredReceiveCount >= POINTS_CONTAINER_DEFAULT_CAPACITY * (mReceiveExpandedCount + 1)) {
                mReceiveHits[team] = CollectionUtil.assembleCapacityExpandedArray(mReceiveHits[team], POINTS_CONTAINER_DEFAULT_CAPACITY);
                mReceiveExpandedCount++;
            }

            mReceiveHits[team][mStoredReceiveCount++] = new Point2D(aHit.x, aHit.y);
        }
        for (TennisBase.BallPoint aDown : downArr) {
            if (mServeFallCount >= POINTS_CONTAINER_DEFAULT_CAPACITY * (mServeFallExpandedCount + 1)) {
                mServeFallPoints[team] = CollectionUtil.assembleCapacityExpandedArray(mServeFallPoints[team], POINTS_CONTAINER_DEFAULT_CAPACITY);
                mServeFallExpandedCount++;
            }

            mServeFallPoints[team][mServeFallCount++] = new Point3D(aDown.x, aDown.y, TennisBase.PlayerServe.IGNORE);
        }
        stopIPCamerasRecord();
    }

    @Override
    public void onDrillNext() {
        sop(TAG, "onDrillNext");
        if (mBreakMode == Rule.BreakMode.FOUR_MINUTES) {
            if (System.currentTimeMillis() - mWholeGameStartTime <= TRAIN_TIME_LIMIT_IN_MILLI) {
                return;
            }
        } else if (mBreakMode == Rule.BreakMode.TWENTY_HITS) {
            mDrillCount++;
            if (mDrillCount <= DRILL_COUNT_LIMIT) {
                return;
            }
        }
        mDrillPresenter.stop();
        mShouldEndGame = true;
    }

    @Override
    public void onError(int code, String errorMessage) {
        sop(TAG, "onError");
    }

    //--**-BLOCK_DIVIDER-**-----------------------Umpire Operator--------------------------//
    @Override
    public void exchangeSide() {
        mReverseSide = !mReverseSide;
    }

    @Override
    public void exchangeServePower() {

    }

    @Override
    public void assignServePower(@TennisBase.BallPower int ballPower) {
        mMatchPresenter.ballPower(ballPower);
    }

    @Override
    public boolean start() {
        if(mStartedBefore)
            reset();
        startIPCamerasRecordAndResetEventCache();
        if (mWholeGameStartTime == 0) {
            mWholeGameStartTime = System.currentTimeMillis();
        }
        mStartedBefore = true;
        return mTennisPresenter.start();
    }

    @Override
    public boolean pause() {
        return mTennisPresenter.pause();
    }

    @Override
    public boolean resume() {
        return mTennisPresenter.resume();
    }

    @Override
    public boolean stop() {
        stopIPCamerasRecord();
        return mTennisPresenter.stop();
    }

    @Override
    public boolean restartOneMatchSet(@TennisBase.CourtArea int serveArea) {
        startIPCamerasRecordAndResetEventCache();
        return mTennisPresenter.restart(serveArea);
    }

    @Override
    public long getWholeGameStartTime() {
        // box to avoid edit unconsciously
        return Long.valueOf(mWholeGameStartTime);
    }

    @Override
    public long getLastingTime() {
        return System.currentTimeMillis() - mWholeGameStartTime;
    }

    @Override
    public Score reverseLastScoreJudge() {
        if (mLastHistoryScore == null) return mScore;
        UmpireDelegate.removeVideoAndNoteFiles(mScore, UmpireDelegate.ExtensionType.NOTE);

        boolean teamTanWonBout = UmpireDelegate.teamTanWonBout(mScore, mLastHistoryScore);
        Score oldEndScore = new Score(mScore);
        mScore = new Score(mLastHistoryScore);
        appendBoutResult(!teamTanWonBout);
        UmpireDelegate.reverseVideoFilesName(oldEndScore, mScore, mLastHistoryScore);

        if (mUmpireCallback != null) {
            mUmpireCallback.onScoreChange(mScore);
        }
        return mScore;
    }

    @Override
    public Score cancelLastScore() {
        UmpireDelegate.removeVideoAndNoteFiles(mScore, UmpireDelegate.ExtensionType.NOTE_AND_VIDEO);
        if (mLastHistoryScore != null)
            mScore = new Score(mLastHistoryScore);
        else mScore = new Score();
        mLastHistoryScore = null;

        if (mUmpireCallback != null) {
            mUmpireCallback.onScoreChange(mScore);
        }
        return mScore;
    }

    @Override
    public Score getScore() {
        return mScore;
    }

    @Override
    public Video[] getVideoOfLastScore() {
        // TODO: 24/04/2017 last score + by Kyle
        return new Video[0];
    }

    @Override
    public Video[] getVideoOfAce(@Rule.Team int team) {
        subpackageVideoIfNecessary(team);
        return toArray(mAceVideos.get(team));
    }

    @Override
    public Video[] getVideoOfWinner(@Rule.Team int team) {
        subpackageVideoIfNecessary(team);
        return toArray(mWinnerVideos.get(team));
    }

    @Override
    public Video[] getVideoOfManyHits(@Rule.Team int team) {
        subpackageVideoIfNecessary(team);
        return toArray(m20HitsVideos.get(team));
    }

    @Override
    public Video[] getVideoOfGeneral(@Rule.Team int team) {
        subpackageVideoIfNecessary(team);
        return toArray(mGeneralVideos.get(team));
    }

    @Override
    public Video[] getAllVideo(@Rule.Team int team) {
        return toArray(mVideos.get(team));
    }

    private  Video[] toArray(Collection<Video> c) {
        return c.toArray(new Video[c.size()]);
    }

    private void subpackageVideoIfNecessary(@Rule.Team int team) {
        if (mVideoArranged[team]) return;

        mVideoArranged[team] = true;
        int tag;
        for (Video v : mVideos.get(team)) {
            if (ApiFromAndroid.TextUtil_isEmpty(v.path)
                    || !v.path.contains(DomainConst.SCORE_TYPE_VIDEO_END)) continue;
            tag = v.tag;
            if (matchBit(tag, Rule.VideoTag.ACE)) mAceVideos.get(team).add(v);
            else if (matchBit(tag, Rule.VideoTag.WINNER)) mWinnerVideos.get(team).add(v);
            else if (matchBit(tag, Rule.VideoTag.ABOVE_20_HITS)) m20HitsVideos.get(team).add(v);
            else if (matchBit(tag, Rule.VideoTag.GENERAL)) mGeneralVideos.get(team).add(v);
        }
    }

    @SuppressWarnings("MagicConstant")
    private boolean matchBit(@Rule.VideoTag int tag, @Rule.VideoTag int bit) {
        return (tag & bit) == bit;
    }

    @Override
    public TennisBase.BaseBoxScore[] getAnalyzeData() {
        return mChannel == TennisBase.Channel.MATCH ? getBoxScores() : getDrillBoxScore();
    }

    @Override
    public Point2D[][] getReceiveHits() {
        return mReceiveHits;
    }

    @Override
    public Point3D[][] getServeFallPoints() {
        return mServeFallPoints;
    }

    public void startIPCamerasRecordAndResetEventCache() {
        sop(TAG, "start record");
        mRecordingTimeStamp = String.valueOf(System.currentTimeMillis());
        startRecode(mRecordingTimeStamp, Rule.Side.A);
        startRecode(mRecordingTimeStamp, Rule.Side.B);
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap, Rule.Event.START_TIME, mRecordingTimeStamp);
    }

    protected void startRecode(String timeStamp, @Rule.Side int side) {
        if (mIPCameraInitOk[side]) {
            mRecordingVideoPath[side] = UmpireDelegate.generateAbsoluteFilePath( mScore, timeStamp, side,mPathPrefix);
            sop(TAG, "path::", mRecordingVideoPath[side]);
            mVideoRecordStartOk[side] = mIPCameras[side].startRecord(mRecordingVideoPath[side]);
        }
    }

    private void stopIPCamerasRecord() {
        stopIPCamerasRecord(Rule.VideoTag.GENERAL, Rule.VideoTag.GENERAL);
    }

    private void stopIPCamerasRecord(@Rule.VideoTag int tagTan, @Rule.VideoTag int tagRed) {
        sop(TAG, "stopIPCamerasRecord");
        if (ApiFromAndroid.TextUtil_isEmpty(mRecordingTimeStamp)) return;

        // keep statement order
        long lastingTime = System.currentTimeMillis() - Long.parseLong(mRecordingTimeStamp);
        UmpireDelegate.putEventIntoCache(mRecordingMatchEventMap, Rule.Event.LASTING_TIME, lastingTime);
        stopRecordAndAppendEndScoreToFileName(mRecordingVideoPath[Rule.Side.A]
                , mReverseSide ? tagRed : tagTan, lastingTime, mRecordingMatchEventMap, Rule.Side.A
                , mReverseSide ? Rule.Team.RED : Rule.Team.TAN);
        stopRecordAndAppendEndScoreToFileName(mRecordingVideoPath[Rule.Side.B]
                , mReverseSide ? tagTan : tagRed, lastingTime, mRecordingMatchEventMap, Rule.Side.B
                , mReverseSide ? Rule.Team.TAN : Rule.Team.RED);

        mRecordingTimeStamp = "";
        mRecordingVideoPath[Rule.Side.A] = "";
        mRecordingVideoPath[Rule.Side.B] = "";
        mNoteFileProcessed = false;
        mRecordingMatchEventMap.clear();
    }

    private void stopRecordAndAppendEndScoreToFileName(String path, @Rule.VideoTag int tag
            , long lastingTime, Map<String, String> map, @Rule.Side int side, @Rule.Team int team) {
        if (mVideoRecordStartOk[side]) {
            mIPCameras[side].stopRecord();
            sop(TAG, mScore, path);
            videoFileAndNoteFileProcess(tag, mScore, lastingTime, path, map, side, team);
        }
    }

    protected void videoFileAndNoteFileProcess(@Rule.VideoTag int tag, Score score, long lastingTime
            , String oldName, Map<String, String> map, @Rule.Side int side, @Rule.Team int team) {
        String storedFileName = UmpireDelegate.renameRecordFile(tag, score, lastingTime, oldName);
        Video v = new Video();
        v.path = storedFileName;
        if (ApiFromAndroid.TextUtil_isEmpty(v.path)
                || !v.path.contains(DomainConst.SCORE_TYPE_VIDEO_END)) return;
        v.tag = tag;
        mVideos.get(team).add(v);
        if (!mNoteFileProcessed) {
            mNoteFileProcessed = true;
            UmpireDelegate.putMatchVideoEventIntoFile(storedFileName, map, side);
        }
    }

    public boolean runOnce() {
        return mTennisPresenter.runOnce();
    }

    @Override
    public int order() {
        return ORDER.UMPIRE;
    }

    @Override
    public void freeMemory() {

    }

    @Override
    public void dispose() {
        super.dispose();
        if (mIPCameraInitOk[Rule.Side.A]) mIPCameras[Rule.Side.A].dispose();
        if (mIPCameraInitOk[Rule.Side.B]) mIPCameras[Rule.Side.B].dispose();
        mBoxScores = null;
        mScore = null;
        mTennisPresenter = null;
    }

    private void sop(Object... objects) {
        if (PRINT)
            LogcatUtil.sop(objects);
    }

    public void disableAutoReverseSide() {
        mAutoReverseDisabled = true;
    }
}
