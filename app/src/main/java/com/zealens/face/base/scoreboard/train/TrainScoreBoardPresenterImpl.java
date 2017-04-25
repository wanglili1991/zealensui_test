package com.zealens.face.base.scoreboard.train;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zealens.face.R;
import com.zealens.face.base.Rule;
import com.zealens.face.common.ResourceConst;
import com.zealens.face.core.CoreContext;
import com.zealens.face.core.internal.DrillBase;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.domain.umpire.UmpireOperator;
import com.zealens.face.presenter.BasePresenterImpl;
import com.zealens.face.tennis.UmpireManagerAppLevel;
import com.zealens.face.util.AppViewUtil;
import com.zealens.face.util.MediaHelper;
import com.zealens.face.view.ScoreBoardView;

import org.jetbrains.annotations.NonNls;

import java.io.File;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class TrainScoreBoardPresenterImpl extends BasePresenterImpl implements TrainScoreBoardPresenter, DrillBase {
    public static final String TAG = TrainScoreBoardPresenterImpl.class.getSimpleName();

    private CoreContext mCoreContext;
    private Context mContext;
    private ViewGroup mRootVG;
    @Rule.ChannelMode
    private int mMode;
    private UmpireOperator mUmpireOperator;
    private boolean mMatchSetPaused;

    private ScoreBoardView[] mScoreBoardViews;
    private TextView[] mAverageSpeed;
    private TextView[] mSuccessRatio;

    private MediaPlayer mMediaPlayer;
    private boolean mReversed;
    private final long AUDIO_PLAY_INTERNAL = 1000;
    private final long STATE_RESET_INTERNAL = 3000;
    private long mLastPlayTimeInMilli;

    /**
     * @see Rule.TrainParamsIndex keep array order
     */
    private int[] mTrainParamsArray;
    private Runnable mEndAction;

    public TrainScoreBoardPresenterImpl(CoreContext coreContext, ViewGroup rootVG
            , @Rule.ChannelMode int mode, int[] array, @NonNls Runnable endAction) {
        mCoreContext = coreContext;
        mContext = coreContext.getApplicationContext();
        mRootVG = rootVG;
        mMode = mode;
        mTrainParamsArray = array;
        mEndAction = endAction;
    }

    @Override
    public void initialize() {
        super.initialize();
        mUmpireOperator = (UmpireOperator) mCoreContext.getApplicationService(UmpireManagerAppLevel.class);

        mUmpireOperator.assignComponentsByPlayModel(parsePlayModel(mTrainParamsArray[Rule.TrainParamsIndex.ITEM])
                , parseDifficulty(mTrainParamsArray[Rule.TrainParamsIndex.LEVEL])
                , mTrainParamsArray[Rule.TrainParamsIndex.MODE]);
        mUmpireOperator.assignDrillBaseCallback(this);
        mMediaPlayer = new MediaPlayer();

        findViewComponents();
        mUmpireOperator.start();
    }

    @TennisBase.PlayModel
    private int parsePlayModel(@Rule.TrainMode int trainMode) {
        switch (trainMode) {
            default:
            case Rule.TrainMode.SERVICE:
                return TennisBase.PlayModel.DRILL_SERVE;
            case Rule.TrainMode.ALTERNATE_SERVICE:
                return TennisBase.PlayModel.DRILL_RECEIVE;
            case Rule.TrainMode.FOREHAND:
                return TennisBase.PlayModel.DRILL_FOREHAND_HIT;
            case Rule.TrainMode.FOREHAND_DOWN_THE_LINE:
                return TennisBase.PlayModel.DRILL_FOREHAND_STRAIGHT;
            case Rule.TrainMode.FOREHAND_CROSS_COURT:
                return TennisBase.PlayModel.DRILL_FOREHAND_SLASH;
            case Rule.TrainMode.BACKHAND:
                return TennisBase.PlayModel.DRILL_BACKHAND_HIT;
            case Rule.TrainMode.BACKHAND_DOWN_THE_LINE:
                return TennisBase.PlayModel.BACKHAND_STRAIGHT;
            case Rule.TrainMode.BACKHAND_CROSS_COURT:
                return TennisBase.PlayModel.DRILL_BACKHAND_SLASH;
            case Rule.TrainMode.VOLLEY:
                return TennisBase.PlayModel.DRILL_VOLLEY;
            case Rule.TrainMode.FREE_STYLE:
                return TennisBase.PlayModel.DRILL_CASUAL;
            case Rule.TrainMode.MULTIPLE_BALLS:
                return TennisBase.PlayModel.DRILL_MULTI_BALL;
        }
    }

    @TennisBase.DrillDifficulty
    private int parseDifficulty(@Rule.Level int level) {
        switch (level) {
            default:
            case Rule.Level.EASY:
                return TennisBase.DrillDifficulty.EASY;
            case Rule.Level.MEDIUM:
                return TennisBase.DrillDifficulty.MEDIUM;
            case Rule.Level.HARD:
                return TennisBase.DrillDifficulty.DIFFICULT;
        }
    }

    private void findViewComponents() {
        int size = Rule.Side.class.getFields().length;
        mScoreBoardViews = new ScoreBoardView[size];
        mAverageSpeed = new TextView[size];
        mSuccessRatio = new TextView[size];

        AppViewUtil.findViewSideAB(mRootVG, mScoreBoardViews, new int[]{R.id.score_board_a, R.id.score_board_b});
        AppViewUtil.findViewSideAB(mRootVG, mAverageSpeed, new int[]{R.id.average_speed_a, R.id.average_speed_b});
        AppViewUtil.findViewSideAB(mRootVG, mSuccessRatio, new int[]{R.id.success_ratio_side_a, R.id.success_ratio_side_b});
    }

    @Override
    public void onResume() {
        if (mMatchSetPaused) {
            // the activity resume may be called on the first loop lifecycle
            mUmpireOperator.resume();
            mMatchSetPaused = false;
        }
    }

    @Override
    public void onPause() {
        mMatchSetPaused = true;
        mUmpireOperator.pause();
        try {
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.pause();
        } catch (Exception ignore) {
        }
    }

    @Override
    public void onStop() {
        mUmpireOperator.stop();
    }

    private void playAudio(@ResourceConst.Audio String audio) {
        if (System.currentTimeMillis() - mLastPlayTimeInMilli > AUDIO_PLAY_INTERNAL) {
            MediaHelper.playAudioUnderAssets(mContext, mMediaPlayer
                    , ResourceConst.AUDIO_DIR + File.separator + audio);
        }
        mLastPlayTimeInMilli = System.currentTimeMillis();
    }

    private void updateScoreTextStatus(TextView[] texts, int[] scores) {
        texts[Rule.Side.A].setText(String.valueOf(scores[mReversed ? Rule.Team.RED : Rule.Team.TAN]));
        texts[Rule.Side.B].setText(String.valueOf(scores[!mReversed ? Rule.Team.RED : Rule.Team.TAN]));
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onDataChange(TennisBase.DrillBoxScore drillBoxScore) {
        boolean ballIn = drillBoxScore.lastResult == TennisBase.DrillResult.IN;
        playAudio(ballIn ? ResourceConst.Audio.IN : ResourceConst.Audio.OUT);

        int team = Rule.Team.TAN;
        ScoreBoardView sbv = mScoreBoardViews[team];
        mRootVG.post(() -> {
            sbv.setSuccessTimesText(drillBoxScore.successfulTimes);
            sbv.setStateImage(ballIn ? Rule.TrainState.IN : Rule.TrainState.OUT);
            sbv.setRealTimeSpeedText(drillBoxScore.speed);
            mAverageSpeed[team].setText(String.valueOf(drillBoxScore.averageSpeed));
            mSuccessRatio[team].setText(String.valueOf(drillBoxScore.successRatio) + "%");
        });
        mRootVG.postDelayed(() -> {
            sbv.setStateImage(Rule.TrainState.NORMAL);
        }, STATE_RESET_INTERNAL);
    }

    @Override
    public void onSpeedChange(int speed) {
        int team = Rule.Team.TAN;
        ScoreBoardView sbv = mScoreBoardViews[team];

        mRootVG.post(() -> {
            sbv.setRealTimeSpeedText(speed);
        });
    }

    @Override
    public void onEnd() {
        mEndAction.run();
    }

    @Override
    public void dispose() {
        super.dispose();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}
