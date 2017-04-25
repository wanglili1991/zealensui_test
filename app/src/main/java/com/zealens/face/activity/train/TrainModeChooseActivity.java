package com.zealens.face.activity.train;

import android.os.Bundle;

import com.zealens.face.R;
import com.zealens.face.activity.base.HeadAccountLayerBaseActivity;
import com.zealens.face.base.Rule;
import com.zealens.face.base.modechoose.ModeChoosePresenterImpl;
import com.zealens.face.common.KeyConst;

public class TrainModeChooseActivity extends HeadAccountLayerBaseActivity {
    @Rule.TrainMode
    private int mTrainMode;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_train_mode_choose;
    }

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        mBasePresenter = new ModeChoosePresenterImpl(getCoreContext(), mActivityContent, this);
        mBasePresenter.initialize();
    }

    @Override
    protected boolean portraitClickable() {
        return true;
    }

    @Override
    protected boolean showExchangeIconWhenLoaded() {
        return true;
    }

    @Rule.ChannelMode
    protected int parseMode() {
        return Rule.ChannelMode.TRAINING;
    }

    @Override
    protected void assembleDataAfterUiAffairs() {
        super.assembleDataAfterUiAffairs();
        mActionButton.setEnabled(false);
    }

    @Override
    protected boolean showBottomControlButton() {
        return true;
    }

    @Override
    protected Runnable assembleBottomControlButtonAction() {
        Bundle bundle = new Bundle();
        bundle.putInt(KeyConst.TRAIN_MODE_TRIGGER, mTrainMode);
        return () -> startActivity(TrainLevelAndBreakChooseActivity.class, bundle);
    }

    @Override
    protected String prepareActionText() {
        return getString(R.string.continue_choose);
    }

    @Override
    public void onRefresh(Object obj) {
        super.onRefresh(obj);
        if (obj instanceof Integer) {
            mTrainMode = (int) obj;
        } else if (obj instanceof Boolean) {
            mActionButton.setEnabled((boolean) obj);
        }
    }
}
