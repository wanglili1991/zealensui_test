package com.zealens.face.activity.match;

import com.zealens.face.R;
import com.zealens.face.activity.base.HeadTimerLayerBaseActivity;
import com.zealens.face.activity.common.AnalyzeGlanceActivity;
import com.zealens.face.base.fakedialog.DialogPresenter;
import com.zealens.face.base.fakedialog.LastScoreIncorrectDialogPresenterImpl;
import com.zealens.face.base.scoreboard.match.MatchScoreBoardPresenter;
import com.zealens.face.base.scoreboard.match.MatchScoreBoardPresenterImpl;

public class MatchScoreBoardActivity extends HeadTimerLayerBaseActivity {
    private DialogPresenter mLastScoreIncorrectPresenter;
    private MatchScoreBoardPresenter mMatchScoreBoardPresenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_match_score_board;
    }

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        mMatchScoreBoardPresenter = new MatchScoreBoardPresenterImpl(getCoreContext(), mActivityContent, mChannelMode);
        mMatchScoreBoardPresenter.initialize();
        mLastScoreIncorrectPresenter = new LastScoreIncorrectDialogPresenterImpl(getCoreContext()
                , mActivityContent, R.layout.video_replay_layout, mChannelMode, mMatchScoreBoardPresenter.getCorrectComponents());
        mLastScoreIncorrectPresenter.initialize();
        findViewById(R.id.last_score_incorrect).setOnClickListener((v -> mLastScoreIncorrectPresenter.loadDialogPage()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMatchScoreBoardPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMatchScoreBoardPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMatchScoreBoardPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMatchScoreBoardPresenter.dispose();
        mLastScoreIncorrectPresenter.dispose();
    }

    @Override
    public void onComplete(Object o) {
        super.onComplete(o);
        triggerChannelDistinguishActivity(AnalyzeGlanceActivity.class, mChannelMode);
        finish();
    }

    @Override
    protected int parseMode() {
        return mChannelMode;
    }
}
