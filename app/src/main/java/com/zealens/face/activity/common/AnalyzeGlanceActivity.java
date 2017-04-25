package com.zealens.face.activity.common;

import com.zealens.face.R;
import com.zealens.face.activity.base.HeadAccountLayerBaseActivity;
import com.zealens.face.activity.match.MatchScoreBoardActivity;
import com.zealens.face.activity.train.TrainScoreBoardActivity;
import com.zealens.face.base.Rule;
import com.zealens.face.base.analyzeglance.presenter.AnalyzeGlancePresenterImpl;
import com.zealens.face.base.fakedialog.DialogPresenter;
import com.zealens.face.base.fakedialog.LastScoreIncorrectDialogPresenterImpl;
import com.zealens.face.common.VideoReplayClickCallback;
import com.zealens.face.domain.umpire.FunctionToolBox;
import com.zealens.face.domain.umpire.UmpireOperator;
import com.zealens.face.tennis.UmpireManagerAppLevel;

public class AnalyzeGlanceActivity extends HeadAccountLayerBaseActivity implements VideoReplayClickCallback {
    private DialogPresenter mLastScoreIncorrectPresenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_analyze_glance;
    }

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        findViewById(R.id.video_replay).setOnClickListener((v) -> {
            triggerChannelDistinguishActivity(VideoReplayActivity.class, mChannelMode);
        });
        findViewById(R.id.analyze).setOnClickListener((v) -> {
            triggerChannelDistinguishActivity(SkillAnalyzeActivity.class, mChannelMode);
        });
        findViewById(R.id.once_more).setOnClickListener((v) -> {
            if (mChannelMode == Rule.ChannelMode.TRAINING)
                triggerChannelDistinguishActivity(TrainScoreBoardActivity.class, mChannelMode);
            else triggerChannelDistinguishActivity(MatchScoreBoardActivity.class, mChannelMode);
        });

        FunctionToolBox toolBox = (UmpireOperator) getCoreContext().getApplicationService(UmpireManagerAppLevel.class);
        mBasePresenter = new AnalyzeGlancePresenterImpl(getCoreContext(), mActivityContent
                , mChannelMode, toolBox.getAnalyzeData());
        mBasePresenter.initialize();

        mLastScoreIncorrectPresenter = new LastScoreIncorrectDialogPresenterImpl(getCoreContext()
                , mActivityContent, R.layout.video_replay_layout, mChannelMode, toolBox);
        mLastScoreIncorrectPresenter.initialize();
        findViewById(R.id.last_score_incorrect).setOnClickListener((v -> mLastScoreIncorrectPresenter.loadDialogPage()));
    }

    @Override
    protected int parseMode() {
        return mChannelMode;
    }

    @Override
    public void onYes() {

    }

    @Override
    public void onNo() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onReplaySideA() {

    }

    @Override
    public void onReplaySideB() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLastScoreIncorrectPresenter != null) mLastScoreIncorrectPresenter.dispose();
    }
}
