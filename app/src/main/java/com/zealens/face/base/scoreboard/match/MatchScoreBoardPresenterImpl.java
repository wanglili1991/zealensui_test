package com.zealens.face.base.scoreboard.match;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zealens.face.R;
import com.zealens.face.base.Rule;
import com.zealens.face.common.GeneralConst;
import com.zealens.face.common.ResourceConst;
import com.zealens.face.core.CoreContext;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.umpire.FunctionToolBox;
import com.zealens.face.domain.umpire.UmpireCallback;
import com.zealens.face.domain.umpire.UmpireOperator;
import com.zealens.face.presenter.BasePresenterImpl;
import com.zealens.face.tennis.UmpireManagerAppLevel;
import com.zealens.face.util.AppViewUtil;
import com.zealens.face.util.MediaHelper;
import com.zealens.face.util.ViewUtil;
import com.zealens.face.view.PlayerStateImageView;

import java.io.File;

import static com.zealens.face.util.LogcatUtil.sop;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class MatchScoreBoardPresenterImpl extends BasePresenterImpl implements MatchScoreBoardPresenter, UmpireCallback {
    public static final String TAG = MatchScoreBoardPresenterImpl.class.getSimpleName();

    private CoreContext mCoreContext;
    private Context mContext;
    private ViewGroup mRootVG;
    @Rule.ChannelMode
    private int mMode;
    private UmpireOperator mUmpireOperator;
    private boolean mMatchSetPaused;

    private TextView[] mBoutScore;
    private TextView[] mGameScore;
    private TextView[] mSetScore;
    private TextView[] mSpeed;
    private View[] mSpeedUnit;
    private PlayerStateImageView[] mStateView;
    @Rule.Side
    private int mServingSide;
    private MediaPlayer mMediaPlayer;
    private boolean mReversed;
    private final long AUDIO_PLAY_INTERNAL = 1000;
    private long mLastPlayTimeInMilli;

    public MatchScoreBoardPresenterImpl(CoreContext coreContext, ViewGroup rootVG, @Rule.ChannelMode int mode) {
        mCoreContext = coreContext;
        mContext = coreContext.getApplicationContext();
        mRootVG = rootVG;
        mMode = mode;
    }

    @Override
    public void initialize() {
        super.initialize();
        mUmpireOperator = (UmpireOperator) mCoreContext.getApplicationService(UmpireManagerAppLevel.class);
        mUmpireOperator.assignComponentsByPlayModel(mMode == Rule.ChannelMode.SINGLE_MATCH
                ? TennisBase.PlayModel.MATCH_SINGLE : TennisBase.PlayModel.MATCH_DOUBLE);
        mUmpireOperator.assignUmpireCallback(this);
        mMediaPlayer = new MediaPlayer();

        findViewComponents();
        mUmpireOperator.start();
    }

    private void findViewComponents() {
        int size = Rule.Side.class.getFields().length;
        mBoutScore = new TextView[size];
        mGameScore = new TextView[size];
        mSetScore = new TextView[size];
        mSpeed = new TextView[size];
        mSpeedUnit = new View[size];
        mStateView = new PlayerStateImageView[size];

        AppViewUtil.findViewSideAB(mRootVG, mBoutScore, new int[]{R.id.bout_score_side_a, R.id.bout_score_side_b});
        AppViewUtil.findViewSideAB(mRootVG, mGameScore, new int[]{R.id.game_score_side_a, R.id.game_score_side_b});
        AppViewUtil.findViewSideAB(mRootVG, mSetScore, new int[]{R.id.set_score_side_a, R.id.set_score_side_b});
        AppViewUtil.findViewSideAB(mRootVG, mSpeed, new int[]{R.id.speed_side_a, R.id.speed_side_b});
        AppViewUtil.findViewSideAB(mRootVG, mSpeedUnit, new int[]{R.id.speed_unit_side_a, R.id.speed_unit_side_b});
        AppViewUtil.findViewSideAB(mRootVG, mStateView, new int[]{R.id.player_state_side_a, R.id.player_state_side_b});
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
        if (mMediaPlayer.isPlaying())
            mMediaPlayer.pause();
    }

    @Override
    public void onStop() {
        mUmpireOperator.stop();
    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onGameEnd() {

    }

    @Override
    public void onServe(@Rule.Side int side) {
        mServingSide = side;
        switchStateViewState(mStateView[side],PlayerStateImageView.Status.serve);
    }

    private void switchStateViewState(PlayerStateImageView item, @PlayerStateImageView.Status int status) {
        mRootVG.post(() -> item.switchStatus(status));
        mRootVG.postDelayed(() -> item.switchStatus(PlayerStateImageView.Status.receive), GeneralConst.SERVE_LASTING_TIME_IN_MILLI);
    }

    @Override
    public void onServeFailed() {
        switchStateViewState(mStateView[mServingSide],PlayerStateImageView.Status.out);
        playAudio(ResourceConst.Audio.OUT);
    }

    @Override
    public void onServeSuccessful() {
        switchStateViewState(mStateView[mServingSide],PlayerStateImageView.Status.in);
        playAudio(ResourceConst.Audio.IN);
    }

    @Override
    public void onServeOut() {
        switchStateViewState(mStateView[mServingSide],PlayerStateImageView.Status.out);
        playAudio(ResourceConst.Audio.OUT);
    }

    @Override
    public void onAce(int playerServe) {
        playAudio(ResourceConst.Audio.ACE);
    }

    private void playAudio(@ResourceConst.Audio String audio) {
        if (System.currentTimeMillis() - mLastPlayTimeInMilli > AUDIO_PLAY_INTERNAL) {
            MediaHelper.playAudioUnderAssets(mContext, mMediaPlayer
                    , ResourceConst.AUDIO_DIR + File.separator + audio);
        }
        mLastPlayTimeInMilli = System.currentTimeMillis();
    }

    @Override
    public void onScoreChange(Score score) {
        sop(TAG, score.getScoreInString());
        mRootVG.post(() -> {
            updateScoreTextStatus(mGameScore, score.game);
            updateScoreTextStatus(mSetScore, score.set);
            updateBoutScore(score);
        });
    }

    private void updateScoreTextStatus(TextView[] texts, int[] scores) {
        texts[Rule.Side.A].setText(String.valueOf(scores[mReversed ? Rule.Team.RED : Rule.Team.TAN]));
        texts[Rule.Side.B].setText(String.valueOf(scores[!mReversed ? Rule.Team.RED : Rule.Team.TAN]));
    }

    private void updateBoutScore(Score score) {
        int tanBoutScore = score.bout[Rule.Team.TAN];
        int redBoutScore = score.bout[Rule.Team.RED];
        String tanScore;
        String redScore;
        int limit = ResourceConst.BOUT_SCORE_LIMIT;
        boolean tanReachLimit = tanBoutScore >= limit;
        boolean redReachLimit = redBoutScore >= limit;
        if (tanReachLimit && redReachLimit) {
            if (tanBoutScore == redBoutScore) {
                tanScore = ResourceConst.BOUT_SCORE[ResourceConst.BoutScoreIndex._4th];
                redScore = ResourceConst.BOUT_SCORE[ResourceConst.BoutScoreIndex._4th];
            } else if (tanBoutScore > limit) {
                tanScore = ResourceConst.BOUT_SCORE[ResourceConst.BoutScoreIndex._ad];
                redScore = ResourceConst.BOUT_SCORE[ResourceConst.BoutScoreIndex._4th];
            } else {
                tanScore = ResourceConst.BOUT_SCORE[ResourceConst.BoutScoreIndex._4th];
                redScore = ResourceConst.BOUT_SCORE[ResourceConst.BoutScoreIndex._ad];
            }
        } else {
            tanScore = ResourceConst.BOUT_SCORE[tanBoutScore];
            redScore = ResourceConst.BOUT_SCORE[redBoutScore];
        }
        mBoutScore[Rule.Side.A].setText(!mReversed ? tanScore : redScore);
        mBoutScore[Rule.Side.B].setText(mReversed ? tanScore : redScore);
    }

    @Override
    public void onBallSpeedUpdate(@Rule.Side int side, int speed) {
        mRootVG.post(() -> {
            mSpeed[side].setText(String.valueOf(speed));
            View[] operateViews = new View[]{mSpeed[side], mSpeedUnit[side]};
            ViewUtil.show(operateViews);
            mSpeed[side].postDelayed(() -> ViewUtil.invisible(operateViews)
                    , GeneralConst.SPEED_LASTING_TIME_IN_MILLI);
        });
    }

    @Override
    public void onNextBout() {

    }

    @Override
    public void onNextGame() {

    }

    @Override
    public void onLet() {
        playAudio(ResourceConst.Audio.NET);
    }

    @Override
    public FunctionToolBox getCorrectComponents() {
        return mUmpireOperator;
    }

    @Override
    public void dispose() {
        super.dispose();
        synchronized (mMediaPlayer) {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }
    }
}
