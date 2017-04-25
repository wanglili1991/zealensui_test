package com.zealens.face.activity.base;

import com.zealens.face.base.headtimer.presenter.HeadTimerLayerPresenter;
import com.zealens.face.base.headtimer.presenter.HeadTimerLayerPresenterImpl;
import com.zealens.face.common.CompleteCallBack;

public abstract class HeadTimerLayerBaseActivity extends PortraitBaseActivity implements CompleteCallBack {
    private HeadTimerLayerPresenter mHeadAccountLayerPresenter;

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        mHeadAccountLayerPresenter = new HeadTimerLayerPresenterImpl(getCoreContext()
                , mActivityContent, prepareMode(), this,this);
        mHeadAccountLayerPresenter.loadHeadLayer();
    }

    @HeadTimerLayerPresenterImpl.Mode
    protected int prepareMode() {
        return HeadTimerLayerPresenterImpl.Mode.match;
    }

    @Override
    protected boolean showBackPressButton() {
        return false;
    }

    @Override
    public void onComplete(Object o) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHeadAccountLayerPresenter.dispose();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
