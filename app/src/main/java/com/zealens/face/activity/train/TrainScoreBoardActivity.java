package com.zealens.face.activity.train;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zealens.face.R;
import com.zealens.face.activity.base.HeadTimerLayerBaseActivity;
import com.zealens.face.activity.common.AnalyzeGlanceActivity;
import com.zealens.face.base.Rule;
import com.zealens.face.base.headtimer.presenter.HeadTimerLayerPresenterImpl;
import com.zealens.face.base.scoreboard.train.TrainScoreBoardPresenter;
import com.zealens.face.base.scoreboard.train.TrainScoreBoardPresenterImpl;
import com.zealens.face.common.KeyConst;
import com.zealens.face.data.user.ChosenUserArea;
import com.zealens.face.user.UserInfo;
import com.zealens.face.util.ViewUtil;

public class TrainScoreBoardActivity extends HeadTimerLayerBaseActivity {
    private View mPlayerInformationCard;
    private View mScoreBoardSideA;
    private View mScoreBoardSideB;
    private View mPlayerInformationSideB;
    /**
     * @see Rule.TrainParamsIndex keep array order
     */
    private int[] mTrainParamsArray;
    private TrainScoreBoardPresenter mTrainScoreBoardPresenter;

    @SuppressWarnings("MagicConstant")
    @Override
    protected void parseNonNullBundle(Bundle bundle) {
        super.parseNonNullBundle(bundle);
        mTrainParamsArray = bundle.getIntArray(KeyConst.TRAIN_ARRAY);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_train_score_board;
    }

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        mPlayerInformationCard = findViewById(R.id.player_information_card);
        mScoreBoardSideA = findViewById(R.id.score_board_a);
        mScoreBoardSideB = findViewById(R.id.score_board_b);
        mPlayerInformationSideB = findViewById(R.id.player_information_side_b);
        UserInfo userInfo = mUserCacheManager.parseChosenPlayer(ChosenUserArea.SIDE_B_1ST);
        if (userInfo == null) {
            ViewUtil.hide(mScoreBoardSideB, mPlayerInformationSideB);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mPlayerInformationCard.getLayoutParams();
            int singleBottomMargin = getCoreContext().getDimensionPixelOffset(R.dimen.train_score_board_user_information_margin_bottom_when_single);
            lp.setMargins(0, 0, 0, singleBottomMargin);
            mPlayerInformationCard.setLayoutParams(lp);

            LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) mScoreBoardSideA.getLayoutParams();
            llp.height = LinearLayout.LayoutParams.MATCH_PARENT;
            mScoreBoardSideA.setLayoutParams(llp);
        }

        mTrainScoreBoardPresenter = new TrainScoreBoardPresenterImpl(getCoreContext()
                , mActivityContent, mChannelMode, mTrainParamsArray,()->onComplete(null));
        mTrainScoreBoardPresenter.initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTrainScoreBoardPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTrainScoreBoardPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTrainScoreBoardPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTrainScoreBoardPresenter.dispose();
    }

    @Override
    protected int parseMode() {
        return Rule.ChannelMode.TRAINING;
    }

    @Override
    @HeadTimerLayerPresenterImpl.Mode
    public int prepareMode() {
        return HeadTimerLayerPresenterImpl.Mode.train;
    }

    @Override
    public void onComplete(Object o) {
        super.onComplete(o);
        if(isFinishing()) return;
        triggerChannelDistinguishActivity(AnalyzeGlanceActivity.class, mChannelMode);
        finish();
    }
}
