package com.zealens.face.activity.train;

import android.os.Bundle;

import com.zealens.face.R;
import com.zealens.face.activity.base.HeadAccountLayerBaseActivity;
import com.zealens.face.base.Rule;
import com.zealens.face.common.KeyConst;
import com.zealens.face.presenter.train.zone.ZoneShowPresenter;
import com.zealens.face.presenter.train.zone.ZoneShowPresenterImpl;

public class TrainLevelAndBreakChooseActivity extends HeadAccountLayerBaseActivity {

    @Rule.TrainMode
    private int mTrainMode;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_train_level_and_break_choose;
    }

    @SuppressWarnings("MagicConstant")
    @Override
    protected void parseNonNullBundle(Bundle bundle) {
        super.parseNonNullBundle(bundle);
        mTrainMode =  bundle.getInt(KeyConst.TRAIN_MODE_TRIGGER, mTrainMode);
    }

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        mBasePresenter = new ZoneShowPresenterImpl(getCoreContext(), mActivityContent, mTrainMode, this);
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
    protected boolean showBottomControlButton() {
        return true;
    }

    @Override
    protected Runnable assembleBottomControlButtonAction() {
        return ((ZoneShowPresenter) mBasePresenter).assembleControlUnitResponse();
    }

    @Override
    protected String prepareActionText() {
        return mContext.getResources().getString(R.string.continue_choose);
    }
}
